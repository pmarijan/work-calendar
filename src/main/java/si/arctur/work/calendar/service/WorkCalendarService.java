package si.arctur.work.calendar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkCalendarService {

	@Value("${name:World}")
	private String name;

	public String getHelloMessage() {
		return "Hello " + this.name;
	}

}
