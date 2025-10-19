package it.game.service.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GameSubject {
    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    public void attach(GameObserver observer) {
        observers.add(observer);
    }

    public void detach(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for(GameObserver observer : observers)
            observer.update();
    }

}
