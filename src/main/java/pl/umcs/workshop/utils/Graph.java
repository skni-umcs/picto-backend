package pl.umcs.workshop.utils;
import pl.umcs.workshop.user.User;
import java.util.*;

public class Graph {
    private List<Map.Entry<Long, Long>> graph = new ArrayList<>();
    int k;
    CircularDoublyLinkedList users;

    public Graph(List<User> users, int k ) {
        this.users = listToCircular(users);
        this.k = k;
        generateGraph();
    }

    public void setUsers(List<User> users) {
        this.users = listToCircular(users);
        generateGraph();
    }

    public void setK(int k) {
        this.k = k;
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

    private boolean pairPresent(List<Map.Entry<Long, Long>> pairs, Long userOne, Long userTwo) {
        //order matters
        for (Map.Entry pair : pairs) {
            if (pair.getKey() == userOne && pair.getValue() == userTwo) {
                return true;
            }
        }
        return false;
    }

    private void addPair(List<Map.Entry<Long, Long>> pairs, Long userOne, Long userTwo) {
        if (!pairPresent(pairs, userOne, userTwo)) {
            pairs.add(new AbstractMap.SimpleEntry(userOne, userTwo));
            pairs.add(new AbstractMap.SimpleEntry(userTwo, userOne));
        }
    }

    private void addEdgesForIndex(List<Map.Entry<Long, Long>> pairs, int index) {
        CircularDoublyLinkedList.Node userOne = users.getElementAtIndex(index);
        int deviation = k / 2;
        CircularDoublyLinkedList.Node currentPreviousUser = userOne;
        CircularDoublyLinkedList.Node currentNextUser = userOne;
        for (int i = 0; i < deviation; i++) {
            currentPreviousUser = currentPreviousUser.getPrevious();
            currentNextUser = currentNextUser.getNext();
            addPair(pairs, userOne.getValue(), currentPreviousUser.getValue());
            addPair(pairs, userOne.getValue(), currentNextUser.getValue());
        }
        if (k % 2 == 1 && index < users.size()/2) {
            CircularDoublyLinkedList.Node opposite = userOne;
            for(int i = 0;i<users.size()/2;i++) {
                opposite = opposite.getNext();
            }
            addPair(pairs,userOne.getValue(), opposite.getValue());
        }
    }

    private void generateGraph() {
        List<Map.Entry<Long, Long>> pairs = new ArrayList<>();
        for (int index = 0; index < users.size(); ++index) {
            addEdgesForIndex(pairs, index);
        }
        this.graph = pairs;
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
