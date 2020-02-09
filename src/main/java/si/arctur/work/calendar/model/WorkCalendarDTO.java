package si.arctur.work.calendar.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class WorkCalendarDTO {
    private Long id;
    private String description;
    @NotNull
    private String name;
    private String workdays;
    @NotNull
    @Min(0)
    private Integer year;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkdays() {
        return workdays;
    }

    public void setWorkdays(String workdays) {
        this.workdays = workdays;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "WorkCalendarDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", workdays='" + workdays + '\'' +
                ", year=" + year +
                '}';
    }
}
