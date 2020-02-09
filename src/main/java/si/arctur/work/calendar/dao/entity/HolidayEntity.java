package si.arctur.work.calendar.dao.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "holiday", uniqueConstraints=
    @UniqueConstraint(columnNames={"name", "date"}))
public class HolidayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "work_free", nullable = false)
    private Boolean workFree;

    @ManyToMany(mappedBy = "holidays")
    private List<WorkCalendarEntity> workCalendars;

    public HolidayEntity() {
    }

    public HolidayEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<WorkCalendarEntity> getWorkCalendars() {
        if(Objects.isNull(this.workCalendars)) {
            this.workCalendars = new ArrayList<>();
        }
        return workCalendars;
    }

    public void setWorkCalendars(List<WorkCalendarEntity> workCalendars) {
        this.workCalendars = workCalendars;
    }
}
