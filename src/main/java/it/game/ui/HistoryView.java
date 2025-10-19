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

        H1 title = new H1("Storico Partite - Offline");
        title.addClassName("title");

        Div spacer = new Div();
        spacer.setHeight("50px");

        Grid<HistoryManager.MatchHistory> grid = new Grid<>(HistoryManager.MatchHistory.class, false);
        grid.addColumn(HistoryManager.MatchHistory::getId).setHeader("ID");
        grid.addColumn(HistoryManager.MatchHistory::getDate).setHeader("Data");
        grid.addColumn(HistoryManager.MatchHistory::getPlayer1).setHeader("Giocatore 1");
        grid.addColumn(HistoryManager.MatchHistory::getPlayer2).setHeader("Giocatore 2");
        grid.addColumn(HistoryManager.MatchHistory::getPlayer3).setHeader("Giocatore 3");
        grid.addColumn(HistoryManager.MatchHistory::getPlayer4).setHeader("Giocatore 4");
        grid.addColumn(HistoryManager.MatchHistory::getPlayer5).setHeader("Giocatore 5");
        grid.addColumn(HistoryManager.MatchHistory::getWinner).setHeader("Vincitore");
        grid.addColumn(HistoryManager.MatchHistory::getPrize).setHeader("Premio (€)");

        grid.setWidth("90%");
        grid.setHeight("600px");

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
