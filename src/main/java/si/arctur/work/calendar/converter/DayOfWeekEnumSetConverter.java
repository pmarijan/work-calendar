package si.arctur.work.calendar.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

@Component
public class DayOfWeekEnumSetConverter implements Converter<String, Set<DayOfWeek>> {

    @Override
    public Set<DayOfWeek> convert(String s) {
        EnumSet<DayOfWeek> set = EnumSet.noneOf(DayOfWeek.class);
        if(StringUtils.isNotBlank(s)) {
            for (String day : s.split(",")) {
                set.add(DayOfWeek.valueOf(day));
            }
        }
        return set;
    }
}
