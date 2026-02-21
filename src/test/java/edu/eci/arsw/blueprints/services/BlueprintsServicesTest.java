package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlueprintsServicesTest {

    @Mock
    private BlueprintPersistence persistence;

    @Mock
    private BlueprintsFilter filter;

    @InjectMocks
    private BlueprintsServices services;

    @Test
    void addNewBlueprintDelegatesToPersistence() throws BlueprintPersistenceException {
        Blueprint bp = new Blueprint("a", "b", List.of());

        services.addNewBlueprint(bp);

        verify(persistence).saveBlueprint(bp);
    }

    @Test
    void getAllBlueprintsReturnsFromPersistence() {
        Set<Blueprint> expected = Set.of(new Blueprint("a", "b", List.of()));
        when(persistence.getAllBlueprints()).thenReturn(expected);

        Set<Blueprint> result = services.getAllBlueprints();

        assertEquals(expected, result);
    }

    @Test
    void getBlueprintsByAuthorDelegatesToPersistence() throws BlueprintNotFoundException {
        Set<Blueprint> expected = Set.of(new Blueprint("a", "b", List.of()));
        when(persistence.getBlueprintsByAuthor("a")).thenReturn(expected);

        Set<Blueprint> result = services.getBlueprintsByAuthor("a");

        assertEquals(expected, result);
    }

    @Test
    void getBlueprintAppliesFilter() throws BlueprintNotFoundException {
        Blueprint stored = new Blueprint("a", "b", List.of(new Point(1, 1)));
        Blueprint filtered = new Blueprint("a", "b", List.of(new Point(1, 1)));
        when(persistence.getBlueprint("a", "b")).thenReturn(stored);
        when(filter.apply(stored)).thenReturn(filtered);

        Blueprint result = services.getBlueprint("a", "b");

        assertEquals(filtered, result);
        verify(filter).apply(stored);
    }

    @Test
    void addPointDelegatesToPersistence() throws BlueprintNotFoundException {
        services.addPoint("a", "b", 1, 2);

        verify(persistence).addPoint("a", "b", 1, 2);
    }
}

