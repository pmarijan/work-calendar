package si.arctur.work.calendar.dao.entity;

import si.arctur.work.calendar.dao.EnumSetToStringConverter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "work_calendar", uniqueConstraints=
    @UniqueConstraint(columnNames={"name", "year"}))
public class WorkCalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "workdays")
    private String workdays;

    @Column(name = "year", nullable = false)
    private Integer year;

    @OneToMany(mappedBy="workCalendar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<WorkweekEntity> workweekSet;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "work_calendar2holiday",
                joinColumns = @JoinColumn(name = "work_calendar_id"), inverseJoinColumns = @JoinColumn(name = "holiday_id"))
    private Set<HolidayEntity> holidays;

    public WorkCalendarEntity() {}

    public WorkCalendarEntity(Long id) {
        this.id = id;
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

    public Set<WorkweekEntity> getWorkweekSet() {
        if(Objects.isNull(this.workweekSet)) {
            this.workweekSet = new HashSet<>();
        }
        return workweekSet;
    }

    public void setWorkweekSet(Set<WorkweekEntity> workweekSet) {
        this.workweekSet = workweekSet;
    }

    public Set<HolidayEntity> getHolidays() {
        if(Objects.isNull(holidays)) {
            holidays = new HashSet<>();
        }
        return holidays;
    }

    public void setHolidays(Set<HolidayEntity> holidays) {
        this.holidays = holidays;
    }
}
