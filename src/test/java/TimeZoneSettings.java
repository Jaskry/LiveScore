import com.codeborne.selenide.Configuration;
import core.dto.Event;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TimeZoneSettings {

    private final ScoresPage scoresPage = new ScoresPage();

    @BeforeClass(alwaysRun = true)
    public void beforeClassTimeZoneSettings() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;


        open("https://www.livescore.com/en/");
        scoresPage.closeCookieBar();
        scoresPage.goToScoresPage();
    }

    @Test
    public void verifyTimeZoneSettings() {
        Event event = scoresPage.goToRandomEventNotStarted();
        scoresPage.goToSettingsPage();
        Map<String, String> timeMap = scoresPage.setRandomTimeZoneExcludeAuto();
        ZoneId zoneBefore = ZoneId.of(timeMap.get("before").replaceAll("\\s", ""));
        ZoneId zoneAfter = ZoneId.of(timeMap.get("after").replaceAll("\\s", ""));

        ZonedDateTime timeZoneExpected = event.getLocalDateTime().atZone(zoneBefore).withZoneSameInstant(zoneAfter);
        String timeExpected = timeZoneExpected.format(DateTimeFormatter.ofPattern("HH:mm"));
        String timeActual = scoresPage.getTimeFromEventDD();

        assertThat("Time is not equals expcted after change timeZone", timeActual, equalTo(timeExpected));
    }
}


