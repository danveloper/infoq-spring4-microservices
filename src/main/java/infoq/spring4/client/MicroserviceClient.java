package infoq.spring4.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MicroserviceClient {
    private static final String FRACTIONALIZED =
            System.getProperty("user.microservice.host", "http://localhost:8080/fractionalized");

    private final RestTemplate restTemplate;

    public MicroserviceClient() {
        this.restTemplate = new RestTemplate();
    }

    public static class FractionalizedUser {
        public final Long id;
        public final String username;

        FractionalizedUser(Long id, String username) {
            this.id = id;
            this.username = username;
        }
    }

    public List<FractionalizedUser> getFractionalizedUsers() {
        List response = restTemplate.getForObject(FRACTIONALIZED, List.class);
        List<FractionalizedUser> users = new ArrayList<>();
        for (Object obj : response) {
            if (obj instanceof Map) {
                Map map = (Map) obj;
                Map user = (Map)map.get("user");
                users.add(new FractionalizedUser(((Integer)user.get("id")).longValue(), (String)user.get("username")));
            }
        }
        return users;
    }

    public int getRankForUser(Long userId) {
        Map response = restTemplate.getForObject(String.format("%s/%s", FRACTIONALIZED, userId), Map.class);
        return (int)response.get("rank");
    }
}
