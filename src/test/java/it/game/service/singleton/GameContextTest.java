package it.game.service.singleton;

import it.game.service.command.GameCommandInvoker;
import it.game.service.facade.GameServiceFacade;
import it.game.service.memento.BoardOriginator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class GameContextTest {

    @Test
    void testSingletonInstance() {
        GameContext instance1 = GameContext.getInstance();
        GameContext instance2 = GameContext.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    void testGetInvoker() {
        GameContext instance = GameContext.getInstance();
        GameCommandInvoker invoker = instance.getInvoker();

        assertNotNull(invoker);
    }

    @Test
    void testGetOriginator() {
        GameContext instance = GameContext.getInstance();
        BoardOriginator originator = instance.getOriginator();

        assertNotNull(originator);
    }

    @Test
    void testGetFacade() {
        GameContext instance = GameContext.getInstance();
        GameServiceFacade facade = instance.getFacade();

        assertNotNull(facade);
    }

}
