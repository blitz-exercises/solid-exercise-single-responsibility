package com.example.userregistration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the complete user registration flow.
 * 
 * IMPORTANT: This test class should remain UNTOUCHED during refactoring exercises.
 * The UserRegistrationService interface defines the public API contract that must be maintained.
 * 
 * Classes that should remain UNTOUCHED:
 * - UserRegistrationService (interface) - defines the immutable contract
 * - UserRegistrationIntegrationTest (this class) - verifies the contract is maintained
 * 
 * Refactoring goal: Extract responsibilities from UserRegistration implementation
 * into separate classes while maintaining the UserRegistrationService interface contract.
 */
public class UserRegistrationIntegrationTest {
    private UserRegistration registration;

    @BeforeEach
    public void setUp() {
        registration = new UserRegistration();
    }

    @Test
    public void testCompleteRegistrationFlow() {
        String email = "user@example.com";
        String password = "SecurePass123";

        // Step 1: Verify email validation
        assertTrue(registration.isValidEmail(email));
        assertFalse(registration.isValidEmail("invalid-email"));
        assertFalse(registration.isValidEmail("notanemail"));

        // Step 2: Verify password validation
        assertTrue(registration.isValidPassword(password));
        assertFalse(registration.isValidPassword("short"));
        assertFalse(registration.isValidPassword("nouppercase123"));
        assertFalse(registration.isValidPassword("NOLOWERCASE123"));

        // Step 3: Verify user does not exist
        assertFalse(registration.userExists(email));

        // Step 4: Register user
        RegistrationResult result = registration.registerUser(email, password);

        // Step 5: Verify registration succeeded
        assertTrue(result.isSuccess());
        assertEquals(email, result.getEmail());
        assertNotNull(result.getVerificationToken());
        assertTrue(result.getVerificationToken().startsWith("VERIFY-"));

        // Step 6: Verify user is stored
        assertTrue(registration.userExists(email));
        User user = registration.getUserByEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());

        // Step 7: Verify user is not activated yet
        assertFalse(registration.isAccountActivated(email));
        assertFalse(user.isActivated());

        // Step 8: Verify verification email was sent
        assertEquals(email, registration.getLastVerificationEmailSentTo());

        // Step 9: Verify verification token was generated
        VerificationToken token = registration.getVerificationToken(result.getVerificationToken());
        assertNotNull(token);
        assertEquals(email, token.getEmail());
        assertFalse(token.isUsed());
        assertFalse(token.isExpired());

        // Step 10: Verify token verification works
        assertTrue(registration.verifyToken(result.getVerificationToken(), email));
        assertFalse(registration.verifyToken("INVALID-TOKEN", email));

        // Step 11: Verify profile was initialized
        assertEquals("en", user.getProfileLanguage());
        assertEquals("UTC", user.getProfileTimezone());
        assertTrue(user.isEmailNotificationsEnabled());

        // Step 12: Verify password was hashed
        assertNotNull(user.getHashedPassword());
        assertTrue(user.getHashedPassword().startsWith("HASHED_"));
        assertTrue(registration.verifyPassword(password, user.getHashedPassword()));

        // Step 13: Verify registration logs were created
        List<String> logs = registration.getRegistrationLogs();
        assertFalse(logs.isEmpty());
        assertTrue(logs.stream().anyMatch(log -> log.contains("Registration attempt")));
        assertTrue(logs.stream().anyMatch(log -> log.contains("User registered successfully")));

        // Step 14: Activate account
        boolean activated = registration.activateAccount(email, result.getVerificationToken());

        // Step 15: Verify activation succeeded
        assertTrue(activated);
        assertTrue(registration.isAccountActivated(email));
        user = registration.getUserByEmail(email);
        assertTrue(user.isActivated());

        // Step 16: Verify token was marked as used
        token = registration.getVerificationToken(result.getVerificationToken());
        assertNotNull(token);
        assertTrue(token.isUsed());

        // Step 17: Verify welcome email was sent
        assertEquals(email, registration.getLastWelcomeEmailSentTo());

        // Step 18: Verify cannot activate with used token
        assertFalse(registration.activateAccount(email, result.getVerificationToken()));

        // Step 19: Verify cannot register duplicate email
        RegistrationResult duplicateResult = registration.registerUser(email, "AnotherPass123");
        assertFalse(duplicateResult.isSuccess());
        assertEquals("Email already registered", duplicateResult.getMessage());
    }

    @Test
    public void testInvalidRegistrationAttempts() {
        // Test invalid email format
        RegistrationResult result = registration.registerUser("invalid-email", "ValidPass123");
        assertFalse(result.isSuccess());
        assertEquals("Invalid email format", result.getMessage());

        // Test weak password
        result = registration.registerUser("user@example.com", "weak");
        assertFalse(result.isSuccess());
        assertEquals("Password does not meet requirements", result.getMessage());

        // Test null values
        assertFalse(registration.isValidEmail(null));
        assertFalse(registration.isValidPassword(null));
    }

    @Test
    public void testPasswordHashing() {
        String password = "TestPassword123";
        String hashed1 = registration.hashPassword(password);
        String hashed2 = registration.hashPassword(password);

        // Verify hashing produces consistent results
        assertEquals(hashed1, hashed2);

        // Verify password verification works
        assertTrue(registration.verifyPassword(password, hashed1));
        assertFalse(registration.verifyPassword("wrongpassword", hashed1));
    }
}


