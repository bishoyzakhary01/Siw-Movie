package it.uniroma3.siw.service;

import it.uniroma3.siw.oauth.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The UserService handles logic for Users.
 */
@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;

    /**
     * This method retrieves a User from the DB based on its ID.
     * @param id the id of the User to retrieve from the DB
     * @return the retrieved User, or null if no User with the passed ID could be found in the DB
     */
    @Transactional
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }
    @Transactional
    public User getUserName(String logina) {
        Optional<User> result = this.userRepository.findByUsername(logina);
        return result.orElse(null);
    }
    /**
     * This method saves a User in the DB.
     * @param user the User to save into the DB
     * @return the saved User
     * @throws DataIntegrityViolationException if a User with the same username
     *                              as the passed User already exists in the DB
     */
    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * This method retrieves all Users from the DB.
     * @return a List with all the retrieved Users
     */
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }
    public void registerNewCustomerAfterOAuthLoginSuccess(String loginName, String fullName, AuthenticationProvider provider) {

        User user = new User();
        if(loginName != null) {
            user.setUsername(loginName);
            user.setName(fullName);
        }
        else{
            user.setUsername(loginName);
        }

        user.setCreationTimestamp(LocalDateTime.now());
        user.setoAuthProvider(provider);


        userRepository.save(user);

    }

    public void updateExistingUser(User user,String fullName, AuthenticationProvider provider){

        user.setName(fullName);
        user.setoAuthProvider(provider);

        userRepository.save(user);

    }
}

