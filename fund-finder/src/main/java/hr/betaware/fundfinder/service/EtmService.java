package hr.betaware.fundfinder.service;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import etm.contrib.renderer.SimpleHtmlRenderer;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import etm.core.renderer.SimpleTextRenderer;

@Service
public class EtmService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EtmService.class);

	private static EtmMonitor etmMonitor;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value(value = "${etm.notification.enabled}")
	private boolean notificationEnabled;

	@Value(value = "${etm.notification.email}")
	private String notificationEmail;

	@PostConstruct
	public void init() {
		LOGGER.info("Initializing ETM monitor...");
		BasicEtmConfigurator.configure(true);
		etmMonitor = EtmManager.getEtmMonitor();
		if (!etmMonitor.isStarted()) {
			etmMonitor.start();
		} else {
			etmMonitor.reset();
		}
		LOGGER.info("ETM monitor successfully started");
	}

	@PreDestroy
	public void destroy() {
		// stop ETM monitor
		if (etmMonitor != null && etmMonitor.isStarted()) {
			etmMonitor.stop();
			LOGGER.info("ETM monitor successfully stopped");
		}
	}

	public EtmPoint createPoint(String symbolicName) {
		EtmPoint point = null;
		if (etmMonitor != null && etmMonitor.isStarted() && etmMonitor.isCollecting()) {
			point = etmMonitor.createPoint(symbolicName);
		}
		return point;
	}

	public void collect(EtmPoint point) {
		point.collect();
	}

	public double getTransactionTime(EtmPoint point) {
		if (point != null) {
			return point.getTransactionTime();
		}
		return -1;
	}

	@Scheduled(cron = "${etm.reset.cron-expression}")
	public void reset() {
		if (etmMonitor != null) {
			String subject = String.format("Fund Finder - Overview of measurement points for period [%s] - [%s]",
					DateFormatUtils.format(etmMonitor.getMetaData().getLastResetTime(), "yyyy-MM-dd HH:mm:ss"),
					DateFormatUtils.format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss"));
			StringWriter stringWriter = new StringWriter();
			etmMonitor.render(new SimpleTextRenderer(stringWriter));
			LOGGER.info("{}\n{}", subject, stringWriter.toString());
			if (notificationEnabled) {
				sendEmail(subject);
			}
			etmMonitor.reset();
		}
	}

	private void sendEmail(String subject) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				StringWriter stringWriter = new StringWriter();
				etmMonitor.render(new SimpleHtmlRenderer(stringWriter));

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("data", stringWriter.toString());

				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(notificationEmail);
				message.setSubject(subject);
				message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "email_etm.vm", "UTF-8", model), true);
			}
		};

		mailSender.send(preparator);

		//		Map<String, Object> model = new HashMap<String, Object>();
		//		model.put("data", stringWriter.toString());
		//
		//		SimpleMailMessage message = new SimpleMailMessage();
		//		message.setTo(notificationEmail);
		//		message.setSubject(subject);
		//		message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "email_etm.vm", "UTF-8", model));
		//		mailSender.send(message);
	}

}
