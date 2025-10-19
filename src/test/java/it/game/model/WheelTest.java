package it.game.model;

import it.game.model.wheel.AbstractWheel;
import it.game.model.wheel.NormalWheel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WheelTest {
    private AbstractWheel wheel;

    @BeforeEach
    void setUp() {
        wheel = new NormalWheel();
    }

    @Test
    void testWheelCreation() {
        List<Sector> sectors = wheel.getResult();
        assertNotNull(sectors);
        assertFalse(sectors.isEmpty());
    }

    @Test
    void testSpin() {
        Sector result = wheel.spin();
        assertNotNull(result);
        assertNotNull(result.value());
    }

    @Test
    void testWheelContainsExpectedSectors() {
        List<Sector> sectors = wheel.getResult();

        boolean hasBancarotta = sectors.stream().anyMatch(s -> s.value().equals("Bancarotta"));
        boolean hasPassa = sectors.stream().anyMatch(s -> s.value().equals("Passa"));
        boolean hasRaddoppia = sectors.stream().anyMatch(s -> s.value().equals("Raddoppia"));
        boolean hasMoney = sectors.stream().anyMatch(s -> s.value().contains("€"));

        assertTrue(hasBancarotta);
        assertTrue(hasPassa);
        assertTrue(hasRaddoppia);
        assertTrue(hasMoney);
    }

    @Test
    void testGetResultImmutability() {
        List<Sector> sectors = wheel.getResult();

        assertThrows(UnsupportedOperationException.class, () -> sectors.add(new Sector("Test")));
    }
}
