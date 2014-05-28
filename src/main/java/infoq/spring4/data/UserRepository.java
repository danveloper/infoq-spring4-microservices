package infoq.spring4.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByUsernameLikeAndCreateDateGreaterThan(String user, Date date);

    @Query("select u from User u JOIN u.phoneNumbers p where u.createDate > ?1 and p.phoneNumber like '352%'")
    List<User> findFractionalizedUsersByCreateDateGreaterThan(Date createDate);
}
