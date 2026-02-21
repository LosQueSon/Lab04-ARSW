package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.impl.PostgresBlueprintPersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresBlueprintPersistenceTest {

    @Mock
    private JdbcTemplate jdbc;

    @Test
    void saveBlueprintInsertsBlueprintAndPoints() throws Exception {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);
        Blueprint bp = new Blueprint("a", "b", List.of(new Point(1, 2), new Point(3, 4)));

        persistence.saveBlueprint(bp);

        verify(jdbc).update(eq("INSERT INTO blueprints(author, name) VALUES (?, ?)"), eq("a"), eq("b"));
        verify(jdbc, times(2)).update(eq("INSERT INTO blueprint_points(author, name, idx, x, y) VALUES (?, ?, ?, ?, ?)"),
                eq("a"), eq("b"), anyInt(), anyInt(), anyInt());
    }

    @Test
    void saveBlueprintDuplicateThrows() {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);
        Blueprint bp = new Blueprint("a", "b", List.of());

        doThrow(new DuplicateKeyException("dup"))
                .when(jdbc)
                .update(eq("INSERT INTO blueprints(author, name) VALUES (?, ?)"), eq("a"), eq("b"));

        assertThrows(BlueprintPersistenceException.class, () -> persistence.saveBlueprint(bp));
    }

    @Test
    void getBlueprintReturnsBlueprintWhenExists() throws Exception {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        when(jdbc.query(eq("SELECT 1 FROM blueprints WHERE author=? AND name=?"),
                any(ResultSetExtractor.class), eq("a"), eq("b")))
                .thenReturn(1);
        when(jdbc.query(eq("SELECT x, y FROM blueprint_points WHERE author=? AND name=? ORDER BY idx ASC"),
                any(RowMapper.class), eq("a"), eq("b")))
                .thenReturn(List.of(new Point(1, 1), new Point(2, 2)));

        Blueprint bp = persistence.getBlueprint("a", "b");

        assertEquals("a", bp.getAuthor());
        assertEquals("b", bp.getName());
        assertEquals(2, bp.getPoints().size());
    }

    @Test
    void getBlueprintThrowsWhenMissing() {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        when(jdbc.query(eq("SELECT 1 FROM blueprints WHERE author=? AND name=?"),
                any(ResultSetExtractor.class), eq("a"), eq("b")))
                .thenReturn(null);

        assertThrows(BlueprintNotFoundException.class, () -> persistence.getBlueprint("a", "b"));
    }

    @Test
    void getBlueprintsByAuthorReturnsSet() throws Exception {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        when(jdbc.query(eq("SELECT name FROM blueprints WHERE author=? ORDER BY name ASC"),
                any(RowMapper.class), eq("a")))
                .thenReturn(List.of("n1", "n2"));
        when(jdbc.query(eq("SELECT 1 FROM blueprints WHERE author=? AND name=?"),
                any(ResultSetExtractor.class), eq("a"), anyString()))
                .thenReturn(1);
        when(jdbc.query(eq("SELECT x, y FROM blueprint_points WHERE author=? AND name=? ORDER BY idx ASC"),
                any(RowMapper.class), eq("a"), anyString()))
                .thenReturn(List.of());

        Set<Blueprint> result = persistence.getBlueprintsByAuthor("a");

        assertEquals(2, result.size());
    }

    @Test
    void getBlueprintsByAuthorThrowsWhenEmpty() {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        when(jdbc.query(eq("SELECT name FROM blueprints WHERE author=? ORDER BY name ASC"),
                any(RowMapper.class), eq("a")))
                .thenReturn(List.of());

        assertThrows(BlueprintNotFoundException.class, () -> persistence.getBlueprintsByAuthor("a"));
    }

    @Test
    void getAllBlueprintsUsesRows() {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        Map<String, Object> row = new HashMap<>();
        row.put("author", "a");
        row.put("name", "b");
        when(jdbc.queryForList(eq("SELECT author, name FROM blueprints ORDER BY author ASC, name ASC")))
                .thenReturn(List.of(row));
        when(jdbc.query(eq("SELECT 1 FROM blueprints WHERE author=? AND name=?"),
                any(ResultSetExtractor.class), eq("a"), eq("b")))
                .thenReturn(1);
        when(jdbc.query(eq("SELECT x, y FROM blueprint_points WHERE author=? AND name=? ORDER BY idx ASC"),
                any(RowMapper.class), eq("a"), eq("b")))
                .thenReturn(List.of());

        Set<Blueprint> result = persistence.getAllBlueprints();

        assertEquals(1, result.size());
    }

    @Test
    void addPointInsertsNextIndex() throws Exception {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        when(jdbc.query(eq("SELECT 1 FROM blueprints WHERE author=? AND name=?"),
                any(ResultSetExtractor.class), eq("a"), eq("b")))
                .thenReturn(1);
        when(jdbc.queryForObject(eq("SELECT COALESCE(MAX(idx), -1) + 1 FROM blueprint_points WHERE author=? AND name=?"),
                eq(Integer.class), eq("a"), eq("b")))
                .thenReturn(3);

        persistence.addPoint("a", "b", 7, 8);

        verify(jdbc).update(eq("INSERT INTO blueprint_points(author, name, idx, x, y) VALUES (?, ?, ?, ?, ?)"),
                eq("a"), eq("b"), eq(3), eq(7), eq(8));
    }

    @Test
    void addPointThrowsWhenMissing() {
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(jdbc);

        when(jdbc.query(eq("SELECT 1 FROM blueprints WHERE author=? AND name=?"),
                any(ResultSetExtractor.class), eq("a"), eq("b")))
                .thenReturn(null);

        assertThrows(BlueprintNotFoundException.class, () -> persistence.addPoint("a", "b", 1, 1));
    }
}

