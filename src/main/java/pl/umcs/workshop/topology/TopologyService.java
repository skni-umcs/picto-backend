package pl.umcs.workshop.topology;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.utils.Graph;

import java.util.*;

@Service
public class TopologyService {
    private Graph userAdjList;

    public void generateBrackets(List<User> users, Topology topology) {
        userAdjList = new Graph(users, topology.getMaxVertexDegree(), topology.getProbabilityOfEdgeRedrawing());
        Random random = new Random();

        for (User user : users) {
            List<User> unusedUsers = new ArrayList<>(users);
            unusedUsers.remove(user);

            while (userAdjList.getAdjVertices(user).size() < topology.getMaxVertexDegree()) {
                User generatedUser = unusedUsers.get((int) ((random.nextFloat() * unusedUsers.size()) - 1));
                //userAdjList.addEdge(user, generatedUser);
                unusedUsers.remove(generatedUser);
            }
        }

        userAdjList.printGraph();
    }
}
