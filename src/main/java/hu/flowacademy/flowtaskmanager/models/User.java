package hu.flowacademy.flowtaskmanager.models;

import hu.flowacademy.flowtaskmanager.models.userDTO.UserLoginDTO;
import hu.flowacademy.flowtaskmanager.models.userDTO.UserRegisterDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "_users")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate dateOfBirth;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @ManyToMany(targetEntity = Task.class, mappedBy = "users", fetch = FetchType.EAGER)
    private List<Task> tasks;

    @Column
    @OneToMany
    private List<Post> posts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role {
        MENTOR,
        STUDENT
    }

    public void userFromUserDTO(UserRegisterDTO userRegisterDTO) {
        this.id = userRegisterDTO.getId();
        this.username = userRegisterDTO.getUsername();
        this.password = userRegisterDTO.getPassword();
        this.firstName = userRegisterDTO.getFirstName();
        this.lastName = userRegisterDTO.getLastName();
        this.dateOfBirth = userRegisterDTO.getDateOfBirth();
        this.role = userRegisterDTO.getRole();
    }

    public void userFromUserDTO(UserLoginDTO userLoginDTO) {
        this.username = userLoginDTO.getUsername();
        this.password = userLoginDTO.getPassword();
    }


}
