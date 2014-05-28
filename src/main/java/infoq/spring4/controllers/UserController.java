package infoq.spring4.controllers;

import infoq.spring4.data.User;
import infoq.spring4.data.UserRepository;
import infoq.spring4.data.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> list() {
        return (List<User>)userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User get(@PathVariable Long userId) {
        return loadUser(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable Long userId, @RequestBody User update) {
        User user = loadUser(userId);
        if (update.getUsername() != null && !user.getUsername().equals(update.getUsername())) {
            user.setUsername(update.getUsername());
        }
        if (update.getEmailAddresses() != null && update.getEmailAddresses().size() > 0 &&
                !user.getEmailAddresses().containsAll(update.getEmailAddresses())) {
            user.setEmailAddresses(update.getEmailAddresses());
        }
        if (update.getPhoneNumbers() != null && update.getPhoneNumbers().size() > 0 &&
                !user.getPhoneNumbers().containsAll(update.getPhoneNumbers())) {
            user.setPhoneNumbers(update.getPhoneNumbers());
        }
        return userRepository.save(user);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create(@Validated @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringWriter sw = new StringWriter();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                sw.append(String.format("%s\n", objectError.getDefaultMessage()));
            }
            throw new BadUserDataException(sw.toString());
        }
        return userRepository.save(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long userId) {
        User user = loadUser(userId);
        userRepository.delete(user);
    }

    private User loadUser(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class UserNotFoundException extends RuntimeException {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class BadUserDataException extends RuntimeException {
        BadUserDataException(String msg) {
            super(msg);
        }
    }
}
