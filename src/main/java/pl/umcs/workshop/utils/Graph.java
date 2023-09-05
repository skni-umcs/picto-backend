package pl.umcs.workshop.utils;
import pl.umcs.workshop.user.User;
import java.util.*;

public class Graph {
    private List<Map.Entry<Long, Long>> graph = new ArrayList<>();
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

    public List<Long> getAdjVertices(User user) {
        List<Long> neighbors = new ArrayList<>();
        for(Map.Entry<Long, Long> edge : graph) {
            if(user.getId().equals(edge.getKey())) {
                neighbors.add(edge.getValue());
            }
            else if(user.getId().equals(edge.getValue())) {
                neighbors.add(edge.getKey());
            }
        }
        return neighbors;
    }

    //TODO: add pairing
    public void addEdge(User one, User two) {
        //placeholder
    }

    private boolean edgePresent(Long userOne, Long userTwo) {
        //order matters
        for (Map.Entry pair : graph) {
            if (pair.getKey() == userOne && pair.getValue() == userTwo) {
                return true;
            }
        }
        return false;
    }

    private void addEdge(Long userOne, Long userTwo) {
        if (!edgePresent(userOne, userTwo)) {
            graph.add(new AbstractMap.SimpleEntry(userOne, userTwo));
            graph.add(new AbstractMap.SimpleEntry(userTwo, userOne));
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
        if (k % 2 == 1 && index < circularUsers.size()/2) {
            CircularDoublyLinkedList.Node opposite = userOne;
            for(int i = 0; i< circularUsers.size()/2; i++) {
                opposite = opposite.getNext();
            }
            addEdge(userOne.getValue(), opposite.getValue());
        }
    }

    private User chooseRandomAvailableUser(Long currentUserId) {
        Random random = new Random();
        List<User> availableUsers = new ArrayList<>();
        for(User newUser : users) {
            if(newUser.getId().equals(currentUserId) || edgePresent(currentUserId,newUser.getId())) {
                //bad vertex
            }
            else {
                availableUsers.add(newUser);
            }
        }
        int userIndex = random.nextInt(availableUsers.size());
        return availableUsers.get(userIndex);
    }

    private void reGenerateEdge(Map.Entry<Long, Long> edge) {
        Long randomUserId = chooseRandomAvailableUser(edge.getKey()).getId();
        edge.setValue(randomUserId);
    }

    private void reGenerateEdges() {
        Random random = new Random();
        for(Map.Entry edge : graph) {
            double r = random.nextDouble();
            if(r < p) {
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
        for (Map.Entry<Long,Long> pair : graph) {
            Long a = pair.getKey();
            Long b = pair.getValue();
            System.out.println(String.format("(%d,%d),", a + 1, b + 1));
        }
    }

    //TODO: move to CircularDoublyLinkedList
    public CircularDoublyLinkedList listToCircular(List<User> users) {
        CircularDoublyLinkedList result = new CircularDoublyLinkedList();
        for (User user : users) {
            result.insertNodeEnd(user.getId());
        }
        return result;
    }
}
