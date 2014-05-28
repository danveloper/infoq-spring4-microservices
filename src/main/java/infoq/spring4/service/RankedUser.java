package infoq.spring4.service;

import infoq.spring4.data.User;

import java.io.Serializable;

public class RankedUser implements Serializable {
    public final User user;
    public final int rank;

    RankedUser(User user, int rank) {
        this.user = user;
        this.rank = rank;
    }
}
