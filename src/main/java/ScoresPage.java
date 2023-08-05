import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.UIAssertionError;
import core.dto.Event;
import org.testng.FileAssert;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.fail;

public class ScoresPage {
    private String eventsNotStartedLoc = "//*[contains(@id, 'home-team-score') and .='']/../..//*[@class='kt' and contains(.,':')]/ancestor::*[@class='gp kp']";
    private String applyButtonLoc = "//*[.='Apply']";


    public void closeCookieBar() {
        SelenideElement cookieBarElem = $x("//*[@id='simpleCookieBarCloseButton']");
        if (cookieBarElem.isDisplayed()) {
            cookieBarElem.click();
        }
    }

    public void goToScoresPage() {
        SelenideElement scorePageElem = tryShouldHave($x("//*[contains(@id,'Scores__top')]"), "Scores page is not visible");
        if (!scorePageElem.attr("id").contains("active")) {
            scorePageElem.click();
        }
    }

    private SelenideElement getRandomEventNotStarted() {
        ElementsCollection eventsElem = tryShouldHave($$x(eventsNotStartedLoc), sizeGreaterThan(0), "Don't have any events not started");
        return eventsElem.get(new Random().nextInt(eventsElem.size())).shouldHave(visible);
    }


    public String getTimeFromEventDD() {
        return tryShouldHave($x("//*[@class='zh']/*[@id='score-or-time']"), "Time is not visible").text();
    }


    public Event goToRandomEventNotStarted() {
        SelenideElement randomEvent = getRandomEventNotStarted();
        Event event = new Event(randomEvent);
        randomEvent.click();
        String timeFromDd = getTimeFromEventDD();
        String timeFromTitle = event.getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        assertThat("Time from title event not equals from dd", timeFromDd, equalTo(timeFromTitle));
        return event;
    }


    public void goToSettingsPage() {
        $x("//*[contains(@data-src,'hamburger-menu')]").should(visible).click();
        tryShouldHave($x("//span[.='Settings']"), "Settings page is not visible").click();
        tryShouldHave($x("//*[@id='content-center']//h1"), text("Settings"), "Settings page was not loaded");
    }

    public Map<String, String> setRandomTimeZoneExcludeAuto() {
        SelenideElement timeZoneElem = $x("//*[@id='TZ_SELECT-label']");
        String timeBefore = timeZoneElem.text();
        timeZoneElem.click();
        ElementsCollection ddElems = $$x("//*[@class='Xi selectItem' and not(.='Automatic')]").should(sizeGreaterThan(0));
        SelenideElement randomTime = ddElems.get(new Random().nextInt(ddElems.size()));
        Map<String, String> timeMap = new HashMap<>();
        timeMap.put("before", timeBefore);
        timeMap.put("after", randomTime.text());
        randomTime.click();
        $x(applyButtonLoc).click();
        tryShouldHave($x("//*[@id='Scores__top-menu-link-active']"), "Scores is not active aft  er click on 'Apply' button");
        return timeMap;
    }


    public static ElementsCollection tryShouldHave(ElementsCollection collection, CollectionCondition condition, String failText) {
        try {
            collection.shouldHave(condition);
        } catch (UIAssertionError e) {
            FileAssert.fail(failText);
        }
        return collection;
    }


    public static SelenideElement tryShouldHave(SelenideElement element, String failText) {
        return tryShouldHave(element, visible, failText);
    }

    public static SelenideElement tryShouldHave(SelenideElement element, Condition condition, String failText) {
        try {
            element.should(condition);
        } catch (UIAssertionError e) {
            fail(failText);
        }
        return element;
    }

}




