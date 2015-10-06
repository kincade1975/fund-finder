package hr.betaware.fundfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class StompService {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private TotalService totalService;

	public void updateTotal() {
		template.convertAndSend("/topic/total", totalService.getTotal());
	}

}
