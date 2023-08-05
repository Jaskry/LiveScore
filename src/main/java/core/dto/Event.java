package core.dto;

import com.codeborne.selenide.SelenideElement;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private LocalDateTime localDateTime;
    private String opponentUp;
    private String opponentDown;
    private String category;
    private String eventId;

    public Event(SelenideElement eventElem) {
        String time = eventElem.$x(".//*[@class='kt']").text();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        this.localDateTime = LocalDateTime.now().with(LocalTime.parse(time, formatter));
        this.opponentUp = eventElem.$x(".//*[@class='op']").text();
        this.opponentDown = eventElem.$x(".//*[@class='qp']").text();
        this.category = eventElem.$x("./ancestor::div[@data-index]/*[@class='Ld']").text();
        this.eventId = eventElem.attr("id");
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

}
