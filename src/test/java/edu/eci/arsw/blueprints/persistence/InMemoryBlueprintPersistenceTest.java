package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBlueprintPersistenceTest {

    @Test
    void getAllBlueprintsContainsSeedData() {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Set<Blueprint> all = persistence.getAllBlueprints();

        assertEquals(3, all.size());
    }

    @Test
    void saveBlueprintAndRetrieve() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();
        Blueprint bp = new Blueprint("mark", "plan", List.of(new Point(1, 1)));

        persistence.saveBlueprint(bp);

        Blueprint loaded = persistence.getBlueprint("mark", "plan");
        assertEquals(bp, loaded);
        assertEquals(1, loaded.getPoints().size());
    }

    @Test
    void saveDuplicateThrows() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();
        Blueprint bp = new Blueprint("dup", "plan", List.of());

        persistence.saveBlueprint(bp);

        assertThrows(BlueprintPersistenceException.class, () -> persistence.saveBlueprint(bp));
    }

    @Test
    void getByAuthorThrowsWhenEmpty() {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        assertThrows(BlueprintNotFoundException.class, () -> persistence.getBlueprintsByAuthor("nobody"));
    }

    @Test
    void addPointUpdatesExistingBlueprint() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();
        Blueprint bp = new Blueprint("z", "p", List.of());
        persistence.saveBlueprint(bp);

        persistence.addPoint("z", "p", 7, 8);

        Blueprint loaded = persistence.getBlueprint("z", "p");
        assertEquals(1, loaded.getPoints().size());
        assertEquals(new Point(7, 8), loaded.getPoints().get(0));
    }

    @Test
    void getBlueprintThrowsWhenMissing() {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        assertThrows(BlueprintNotFoundException.class, () -> persistence.getBlueprint("x", "y"));
    }
}

