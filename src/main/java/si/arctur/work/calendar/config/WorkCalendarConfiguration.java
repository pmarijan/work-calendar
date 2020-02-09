package si.arctur.work.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import java.time.format.DateTimeFormatter;

@Configuration
public class WorkCalendarConfiguration {

    //set default date format
    @Bean
    public FormattingConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();

        //default LocalDate format
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        //default LocalDateTime format
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        registrar.registerFormatters(conversionService);

        return conversionService;
    }
}
