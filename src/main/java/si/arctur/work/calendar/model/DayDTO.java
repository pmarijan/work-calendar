package si.arctur.work.calendar.model;

import java.time.LocalDate;

public class DayDTO {
    private LocalDate date;
    private String dayOfWeek;
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return "DayDTO{" +
                "date=" + date +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", holiday=" + holiday +
                ", isWeekend=" + isWeekend +
                '}';
    }
}
