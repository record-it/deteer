package pl.recordit.deteer.service;

import org.springframework.stereotype.Service;
import pl.recordit.deteer.entity.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class VerifyingTokenService {
    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private Optional<String> generateToken(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Optional.of(bytesToHex(digest.digest(str.getBytes(StandardCharsets.UTF_8))));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token, User user){
        Optional<String> generated = generateTokenForUser(user);
        return generateTokenForUser(user)
                .flatMap(userToken -> Optional.of(Objects.equals(token, userToken)))
                .orElse(false);
    }

    public Optional<String> generateTokenForUser(User user){
        if (user == null){
            return Optional.empty();
        }
        return generateToken(user.getDataToken());
    }

    public String generateVerifyingLinkForUser(User user, BiFunction<Long, String, String> linkGenerator){
        return generateTokenForUser(user)
                .flatMap(token -> Optional.of(linkGenerator.apply(user.getId(), token)))
                .orElse("");

    }
}
