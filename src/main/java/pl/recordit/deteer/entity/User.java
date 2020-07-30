package pl.recordit.deteer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.recordit.deteer.model.UserRole;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private String email;

    private LocalDate registered;

    private String password;

    @Enumerated
    private UserRole role;

    private boolean active;

    private boolean verified;

    public String getEmail(){
        return this.email;
    }

    public boolean isActive(){
        return active;
    }

    public boolean isVerified() {
        return verified;
    }

    public LocalDate getRegistered() {
        return registered;
    }
}
