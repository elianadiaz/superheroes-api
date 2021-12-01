package integration;

import com.mindata.superheroes.SuperheroApiApplication;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.utils.RestResponsePage;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = SuperheroApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuperheroControllerIT {

    private static final String HEADER_TOKEN = "Token";
    private static final String TOKEN_ADMIN = "TEST_ADMIN_ROLE";

    private String baseUrl;
    private HttpHeaders headers;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        if (StringUtils.isEmpty(baseUrl)) {
            this.baseUrl = new StringBuilder("http://localhost:").append(port).append("/superheroes").toString();
        }

        if (headers == null) {
            headers = new HttpHeaders();
            headers.add(HEADER_TOKEN, TOKEN_ADMIN);
        }
    }

    @Test
    public void giveAllWhenGetSuperherosThenReturnAllSuperheroes() {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("page", 0)
            .queryParam("size", 100);

        final ResponseEntity<RestResponsePage<Superhero>> response = this.restTemplate.exchange(builder.build().encode().toUriString(),
            HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() { });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Page<Superhero> superheroPage = response.getBody();
        assertFalse(superheroPage.isEmpty());
    }

    @Test
    public void giveAllWhenGetSuperherosByNameThenReturnOneSuperhero() {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("page", 0)
            .queryParam("size", 100)
            .queryParam("name", "1");

        final ResponseEntity<RestResponsePage<Superhero>> response = this.restTemplate.exchange(builder.build().encode().toUriString(),
            HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() { });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Page<Superhero> superheroPage = response.getBody();
        assertFalse(superheroPage.isEmpty());
        assertNotEquals(0, superheroPage.getTotalElements(), 0);
    }

    @Test
    public void giveIdWhenGetSuperheroByIdThenReturnSuperhero() {
        final long id = 1;
        final ResponseEntity<Superhero> response = this.restTemplate.exchange(new StringBuilder(this.baseUrl).append("/").append(id).toString(),
            HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() { });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Superhero superhero = response.getBody();
        assertEquals(id, superhero.getId(), 0);
        assertEquals("Superhero 1", superhero.getName());
    }

    @Test
    public void giveIncorrectIdWhenGetSuperheroByIdThenReturnBadRequest() {
        final ResponseEntity<Superhero> response = this.restTemplate.exchange(new StringBuilder(this.baseUrl).append("/").append(-1).toString(),
            HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() { });

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void giveNewNameWhenUpdateSuperheroThenReturnUpdatedSuperhero() {
        final long id = 1;
        final String newName = "New name";
        final Superhero superhero = new Superhero();
        superhero.setName(newName);
        HttpEntity<Superhero> requestEntity = new HttpEntity<>(superhero, headers);
        final ResponseEntity<Superhero> response = this.restTemplate.exchange(new StringBuilder(this.baseUrl).append("/").append(id).toString(),
            HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() { });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Superhero updatedSuperhero = response.getBody();
        assertEquals(id, updatedSuperhero.getId(), 0);
        assertEquals(newName, updatedSuperhero.getName());
    }

    @Test
    public void giveIdToDeleteWhenDeleteSuperheroByIdThenReturnSuccess() {
        final ResponseEntity<Superhero> response = this.restTemplate.exchange(new StringBuilder(this.baseUrl).append("/").append(2).toString(),
            HttpMethod.DELETE, new HttpEntity<>(headers), new ParameterizedTypeReference<>() { });

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
