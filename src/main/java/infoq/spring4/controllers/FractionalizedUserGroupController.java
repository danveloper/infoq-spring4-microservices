package infoq.spring4.controllers;

import infoq.spring4.data.User;
import infoq.spring4.data.UserRepository;
import infoq.spring4.service.RankedUser;
import infoq.spring4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping(value = "/fractionalized")
public class FractionalizedUserGroupController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<RankedUser> list() {
        return userService.getFractionalizedUserDemographic();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public RankedUser get(@PathVariable Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new RankedUserNotFoundException();
        }

        return userService.getRankedUser(user);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find ranked user!")
    private static class RankedUserNotFoundException extends RuntimeException {}
}
