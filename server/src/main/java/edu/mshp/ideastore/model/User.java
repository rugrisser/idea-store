package edu.mshp.ideastore.model;

import edu.mshp.ideastore.module.cypher.AESEncryptor;
import edu.mshp.ideastore.module.cypher.IEncryptor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.persistence.*;
import java.security.InvalidKeyException;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private String login;

    @Getter
    @Setter
    @Column(unique = true)
    private String email;

    private String password;

    public User() {
    }

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        setPassword(password);
    }

    @SneakyThrows
    public String getPassword() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        IEncryptor encryptor = new AESEncryptor("password");
        return encryptor.decrypt(password);
    }

    @SneakyThrows
    public void setPassword(String password) {
        IEncryptor encryptor = new AESEncryptor("password");
        this.password = encryptor.encrypt(password);
    }
}
