package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UndersamplingFilterTest {

    @Test
    void smallBlueprintReturnsSameInstance() {
        UndersamplingFilter filter = new UndersamplingFilter();
        Blueprint bp = new Blueprint("a", "b", List.of(new Point(1, 1), new Point(2, 2)));

        assertSame(bp, filter.apply(bp));
    }

    @Test
    void keepsEvenIndexPoints() {
        UndersamplingFilter filter = new UndersamplingFilter();
        Blueprint bp = new Blueprint("a", "b", List.of(
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3),
                new Point(4, 4)
        ));

        Blueprint out = filter.apply(bp);

        assertNotSame(bp, out);
        assertEquals(List.of(new Point(0, 0), new Point(2, 2), new Point(4, 4)), out.getPoints());
    }
}

