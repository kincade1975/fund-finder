package hr.betaware.fundfinder.resource;

import java.util.ArrayList;
import java.util.List;

public class ValidationResource {

	private List<String> messages = new ArrayList<>();

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
