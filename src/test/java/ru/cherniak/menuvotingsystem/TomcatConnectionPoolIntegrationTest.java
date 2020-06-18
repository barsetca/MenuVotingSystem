package ru.cherniak.menuvotingsystem;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-mvc.xml",
})

@RunWith(SpringRunner.class)
public class TomcatConnectionPoolIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void givenTomcatConnectionPoolInstance__whenCheckedPoolClassName__thenCorrect() {
        assertThat(dataSource.getClass().getName())
                .isEqualTo("org.apache.tomcat.jdbc.pool.DataSource");
    }
}
