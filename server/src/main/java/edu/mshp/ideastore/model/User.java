package edu.mshp.ideastore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.mshp.ideastore.module.cypher.AESEncryptor;
import edu.mshp.ideastore.module.cypher.IEncryptor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.persistence.*;
import java.security.InvalidKeyException;
import java.util.Date;

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

    @JsonIgnore
    private String password;

    @CreatedDate
    @Getter
    @JsonIgnore
    private Date created;

    @LastModifiedDate
    @Getter
    @JsonIgnore
    private Date updated;

    public User() {
    }

    public User(String login, String email, String password) {
        Date now = new Date();

        this.login = login;
        this.email = email;
        created = now;
        updated = now;
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
        updateDate();
    }

    @SneakyThrows
    public Boolean comparePassword(String password) {
        IEncryptor encryptor = new AESEncryptor("password");
        String encryptedPassword = encryptor.encrypt(password);

        return encryptedPassword.equals(this.password);
    }

    public void updateDate() {
        updated = new Date();
    }
}
