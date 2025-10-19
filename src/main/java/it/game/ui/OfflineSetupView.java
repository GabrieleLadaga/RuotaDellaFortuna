package it.game.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.game.service.singleton.GameContext;
import it.game.ui.components.CustomSlider;

import java.util.ArrayList;
import java.util.List;

@Route("offline-setup")
@PageTitle("Ruota della Fortuna")
public class OfflineSetupView extends VerticalLayout {
    private final CustomSlider playerSlider;
    private final CustomSlider roundSlider;
    private final VerticalLayout nameLayout;
    private final List<TextField> nameField = new ArrayList<>();

    public OfflineSetupView() {
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
        titleDiv.setWidth("20px");

        // -- Titolo --
        H1 title = new H1("Setup della Partita");
        title.addClassName("title");

        Div spacer = new Div();
        spacer.setHeight("10px");

        // -- Sezione Slider Giocatori e Rounds --
        HorizontalLayout slidersLayout = new HorizontalLayout();
        slidersLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        slidersLayout.setSpacing(true);
        slidersLayout.setWidthFull();
        slidersLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        // -- Numero di Giocatori --
        Span playerLabel = new Span("Numero di Giocatori");
        playerLabel.addClassName("label");

        playerSlider = new CustomSlider();
        playerSlider.setMin(2);
        playerSlider.setMax(5);
        playerSlider.setValue(3);

        playerSlider.addValueChangeListener(e -> updatePlayerFields(e.getValue()));

        VerticalLayout playerLayout = new VerticalLayout(playerLabel, playerSlider);
        playerLayout.setAlignItems(Alignment.CENTER);

        // -- Numero di Rounds --
        Span roundLabel = new Span("Numero di Rounds");
        roundLabel.addClassName("label");

        roundSlider = new CustomSlider();
        roundSlider.setMin(1);
        roundSlider.setMax(10);
        roundSlider.setValue(5);

        VerticalLayout roundLayout = new VerticalLayout(roundLabel, roundSlider);
        roundLayout.setAlignItems(Alignment.CENTER);

        slidersLayout.add(playerLayout, roundLayout);

        // -- Sezione Nomi --
        Span namesLabel = new Span("Nomi");
        namesLabel.addClassName("label");

        nameLayout = new VerticalLayout();
        nameLayout.setPadding(false);
        nameLayout.setSpacing(true);
        nameLayout.setHeight("280px");
        nameLayout.setAlignItems(Alignment.CENTER);

        updatePlayerFields(2);

        Button playButton = new Button("Gioca", e -> startGame());
        playButton.addClassName("button");

        card.add(titleDiv, title, spacer, slidersLayout, namesLabel, nameLayout, playButton);
        add(card);
    }

    private void updatePlayerFields(int count) {
        nameLayout.removeAll();
        nameField.clear();

        for (int i = 1; i <= count; i++) {
            TextField field = new TextField();
            field.setWidth("250px");
            field.addClassName("name-field");
            field.setPlaceholder("Giocatore " + i);
            nameField.add(field);
            nameLayout.add(field);
        }
    }

    private void startGame() {
        int numRounds = roundSlider.getValue();
        List<String> names = new ArrayList<>();

        for(TextField field : nameField) {
            String name = field.getValue().trim().isEmpty() ? field.getPlaceholder() : field.getValue().trim();
            names.add(name);
        }

        GameContext ctx = GameContext.getInstance();
        ctx.getFacade().startGame(names, numRounds);

        getUI().ifPresent(ui -> ui.navigate("wheel"));
    }
}
