package it.game.model.wheel;

import it.game.model.Sector;

import java.util.List;

public class NormalWheel extends AbstractWheel {

    public NormalWheel() {
        this.result = List.of(new Sector("Bancarotta"), new Sector("100€"), new Sector("200€"), new Sector("300€"), new Sector("400€"), new Sector("Passa"), new Sector("500€"), new Sector("600€"), new Sector("700€"), new Sector("800€"), new Sector("Raddoppia"), new Sector("900€"), new Sector("1000€"),
                new Sector("100€"), new Sector("200€"), new Sector("Bancarotta"), new Sector("300€"), new Sector("400€"), new Sector("500€"), new Sector("600€"), new Sector("Passa"), new Sector("700€"), new Sector("800€"), new Sector("900€"));
    }

}
