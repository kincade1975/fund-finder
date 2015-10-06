//package hr.betaware.fundfinder.messaging;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import hr.betaware.fundfinder.resource.TotalResource;
//
//@Service
//public class ScheduleTask {
//
//	@Autowired
//	private SimpMessagingTemplate template;
//
//	// this will send a message to an end-point on which a client can subscribe
//	@Scheduled(fixedRate = 5000)
//	public void trigger() {
//		// sends the message to /topic/message
//		TotalResource resource = new TotalResource();
//		resource.setTotalArticles(10L);
//		resource.setTotalInvestments(5L);
//		resource.setTotalTenders(3L);
//		resource.setTotalUsers(20L);
//		template.convertAndSend("/topic/total", resource);
//	}
//
//}
