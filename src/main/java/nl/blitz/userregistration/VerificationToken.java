package nl.blitz.userregistration;

import java.time.LocalDateTime;

public class VerificationToken {
    private final String token;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private boolean used;

    public VerificationToken(String token, String email, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
        this.used = false;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}


