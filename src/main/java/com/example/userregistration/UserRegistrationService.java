package com.example.userregistration;

import java.util.List;

/**
 * Contract for user registration operations.
 * 
 * IMPORTANT: This interface should remain UNTOUCHED during refactoring exercises.
 * It defines the public API that UserRegistration must implement.
 * 
 * Refactoring goal: Extract responsibilities from UserRegistration implementation
 * into separate classes while maintaining this interface contract.
 */
public interface UserRegistrationService {
    // User registration
    RegistrationResult registerUser(String email, String password);
    
    // User management
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean userExists(String email);
    
    // Validation
    boolean isValidEmail(String email);
    boolean isValidPassword(String password);
    
    // Account activation
    boolean activateAccount(String email, String token);
    boolean isAccountActivated(String email);
    
    // Password operations
    String hashPassword(String password);
    boolean verifyPassword(String password, String hashedPassword);
    
    // Verification token management
    String generateVerificationToken(String email);
    VerificationToken getVerificationToken(String token);
    boolean verifyToken(String token, String email);
    
    // Email operations
    void sendVerificationEmail(String email, String token);
    void sendWelcomeEmail(String email);
    String getLastVerificationEmailSentTo();
    String getLastWelcomeEmailSentTo();
    
    // Profile initialization
    void initializeUserProfile(User user);
    
    // Logging (for testing)
    List<String> getRegistrationLogs();
}


