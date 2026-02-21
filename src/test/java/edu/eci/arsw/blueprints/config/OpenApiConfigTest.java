package edu.eci.arsw.blueprints.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenApiConfigTest {

    @Test
    void apiBeanHasMetadata() {
        OpenApiConfig config = new OpenApiConfig();

        var api = config.api();

        assertNotNull(api.getInfo());
        assertEquals("ARSW Blueprints API", api.getInfo().getTitle());
        assertEquals("v1", api.getInfo().getVersion());
    }
}

