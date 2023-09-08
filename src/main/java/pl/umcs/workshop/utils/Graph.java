package pl.umcs.workshop.utils;

import static pl.umcs.workshop.utils.CircularDoublyLinkedList.listToCircular;

import java.util.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.umcs.workshop.user.User;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Graph {
    @Getter
    private final List<Map.Entry<User, User>> edges = new ArrayList<>();
    private final int k;
    private final double p;
    private CircularDoublyLinkedList circularUsers;
    private List<User> users;

    public List<User> getAdjVertices(User user) {
        List<User> neighbors = new ArrayList<>();

        for (Map.Entry<User, User> edge : edges) {
            if (user.getId().equals(edge.getKey().getId())) {
                neighbors.add(edge.getValue());
            } else if (user.getId().equals(edge.getValue().getId())) {
                neighbors.add(edge.getKey());
            }
        }

        return neighbors;
    }

    private boolean edgeNotPresent(User userOne, User userTwo) {
        for (Map.Entry<User, User> pair : edges) {
            if (pair.getKey().equals(userOne) && pair.getValue().equals(userTwo)
                    || pair.getKey().equals(userTwo) && pair.getValue().equals(userOne)) {
                return false;
            }
        }

        return true;
    }

    private void addEdge(User userOne, User userTwo) {
        if (edgeNotPresent(userOne, userTwo) && !userOne.equals(userTwo)) {
            edges.add(new AbstractMap.SimpleEntry<>(userOne, userTwo));
        }
    }

    private void addEdgesForIndex(int index) {
        CircularDoublyLinkedList.Node userOne = circularUsers.getElementAtIndex(index);
        CircularDoublyLinkedList.Node currentPreviousUser = userOne;
        CircularDoublyLinkedList.Node currentNextUser = userOne;

        int deviation = k / 2;
        for (int i = 0; i < deviation; i++) {
            currentPreviousUser = currentPreviousUser.getPrevious();
            currentNextUser = currentNextUser.getNext();
            addEdge(userOne.getValue(), currentPreviousUser.getValue());
            addEdge(userOne.getValue(), currentNextUser.getValue());
        }

        if (k % 2 == 1 && index < circularUsers.size() / 2) {
            CircularDoublyLinkedList.Node opposite = userOne;

            for (int i = 0; i < circularUsers.size() / 2; i++) {
                opposite = opposite.getNext();
            }

            addEdge(userOne.getValue(), opposite.getValue());
        }
    }

    private @Nullable User chooseRandomAvailableUser(User currentUser) {
        Random random = new Random();
        List<User> availableUsers = new ArrayList<>();

        for (User newUser : users) {
            if (!newUser.getId().equals(currentUser.getId()) && edgeNotPresent(currentUser, newUser)) {
                availableUsers.add(newUser);
            }
        }

        if(availableUsers.isEmpty()) {
            return null;
        }

        int userIndex = random.nextInt(availableUsers.size());

        return availableUsers.get(userIndex);
    }

    private void reGenerateEdge(@NotNull Map.Entry<User, User> edge) {
        User randomUser = chooseRandomAvailableUser(edge.getKey());
        if(randomUser != null) {
            edge.setValue(randomUser);
        }
    }

    private void reGenerateEdges() {
        Random random = new Random();

        for (Map.Entry<User, User> edge : edges) {
            double randomDouble = random.nextDouble();

            if (randomDouble < p) {
                reGenerateEdge(edge);
            }
        }
    }

    public List<Map.Entry<User, User>> generateGraph() {
        circularUsers = listToCircular(users);

        for (int index = 0; index < circularUsers.size(); index++) {
            addEdgesForIndex(index);
        }
        reGenerateEdges();

        return edges;
    }

    public void printGraph() {
        for (Map.Entry<User, User> pair : edges) {
            Long a = pair.getKey().getId();
            Long b = pair.getValue().getId();

            System.out.printf("(%d,%d),%n", a + 1, b + 1);
        }
    }
}
