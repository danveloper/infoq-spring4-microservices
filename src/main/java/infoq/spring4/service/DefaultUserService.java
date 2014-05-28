package infoq.spring4.service;

import infoq.spring4.data.Phone;
import infoq.spring4.data.User;
import infoq.spring4.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultUserService implements UserService {
    private static final Date LOOKUP_DATE;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, Calendar.JANUARY, 1);
        LOOKUP_DATE = calendar.getTime();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<RankedUser> getFractionalizedUserDemographic() {
        List<User> fractionalizedUsers = userRepository.findFractionalizedUsersByCreateDateGreaterThan(LOOKUP_DATE);
        List<RankedUser> rankedUsers = new ArrayList<>();
        for (User user : fractionalizedUsers) {
            rankedUsers.add(getRankedUser(user));
        }
        return rankedUsers;
    }

    @Override
    public RankedUser getRankedUser(User user) {
        int score = getFractionalizedPhoneScore(user.getPhoneNumbers()) +
                getFractionalizedEmailScore(user.getEmailAddresses()) +
                getFractionalizedUsernameScore(user.getUsername());
        return new RankedUser(user, score);
    }

    private int getFractionalizedPhoneScore(Set<Phone> phoneNumbers) {
        int score = 0;
        for (Phone phone : phoneNumbers) {
            if (phone != null && phone.getPhoneNumber() != null
                    && "US".equals(phone.getCountryCode()) && phone.getPhoneNumber().startsWith("352")) {
                score += 2;
            }
        }
        return score;
    }

    private int getFractionalizedEmailScore(Set<String> emailAddresses) {
        int score = 0;
        for (String email : emailAddresses) {
            if (email != null && email.endsWith("@infoq.com")) {
                score += 2;
            }
        }
        return score;
    }

    private int getFractionalizedUsernameScore(String username) {
        return username != null && username.length() > 6 ? username.substring(6).length() : 0;
    }
}
