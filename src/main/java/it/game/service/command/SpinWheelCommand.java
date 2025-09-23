package it.game.service.command;

import it.game.model.Sector;
import it.game.model.wheel.AbstractWheel;

public class SpinWheelCommand implements GameCommand<Sector> {
    private final AbstractWheel wheel;

    public SpinWheelCommand(AbstractWheel wheel) {
        this.wheel = wheel;
    }

    @Override
    public Sector execute() {
        return wheel.spin();
    }
}
