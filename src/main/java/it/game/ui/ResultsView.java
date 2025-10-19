package it.game.ui;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.game.model.Player;
import it.game.service.facade.GameServiceFacade;
import it.game.service.singleton.GameContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Route("results")
@CssImport("./../../../frontend/styles/main-styles.css")
@PageTitle("Ruota della Fortuna")
public class ResultsView extends VerticalLayout {

    public ResultsView() {
        GameServiceFacade facade = GameContext.getInstance().getFacade();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle().set("background", "linear-gradient(110deg, #00008B, #9ac4f5)");

        VerticalLayout card = new VerticalLayout();
        card.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        card.setSpacing(true); //spaziatura automatica
        card.setPadding(true); //padding automatico

        card.setWidth("600px");
        card.setHeight("750px");
        card.addClassName("card");

        H1 title = new H1("Classifica Finale");
        title.addClassName("title");

        List<Player> players = new ArrayList<>(facade.getPlayers());

        players.sort(Comparator.comparingInt(Player::getScore).reversed());

        Div podium = createPodium(players);

        VerticalLayout rankingList = new VerticalLayout();
        rankingList.addClassName("ranking-list");
        rankingList.setHeight("250px");
        for(int i=0; i<players.size(); i++) {
            Player p = players.get(i);
            Div row = new Div(new Span((i + 1) + "°) " + p.getName() + " - " + p.getScore() + "€"));
            row.addClassName("ranking-row");
            rankingList.add(row);
        }

        Button startButton = new Button("Menù Start");
        startButton.addClassName("button");
        startButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("start")));

        card.add(title, podium, rankingList, startButton);
        add(card);
    }

    private Div createPodium(List<Player> players) {
        Div podium = new Div();
        podium.addClassName("podium");
        podium.setWidth(400, Unit.PIXELS);

        Div first = createPodiumBlock(players.get(0), "first");
        Div second = players.size() > 1 ? createPodiumBlock(players.get(1), "second") : null;
        Div third = players.size() > 2 ? createPodiumBlock(players.get(2), "third") : null;

        if(third != null) podium.add(third);
        podium.add(first);
        if(second != null) podium.add(second);

        return podium;
    }

    private Div createPodiumBlock(Player player, String positionClass) {
        Div block = new Div();
        block.addClassName("podium-block");
        block.addClassName(positionClass);

        Span name = new Span(player.getName());
        Span score = new Span(player.getScore() + "€");

        block.add(name, score);
        return block;
    }

}
