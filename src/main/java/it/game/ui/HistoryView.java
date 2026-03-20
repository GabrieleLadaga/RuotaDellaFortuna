package it.game.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.game.service.singleton.HistoryManager;

import java.sql.SQLException;
import java.util.List;

@Route("history")
@PageTitle("Ruota della Fortuna")
@CssImport("./../../../frontend/styles/main-styles.css")
public class HistoryView extends VerticalLayout {

    public HistoryView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSpacing(false);
        setPadding(false);

        getStyle().set("background", "linear-gradient(110deg, #00008B, #9ac4f5)");

        VerticalLayout card = new VerticalLayout();
        card.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        card.setSpacing(true);
        card.setPadding(true);
        card.setWidth("920px");
        card.setHeight("700px");
        card.addClassName("card");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        Div titleDiv = new Div();
        titleDiv.setWidth("20px");

        H1 title = new H1("Storico Partite - Offline");
        title.addClassName("title");

        Div spacer = new Div();
        spacer.setHeight("10px");

        Grid<HistoryManager.MatchHistory> grid = new Grid<>(HistoryManager.MatchHistory.class, false);

        grid.setWidthFull();
        grid.setHeight("300px");

        grid.setColumnReorderingAllowed(true);

        grid.addColumn(HistoryManager.MatchHistory::getId)
                .setHeader("ID")
                .setFlexGrow(1)
                .setAutoWidth(true);

        grid.addColumn(HistoryManager.MatchHistory::getDate)
                .setHeader("Data")
                .setFlexGrow(2)
                .setAutoWidth(true);

        grid.addColumn(match -> {
            String player = match.getPlayer1();
            return (player != null && !player.isEmpty()) ? player : "/";
        }).setHeader("Giocatore 1").setFlexGrow(2).setAutoWidth(true);

        grid.addColumn(match -> {
            String player = match.getPlayer2();
            return (player != null && !player.isEmpty()) ? player : "/";
        }).setHeader("Giocatore 2").setFlexGrow(2).setAutoWidth(true);

        grid.addColumn(match -> {
            String player = match.getPlayer3();
            return (player != null && !player.isEmpty()) ? player : "/";
        }).setHeader("Giocatore 3").setFlexGrow(2).setAutoWidth(true);

        grid.addColumn(match -> {
            String player = match.getPlayer4();
            return (player != null && !player.isEmpty()) ? player : "/";
        }).setHeader("Giocatore 4").setFlexGrow(2).setAutoWidth(true);

        grid.addColumn(match -> {
            String player = match.getPlayer5();
            return (player != null && !player.isEmpty()) ? player : "/";
        }).setHeader("Giocatore 5").setFlexGrow(2).setAutoWidth(true);

        grid.addColumn(match -> {
            String winner = match.getWinner();
            return (winner != null && !winner.isEmpty()) ? winner : "/";
        }).setHeader("Vincitore").setFlexGrow(2).setAutoWidth(true);

        grid.addColumn(match -> {
            String prize = String.valueOf(match.getPrize());
            return (prize != null && !prize.isEmpty()) ? prize : "/";
        }).setHeader("Premio (€)").setFlexGrow(1).setAutoWidth(true);

        grid.getStyle().set("overflow-y", "auto");
        grid.getStyle().set("overflow-x", "hidden");

        try {
            List<HistoryManager.MatchHistory> matches = HistoryManager.getInstance().getAllMatch();
            grid.setItems(matches);
        } catch (SQLException e) {
            System.err.println("Errore nel caricamento dei dati dal database: " + e.getMessage());
        }

        Button backButton = new Button("Back", new Icon(VaadinIcon.ARROW_BACKWARD));
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("start")));
        backButton.addClassName("button-arrow");

        card.add(titleDiv, title, spacer, grid, backButton);
        add(card);
    }
}