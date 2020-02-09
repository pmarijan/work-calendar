package si.arctur.work.calendar.model;

import java.time.LocalDate;

public class DayDTO {
    private LocalDate date;
    private HolidayDTO holiday;
    private Boolean isWeekend;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HolidayDTO getHoliday() {
        return holiday;
    }

    public void setHoliday(HolidayDTO holiday) {
        this.holiday = holiday;
    }

    public Boolean getWeekend() {
        return isWeekend;
    }

    public void setWeekend(Boolean weekend) {
        isWeekend = weekend;
    }

    @Override
    public String toString() {
        return "DayDTO{" +
                "date=" + date +
                ", holiday=" + holiday +
                ", isWeekend=" + isWeekend +
                '}';
    }
}
