package infoq.spring4.data;

import infoq.spring4.service.RankedUser;
import infoq.spring4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "username", "username.empty");
        User user = (User) target;
        RankedUser rankedUser = userService.getRankedUser(user);
        if (rankedUser.rank > 10) {
            errors.reject("rank", "rank.too.high");
        }
    }
}
