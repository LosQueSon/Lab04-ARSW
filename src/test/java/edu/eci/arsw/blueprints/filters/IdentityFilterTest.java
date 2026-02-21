package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class IdentityFilterTest {

    @Test
    void returnsSameBlueprintInstance() {
        IdentityFilter filter = new IdentityFilter();
        Blueprint bp = new Blueprint("a", "b", List.of(new Point(1, 1)));

        assertSame(bp, filter.apply(bp));
    }
}

