package org.kaesoron.pioneer_app.integrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kaesoron.pioneer_app.BankApplication;
import org.kaesoron.pioneer_app.entity.Account;
import org.kaesoron.pioneer_app.entity.User;
import org.kaesoron.pioneer_app.repository.AccountRepository;
import org.kaesoron.pioneer_app.repository.UserRepository;
import org.kaesoron.pioneer_app.security.JwtService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(classes = BankApplication.class)
class TransferIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("bankdb")
            .withUsername("postgres")
            .withPassword("postgres");
    @MockBean
    private JwtEncoder jwtEncoder;
    @MockBean
    private JwtDecoder jwtDecoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    private User user1;
    private User user2;

    @DynamicPropertySource
    static void setProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        accountRepository.deleteAll();

        user1 = User.builder()
                .name("User One")
                .password("secret")
                .build();

        user2 = User.builder()
                .name("User Two")
                .password("secret")
                .build();

        Account account1 = Account.builder()
                .user(user1)
                .balance(new BigDecimal("100.00"))
                .build();

        Account account2 = Account.builder()
                .user(user2)
                .balance(new BigDecimal("50.00"))
                .build();

        user1.setAccount(account1);
        account1.setUser(user1);
        user2.setAccount(account2);
        account2.setUser(user2);

        userRepository.saveAll(List.of(user1, user2));
        accountRepository.saveAll(List.of(account1, account2));
        userRepository.saveAll(List.of(user1, user2));

        Jwt jwt = new Jwt(
                "mock-token-value", // токен (можно любой)
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"), // заголовок (header)
                Map.of("sub", user1.getId(), "scope", "read write") // полезная нагрузка (claims)
        );
        Mockito.when(jwtEncoder.encode(Mockito.any())).thenReturn(jwt);
        Mockito.when(jwtDecoder.decode("mock-token-value")).thenReturn(jwt);
    }


    @Test
    void transferWithMockedJwt() throws Exception {
        String token = generateTokenWithSub(String.valueOf(user1.getId()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {
                                  "toUserId": %d,
                                  "amount": 25.00
                                }
                                """.formatted(user2.getId())))
                .andExpect(status().isOk());

        Account from = accountRepository.findByUser(user1).orElseThrow();
        Account to = accountRepository.findByUser(user2).orElseThrow();

        Assertions.assertAll(
                () -> assertThat(from.getBalance()).isEqualByComparingTo("75.00"),
                () -> assertThat(to.getBalance()).isEqualByComparingTo("75.00")
        );
    }

    private String generateTokenWithSub(String sub) {
        return jwtService.generateToken(Long.parseLong(sub));
    }
}