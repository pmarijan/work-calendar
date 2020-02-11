package si.arctur.work.calendar.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

//@Converter(autoApply = true)
@Component
@Deprecated
public class EnumSetToStringConverter implements AttributeConverter<Set<DayOfWeek>, String> {
    private static final Logger LOG = LoggerFactory.getLogger(EnumSetToStringConverter.class);

    @Override
    public String convertToDatabaseColumn(Set<DayOfWeek> dayOfWeeks) {
        LOG.info("dayOfWeeks={}", dayOfWeeks);
        if(Objects.isNull(dayOfWeeks)) {
            return null;
        }
        return dayOfWeeks.stream().map(d -> d.name()).collect(Collectors.joining(","));
    }

    @Override
    public Set<DayOfWeek> convertToEntityAttribute(String input) {
        LOG.info("input={}", input);

        if(StringUtils.isBlank(input)) {
            return null;
        }

        EnumSet<DayOfWeek> set = EnumSet.noneOf(DayOfWeek.class);
        for (String day : input.split(",")) {
            set.add(DayOfWeek.valueOf(day));
        }
        return set;
    }
}
