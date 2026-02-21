package edu.eci.arsw.blueprints.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.arsw.blueprints.controllers.dto.NewBlueprintRequest;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlueprintsAPIController.class)
class BlueprintsAPIControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BlueprintsServices services;

    @Test
    void getAllReturnsOk() throws Exception {
        Set<Blueprint> data = Set.of(new Blueprint("a", "b", List.of(new Point(1, 1))));
        when(services.getAllBlueprints()).thenReturn(data);

        mvc.perform(get("/api/v1/blueprints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("execute ok"));
    }

    @Test
    void getByAuthorReturnsOk() throws Exception {
        Set<Blueprint> data = Set.of(new Blueprint("a", "b", List.of()));
        when(services.getBlueprintsByAuthor("a")).thenReturn(data);

        mvc.perform(get("/api/v1/blueprints/a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getByAuthorReturnsNotFound() throws Exception {
        when(services.getBlueprintsByAuthor("missing"))
                .thenThrow(new BlueprintNotFoundException("No blueprints"));

        mvc.perform(get("/api/v1/blueprints/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void getByAuthorAndNameReturnsOk() throws Exception {
        Blueprint data = new Blueprint("a", "b", List.of(new Point(1, 1)));
        when(services.getBlueprint("a", "b")).thenReturn(data);

        mvc.perform(get("/api/v1/blueprints/a/b"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getByAuthorAndNameReturnsNotFound() throws Exception {
        when(services.getBlueprint("a", "missing"))
                .thenThrow(new BlueprintNotFoundException("No blueprint"));

        mvc.perform(get("/api/v1/blueprints/a/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void postCreatesBlueprint() throws Exception {
        NewBlueprintRequest req = new NewBlueprintRequest("a", "b", List.of(new Point(1, 2)));

        mvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("created"));

        verify(services).addNewBlueprint(any(Blueprint.class));
    }

    @Test
    void postReturnsBadRequestWhenInvalid() throws Exception {
        mvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postReturnsBadRequestWhenDuplicate() throws Exception {
        doThrow(new BlueprintPersistenceException("dup"))
                .when(services)
                .addNewBlueprint(any(Blueprint.class));

        NewBlueprintRequest req = new NewBlueprintRequest("a", "b", List.of());

        mvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void addPointReturnsAccepted() throws Exception {
        mvc.perform(put("/api/v1/blueprints/a/b/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Point(1, 2))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.code").value(202));
    }

    @Test
    void addPointReturnsNotFound() throws Exception {
        doThrow(new BlueprintNotFoundException("missing"))
                .when(services)
                .addPoint("a", "b", 1, 2);

        mvc.perform(put("/api/v1/blueprints/a/b/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Point(1, 2))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}

