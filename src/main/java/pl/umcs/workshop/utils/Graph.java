package pl.umcs.workshop.utils;

import pl.umcs.workshop.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Long, List<Long>> adjUsers = new HashMap<>();

    public void addVertex(User user) {
        adjUsers.putIfAbsent(user.getId(), new ArrayList<>());
    }

    public void removeVertex(User user) {
        adjUsers.values().forEach(e -> e.remove(user.getId()));
        adjUsers.remove(user.getId());
    }

    public void addEdge(User userOne, User userTwo) {
        adjUsers.get(userOne.getId()).add(userTwo.getId());
        adjUsers.get(userTwo.getId()).add(userOne.getId());
    }

    public void removeEdge(User userOne, User userTwo) {
        List<Long> eV1 = adjUsers.get(userOne.getId());
        List<Long> eV2 = adjUsers.get(userTwo.getId());

        if (eV1 != null)
            eV1.remove(userTwo.getId());
        if (eV2 != null)
            eV2.remove(userOne.getId());
    }

    public void addAllVertices(List<User> users) {
        for (User user : users) {
            addVertex(user);
        }
    }

    public List<Long> getAdjVertices(User user) {
        return adjUsers.get(user.getId());
    }

    public void printGraph() {
        for (Long userId : adjUsers.keySet()) {
            System.out.print(userId + ": ");

            for (Long neighId : adjUsers.get(userId)) {
                System.out.print(neighId + " ");
            }
            System.out.println();
        }
    }
}
