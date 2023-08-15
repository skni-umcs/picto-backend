package pl.umcs.workshop.topology;

import org.springframework.stereotype.Service;
import pl.umcs.workshop.user.User;

import java.util.List;
import java.util.stream.Stream;

@Service
public class TopologyService {
    public List<Integer> generateBrackets(List<User> users) {
        // TODO: generate brackets based on topology
        // For now just returns a dummy list

        return Stream.of(1, 2, 3, 4, 5).toList();
    }
}
