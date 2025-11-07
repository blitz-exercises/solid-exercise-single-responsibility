package nl.blitz.userregistration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UserRegistration implements UserRegistrationService {
    private static final Logger logger = Logger.getLogger(UserRegistration.class.getName());
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int TOKEN_EXPIRY_HOURS = 24;
    
    private final List<User> users;
    private final List<VerificationToken> tokens;
    private final List<String> registrationLogs;
    private String lastVerificationEmailSentTo;
    private String lastWelcomeEmailSentTo;

    public UserRegistration() {
        this.users = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.registrationLogs = new ArrayList<>();
    }

    @Override
    public RegistrationResult registerUser(String email, String password) {
        logEvent("Registration attempt for email: " + email);
        
        // Validate email format
        if (!isValidEmail(email)) {
            logEvent("Registration failed: Invalid email format");
            return new RegistrationResult(false, "Invalid email format", email, null);
        }
        
        // Check for duplicate email
        if (userExists(email)) {
            logEvent("Registration failed: Email already exists");
            return new RegistrationResult(false, "Email already registered", email, null);
        }
        
        // Validate password strength
        if (!isValidPassword(password)) {
            logEvent("Registration failed: Password does not meet requirements");
            return new RegistrationResult(false, "Password does not meet requirements", email, null);
        }
        
        // Hash password
        String hashedPassword = hashPassword(password);
        
        // Create user
        User user = new User(email, hashedPassword);
        
        // Initialize user profile
        initializeUserProfile(user);
        
        // Store user
        users.add(user);
        
        // Generate verification token
        String token = generateVerificationToken(email);
        
        // Send verification email
        sendVerificationEmail(email, token);
        
        logEvent("User registered successfully: " + email);
        
        return new RegistrationResult(true, "Registration successful. Please check your email for verification.", email, token);
    }

    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }

    @Override
    public boolean userExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public User getUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public String hashPassword(String password) {
        // Simulate password hashing (simplified for exercise)
        // In real application, use proper hashing like BCrypt
        String salt = "SALT_" + password.length();
        String hash = password + salt;
        return "HASHED_" + hash.hashCode();
    }

    @Override
    public boolean verifyPassword(String password, String hashedPassword) {
        String computedHash = hashPassword(password);
        return computedHash.equals(hashedPassword);
    }

    @Override
    public String generateVerificationToken(String email) {
        String token = "VERIFY-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);
        
        VerificationToken verificationToken = new VerificationToken(token, email, expiresAt);
        tokens.add(verificationToken);
        
        logger.info("Generated verification token for: " + email);
        return token;
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokens.stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean verifyToken(String token, String email) {
        VerificationToken verificationToken = getVerificationToken(token);
        if (verificationToken == null) {
            return false;
        }
        if (verificationToken.isUsed()) {
            return false;
        }
        if (verificationToken.isExpired()) {
            return false;
        }
        return verificationToken.getEmail().equalsIgnoreCase(email);
    }

    @Override
    public boolean activateAccount(String email, String token) {
        logEvent("Account activation attempt for: " + email);
        
        if (!verifyToken(token, email)) {
            logEvent("Account activation failed: Invalid or expired token");
            return false;
        }
        
        User user = getUserByEmail(email);
        if (user == null) {
            logEvent("Account activation failed: User not found");
            return false;
        }
        
        user.setActivated(true);
        
        VerificationToken verificationToken = getVerificationToken(token);
        if (verificationToken != null) {
            verificationToken.setUsed(true);
        }
        
        // Send welcome email
        sendWelcomeEmail(email);
        
        logEvent("Account activated successfully: " + email);
        return true;
    }

    @Override
    public boolean isAccountActivated(String email) {
        User user = getUserByEmail(email);
        return user != null && user.isActivated();
    }

    @Override
    public void sendVerificationEmail(String email, String token) {
        String subject = "Verify Your Account";
        String verificationLink = "https://example.com/verify?token=" + token + "&email=" + email;
        String body = buildVerificationEmailBody(email, verificationLink, token);
        
        logger.info("Sending verification email to: " + email);
        logger.info("Subject: " + subject);
        logger.info("Body: " + body);
        
        // Simulate email sending
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        lastVerificationEmailSentTo = email;
        logger.info("Verification email sent successfully to: " + email);
    }

    private String buildVerificationEmailBody(String email, String verificationLink, String token) {
        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("Thank you for registering with us!\n\n");
        body.append("Please verify your email address by clicking the link below:\n");
        body.append(verificationLink).append("\n\n");
        body.append("Or use this verification code: ").append(token).append("\n\n");
        body.append("This link will expire in ").append(TOKEN_EXPIRY_HOURS).append(" hours.\n\n");
        body.append("If you did not create an account, please ignore this email.\n\n");
        body.append("Best regards,\n");
        body.append("The Team");
        return body.toString();
    }

    @Override
    public void sendWelcomeEmail(String email) {
        User user = getUserByEmail(email);
        if (user == null) {
            return;
        }
        
        String subject = "Welcome to Our Platform!";
        String body = buildWelcomeEmailBody(user);
        
        logger.info("Sending welcome email to: " + email);
        logger.info("Subject: " + subject);
        logger.info("Body: " + body);
        
        // Simulate email sending
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        lastWelcomeEmailSentTo = email;
        logger.info("Welcome email sent successfully to: " + email);
    }

    private String buildWelcomeEmailBody(User user) {
        StringBuilder body = new StringBuilder();
        body.append("Welcome ").append(user.getEmail()).append("!\n\n");
        body.append("Your account has been successfully activated.\n\n");
        body.append("Your account details:\n");
        body.append("- Email: ").append(user.getEmail()).append("\n");
        body.append("- Registration Date: ").append(user.getRegistrationDate()).append("\n");
        body.append("- Language: ").append(user.getProfileLanguage()).append("\n");
        body.append("- Timezone: ").append(user.getProfileTimezone()).append("\n");
        body.append("- Email Notifications: ").append(user.isEmailNotificationsEnabled() ? "Enabled" : "Disabled").append("\n\n");
        body.append("Thank you for joining us!\n\n");
        body.append("Best regards,\n");
        body.append("The Team");
        return body.toString();
    }

    @Override
    public String getLastVerificationEmailSentTo() {
        return lastVerificationEmailSentTo;
    }

    @Override
    public String getLastWelcomeEmailSentTo() {
        return lastWelcomeEmailSentTo;
    }

    @Override
    public void initializeUserProfile(User user) {
        // Initialize default profile settings
        user.setProfileLanguage("en");
        user.setProfileTimezone("UTC");
        user.setEmailNotificationsEnabled(true);
        
        logger.info("Initialized default profile for user: " + user.getEmail());
    }

    @Override
    public List<String> getRegistrationLogs() {
        return new ArrayList<>(registrationLogs);
    }

    private void logEvent(String event) {
        String timestamp = LocalDateTime.now().toString();
        String logEntry = "[" + timestamp + "] " + event;
        registrationLogs.add(logEntry);
        logger.info(logEntry);
    }
}


