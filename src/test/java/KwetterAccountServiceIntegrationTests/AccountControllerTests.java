package KwetterAccountServiceIntegrationTests;

import DTO.DeleteAccountRequestDto;
import DTO.UpdateAccountRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import rest.KwetterAccountServiceRestServer;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static java.lang.String.format;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(
        classes = KwetterAccountServiceRestServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountControllerTests {
    private ObjectMapper mapper;
    private HttpHeaders headers;

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        this.url = format("http://localhost:%d/account", port);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testGetAccountByUserId() throws JsonProcessingException {
        String userId = "1fd1e841-cb19-4a45-a720-d06beb983d2d";
        this.url = format("http://localhost:%d/account/user/%s", port, userId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Account account = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);

        Assertions.assertEquals(userId, account.getUserId());
    }

    @Test
    public void testGetAccountById() throws JsonProcessingException {
        int accountId = 1;
        this.url = format("http://localhost:%d/account/%d", port, accountId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Account account = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length() > 0);

        Assertions.assertEquals(accountId, account.getId());
    }

    @Test
    public void testGetNonExistingAccountById() {
        int accountId = 100;
        this.url = format("http://localhost:%d/account/%d", port, accountId);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String message = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("This account does not exist!", message);
    }

    @Test
    public void testCreateAccount() {
        this.url = format("http://localhost:%d/account", port);
        Account requestAccount = new Account("db3196c5-7a6a-4650-84fb-4e4b94728f86", "Spring", "Integration test", LocalDate.parse("1997-01-01"), "Male", "United States of America", "32605849");

        HttpEntity<Account> request = new HttpEntity<>(requestAccount, headers);

        ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.POST, request, Account.class);
        Account responseAccount = response.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(responseAccount);
        Assertions.assertEquals("db3196c5-7a6a-4650-84fb-4e4b94728f86", requestAccount.getUserId());
        Assertions.assertEquals("Spring", requestAccount.getFirstName());
        Assertions.assertEquals("Integration test", requestAccount.getLastName());
        Assertions.assertEquals(LocalDate.parse("1997-01-01"), requestAccount.getDateOfBirth());
        Assertions.assertEquals("Male", requestAccount.getGender());
        Assertions.assertEquals("United States of America", requestAccount.getCountry());
        Assertions.assertEquals("32605849", requestAccount.getPhoneNumber());
    }

    @Test
    public void testCreateDuplicateAccount() {
        this.url = format("http://localhost:%d/account", port);
        Account requestAccount = new Account("1fd1e841-cb19-4a45-a720-d06beb983d2d", "Spring", "Integration test", LocalDate.parse("1997-01-01"), "Male", "United States of America", "32605849");

        HttpEntity<Account> request = new HttpEntity<>(requestAccount, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String message = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Account already exists!", message);
    }

    @Test
    public void testUpdateAccount() {
        this.url = format("http://localhost:%d/account", port);
        UpdateAccountRequestDto requestUpdateAccount = new UpdateAccountRequestDto("1fd1e841-cb19-4a45-a720-d06beb983d2d", 1,
                "Spring", "Integration test", "maxim@test.com", "Male", LocalDate.parse("1997-01-01"), "The Netherlands", "32605849", "Test#$%Pwd");

        HttpEntity<UpdateAccountRequestDto> request = new HttpEntity<>(requestUpdateAccount, headers);

        ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.PUT, request, Account.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateNonExistingAccount() {
        this.url = format("http://localhost:%d/account", port);
        UpdateAccountRequestDto requestUpdateAccount = new UpdateAccountRequestDto("1fd1e841-cb19-4a45-a720-d06beb983d2d", 100,
                "Spring", "Integration test", "maxim@test.com", "Male", LocalDate.parse("1997-01-01"), "The Netherlands", "32605849", "Test#$%Pwd");

        HttpEntity<UpdateAccountRequestDto> request = new HttpEntity<>(requestUpdateAccount, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        String message = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("This account does not exist!", message);
    }

    @Test
    public void testDeleteAccount() {
        this.url = format("http://localhost:%d/account", port);
        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("1fd1e841-cb19-4a45-a720-d06beb983d2d", 1);

        HttpEntity<DeleteAccountRequestDto> request = new HttpEntity<>(deleteAccountRequestDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNotUserAccount() {
        this.url = format("http://localhost:%d/account", port);
        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("db3196c5-7a6a-4650-84fb-4e4b94728f86", 20);

        HttpEntity<DeleteAccountRequestDto> request = new HttpEntity<>(deleteAccountRequestDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        String message = response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Can't delete this account!", message);
    }
}
