package si.arctur.work.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.WorkCalendarConverter;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.model.WorkCalendarDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkCalendarService {

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private WorkCalendarConverter workCalendarConverter;

	public List<WorkCalendarDTO> getWorkCalendars() {
		return calendarRepository.getAllWorkCalendars().stream().map(cal -> workCalendarConverter.convert(cal)).collect(Collectors.toList());
	}
}
