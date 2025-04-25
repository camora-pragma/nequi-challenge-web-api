package co.com.nequi.r2dbc.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlywayConfigTest {

    @Test
    void shouldConfigureFlywayCorrectly() {
        // Arrange
        PostgresqlConnectionProperties properties = mock(PostgresqlConnectionProperties.class);

        when(properties.host()).thenReturn("localhost");
        when(properties.port()).thenReturn(5432);
        when(properties.database()).thenReturn("testdb");
        when(properties.username()).thenReturn("user");
        when(properties.password()).thenReturn("password");
        when(properties.schema()).thenReturn("public");

        FlywayConfig config = new FlywayConfig();

        // Act
        Flyway flyway = config.flyway(properties);

        // Assert
        assertEquals("jdbc:postgresql://localhost:5432/testdb", flyway.getConfiguration().getUrl());
        assertEquals("user", flyway.getConfiguration().getUser());
        assertEquals("public", flyway.getConfiguration().getSchemas()[0]);
    }
}
