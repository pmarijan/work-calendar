package si.arctur.work.calendar.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "workweek", uniqueConstraints=
    @UniqueConstraint(columnNames={"week_number", "work_calendar_id"}))
public class WorkweekEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_calendar_id", nullable = false)
    private WorkCalendarEntity workCalendar;

    public WorkweekEntity(Long id) {
        this.id = id;
    }

    public WorkweekEntity() {
    }

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

    public WorkCalendarEntity getWorkCalendar() {
        return workCalendar;
    }

    public void setWorkCalendar(WorkCalendarEntity workCalendar) {
        this.workCalendar = workCalendar;
    }
}
