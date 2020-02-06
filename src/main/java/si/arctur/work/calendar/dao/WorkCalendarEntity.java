package si.arctur.work.calendar.dao;

import javax.persistence.*;

@Entity
@Table(name = "work_calendar")
public class WorkCalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "description", nullable = true, unique = true)
    private String description;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "workdays", nullable = true, unique = false)
    private String workdays;

    @Column(name = "year", nullable = false, unique = false)
    private Integer year;

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
}
