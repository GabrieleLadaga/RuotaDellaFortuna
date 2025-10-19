package it.game.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.game.model.Player;
import it.game.service.GameManager;
import it.game.service.facade.GameServiceFacade;
import it.game.service.memento.BoardMemento;
import it.game.service.observer.GameObserver;
import it.game.service.singleton.GameContext;

import java.util.ArrayList;
import java.util.List;

@Route("board")
@PageTitle("Ruota della Fortuna")
@CssImport("./../../../frontend/styles/main-styles.css")
public class BoardView extends VerticalLayout implements GameObserver {

    private static final int ROWS = 4;
    private static final int COLUMNS = 12;

    private final GameServiceFacade facade;

    private Div[][] cells;
    private Span partialSpan;
    private Span totalSpan;
    private Span wheelValueSpan;
    private Span namePlayer;
    private final TextField inputLetter;

    private final Button buyVowelButton;
    private final Button giveSolutionButton;
    private final Button spinWheelButton;

    private boolean isBuyingVowel = false;
    private boolean isSolving = false;
    private final StringBuilder proposedSolution = new StringBuilder();

    private final List<int[]> userFilledCells = new ArrayList<>();
    private final Button deleteLetterButton;

    private BoardMemento memento;

    public BoardView() {
        this.facade = GameContext.getInstance().getFacade();
        GameManager gameManager = GameContext.getInstance().getGameManager();
        gameManager.attach(this);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background", "linear-gradient(110deg, #00008B, #9ac4f5)");

        // -- main layout card --
        H1 title = new H1("Tabellone");
        title.addClassName("title");

        VerticalLayout main = new VerticalLayout();
        main.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        main.setSpacing(true);
        main.setWidth("800px");
        main.setHeight("700px");
        main.addClassName("card");

        // -- info / board / controls --
        HorizontalLayout info = createInfoPanel();
        Div board = createBoard();

        Div category = new Div();
        category.setHeight("40px");
        category.setWidth("100%");
        category.addClassName("category");
        category.setText("Categoria: " + facade.getCategory());

        inputLetter = new TextField("Inserisci una Lettera");
        inputLetter.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        inputLetter.setMaxLength(1);
        inputLetter.setWidth("200px");

        deleteLetterButton = new Button(new Icon(VaadinIcon.ARROW_BACKWARD), e -> removeLastInsertedLetter());
        deleteLetterButton.setEnabled(false);

        HorizontalLayout inputLayout = new HorizontalLayout(inputLetter, deleteLetterButton);
        inputLayout.setSpacing(true);
        inputLayout.setAlignItems(Alignment.END);

        buyVowelButton = new Button("Compra una vocale", e -> toggleBuyVowelMode());
        buyVowelButton.setEnabled(false);

        spinWheelButton = new Button("Gira la ruota", e -> getUI().ifPresent(ui -> ui.navigate("wheel")));
        spinWheelButton.setEnabled(false);

        giveSolutionButton = new Button("Dai la soluzione", e -> toggleSolutionMode());

        HorizontalLayout buttonLayout = new HorizontalLayout(giveSolutionButton, spinWheelButton, buyVowelButton);
        buttonLayout.setSpacing(true);

        // -- inputLetter listener --
        inputLetter.addValueChangeListener(e -> {
            String val = e.getValue();
            if (val == null || val.isBlank()) return;

            char ch = val.trim().toUpperCase().charAt(0);

            if(isSolving) {
                if(Character.isLetter(ch) || ch == ' ') insertNextLetter(ch);

                inputLetter.clear();
                return;
            }

            if(facade.alreadyCalledLetter(ch)) {
                showLetterAlreadyCalledDialog(ch);
                inputLetter.clear();
                return;
            }

            if(isBuyingVowel) {
                if("AEIOU".indexOf(ch) == -1) {
                    Notification.show("Inserisci solo vocali!");
                    inputLetter.clear();
                    return;
                }

                int currentPartial = facade.getAllPartialJackpot().get(facade.getTurn());
                if(currentPartial < 300) {
                    Notification.show("Montepremi insufficiente");
                    inputLetter.clear();
                    return;
                }

                boolean ok = facade.buyVowel(ch);
                if(ok) {
                    Notification.show("Hai acquistato la vocale: " + ch + "!");
                    update();

                    if(facade.checkIfSolvedAfterGuess()) showRoundWonDialog();

                } else {
                    Dialog dialog = new Dialog();
                    dialog.addClassName("dialog");
                    dialog.add(new H1("Vocale non presente!"));
                    dialog.add(new H2("Il turno passa al giocatore successivo."));

                    Button okButton = new Button("Ok", ev -> {
                        dialog.close();
                        GameContext.getInstance().getGameManager().nextTurn();
                        update();

                        getUI().ifPresent(ui -> ui.navigate("wheel"));
                    });
                    okButton.getStyle().set("margin-top", "15px");
                    dialog.add(okButton);
                    dialog.open();
                }
                toggleBuyVowelMode();
            } else {
                if("AEIOU".indexOf(ch) != -1) {
                    Notification.show("Inserisci solo una consonante!");
                    inputLetter.clear();
                    return;
                }

                if(!facade.canInsertConsonant()) {
                    Notification.show("Devi prima girare la ruota prima di inserire un'altra consonante!");
                    inputLetter.clear();
                    return;
                }

                boolean found = facade.guessLetter(ch);
                if (found) {
                    Notification.show("Lettera trovata");
                    buyVowelButton.setEnabled(true);
                    spinWheelButton.setEnabled(true);
                    update();

                    if(facade.checkIfSolvedAfterGuess()) showRoundWonDialog();

                } else {
                    Dialog dialog = new Dialog();
                    dialog.addClassName("dialog");

                    dialog.add(new H1("Lettera non presente!"));
                    dialog.add(new H2("Il turno passa al giocatore successivo!"));

                    Button okButton = new Button("Ok", ev -> {
                        dialog.close();
                        buyVowelButton.setEnabled(false);
                        spinWheelButton.setEnabled(false);
                        getUI().ifPresent(ui -> ui.navigate("wheel"));
                    });
                    okButton.getStyle().set("margin-top", "15px");
                    dialog.add(okButton);

                    dialog.open();
                }
            }
            inputLetter.clear();
        });

        // -- compose main content --
        Div titleDiv = new Div();
        titleDiv.setWidth("20px");

        Div spacer = new Div();
        spacer.setHeight("40px");

        main.add(titleDiv, title, spacer, board, category, info, inputLayout, buttonLayout);

        add(main);

        update();
    }

    private void showLetterAlreadyCalledDialog(char letter) {
        Dialog dialog = new Dialog();
        dialog.addClassName("dialog");

        dialog.add(new H1("Lettera già chiamata!"));
        dialog.add(new H2("La lettera " + letter + " è già stata chiamata in precedenza."));
        dialog.add(new H2("Il turno passa al giocatore successivo."));

        Button okButton = new Button("Ok", ev -> {
            dialog.close();
            buyVowelButton.setEnabled(false);
            spinWheelButton.setEnabled(false);
            GameContext.getInstance().getGameManager().nextTurn();
            getUI().ifPresent(ui -> ui.navigate("wheel"));
        });
        okButton.getStyle().set("margin-top", "15px");
        dialog.add(okButton);

        dialog.open();
    }

    private void showRoundWonDialog() {
        Dialog dialog = new Dialog();
        dialog.addClassName("dialog");
        dialog.add(new H1("Hai completato la frase!"));
        dialog.add(new H2("Complimenti, round vinto!"));

        Button okButton = new Button("Ok", ev -> {
            dialog.close();
            facade.endRound();
            if(facade.nextRound()) {
                facade.startRound();
                getUI().ifPresent(ui -> ui.navigate("wheel"));
            } else {
                getUI().ifPresent(ui -> ui.navigate("results"));
            }
        });
        dialog.add(okButton);
        dialog.open();
    }

    private void toggleSolutionMode() {
        isSolving = !isSolving;

        if(isSolving) {
            memento = saveCurrentBoardState();

            giveSolutionButton.getStyle().set("background-color", "#32CD32");
            Notification.show("Modalità soluzione attivata! Digita direttamente sul tabellone.");

            deleteLetterButton.setEnabled(true);
            inputLetter.setEnabled(true);
            proposedSolution.setLength(0); //reset
            userFilledCells.clear();
        } else {
            giveSolutionButton.getStyle().remove("background-color");
            Notification.show("Modalità soluzione disattivata.");

            deleteLetterButton.setEnabled(false);
            inputLetter.clear();

            restoreSavedBoardState();
            userFilledCells.clear();
            proposedSolution.setLength(0); //reset
        }
    }

    private BoardMemento saveCurrentBoardState() {
        return facade.saveBoardState(cells);
    }

    private void restoreSavedBoardState() {
        facade.restoreBoardState(memento, cells);
        update();
    }

    private void insertNextLetter(char ch) {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                Div cell = cells[i][j];
                String bgColor = cell.getStyle().get("background-color");
                if("#e0e0e0".equals(bgColor) && cell.getText().isBlank()) {
                    cell.setText(String.valueOf(ch));
                    cell.getStyle().set("background-color", "#ffffff");
                    userFilledCells.add(new int[]{i, j});
                    proposedSolution.append(ch);
                    checkIfSolutionComplete();
                    return;
                }
            }
        }
    }

    private void removeLastInsertedLetter() {
        if(userFilledCells.isEmpty()) {
            Notification.show("Nessuna lettera da cancellare");
            return;
        }

        int[] last = userFilledCells.remove(userFilledCells.size() - 1);
        Div cell = cells[last[0]][last[1]];
        cell.setText("");
        cell.getStyle().set("background-color", "#e0e0e0");

        if(proposedSolution.length() > 0) proposedSolution.setLength(proposedSolution.length() - 1);
    }

    private void checkIfSolutionComplete() {
        boolean allFilled = true;
        for(int i = 0; i < ROWS && allFilled; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                Div cell = cells[i][j];
                String bgColor = cell.getStyle().get("background-color");
                if("#e0e0e0".equals(bgColor) && cell.getText().isBlank()) {
                    allFilled = false;
                    break;
                }
            }
        }

        if(allFilled) {
            StringBuilder guess = reconstructPhraseFromBuilder();

            boolean correct = facade.solvePuzzle(guess.toString().trim());
            if(correct) {
                update();

                Dialog dialog = new Dialog();
                dialog.addClassName("dialog");

                dialog.add(new H1("Soluzione corretta. Hai vinto!"));
                Button okButton = new Button("Ok", e -> {
                    dialog.close();
                    facade.endRound();
                    if(facade.nextRound()) {
                        facade.startRound();
                        getUI().ifPresent(ui -> ui.navigate("wheel"));
                    } else {
                        getUI().ifPresent(ui -> ui.navigate("results"));
                    }
                });
                okButton.getStyle().set("margin-top", "15px");
                dialog.add(okButton);

                dialog.open();
            } else {
                update();

                Dialog errorDialog = new Dialog();
                errorDialog.addClassName("dialog");

                errorDialog.add(new H1("Soluzione errata!"));
                errorDialog.add(new H2("Il turno passa al giocatore successivo."));

                Button okButton = new Button("Ok", e -> {
                    errorDialog.close();

                    restoreSavedBoardState();
                    GameContext.getInstance().getGameManager().nextTurn();
                    getUI().ifPresent(ui -> ui.navigate("wheel"));
                });
                okButton.getStyle().set("margin-top", "15px");
                errorDialog.add(okButton);

                errorDialog.open();
            }

            // Reset modalità soluzione
            isSolving = false;
            deleteLetterButton.setEnabled(false);
            giveSolutionButton.getStyle().remove("background-color");
            proposedSolution.setLength(0);
            userFilledCells.clear();
            update();
        }
    }

    private StringBuilder reconstructPhraseFromBuilder() {
        StringBuilder guess = new StringBuilder();
        for(int i = 0; i < ROWS; i++) {
            StringBuilder rowBuilder = new StringBuilder();
            boolean rowHasContent = false;
            for(int j = 0; j < COLUMNS; j++) {
                String text = cells[i][j].getText();
                if(!text.isBlank()) {
                    rowBuilder.append(text);
                    rowHasContent = true;
                } else {
                    if(rowBuilder.length() > 0 && rowBuilder.charAt(rowBuilder.length()-1) != ' ') {
                        rowBuilder.append(" ");
                    }
                }
            }

            if(rowHasContent) {
                if(guess.length() > 0) {
                    guess.append(" ");
                }
                guess.append(rowBuilder.toString().trim());
            }
        }
        return guess;
    }

    private void toggleBuyVowelMode() {
        isBuyingVowel = !isBuyingVowel;

        if(isBuyingVowel) {
            buyVowelButton.getStyle().set("background-color", "#FFD700");
            inputLetter.setPlaceholder("Inserisci una vocale...");
            Notification.show("Modalità acquisto vocale attivata!");
        } else {
            buyVowelButton.getStyle().remove("background-color");
            inputLetter.setPlaceholder("Inserisci una consonante...");
            Notification.show("Torna alla modalità consonante.");
        }
    }

    private HorizontalLayout createInfoPanel() {
        Player player = facade.getCurrentPlayer();
        int turn = facade.getTurn();
        int partial = facade.getAllPartialJackpot().get(turn);
        int total = player.getScore();
        int wheelValue = facade.getCurrentWheelValue();

        namePlayer = new Span("È il turno di: " + player.getName());
        partialSpan = new Span("Montepremi Parziali: " + partial + "€");
        totalSpan = new Span("Totale: " + total + "€");
        wheelValueSpan = new Span("Cifra ruota: " + (wheelValue == -2 ? "Raddoppia" : wheelValue + "€"));

        HorizontalLayout layout = new HorizontalLayout(partialSpan, totalSpan, wheelValueSpan);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);

        VerticalLayout infoPanel = new VerticalLayout(namePlayer, layout);

        HorizontalLayout returnPanel = new HorizontalLayout(infoPanel);
        returnPanel.setSpacing(true);
        returnPanel.setAlignItems(Alignment.CENTER);
        returnPanel.addClassName("label");

        return returnPanel;
    }

    private Div createBoard() {
        Div board = new Div();

        board.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(" + COLUMNS + ", 40px)")
                .set("grid-template-rows", "repeat(" + ROWS + ", 40px)")
                .set("gap", "5px")
                .set("justify-content", "center");

        cells = new Div[ROWS][COLUMNS];

        List<String> tokens = facade.getMarkedTokens();
        List<List<String>> rows = wrapTokensIntoRows(tokens);

        int usedRows = rows.size();
        int startRow = Math.max(0, (ROWS - usedRows) / 2);

        for(int i = 0; i < ROWS; i++) {
            List<String> rowTokens = (i >= startRow && (i - startRow) < usedRows) ? rows.get(i - startRow) : new ArrayList<>();
            for(int j = 0; j < COLUMNS; j++) {
                Div cell = new Div();
                cell.getStyle()
                        .set("width", "40px")
                        .set("height", "40px")
                        .set("display", "flex")
                        .set("align-items", "center")
                        .set("justify-content", "center")
                        .set("border", "1px solid white")
                        .set("font-weight", "bold");

                String token = j < rowTokens.size() ? rowTokens.get(j) : " ";
                if(" ".equals(token)) {
                    cell.getStyle().set("background-color", "#00008B"); //cella vuota
                    cell.setText("");
                } else if("_".equals(token)) {
                    cell.getStyle().set("background-color", "#e0e0e0"); //lettera nascosta
                    cell.setText("");
                } else {
                    cell.getStyle().set("background-color", "#ffffff"); //lettera/punteggiatura mostrata
                    cell.setText(token);
                }
                cells[i][j] = cell;
                board.add(cell);
            }
        }
        return board;
    }

    private List<List<String>> wrapTokensIntoRows(List<String> tokens) {
        List<List<String>> words = new ArrayList<>();
        List<String> currentWord = new ArrayList<>();
        for(String token : tokens) {
            if(" ".equals(token)) {
                if(!currentWord.isEmpty()) {
                    words.add(currentWord);
                    currentWord = new ArrayList<>();
                }
            } else {
                currentWord.add(token);
            }
        }
        if(!currentWord.isEmpty()) words.add(currentWord);

        List<List<String>> rows = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();
        for(List<String> word : words) {
            int wordLen = word.size();
            int needed = wordLen + (currentRow.isEmpty() ? 0 : 1);
            if(currentRow.size() + needed > COLUMNS) {
                rows.add(currentRow);
                currentRow = new ArrayList<>();
            }
            if(!currentRow.isEmpty()) currentRow.add(" ");
            currentRow.addAll(word);
        }
        if(!currentRow.isEmpty()) rows.add(currentRow);

        if(rows.isEmpty()) rows.add(new ArrayList<>());
        return rows;
    }

    private boolean isUserFilled(int i, int j) {
        for(int[] coords : userFilledCells) {
            if(coords[0] == i && coords[1] == j) return true;
        }
        return false;
    }

    private void updateBoardCells(List<String> tokens) {
        List<List<String>> rows = wrapTokensIntoRows(tokens);
        int usedRows = rows.size();
        int startRow = Math.max(0, (ROWS - usedRows) / 2);

        for (int i = 0; i < ROWS; i++) {
            List<String> rowTokens = (i >= startRow && (i - startRow) < usedRows) ? rows.get(i - startRow) : new ArrayList<>();
            for (int j = 0; j < COLUMNS; j++) {
                Div cell = cells[i][j];

                if(isSolving && isUserFilled(i, j)) continue;

                String token = j < rowTokens.size() ? rowTokens.get(j) : " ";
                if (" ".equals(token)) {
                    cell.getStyle().set("background-color", "#00008B");
                    cell.setText("");
                } else if ("_".equals(token)) {
                    cell.getStyle().set("background-color", "#e0e0e0");
                    cell.setText("");
                } else {
                    cell.getStyle().set("background-color", "#ffffff");
                    cell.setText(token);
                }
            }
        }
    }

    @Override
    public void update() {
        getUI().ifPresent(ui -> ui.access(() -> {
            List<String> tokens = facade.getMarkedTokens();
            updateBoardCells(tokens);

            int turn = facade.getTurn();
            List<Integer> partials = facade.getAllPartialJackpot();
            int partial = (partials != null && turn < partials.size()) ? partials.get(turn) : 0;
            int total = facade.getCurrentPlayer() != null? facade.getCurrentPlayer().getScore() : 0;
            String nameP = facade.getCurrentPlayer().getName();
            int wheelValue = facade.getCurrentWheelValue();

            namePlayer.setText("È il turno di: " + nameP);
            partialSpan.setText("Parziale: " + partial + "€");
            totalSpan.setText("Totale: " + total + "€");
            wheelValueSpan.setText("Cifra ruota: " + (wheelValue == -2 ? "Raddoppia" : wheelValue + "€"));

            updateLettersState();
        }));
    }

    private void updateLettersState() {
        boolean wasVowelButtonEnabled = buyVowelButton.isEnabled();
        boolean wasSpinWheelButtonEnabled = spinWheelButton.isEnabled();

        if(facade.checkFinishVowels()) { //Stato Vocali
            buyVowelButton.setEnabled(false);
            if(wasVowelButtonEnabled) Notification.show("Vocali Terminate!");
        }

        if(facade.checkFinishConsonants()) { //Stato Consonanti
            spinWheelButton.setEnabled(false);
            if(wasSpinWheelButtonEnabled) Notification.show("Consonanti Terminate!");
        }
    }

}
