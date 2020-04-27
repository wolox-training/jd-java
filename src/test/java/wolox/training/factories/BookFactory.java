package wolox.training.factories;

import com.github.javafaker.Faker;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class BookFactory {

    private Faker faker = new Faker();

    public HashMap<String, Object> book() {
        return new HashMap<String, Object>() {{
            put("author", faker.book().author());
            put("image", faker.internet().image());
            put("title", faker.book().title());
            put("subtitle", faker.book().title());
            put("publisher", faker.book().publisher());
            put("year", getYear());
            put("pages", faker.number().digits(3));
            put("isbn", faker.hashCode());
        }};
    }

    private int getYear() {
        Date date = faker.date().past(faker.random().nextInt(0, 1000), TimeUnit.DAYS);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
