package infoq.spring4.controllers;

import infoq.spring4.data.Phone;
import infoq.spring4.data.User;
import infoq.spring4.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Transactional
@RequestMapping("/user/{userId}/phoneNumbers")
public class PhoneController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Set<Phone> list(@PathVariable Long userId) {
        User user = loadUser(userId);
        return user.getPhoneNumbers();
    }

    @RequestMapping(value = "/{phoneId}", method = RequestMethod.GET)
    public Phone get(@PathVariable Long userId, @PathVariable Long phoneId) {
        User user = loadUser(userId);
        for (Phone phone : user.getPhoneNumbers()) {
            if (phone != null && phoneId.equals(phone.getId())) {
                return phone;
            }
        }
        throw new PhoneNotFoundException();
    }

    @RequestMapping(value = "/{phoneId}", method = RequestMethod.PUT)
    public Phone update(@PathVariable Long userId, @PathVariable Long phoneId, @RequestBody Phone updated) {
        Phone phone = get(userId, phoneId);
        if (updated != null && updated.getPhoneNumber() != null && !phone.getPhoneNumber().equals(updated.getPhoneNumber())) {
            phone.setPhoneNumber(updated.getPhoneNumber());
        }
        if (updated != null && updated.getCountryCode() != null && !phone.getCountryCode().equals(updated.getCountryCode())) {
            phone.setCountryCode(updated.getCountryCode());
        }
        return phone;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Phone create(@PathVariable Long userId, @RequestBody Phone phone) {
        User user = loadUser(userId);
        user.getPhoneNumbers().add(phone);
        userRepository.save(user);
        return phone;
    }

    @RequestMapping(value = "/{phoneId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long userId, @PathVariable Long phoneId) {
        User user = userRepository.getOne(userId);
        Phone phone = get(userId, phoneId);
        user.getPhoneNumbers().remove(phone);
        userRepository.save(user);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class PhoneNotFoundException extends RuntimeException {}

    private User loadUser(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        user.getPhoneNumbers().size();
        return user;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class UserNotFoundException extends RuntimeException {}
}
