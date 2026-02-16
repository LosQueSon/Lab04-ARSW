package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    private final JdbcTemplate jdbc;

    public PostgresBlueprintPersistence(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Point> pointMapper = (rs, rowNum) ->
            new Point(rs.getInt("x"), rs.getInt("y"));

    @Override
    @Transactional
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        try {
            jdbc.update(
                    "INSERT INTO blueprints(author, name) VALUES (?, ?)",
                    bp.getAuthor(), bp.getName()
            );

            var pts = bp.getPoints();
            for (int i = 0; i < pts.size(); i++) {
                Point p = pts.get(i);
                jdbc.update(
                        "INSERT INTO blueprint_points(author, name, idx, x, y) VALUES (?, ?, ?, ?, ?)",
                        bp.getAuthor(), bp.getName(), i, p.x(), p.y()
                );
            }
        } catch (DuplicateKeyException e) {
            throw new BlueprintPersistenceException(
                    "Blueprint already exists: %s/%s".formatted(bp.getAuthor(), bp.getName())
            );
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Integer exists = jdbc.query(
                "SELECT 1 FROM blueprints WHERE author=? AND name=?",
                rs -> rs.next() ? 1 : null,
                author, name
        );
        if (exists == null) {
            throw new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name));
        }

        List<Point> points = jdbc.query(
                "SELECT x, y FROM blueprint_points WHERE author=? AND name=? ORDER BY idx ASC",
                pointMapper,
                author, name
        );

        return new Blueprint(author, name, points);
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        List<String> names = jdbc.query(
                "SELECT name FROM blueprints WHERE author=? ORDER BY name ASC",
                (rs, rowNum) -> rs.getString("name"),
                author
        );

        if (names.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints for author: " + author);
        }

        Set<Blueprint> result = new HashSet<>();
        for (String name : names) {
            result.add(getBlueprint(author, name));
        }
        return result;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT author, name FROM blueprints ORDER BY author ASC, name ASC"
        );

        Set<Blueprint> result = new HashSet<>();
        for (Map<String, Object> r : rows) {
            String author = (String) r.get("author");
            String name = (String) r.get("name");
            try {
                result.add(getBlueprint(author, name));
            } catch (BlueprintNotFoundException ignored) { }
        }
        return result;
    }

    @Override
    @Transactional
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        Integer exists = jdbc.query(
                "SELECT 1 FROM blueprints WHERE author=? AND name=?",
                rs -> rs.next() ? 1 : null,
                author, name
        );
        if (exists == null) {
            throw new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name));
        }

        Integer nextIdx = jdbc.queryForObject(
                "SELECT COALESCE(MAX(idx), -1) + 1 FROM blueprint_points WHERE author=? AND name=?",
                Integer.class,
                author, name
        );

        jdbc.update(
                "INSERT INTO blueprint_points(author, name, idx, x, y) VALUES (?, ?, ?, ?, ?)",
                author, name, nextIdx, x, y
        );
    }
}
