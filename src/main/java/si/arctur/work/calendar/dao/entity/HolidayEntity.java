package si.arctur.work.calendar.dao.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "holiday", uniqueConstraints=
    @UniqueConstraint(columnNames={"name", "date"}))
public class HolidayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "work_free", nullable = false)
    private Boolean workFree;

    @ManyToMany(mappedBy = "holidays")
    private Set<WorkCalendarEntity> workCalendars;

    public HolidayEntity() {
    }

    public HolidayEntity(long id) {
        this.id = id;
    }

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

    public Set<WorkCalendarEntity> getWorkCalendars() {
        if(Objects.isNull(this.workCalendars)) {
            this.workCalendars = new HashSet<>();
        }
        return workCalendars;
    }

    public void setWorkCalendars(Set<WorkCalendarEntity> workCalendars) {
        this.workCalendars = workCalendars;
    }
}
