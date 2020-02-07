package si.arctur.work.calendar.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "workweek")
public class WorkweekEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "week_number", nullable = false, unique = false)
    private Integer weekNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_calendar_id", nullable = false)
    private WorkCalendarEntity workCalendar;

    public WorkweekEntity(long id) {
        this.id = id;
    }

    public WorkweekEntity() {
    }

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

    public WorkCalendarEntity getWorkCalendar() {
        return workCalendar;
    }

    public void setWorkCalendar(WorkCalendarEntity workCalendar) {
        this.workCalendar = workCalendar;
    }
}
