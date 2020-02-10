package si.arctur.work.calendar.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.PropertyEditorSupport;
import java.time.DayOfWeek;
import java.util.EnumSet;

public class StringToEnumSetConverter extends PropertyEditorSupport {
    private static final Logger LOG = LoggerFactory.getLogger(StringToEnumSetConverter.class);

    public void setAsText(final String text) throws IllegalArgumentException {
        LOG.info("text={}", text);
        EnumSet<DayOfWeek> set = EnumSet.noneOf(DayOfWeek.class);
        if(StringUtils.isNotBlank(text)) {
            for (String day : text.split(",")) {
                set.add(DayOfWeek.valueOf(day));
            }
        }

        LOG.info("set={}", set);
        setValue(set);
    }
}
