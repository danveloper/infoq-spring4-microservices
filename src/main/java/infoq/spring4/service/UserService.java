package infoq.spring4.service;

import infoq.spring4.data.User;

import java.util.List;

public interface UserService {
    List<RankedUser> getFractionalizedUserDemographic();
    RankedUser getRankedUser(User user);
}
