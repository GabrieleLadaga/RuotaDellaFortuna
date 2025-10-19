package it.game.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.game.model.Sector;
import it.game.service.facade.GameServiceFacade;
import it.game.service.observer.GameObserver;
import it.game.service.singleton.GameContext;

import java.util.List;

@Route("wheel")
@PageTitle("Ruota della Fortuna")
@CssImport("./../../../frontend/styles/main-styles.css")
@JsModule("./components/wheel-component.js")
public class WheelView extends VerticalLayout {

    private Span namePlayer;
    private final GameServiceFacade facade;

    public WheelView() {
        this.facade = GameContext.getInstance().getFacade();

        List<String> segmentNames = facade.getWheel().stream().map(Sector::value).toList();

        ObjectMapper mapper = new ObjectMapper();
        String segmentJson = "";
        try {
            segmentJson = mapper.writeValueAsString(segmentNames);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        getStyle().set("background", "linear-gradient(110deg, #00008B, #9ac4f5)");

        VerticalLayout card = new VerticalLayout();
        card.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        card.setSpacing(true); //spaziatura automatica
        card.setPadding(true); //padding automatico

        card.setWidth("600px");
        card.setHeight("750px");
        card.addClassName("card");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        H1 title = new H1("Gira la ruota");
        title.addClassName("title");

        namePlayer = new Span("Gira la ruota " + facade.getCurrentPlayer().getName() + "!");
        HorizontalLayout nameLayout = new HorizontalLayout(namePlayer);
        nameLayout.setSpacing(true);
        nameLayout.setAlignItems(Alignment.START);
        nameLayout.addClassName("label");

        Div wheelContainer = new Div();
        wheelContainer.getStyle()
                .set("position", "relative")
                .set("width", "400px")
                .set("height", "400px");

        Div indicator = new Div();
        indicator.getStyle()
                .set("position", "absolute")
                .set("top", "-10px")
                .set("left", "50%")
                .set("transform", "translate(-50%)")
                .set("wight", "0")
                .set("height", "0")
                .set("border-left", "15px solid transparent")
                .set("border-right", "15px solid transparent")
                .set("border-top", "20px solid red");

        Div wheel = new Div();
        wheel.getElement().setProperty("innerHTML", "<wheel-component style='width:400px;height:400px;display:block;'></wheel-component>");
        wheel.getElement().executeJs("this.querySelector('wheel-component').setAttribute('segments', $0);", segmentJson);

        Button spinButton = new Button(new Icon(VaadinIcon.PLAY));
        spinButton.addClassName("spin-button");
        spinButton.getStyle()
                  .set("position", "absolute")
                  .set("top", "50%")
                  .set("left", "50%")
                  .set("transform", "translate(-50%, -50%)");

        wheel.getElement().addEventListener("wheel-stopped",ev -> {
            String result = ev.getEventData().getString("event.detail");

            String titleText;
            String outcomeText = "Esito: " + result;

            if(result.equalsIgnoreCase("Bancarotta") || result.equalsIgnoreCase("Passa")) {
                titleText = "Mi dispiace, tocca all'avversario!";
            } else {
                titleText = "Complimenti!";
            }

            Dialog dialog = new Dialog();
            dialog.addClassName("dialog");

            dialog.add(new H1(titleText));
            dialog.add(new Div(new H1(outcomeText)));

            Button okButton = new Button("Ok", e -> {
                dialog.close();
                if(result.equalsIgnoreCase("Bancarotta") || result.equalsIgnoreCase("Passa")) {

                    updatePlayerName();
                    getUI().ifPresent(ui -> ui.navigate("wheel"));

                } else {

                    facade.startRound();
                    getUI().ifPresent(ui -> ui.navigate("board"));

                }
            });
            okButton.getStyle().set("margin-top", "15px");
            dialog.add(okButton);

            dialog.open();
        }).addEventData("event.detail");

        spinButton.addClickListener(e -> {
            String outcome = facade.spinWheel();
            int index = segmentNames.indexOf(outcome);

            if(index < 0) {
                Notification.show("Risultato non riconosciuto: " + outcome);
                return;
            }

            wheel.getElement().executeJs("this.querySelector('wheel-component').spinTo($0);", index);
        });

        wheelContainer.add(wheel, spinButton, indicator);
        card.add(title, nameLayout, wheelContainer);

        add(card);
    }

    private void updatePlayerName() {
        namePlayer.setText("Gira la ruota " + facade.getCurrentPlayer().getName() + "!");
    }

}
