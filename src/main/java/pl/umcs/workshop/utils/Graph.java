package pl.umcs.workshop.utils;


import pl.umcs.workshop.user.User;

import java.util.*;

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


    public static Map<Integer, Integer> lowestValues(Map<Integer, Integer> values) {
        int lowest = Integer.MAX_VALUE;
        Map<Integer, Integer> result = new HashMap<>();
        for (Integer key : values.keySet()) {
            Integer value = values.get(key);
            if(value == lowest) {
                result.put(key, value);
            }
            else if(value < lowest) {
                lowest = value;
                result = new HashMap<>();
                result.put(key, value);
            }
        }
        return result;
    }

    public static Integer chooseRandomLowestVertexPositionWithoutTaken(Map<Integer, Integer> verticesConnectionCount, Integer currentVertex, List<Map.Entry<Integer,Integer>> pairs) {
        Random random = new Random();
        Set lowestValueVertices = lowestValues(verticesConnectionCount).keySet();
        if(lowestValueVertices.contains(currentVertex)) {
            lowestValueVertices.remove(currentVertex);
        }
        List<Integer> lowestValueList = new ArrayList<>();
        lowestValueList.addAll(lowestValueVertices);
        Integer chosenPosition = random.nextInt(lowestValueList.size());
        chosenPosition = lowestValueList.get(chosenPosition);
        while(lowestValueList.size() > 0) {
            if(pairPresent(pairs,currentVertex,chosenPosition) || chosenPosition == currentVertex) {
                lowestValueList.remove(chosenPosition);
                chosenPosition = random.nextInt(lowestValueList.size());
                chosenPosition = lowestValueList.get(chosenPosition);
            }
            else {
                return chosenPosition;
            }
        }
        return -1;

    }

    public static void modifyConnectionCount(Map<Integer,Integer> connectionCount, Integer vertex, Integer value) {
        connectionCount.put(vertex,connectionCount.get(vertex)+value);
    }

    public static boolean pairPresent(List<Map.Entry<Integer,Integer>> pairs, Integer a, Integer b) {
        for(Map.Entry<Integer, Integer> pair : pairs) {
            if(pair.getKey() == a || pair.getKey() == b) {
                if(pair.getValue() == a || pair.getValue() == b) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Map.Entry<Integer, Integer>> generateGraph(int k, int n) {
        List<Map.Entry<Integer, Integer>> result = new ArrayList<>();
        Map<Integer, Integer> verticesConnectionCount = new HashMap<>();
        for(int i = 0;i<n;++i) {
            verticesConnectionCount.put(i,0);
        }
        for(int i = 0;i<n;++i) {
            Integer lowestVertexPosition = chooseRandomLowestVertexPositionWithoutTaken(verticesConnectionCount, null, result);
            if(verticesConnectionCount.get(lowestVertexPosition) >= k) {
                continue;
            }

            Integer anotherLowestVertex = chooseRandomLowestVertexPositionWithoutTaken(verticesConnectionCount, lowestVertexPosition, result);
            if(verticesConnectionCount.get(anotherLowestVertex) >= k) {
                break;
            }
            while(verticesConnectionCount.get(lowestVertexPosition) < k && verticesConnectionCount.get(anotherLowestVertex) < k) {
                modifyConnectionCount(verticesConnectionCount,lowestVertexPosition,1);
                modifyConnectionCount(verticesConnectionCount,anotherLowestVertex,1);
                result.add(Map.entry(lowestVertexPosition, anotherLowestVertex));
                anotherLowestVertex = chooseRandomLowestVertexPositionWithoutTaken(verticesConnectionCount, lowestVertexPosition, result);
            }
            verticesConnectionCount.remove(lowestVertexPosition);

        }
        return result;
    }

    public void printGraph() {
        List<Map.Entry<Integer, Integer>> result = generateGraph(3,30);
        for(Map.Entry<Integer, Integer> pair : result) {
            System.out.println(String.format("(%d,%d),",pair.getKey(),pair.getValue()));
        }
        /*for (Long userId : adjUsers.keySet()) {
            System.out.print(userId + ": ");

            for (Long neighId : adjUsers.get(userId)) {
                System.out.print(neighId + " ");
            }
            System.out.println();
        }*/
    }
}
