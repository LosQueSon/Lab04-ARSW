package edu.eci.arsw.blueprints.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlueprintTest {

    @Test
    void constructorCopiesPointsAndExposesUnmodifiableList() {
        Blueprint bp = new Blueprint("alice", "bp1", List.of(new Point(1, 2), new Point(3, 4)));

        assertEquals(2, bp.getPoints().size());
        assertEquals(new Point(1, 2), bp.getPoints().get(0));
        assertThrows(UnsupportedOperationException.class,
                () -> bp.getPoints().add(new Point(9, 9)));
    }

    @Test
    void addPointAppends() {
        Blueprint bp = new Blueprint("alice", "bp2", List.of());
        bp.addPoint(new Point(5, 6));

        assertEquals(1, bp.getPoints().size());
        assertEquals(new Point(5, 6), bp.getPoints().get(0));
    }

    @Test
    void equalsAndHashCodeUseAuthorAndName() {
        Blueprint a = new Blueprint("bob", "n1", List.of());
        Blueprint b = new Blueprint("bob", "n1", List.of(new Point(1, 1)));
        Blueprint c = new Blueprint("bob", "n2", List.of());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}

