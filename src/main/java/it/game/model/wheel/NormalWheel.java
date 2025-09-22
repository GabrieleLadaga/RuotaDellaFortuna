package it.game.model.wheel;

import it.game.model.Sector;

import java.util.List;

public class NormalWheel extends AbstractWheel {

    public NormalWheel() {
        this.result = List.of(new Sector("100€"), new Sector("200€"), new Sector("300€"), new Sector("400€"), new Sector("500€"),
                         new Sector("600€"), new Sector("700€"), new Sector("800€"), new Sector("900€"), new Sector("1000€"),
                         new Sector("Passa Turno"), new Sector("Bancarotta"), new Sector("Raddoppia"));
    }

}
