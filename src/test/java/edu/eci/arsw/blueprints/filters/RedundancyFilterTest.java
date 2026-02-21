package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class RedundancyFilterTest {

    @Test
    void removesConsecutiveDuplicatePoints() {
        RedundancyFilter filter = new RedundancyFilter();
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(1, 1),
                new Point(1, 1),
                new Point(2, 2),
                new Point(2, 2),
                new Point(3, 3)
        ));

        Blueprint out = filter.apply(bp);

        assertNotSame(bp, out);
        assertEquals(List.of(new Point(1, 1), new Point(2, 2), new Point(3, 3)), out.getPoints());
    }
}

