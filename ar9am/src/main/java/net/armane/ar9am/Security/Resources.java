package net.armane.ar9am.Security;

public interface Resources {
    String JWT_SECRET = "Ar-Ma-Ne-Corp";
    long EXPIRATION_TIME = 864_000_000; // 10 jrs
    String TOKEN_PREFIX = "Bearer ";
    String AUTHORIZATION = "Authorization";
}
