package it.game.service.singleton;

import it.game.service.GameManager;
import it.game.service.command.GameCommandInvoker;
import it.game.service.facade.GameServiceFacade;
import it.game.service.facade.OfflineGameServiceFacade;
import it.game.service.memento.BoardOriginator;

public class GameContext {

    private static GameContext instance;

    private final GameManager gameManager;
    private final GameCommandInvoker invoker;
    private final BoardOriginator originator;
    private final GameServiceFacade facade;

    private GameContext() {
        this.gameManager = new GameManager();
        this.invoker = new GameCommandInvoker();
        this.originator = new BoardOriginator();
        this.facade = new OfflineGameServiceFacade(gameManager, invoker, originator);
    }

    public static synchronized GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }

    public GameManager getGameManager() { return gameManager; }
    public GameCommandInvoker getInvoker() { return invoker; }
    public BoardOriginator getOriginator() { return originator; }
    public GameServiceFacade getFacade() { return facade; }
}
