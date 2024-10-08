package com.devtegani_study.investimentsaggregator.entities;

import com.devtegani_study.investimentsaggregator.controllers.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(name = "userName")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public User(UserDTO userData) {
        this.userId = UUID.randomUUID();
        this.userName = userData.username();
        this.email = userData.email();
        this.password = userData.password();
        this.creationTimestamp = Instant.now();
        this.updateTimestamp = null;
    }

}
