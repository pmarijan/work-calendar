package si.arctur.work.calendar.model;

public class WorkweekDTO {
    private long id;
    private String description;
    private Integer weekNumber;
    private WorkCalendarDTO workCalendar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public WorkCalendarDTO getWorkCalendar() {
        return workCalendar;
    }

    public void setWorkCalendar(WorkCalendarDTO workCalendar) {
        this.workCalendar = workCalendar;
    }
}