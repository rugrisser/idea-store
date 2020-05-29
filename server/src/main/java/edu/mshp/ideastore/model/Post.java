package edu.mshp.ideastore.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @Column(columnDefinition = "text")
    private String body;

    @Getter
    @Setter
    @Column(columnDefinition = "text")
    private String sub;

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Getter
    @CreatedDate
    private final Date created;

    @Getter
    @LastModifiedDate
    private Date updated;

    public Post() {
        Date now = new Date();
        created = now;
        updated = now;
    }

    public void updateDate() {
        updated = new Date();
    }
}
