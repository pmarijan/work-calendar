package si.arctur.work.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import si.arctur.work.calendar.converter.WorkCalendarConverter;
import si.arctur.work.calendar.dao.entity.WorkCalendarEntity;
import si.arctur.work.calendar.dao.repository.CalendarRepository;
import si.arctur.work.calendar.model.WorkCalendarDTO;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkCalendarService {

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private WorkCalendarConverter workCalendarConverter;

	public List<WorkCalendarDTO> getWorkCalendars(String description, String name, String workDays, Integer year) {

		WorkCalendarEntity workCalendarEntity = new WorkCalendarEntity();
		workCalendarEntity.setDescription(description);
		workCalendarEntity.setName(name);
		workCalendarEntity.setWorkdays(workDays);
		workCalendarEntity.setYear(year);

		return calendarRepository.findAll(Example.of(workCalendarEntity)).stream().map(cal -> workCalendarConverter.convert(cal)).collect(Collectors.toList());
	}

	public WorkCalendarDTO getWorkCalendar(Long id) {
		return workCalendarConverter.convert(calendarRepository.getWorkCalendarEntityById(id));
	}
}