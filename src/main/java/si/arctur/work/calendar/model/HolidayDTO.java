package si.arctur.work.calendar.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HolidayDTO {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private LocalDate date;
    @NotNull
    private Boolean workFree;
    private Set<WorkCalendarDTO> workCalendars;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getWorkFree() {
        return workFree;
    }

    public void setWorkFree(Boolean workFree) {
        this.workFree = workFree;
    }

    public Set<WorkCalendarDTO> getWorkCalendars() {
        if(Objects.isNull(workCalendars)) {
            workCalendars = new HashSet<>();
        }
        return workCalendars;
    }

    public void setWorkCalendars(Set<WorkCalendarDTO> workCalendars) {
        this.workCalendars = workCalendars;
    }

    @Override
    public String toString() {
        return "HolidayDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", workFree=" + workFree +
                ", workCalendars=" + workCalendars +
                '}';
    }
}
