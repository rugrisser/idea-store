package edu.mshp.ideastore.module.jwt;

import edu.mshp.ideastore.model.User;
import edu.mshp.ideastore.module.cypher.AESEncryptor;
import edu.mshp.ideastore.module.cypher.IEncryptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.Date;

/**
 * JWT Токен
 * @author rugrisser
 */
public class JWTToken {

    /** Срок жизни токена */
    private static final Long LIFETIME = 604800000L;
    /** Название ключа шифрования */
    private static final String KEY_TYPE = "jwt";
    /** Секретное слово */
    private static final String SECRET = "seulavemag";

    /** ID пользователя */
    @Getter
    private static Long id;
    /** Логин пользователя */
    private static String login;
    /** E-Mail пользователя */
    @Getter
    private static String email;
    /** Текущее время (дата создания) */
    @Getter
    private static Date issued;
    /** Срок годности токена */
    @Getter
    private static Date expiration;

    /**
     * Конструктор токена по модели пользователя
     * @param user Модель пользователя
     */
    public JWTToken(User user) {
        id = user.getId();
        login = user.getLogin();
        email = user.getEmail();
        issued = new Date();
        expiration = new Date(issued.getTime() + LIFETIME);
    }

    /**
     * Конструктор токена по токену (конструктор считает, что токен зашифрован)
     * @param token JWT Токен
     */
    public JWTToken(String token) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        this(token, true);
    }

    /**
     * Конструктор токена по токену
     * @param token JWT Токен
     * @param encrypted Использование дополнительного шифрования
     */
    public JWTToken(String token, Boolean encrypted) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        if (encrypted) {
            IEncryptor encryptor = new AESEncryptor(KEY_TYPE);
            token = encryptor.decrypt(token);
        }

        Claims body = (Claims) Jwts.parser().setSigningKey(SECRET).parse(token).getBody();
        login = body.getSubject();
        id = Long.valueOf((Integer) body.get("id"));
        email = (String) body.get("email");
        issued = body.getIssuedAt();
        expiration = body.getExpiration();
    }

    /**
     * Создание токена (с шифрованием)
     * @return зашифрованный JWT токен
     */
    public String createToken() {
        return createToken(true);
    }

    /**
     * Создание токена
     * @param encrypted Дополнительное шифрование токена
     * @return JWT Токен
     */
    @SneakyThrows
    public String createToken(Boolean encrypted) {
        issued = new Date();
        expiration = new Date(issued.getTime() + LIFETIME);

        Claims claims = Jwts.claims();
        claims.setSubject(login);
        claims.put("id", id);
        claims.put("email", email);
        claims.setIssuedAt(issued);
        claims.setExpiration(expiration);

        String token = Jwts.builder().addClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET).compact();

        if (encrypted) {
            IEncryptor encryptor = new AESEncryptor(KEY_TYPE);
            token = encryptor.encrypt(token);
        }

        return token;
    }
}
