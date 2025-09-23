package it.game.service.observer;

import java.util.List;

public abstract class GameSubject {
    private List<GameObserver> observers;

    public void attach(GameObserver observer) {
        observers.add(observer);
    }

    public void detach(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update();
        }
    }

}
