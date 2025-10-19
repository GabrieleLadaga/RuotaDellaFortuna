package it.game.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("start") // http://localhost:8080
@PageTitle("Ruota della Fortuna")
@CssImport("./../../../frontend/styles/main-styles.css")
public class StartView extends VerticalLayout {

    public StartView() {
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

        Div titleDiv = new Div();
        titleDiv.setHeight("170px");

        H1 title = new H1("Ruota della Fortuna");
        title.addClassName("title");

        Div spacer = new Div();
        spacer.setHeight("100px");

        Button play = new Button("Play", new Icon(VaadinIcon.PLAY));
        Button history = new Button("History", new Icon(VaadinIcon.BAR_CHART));

        play.addClassName("button");
        history.addClassName("button");

        play.addClickListener(e -> UI.getCurrent().navigate("offline-setup"));
        history.addClickListener(e -> UI.getCurrent().navigate("history"));

        card.add(titleDiv, title, spacer, play, history);
        add(card);
    }

}
