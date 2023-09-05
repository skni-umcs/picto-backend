package pl.umcs.workshop.utils;

import pl.umcs.workshop.user.User;

import java.util.*;

public class Graph {

    private List<Map.Entry<User, User>> edges = new ArrayList<>();
    int k;
    double p;
    CircularDoublyLinkedList circularUsers;

    List<User> users;

    public Graph(List<User> users, int k, double p) {
        this.users = users;
        this.circularUsers = listToCircular(users);
        this.k = k;
        this.p = p;
        generateGraph();
    }

    public List<Map.Entry<User, User>> getEdges() {
        return edges;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.circularUsers = listToCircular(users);
        generateGraph();
    }

    public void setK(int k) {
        this.k = k;
        generateGraph();
    }

    public void setP(double p) {
        this.p = p;
        generateGraph();
    }

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

    //TODO: add pairing
    public void addPair(User one, User two) {
        //placeholder
    }

    private boolean edgePresent(User userOne, User userTwo) {
        for (Map.Entry pair : edges) {
            if (pair.getKey().equals(userOne) && pair.getValue().equals(userTwo) || pair.getKey().equals(userTwo) && pair.getValue().equals(userOne)) {
                return true;
            }
        }
        return false;
    }

    private void addEdge(User userOne, User userTwo) {
        if (!edgePresent(userOne, userTwo) && !userOne.equals(userTwo)) {
            edges.add(new AbstractMap.SimpleEntry(userOne, userTwo));
        }
    }

    private void addEdgesForIndex(int index) {
        CircularDoublyLinkedList.Node userOne = circularUsers.getElementAtIndex(index);
        int deviation = k / 2;
        CircularDoublyLinkedList.Node currentPreviousUser = userOne;
        CircularDoublyLinkedList.Node currentNextUser = userOne;
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

    private User chooseRandomAvailableUser(User currentUser) {
        Random random = new Random();
        List<User> availableUsers = new ArrayList<>();
        for (User newUser : users) {
            if (newUser.getId().equals(currentUser.getId()) || edgePresent(currentUser, newUser)) {
                //bad vertex
            } else {
                availableUsers.add(newUser);
            }
        }
        int userIndex = random.nextInt(availableUsers.size());
        return availableUsers.get(userIndex);
    }

    private void reGenerateEdge(Map.Entry<User, User> edge) {
        User randomUser = chooseRandomAvailableUser(edge.getKey());
        edge.setValue(randomUser);
    }

    private void reGenerateEdges() {
        Random random = new Random();
        for (Map.Entry<User, User> edge : edges) {
            double r = random.nextDouble();
            if (r < p) {
                reGenerateEdge(edge);
            }
        }
    }

    private void generateGraph() {
        for (int index = 0; index < circularUsers.size(); ++index) {
            addEdgesForIndex(index);
        }
        reGenerateEdges();
    }

    public void printGraph() {
        for (Map.Entry<User, User> pair : edges) {
            Long a = pair.getKey().getId();
            Long b = pair.getValue().getId();
            System.out.println(String.format("(%d,%d),", a + 1, b + 1));
        }
    }

    //TODO: move to CircularDoublyLinkedList
    public CircularDoublyLinkedList listToCircular(List<User> users) {
        CircularDoublyLinkedList result = new CircularDoublyLinkedList();
        for (User user : users) {
            result.insertNodeEnd(user);
        }
        return result;
    }
}
