package hackatonGrupUn.repteTres.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackatonGrupUn.repteTres.dto.LoginRequestDto;
import hackatonGrupUn.repteTres.model.User;
import hackatonGrupUn.repteTres.model.userEnum.District;
import hackatonGrupUn.repteTres.service.AuthService;
import hackatonGrupUn.repteTres.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Login: Should return 200 and success message when credentials are valid")
    void login_ShouldReturnOkAndSuccessMessage_WhenCredentialsAreValid() {
        LoginRequestDto loginRequest = new LoginRequestDto("validUser", "validPassword");
        when(authService.login("validUser", "validPassword")).thenReturn(true);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Successfully logged in");

        verify(authService, times(1)).login("validUser", "validPassword");
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Login: Should return 401 and error message when credentials are invalid")
    void login_ShouldReturnUnauthorizedAndErrorMessage_WhenCredentialsAreInvalid() {
        LoginRequestDto loginRequest = new LoginRequestDto("invalidUser", "wrongPassword");
        when(authService.login("invalidUser", "wrongPassword")).thenReturn(false);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Invalid credentials");

        verify(authService, times(1)).login("invalidUser", "wrongPassword");
    }

    @Test
    @DisplayName("Login: Should return 401 when username is correct but password is wrong")
    void login_ShouldReturnUnauthorized_WhenPasswordIsWrong() {
        LoginRequestDto loginRequest = new LoginRequestDto("validUser", "wrongPassword");
        when(authService.login("validUser", "wrongPassword")).thenReturn(false);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Invalid credentials");

        verify(authService, times(1)).login("validUser", "wrongPassword");
    }

    @Test
    @DisplayName("Login: Should handle empty username")
    void login_ShouldHandleEmptyUsername() {
        LoginRequestDto loginRequest = new LoginRequestDto("", "password");
        when(authService.login("", "password")).thenReturn(false);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authService, times(1)).login("", "password");
    }

    @Test
    @DisplayName("Login: Should handle empty password")
    void login_ShouldHandleEmptyPassword() {
        LoginRequestDto loginRequest = new LoginRequestDto("username", "");
        when(authService.login("username", "")).thenReturn(false);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authService, times(1)).login("username", "");
    }

    @Test
    @DisplayName("Login: Should handle null username in DTO")
    void login_ShouldHandleNullUsername() {
        LoginRequestDto loginRequest = new LoginRequestDto(null, "password");
        when(authService.login(null, "password")).thenReturn(false);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authService, times(1)).login(null, "password");
    }

    @Test
    @DisplayName("Login: Should handle null password in DTO")
    void login_ShouldHandleNullPassword() {
        LoginRequestDto loginRequest = new LoginRequestDto("username", null);
        when(authService.login("username", null)).thenReturn(false);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authService, times(1)).login("username", null);
    }

    @Test
    @DisplayName("Login: Should handle username with special characters")
    void login_ShouldHandleSpecialCharactersInUsername() {
        LoginRequestDto loginRequest = new LoginRequestDto("user@example.com", "password123");
        when(authService.login("user@example.com", "password123")).thenReturn(true);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(authService, times(1)).login("user@example.com", "password123");
    }

    @Test
    @DisplayName("Login: Should handle password with spaces")
    void login_ShouldHandlePasswordWithSpaces() {
        LoginRequestDto loginRequest = new LoginRequestDto("username", "pass word 123");
        when(authService.login("username", "pass word 123")).thenReturn(true);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(authService, times(1)).login("username", "pass word 123");
    }

    @Test
    @DisplayName("Login: Should call authService exactly once")
    void login_ShouldCallAuthServiceOnce() {
        LoginRequestDto loginRequest = new LoginRequestDto("user", "pass");
        when(authService.login(anyString(), anyString())).thenReturn(true);

        userController.login(loginRequest);

        verify(authService, times(1)).login("user", "pass");
        verifyNoMoreInteractions(authService);
        verifyNoInteractions(userService);
    }


    @Test
    @DisplayName("Login MockMvc: Should return 200 when valid credentials")
    void login_WithMockMvc_ShouldReturnOk_WhenValidCredentials() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("validUser", "validPassword");
        when(authService.login("validUser", "validPassword")).thenReturn(true);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully logged in"));

        verify(authService, times(1)).login("validUser", "validPassword");
    }

    @Test
    @DisplayName("Login MockMvc: Should return 401 when invalid credentials")
    void login_WithMockMvc_ShouldReturnUnauthorized_WhenInvalidCredentials() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("invalidUser", "wrongPassword");
        when(authService.login("invalidUser", "wrongPassword")).thenReturn(false);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));

        verify(authService, times(1)).login("invalidUser", "wrongPassword");
    }

    @Test
    @DisplayName("Login MockMvc: Should require POST method")
    void login_WithMockMvc_ShouldRequirePostMethod() throws Exception {
        mockMvc.perform(get("/api/users/login"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Login MockMvc: Should require Content-Type application/json")
    void login_WithMockMvc_ShouldRequireJsonContentType() throws Exception {
        String jsonRequest = "{\"username\":\"user\",\"password\":\"pass\"}";
        when(authService.login("user", "pass")).thenReturn(true);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(authService, times(1)).login("user", "pass");
    }


    @Test
    @DisplayName("GetAllUsers: Should return 200 and list of users when users exist")
    void getAllUsers_ShouldReturnOkAndUserList_WhenUsersExist() {
        List<User> expectedUsers = Arrays.asList(
                new User(1L, "user1", "pass1", 150.5, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 200.3, "user2@example.com", District.GRÀCIA),
                new User(3L, "user3", "pass3", 175.8, "user3@example.com", District.SARRIÀ_SANT_GERVASI)
        );

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()).isEqualTo(expectedUsers);

        verify(userService, times(1)).getAllUsers();
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("GetAllUsers: Should return empty list when no users exist")
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should return single user when only one exists")
    void getAllUsers_ShouldReturnSingleUser_WhenOnlyOneUserExists() {
        List<User> singleUser = Collections.singletonList(
                new User(1L, "onlyUser", "password", 100.0, "only@example.com", District.CIUTAT_VELLA)
        );

        when(userService.getAllUsers()).thenReturn(singleUser);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getUsername()).isEqualTo("onlyUser");
        assertThat(response.getBody().get(0).getEmail()).isEqualTo("only@example.com");

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should handle users from different districts")
    void getAllUsers_ShouldHandleUsersFromDifferentDistricts() {
        List<User> usersFromDifferentDistricts = Arrays.asList(
                new User(1L, "user1", "pass1", 150.0, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 200.0, "user2@example.com", District.GRÀCIA),
                new User(3L, "user3", "pass3", 180.0, "user3@example.com", District.SARRIÀ_SANT_GERVASI),
                new User(4L, "user4", "pass4", 120.0, "user4@example.com", District.CIUTAT_VELLA),
                new User(5L, "user5", "pass5", 95.5, "user5@example.com", District.SANTS_MONJUÏC)
        );

        when(userService.getAllUsers()).thenReturn(usersFromDifferentDistricts);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
        assertThat(response.getBody()).extracting(User::getDistrict)
                .containsExactly(District.EIXAMPLE, District.GRÀCIA, District.SARRIÀ_SANT_GERVASI,
                        District.CIUTAT_VELLA, District.SANTS_MONJUÏC);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should handle users with null waterConsumption")
    void getAllUsers_ShouldHandleNullWaterConsumption() {
        List<User> usersWithNullConsumption = Arrays.asList(
                new User(1L, "user1", "pass1", null, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 200.0, "user2@example.com", District.GRÀCIA)
        );

        when(userService.getAllUsers()).thenReturn(usersWithNullConsumption);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getWaterConsumption()).isNull();
        assertThat(response.getBody().get(1).getWaterConsumption()).isEqualTo(200.0);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should handle users with zero water consumption")
    void getAllUsers_ShouldHandleZeroWaterConsumption() {
        List<User> usersWithZeroConsumption = Arrays.asList(
                new User(1L, "user1", "pass1", 0.0, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 0.0, "user2@example.com", District.GRÀCIA)
        );

        when(userService.getAllUsers()).thenReturn(usersWithZeroConsumption);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).allMatch(user -> user.getWaterConsumption() == 0.0);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should handle users with null district")
    void getAllUsers_ShouldHandleNullDistrict() {
        List<User> usersWithNullDistrict = Arrays.asList(
                new User(1L, "user1", "pass1", 150.0, "user1@example.com", null),
                new User(2L, "user2", "pass2", 200.0, "user2@example.com", District.GRÀCIA)
        );

        when(userService.getAllUsers()).thenReturn(usersWithNullDistrict);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getDistrict()).isNull();
        assertThat(response.getBody().get(1).getDistrict()).isEqualTo(District.GRÀCIA);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should handle large number of users")
    void getAllUsers_ShouldHandleLargeDataset() {
        List<User> largeUserList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeUserList.add(new User(
                    (long) i,
                    "user" + i,
                    "pass" + i,
                    100.0 + i,
                    "user" + i + "@example.com",
                    District.values()[i % District.values().length]
            ));
        }

        when(userService.getAllUsers()).thenReturn(largeUserList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1000);

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers: Should call userService exactly once")
    void getAllUsers_ShouldCallUserServiceOnce() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(authService);
    }


    @Test
    @DisplayName("GetAllUsers MockMvc: Should return 200 and JSON array")
    void getAllUsers_WithMockMvc_ShouldReturnJsonArray() throws Exception {
        // Given
        List<User> users = Arrays.asList(
                new User(1L, "user1", "pass1", 150.0, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 200.0, "user2@example.com", District.GRÀCIA)
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers MockMvc: Should return empty array when no users")
    void getAllUsers_WithMockMvc_ShouldReturnEmptyArray() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().json("[]"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GetAllUsers MockMvc: Should verify correct endpoint mapping")
    void getAllUsers_WithMockMvc_ShouldMapToCorrectEndpoint() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/allUsers"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GetAllUsers MockMvc: Should require GET method")
    void getAllUsers_WithMockMvc_ShouldRequireGetMethod() throws Exception {
        mockMvc.perform(post("/api/users/all"))
                .andExpect(status().isMethodNotAllowed());
    }


    @Test
    @DisplayName("Should handle multiple concurrent login attempts")
    void login_ShouldHandleConcurrentAttempts() {
        LoginRequestDto request1 = new LoginRequestDto("user1", "pass1");
        LoginRequestDto request2 = new LoginRequestDto("user2", "pass2");

        when(authService.login("user1", "pass1")).thenReturn(true);
        when(authService.login("user2", "pass2")).thenReturn(false);

        ResponseEntity<String> response1 = userController.login(request1);
        ResponseEntity<String> response2 = userController.login(request2);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authService, times(1)).login("user1", "pass1");
        verify(authService, times(1)).login("user2", "pass2");
    }

    @Test
    @DisplayName("GetAllUsers: Should verify user properties are correctly mapped")
    void getAllUsers_ShouldVerifyUserPropertiesCorrectlyMapped() {
        User testUser = new User(
                1L,
                "testUser",
                "testPassword",
                250.75,
                "test@example.com",
                District.EIXAMPLE
        );

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(testUser));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        User returnedUser = response.getBody().get(0);
        assertThat(returnedUser.getId()).isEqualTo(1L);
        assertThat(returnedUser.getUsername()).isEqualTo("testUser");
        assertThat(returnedUser.getPassword()).isEqualTo("testPassword");
        assertThat(returnedUser.getWaterConsumption()).isEqualTo(250.75);
        assertThat(returnedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(returnedUser.getDistrict()).isEqualTo(District.EIXAMPLE);

        verify(userService, times(1)).getAllUsers();
    }
}