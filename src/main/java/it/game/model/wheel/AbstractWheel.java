package it.game.model.wheel;

import it.game.model.Sector;

import java.util.List;
import java.util.Random;

public abstract class AbstractWheel {
    protected List<Sector> result;
    private final Random rand = new Random();

    public Sector spin() {
        return result.get(rand.nextInt(result.size()));
    }

}
