package si.arctur.work.calendar.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class WorkweekDTO {
    private Long id;
    @NotNull
    private String description;
    @NotNull
    @Min(1)
    @Max(54)
    private Integer weekNumber;

    private WorkCalendarDTO workCalendar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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