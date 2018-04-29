package nl.shanelab.kwetter.web.jsfbean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Named
@RequestScoped
public class LocaleServlet implements JsfBeanServlet {

    public String formatDate(Date date) {
        return this.formatDate(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    public String formatDate(LocalDate date) {
        return this.formatDate(date.atStartOfDay());
    }

    public String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
