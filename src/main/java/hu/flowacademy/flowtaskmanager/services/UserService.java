package hu.flowacademy.flowtaskmanager.services;

import hu.flowacademy.flowtaskmanager.exceptions.ValidationException;
import hu.flowacademy.flowtaskmanager.models.Post;
import hu.flowacademy.flowtaskmanager.models.User;
import hu.flowacademy.flowtaskmanager.models.userDTO.UserRegisterDTO;
import hu.flowacademy.flowtaskmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByName(String name) {
        return userRepository.findByUsername(name);
    }

//        List<Post> listOfPosts = listOfUsers.stream().map(u -> u.getPosts().stream().filter(x -> x.getId() == id).map(y -> y.getUser()))
    public User saveUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        if (userRegisterDTO.getId() == null) {
            user.userFromUserDTO(userRegisterDTO);
            usernameValidator(user);
            // passwordValidator(user);
            // roleValidator(user);
            // user.setRole(User.Role.MENTOR);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User updateUser(UserRegisterDTO userRegisterDTO) {
        User userInDb = findUserByName(userRegisterDTO.getUsername()); // kiszedjük a db-ből a név alapján a user-t.
        if (userInDb == null) throw new ValidationException("There is no user with this username: " + userRegisterDTO.getUsername()); // ha nincs ilyen user -> exception
        userRegisterDTO.setId(userInDb.getId()); // beállítjuk a bemeneti paraméteren jött DTO-nak az ID-jét az adatbázisból jött user ID-jére.
        // User user = findUserById(userRegisterDTO.getId());
        userInDb.setRole(userRegisterDTO.getRole());
        roleValidator(userInDb);
        return userRepository.save(userInDb);
    }

    public void usernameValidator(User user) {
        if (findUserByName(user.getUsername()) != null) {
            throw new ValidationException("There is a registered user with this username.");
        }
        if (user.getUsername().length() < 3 || user.getUsername().length() > 20) {
            throw new ValidationException("The username needs to be between 3 and 20 characters.");
        }
    }

    public void passwordValidator(User user) {
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new ValidationException("The password needs to have at least 1 lowercase, 1 uppercase, 1 special, 1 digit character," +
                    " and needs to be between 6 and 16 characters.");
        }
    }

    public void roleValidator(User user) {
        if (user.getRole() == null) {
            throw new ValidationException("There is no role set to the user");
        }
    }

    public void usernameEqualityValidator(User user) {
        if (findUserById(user.getId()).getUsername() != user.getUsername()) {
            throw new ValidationException("You cannot change the username.");
        }
    }

    public void dateOfBirthEqualityValidator(User user) {
        if (findUserById(user.getId()).getDateOfBirth() != user.getDateOfBirth()) {
            throw new ValidationException("You cannot change your date of birth.");
        }
    }

    public void firstnameEqualityValidator(User user) {
        if (findUserById(user.getId()).getFirstName() != user.getFirstName()) {
            throw new ValidationException("You cannot change your firstname.");
        }
    }

    public void lastnameEqualityValidator(User user) {
        if (findUserById(user.getId()).getLastName() != user.getLastName()) {
            throw new ValidationException("You cannot change your lastname.");
        }
    }

    public void userDelete(Long id) {
        userRepository.deleteById(id);
    }

}