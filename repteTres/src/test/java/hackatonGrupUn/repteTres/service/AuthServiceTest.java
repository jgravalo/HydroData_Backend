package hackatonGrupUn.repteTres.service;

import hackatonGrupUn.repteTres.model.User;
import hackatonGrupUn.repteTres.model.userEnum.District;
import hackatonGrupUn.repteTres.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
                1L,
                "testUser",
                "correctPassword",
                150.0,
                "test@example.com",
                District.EIXAMPLE
        );
    }

    // ========== SUCCESSFUL LOGIN TESTS ==========

    @Test
    @DisplayName("Login: Should return true when username and password are correct")
    void login_ShouldReturnTrue_WhenCredentialsAreCorrect() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", "correctPassword");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("Login: Should return true with case-sensitive username")
    void login_ShouldReturnTrue_WithCaseSensitiveUsername() {
        User userWithCaps = new User(
                2L,
                "TestUser",
                "password123",
                200.0,
                "testuser@example.com",
                District.GRÀCIA
        );
        when(userRepository.findByUsername("TestUser")).thenReturn(Optional.of(userWithCaps));

        boolean result = authService.login("TestUser", "password123");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername("TestUser");
    }

    @Test
    @DisplayName("Login: Should return true with password containing special characters")
    void login_ShouldReturnTrue_WithSpecialCharactersInPassword() {
        User userWithSpecialPassword = new User(
                3L,
                "specialUser",
                "P@ssw0rd!#$%",
                100.0,
                "special@example.com",
                District.SARRIÀ_SANT_GERVASI
        );
        when(userRepository.findByUsername("specialUser")).thenReturn(Optional.of(userWithSpecialPassword));

        boolean result = authService.login("specialUser", "P@ssw0rd!#$%");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername("specialUser");
    }

    @Test
    @DisplayName("Login: Should return true with password containing spaces")
    void login_ShouldReturnTrue_WithPasswordContainingSpaces() {
        User userWithSpacedPassword = new User(
                4L,
                "spacedUser",
                "pass word 123",
                125.5,
                "spaced@example.com",
                District.CIUTAT_VELLA
        );
        when(userRepository.findByUsername("spacedUser")).thenReturn(Optional.of(userWithSpacedPassword));

        boolean result = authService.login("spacedUser", "pass word 123");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername("spacedUser");
    }

    // ========== FAILED LOGIN TESTS - WRONG PASSWORD ==========

    @Test
    @DisplayName("Login: Should return false when password is incorrect")
    void login_ShouldReturnFalse_WhenPasswordIsIncorrect() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", "wrongPassword");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("Login: Should return false when password has different case")
    void login_ShouldReturnFalse_WhenPasswordCaseIsDifferent() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", "CORRECTPASSWORD");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("Login: Should return false when password is almost correct")
    void login_ShouldReturnFalse_WhenPasswordIsAlmostCorrect() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", "correctPasswor"); // Missing last character

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("Login: Should return false when password has extra spaces")
    void login_ShouldReturnFalse_WhenPasswordHasExtraSpaces() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", "correctPassword ");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    // ========== FAILED LOGIN TESTS - USER NOT FOUND ==========

    @Test
    @DisplayName("Login: Should return false when user does not exist")
    void login_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        boolean result = authService.login("nonExistentUser", "anyPassword");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("nonExistentUser");
    }

    @Test
    @DisplayName("Login: Should return false when username is correct but user not in database")
    void login_ShouldReturnFalse_WhenUsernameNotInDatabase() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        boolean result = authService.login("testUser", "correctPassword");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    // ========== EDGE CASES - NULL AND EMPTY VALUES ==========

    @Test
    @DisplayName("Login: Should return false when username is null")
    void login_ShouldReturnFalse_WhenUsernameIsNull() {
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        boolean result = authService.login(null, "anyPassword");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    @DisplayName("Login: Should return false when password is null")
    void login_ShouldReturnFalse_WhenPasswordIsNull() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", null);

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("Login: Should return false when both username and password are null")
    void login_ShouldReturnFalse_WhenBothAreNull() {
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        boolean result = authService.login(null, null);

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    @DisplayName("Login: Should return false when username is empty")
    void login_ShouldReturnFalse_WhenUsernameIsEmpty() {
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());

        boolean result = authService.login("", "anyPassword");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("");
    }

    @Test
    @DisplayName("Login: Should return false when password is empty")
    void login_ShouldReturnFalse_WhenPasswordIsEmpty() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = authService.login("testUser", "");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    // ========== PARAMETERIZED TESTS ==========

    @ParameterizedTest
    @ValueSource(strings = {"user1", "user@example.com", "user.name", "user_123", "user-test"})
    @DisplayName("Login: Should handle various username formats")
    void login_ShouldHandleVariousUsernameFormats(String username) {
        User user = new User(1L, username, "password", 100.0, "email@example.com", District.EIXAMPLE);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = authService.login(username, "password");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername(username);
    }

    @ParameterizedTest
    @CsvSource({
            "user1, password1, password1, true",
            "user2, password2, wrongPassword, false",
            "user3, 12345, 12345, true",
            "user4, 12345, 54321, false"
    })
    @DisplayName("Login: Should validate multiple credential combinations")
    void login_ShouldValidateMultipleCredentialCombinations(
            String username, String storedPassword, String providedPassword, boolean expectedResult) {
        User user = new User(1L, username, storedPassword, 100.0, "email@example.com", District.EIXAMPLE);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = authService.login(username, providedPassword);

        assertThat(result).isEqualTo(expectedResult);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("Login: Should return false for invalid usernames")
    void login_ShouldReturnFalse_ForInvalidUsernames(String invalidUsername) {
        when(userRepository.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        boolean result = authService.login(invalidUsername, "anyPassword");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUsername(invalidUsername);
    }

    // ========== REPOSITORY INTERACTION TESTS ==========

    @Test
    @DisplayName("Login: Should call repository exactly once per login attempt")
    void login_ShouldCallRepositoryOnce() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        authService.login("testUser", "correctPassword");

        verify(userRepository, times(1)).findByUsername("testUser");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Login: Should not call repository methods other than findByUsername")
    void login_ShouldOnlyCallFindByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        authService.login("testUser", "correctPassword");

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(userRepository, never()).findAll();
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Login: Should handle multiple sequential login attempts")
    void login_ShouldHandleMultipleSequentialAttempts() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean firstAttempt = authService.login("testUser", "wrongPassword");
        boolean secondAttempt = authService.login("testUser", "wrongPassword2");
        boolean thirdAttempt = authService.login("testUser", "correctPassword");

        assertThat(firstAttempt).isFalse();
        assertThat(secondAttempt).isFalse();
        assertThat(thirdAttempt).isTrue();
        verify(userRepository, times(3)).findByUsername("testUser");
    }

    @Test
    @DisplayName("Login: Should handle concurrent different user logins")
    void login_ShouldHandleDifferentUserLoginsConcurrently() {
        User user1 = new User(1L, "user1", "pass1", 100.0, "user1@example.com", District.EIXAMPLE);
        User user2 = new User(2L, "user2", "pass2", 150.0, "user2@example.com", District.GRÀCIA);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));

        boolean result1 = authService.login("user1", "pass1");
        boolean result2 = authService.login("user2", "pass2");

        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, times(1)).findByUsername("user2");
    }

    // ========== SPECIAL CASES ==========

    @Test
    @DisplayName("Login: Should handle very long username")
    void login_ShouldHandleVeryLongUsername() {
        String longUsername = "a".repeat(500);
        User userWithLongName = new User(
                10L,
                longUsername,
                "password",
                100.0,
                "long@example.com",
                District.EIXAMPLE
        );
        when(userRepository.findByUsername(longUsername)).thenReturn(Optional.of(userWithLongName));

        boolean result = authService.login(longUsername, "password");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername(longUsername);
    }

    @Test
    @DisplayName("Login: Should handle very long password")
    void login_ShouldHandleVeryLongPassword() {
        String longPassword = "p".repeat(1000);
        User userWithLongPassword = new User(
                11L,
                "longPassUser",
                longPassword,
                100.0,
                "longpass@example.com",
                District.GRÀCIA
        );
        when(userRepository.findByUsername("longPassUser")).thenReturn(Optional.of(userWithLongPassword));

        boolean result = authService.login("longPassUser", longPassword);

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername("longPassUser");
    }

    @Test
    @DisplayName("Login: Should handle username with unicode characters")
    void login_ShouldHandleUnicodeUsername() {
        String unicodeUsername = "user_测试_🔐";
        User unicodeUser = new User(
                12L,
                unicodeUsername,
                "password",
                100.0,
                "unicode@example.com",
                District.SARRIÀ_SANT_GERVASI
        );
        when(userRepository.findByUsername(unicodeUsername)).thenReturn(Optional.of(unicodeUser));

        boolean result = authService.login(unicodeUsername, "password");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUsername(unicodeUsername);
    }

    @Test
    @DisplayName("Login: Should be case-sensitive for password comparison")
    void login_ShouldBeCaseSensitiveForPassword() {
        User user = new User(1L, "testUser", "Password", 100.0, "test@example.com", District.EIXAMPLE);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        boolean withCorrectCase = authService.login("testUser", "Password");
        boolean withLowerCase = authService.login("testUser", "password");
        boolean withUpperCase = authService.login("testUser", "PASSWORD");

        assertThat(withCorrectCase).isTrue();
        assertThat(withLowerCase).isFalse();
        assertThat(withUpperCase).isFalse();
        verify(userRepository, times(3)).findByUsername("testUser");
    }
}