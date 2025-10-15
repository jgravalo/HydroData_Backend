package hackatonGrupUn.repteTres.service;

import hackatonGrupUn.repteTres.model.User;
import hackatonGrupUn.repteTres.model.userEnum.District;
import hackatonGrupUn.repteTres.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
                1L,
                "testUser",
                "password123",
                150.5,
                "test@example.com",
                District.EIXAMPLE
        );
    }

    // ========== GET ALL USERS TESTS ==========

    @Test
    @DisplayName("GetAllUsers: Should return all users from repository")
    void getAllUsers_ShouldReturnAllUsers_WhenRepositoryHasData() {
        // Given
        List<User> expectedUsers = Arrays.asList(
                new User(1L, "user1", "pass1", 100.0, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 150.0, "user2@example.com", District.GRÀCIA),
                new User(3L, "user3", "pass3", 200.0, "user3@example.com", District.SARRIÀ_SANT_GERVASI)
        );
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(expectedUsers);
        assertThat(result).containsExactlyElementsOf(expectedUsers);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAllUsers: Should return empty list when no users exist")
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAllUsers: Should return single user when repository has one record")
    void getAllUsers_ShouldReturnSingleUser_WhenOnlyOneUserExists() {
        // Given
        List<User> singleUserList = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(singleUserList);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testUser);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAllUsers: Should handle large dataset")
    void getAllUsers_ShouldHandleLargeDataset() {
        // Given
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
        when(userRepository.findAll()).thenReturn(largeUserList);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1000);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAllUsers: Should call repository exactly once")
    void getAllUsers_ShouldCallRepositoryOnce() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        userService.getAllUsers();

        // Then
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("GetAllUsers: Should return users with different districts")
    void getAllUsers_ShouldReturnUsersWithDifferentDistricts() {
        // Given
        List<User> users = Arrays.asList(
                new User(1L, "user1", "pass1", 100.0, "user1@example.com", District.EIXAMPLE),
                new User(2L, "user2", "pass2", 150.0, "user2@example.com", District.GRÀCIA),
                new User(3L, "user3", "pass3", 200.0, "user3@example.com", null)
        );
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(User::getDistrict)
                .containsExactly(District.EIXAMPLE, District.GRÀCIA, null);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAllUsers: Should maintain order returned by repository")
    void getAllUsers_ShouldMaintainOrderFromRepository() {
        // Given
        List<User> orderedUsers = Arrays.asList(
                new User(3L, "third", "pass3", 100.0, "third@example.com", District.SARRIÀ_SANT_GERVASI),
                new User(1L, "first", "pass1", 150.0, "first@example.com", District.EIXAMPLE),
                new User(2L, "second", "pass2", 200.0, "second@example.com", District.GRÀCIA)
        );
        when(userRepository.findAll()).thenReturn(orderedUsers);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result).containsExactlyElementsOf(orderedUsers);

        verify(userRepository, times(1)).findAll();
    }

    // ========== FIND BY USERNAME TESTS ==========

    @Test
    @DisplayName("FindByUsername: Should return Optional with user when user exists")
    void findByUsername_ShouldReturnOptionalWithUser_WhenUserExists() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByUsername("testUser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testUser);
        assertThat(result.get().getUsername()).isEqualTo("testUser");
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");

        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    @DisplayName("FindByUsername: Should return empty Optional when user does not exist")
    void findByUsername_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByUsername("nonExistentUser");

        // Then
        assertThat(result).isEmpty();

        verify(userRepository, times(1)).findByUsername("nonExistentUser");
    }

    @Test
    @DisplayName("FindByUsername: Should be case-sensitive")
    void findByUsername_ShouldBeCaseSensitive() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("TestUser")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("TESTUSER")).thenReturn(Optional.empty());

        // When
        Optional<User> correctCase = userService.findByUsername("testUser");
        Optional<User> differentCase1 = userService.findByUsername("TestUser");
        Optional<User> differentCase2 = userService.findByUsername("TESTUSER");

        // Then
        assertThat(correctCase).isPresent();
        assertThat(differentCase1).isEmpty();
        assertThat(differentCase2).isEmpty();

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(userRepository, times(1)).findByUsername("TestUser");
        verify(userRepository, times(1)).findByUsername("TESTUSER");
    }

    @Test
    @DisplayName("FindByUsername: Should handle username with special characters")
    void findByUsername_ShouldHandleSpecialCharacters() {
        // Given
        User userWithSpecialChars = new User(
                2L,
                "user@example.com",
                "password",
                100.0,
                "user@example.com",
                District.GRÀCIA
        );
        when(userRepository.findByUsername("user@example.com")).thenReturn(Optional.of(userWithSpecialChars));

        // When
        Optional<User> result = userService.findByUsername("user@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("user@example.com");

        verify(userRepository, times(1)).findByUsername("user@example.com");
    }

    @Test
    @DisplayName("FindByUsername: Should call repository exactly once")
    void findByUsername_ShouldCallRepositoryOnce() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When
        userService.findByUsername("anyUser");

        // Then
        verify(userRepository, times(1)).findByUsername("anyUser");
        verifyNoMoreInteractions(userRepository);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("FindByUsername: Should handle invalid usernames")
    void findByUsername_ShouldHandleInvalidUsernames(String invalidUsername) {
        // Given
        when(userRepository.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByUsername(invalidUsername);

        // Then
        assertThat(result).isEmpty();

        verify(userRepository, times(1)).findByUsername(invalidUsername);
    }

    @Test
    @DisplayName("FindByUsername: Should return user with all properties populated")
    void findByUsername_ShouldReturnUserWithAllProperties() {
        // Given
        User completeUser = new User(
                10L,
                "completeUser",
                "securePassword",
                175.5,
                "complete@example.com",
                District.CIUTAT_VELLA
        );
        when(userRepository.findByUsername("completeUser")).thenReturn(Optional.of(completeUser));

        // When
        Optional<User> result = userService.findByUsername("completeUser");

        // Then
        assertThat(result).isPresent();
        User foundUser = result.get();
        assertThat(foundUser.getId()).isEqualTo(10L);
        assertThat(foundUser.getUsername()).isEqualTo("completeUser");
        assertThat(foundUser.getPassword()).isEqualTo("securePassword");
        assertThat(foundUser.getWaterConsumption()).isEqualTo(175.5);
        assertThat(foundUser.getEmail()).isEqualTo("complete@example.com");
        assertThat(foundUser.getDistrict()).isEqualTo(District.CIUTAT_VELLA);

        verify(userRepository, times(1)).findByUsername("completeUser");
    }

    @Test
    @DisplayName("FindByUsername: Should handle very long username")
    void findByUsername_ShouldHandleVeryLongUsername() {
        // Given
        String longUsername = "a".repeat(500);
        User userWithLongName = new User(
                5L,
                longUsername,
                "password",
                100.0,
                "long@example.com",
                District.GRÀCIA
        );
        when(userRepository.findByUsername(longUsername)).thenReturn(Optional.of(userWithLongName));

        // When
        Optional<User> result = userService.findByUsername(longUsername);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).hasSize(500);

        verify(userRepository, times(1)).findByUsername(longUsername);
    }

    // ========== SAVE USER TESTS ==========

    @Test
    @DisplayName("Save: Should save new user and return saved user")
    void save_ShouldSaveNewUser_AndReturnSavedUser() {
        // Given
        User newUser = new User(
                null,
                "newUser",
                "password",
                120.0,
                "new@example.com",
                District.GRÀCIA
        );
        User savedUser = new User(
                10L,
                "newUser",
                "password",
                120.0,
                "new@example.com",
                District.GRÀCIA
        );
        when(userRepository.save(newUser)).thenReturn(savedUser);

        // When
        User result = userService.save(newUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getUsername()).isEqualTo("newUser");
        assertThat(result.getEmail()).isEqualTo("new@example.com");

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Save: Should update existing user")
    void save_ShouldUpdateExistingUser() {
        // Given
        User existingUser = new User(
                1L,
                "existingUser",
                "oldPassword",
                100.0,
                "old@example.com",
                District.EIXAMPLE
        );
        User updatedUser = new User(
                1L,
                "existingUser",
                "newPassword",
                150.0,
                "new@example.com",
                District.GRÀCIA
        );
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // When
        User result = userService.save(existingUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassword()).isEqualTo("newPassword");
        assertThat(result.getWaterConsumption()).isEqualTo(150.0);
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        assertThat(result.getDistrict()).isEqualTo(District.GRÀCIA);

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Save: Should pass exact user object to repository")
    void save_ShouldPassExactUserObjectToRepository() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.save(testUser);

        // Then
        verify(userRepository, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser).isSameAs(testUser);
        assertThat(capturedUser.getUsername()).isEqualTo("testUser");
        assertThat(capturedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Save: Should call repository exactly once")
    void save_ShouldCallRepositoryOnce() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.save(testUser);

        // Then
        verify(userRepository, times(1)).save(testUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Save: Should handle user with null waterConsumption")
    void save_ShouldHandleUserWithNullWaterConsumption() {
        // Given
        User userWithNullConsumption = new User(
                null,
                "user",
                "password",
                null,
                "user@example.com",
                District.EIXAMPLE
        );
        User savedUser = new User(
                1L,
                "user",
                "password",
                null,
                "user@example.com",
                District.EIXAMPLE
        );
        when(userRepository.save(userWithNullConsumption)).thenReturn(savedUser);

        // When
        User result = userService.save(userWithNullConsumption);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getWaterConsumption()).isNull();

        verify(userRepository, times(1)).save(userWithNullConsumption);
    }

    @Test
    @DisplayName("Save: Should handle user with null district")
    void save_ShouldHandleUserWithNullDistrict() {
        // Given
        User userWithNullDistrict = new User(
                null,
                "user",
                "password",
                100.0,
                "user@example.com",
                null
        );
        User savedUser = new User(
                1L,
                "user",
                "password",
                100.0,
                "user@example.com",
                null
        );
        when(userRepository.save(userWithNullDistrict)).thenReturn(savedUser);

        // When
        User result = userService.save(userWithNullDistrict);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDistrict()).isNull();

        verify(userRepository, times(1)).save(userWithNullDistrict);
    }

    @Test
    @DisplayName("Save: Should handle user with zero water consumption")
    void save_ShouldHandleUserWithZeroWaterConsumption() {
        // Given
        User userWithZeroConsumption = new User(
                null,
                "user",
                "password",
                0.0,
                "user@example.com",
                District.GRÀCIA
        );
        User savedUser = new User(
                1L,
                "user",
                "password",
                0.0,
                "user@example.com",
                District.GRÀCIA
        );
        when(userRepository.save(userWithZeroConsumption)).thenReturn(savedUser);

        // When
        User result = userService.save(userWithZeroConsumption);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getWaterConsumption()).isEqualTo(0.0);

        verify(userRepository, times(1)).save(userWithZeroConsumption);
    }

    @Test
    @DisplayName("Save: Should handle user with negative water consumption")
    void save_ShouldHandleUserWithNegativeWaterConsumption() {
        // Given
        User userWithNegativeConsumption = new User(
                null,
                "user",
                "password",
                -50.0,
                "user@example.com",
                District.SARRIÀ_SANT_GERVASI
        );
        User savedUser = new User(
                1L,
                "user",
                "password",
                -50.0,
                "user@example.com",
                District.SARRIÀ_SANT_GERVASI
        );
        when(userRepository.save(userWithNegativeConsumption)).thenReturn(savedUser);

        // When
        User result = userService.save(userWithNegativeConsumption);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getWaterConsumption()).isEqualTo(-50.0);

        verify(userRepository, times(1)).save(userWithNegativeConsumption);
    }

    @Test
    @DisplayName("Save: Should save user with all districts")
    void save_ShouldSaveUserWithAllDistricts() {
        // Given
        for (District district : District.values()) {
            User user = new User(null, "user", "password", 100.0, "user@example.com", district);
            User savedUser = new User(1L, "user", "password", 100.0, "user@example.com", district);
            when(userRepository.save(user)).thenReturn(savedUser);

            // When
            User result = userService.save(user);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getDistrict()).isEqualTo(district);
        }

        verify(userRepository, times(District.values().length)).save(any(User.class));
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    @DisplayName("Integration: Should save user and then find by username")
    void integration_ShouldSaveUserAndThenFindByUsername() {
        // Given
        User newUser = new User(
                null,
                "integrationUser",
                "password",
                125.0,
                "integration@example.com",
                District.EIXAMPLE
        );
        User savedUser = new User(
                5L,
                "integrationUser",
                "password",
                125.0,
                "integration@example.com",
                District.EIXAMPLE
        );

        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userRepository.findByUsername("integrationUser")).thenReturn(Optional.of(savedUser));

        // When
        User saved = userService.save(newUser);
        Optional<User> found = userService.findByUsername("integrationUser");

        // Then
        assertThat(saved).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getUsername()).isEqualTo(saved.getUsername());

        verify(userRepository, times(1)).save(newUser);
        verify(userRepository, times(1)).findByUsername("integrationUser");
    }

    @Test
    @DisplayName("Integration: Should save user and retrieve in getAllUsers")
    void integration_ShouldSaveUserAndRetrieveInGetAllUsers() {
        // Given
        User newUser = new User(null, "user1", "pass1", 100.0, "user1@example.com", District.GRÀCIA);
        User savedUser = new User(1L, "user1", "pass1", 100.0, "user1@example.com", District.GRÀCIA);

        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));

        // When
        User saved = userService.save(newUser);
        List<User> allUsers = userService.getAllUsers();

        // Then
        assertThat(saved).isNotNull();
        assertThat(allUsers).hasSize(1);
        assertThat(allUsers.get(0).getId()).isEqualTo(saved.getId());

        verify(userRepository, times(1)).save(newUser);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Integration: Should handle multiple sequential operations")
    void integration_ShouldHandleMultipleSequentialOperations() {
        // Given
        User user1 = new User(null, "user1", "pass1", 100.0, "user1@example.com", District.EIXAMPLE);
        User user2 = new User(null, "user2", "pass2", 150.0, "user2@example.com", District.GRÀCIA);
        User savedUser1 = new User(1L, "user1", "pass1", 100.0, "user1@example.com", District.EIXAMPLE);
        User savedUser2 = new User(2L, "user2", "pass2", 150.0, "user2@example.com", District.GRÀCIA);

        when(userRepository.save(user1)).thenReturn(savedUser1);
        when(userRepository.save(user2)).thenReturn(savedUser2);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(savedUser1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(savedUser2));
        when(userRepository.findAll()).thenReturn(Arrays.asList(savedUser1, savedUser2));

        // When
        User saved1 = userService.save(user1);
        User saved2 = userService.save(user2);
        Optional<User> found1 = userService.findByUsername("user1");
        Optional<User> found2 = userService.findByUsername("user2");
        List<User> allUsers = userService.getAllUsers();

        // Then
        assertThat(saved1).isNotNull();
        assertThat(saved2).isNotNull();
        assertThat(found1).isPresent();
        assertThat(found2).isPresent();
        assertThat(allUsers).hasSize(2);

        verify(userRepository, times(2)).save(any(User.class));
        verify(userRepository, times(2)).findByUsername(anyString());
        verify(userRepository, times(1)).findAll();
    }

    // ========== METHOD ISOLATION TESTS ==========

    @Test
    @DisplayName("Should verify each method calls only its corresponding repository method")
    void shouldVerifyMethodIsolation() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
        verify(userRepository, never()).findByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));

        reset(userRepository);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        userService.findByUsername("test");
        verify(userRepository, times(1)).findByUsername("test");
        verify(userRepository, never()).findAll();
        verify(userRepository, never()).save(any(User.class));

        reset(userRepository);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.save(testUser);
        verify(userRepository, times(1)).save(testUser);
        verify(userRepository, never()).findAll();
        verify(userRepository, never()).findByUsername(anyString());
    }
}