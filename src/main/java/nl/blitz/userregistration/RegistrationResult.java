package nl.blitz.userregistration;

public class RegistrationResult {
    private final boolean success;
    private final String message;
    private final String email;
    private final String verificationToken;

    public RegistrationResult(boolean success, String message, String email, String verificationToken) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.verificationToken = verificationToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationToken() {
        return verificationToken;
    }
}


