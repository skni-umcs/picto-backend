package pl.umcs.workshop.utils;


import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.user.User;

import java.time.LocalDateTime;
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

    public static Map<Long, Long> lowestValues(Map<Long, Long> values) {
        Map<Long, Long> result = new HashMap<>();
        Long lowest = Long.MAX_VALUE;

        for (Long key : values.keySet()) {
            Long value = values.get(key);

            if (Objects.equals(value, lowest)) {
                result.put(key, value);
            } else if (value < lowest) {
                lowest = value;
                result.clear();
                result.put(key, value);
            }
        }

        return result;
    }

    public static Long chooseRandomLowestVertexPositionWithoutTaken(Map<Long, Long> verticesConnectionCount, Long currentVertex, List<Map.Entry<Long,Long>> pairs) {
        Random random = new Random();
        modifyConnectionCount(verticesConnectionCount, currentVertex, (long) verticesConnectionCount.size()); //is temporary increased so it cannot be the lowest one
        Set<Long> lowestValueVertices = lowestValues(verticesConnectionCount).keySet();
        modifyConnectionCount(verticesConnectionCount, currentVertex, (long) -verticesConnectionCount.size());

        if(lowestValueVertices.isEmpty()) {
            return -1L; //it means there are no more vertices to connect to
        }

        List<Long> lowestValueList = new ArrayList<>(lowestValueVertices);
        Long chosenPosition = random.nextLong(lowestValueList.size());
        chosenPosition = lowestValueList.get(Math.toIntExact(chosenPosition));

        while (!lowestValueList.isEmpty()) {
            if (pairPresent(pairs, currentVertex, chosenPosition) || Objects.equals(chosenPosition, currentVertex)) {
                lowestValueList.remove(chosenPosition);
                chosenPosition = random.nextLong(lowestValueList.size());
                chosenPosition = lowestValueList.get(Math.toIntExact(chosenPosition));
            } else {
                return chosenPosition;
            }
        }

        return -1L;
    }

    public static void modifyConnectionCount(Map<Long,Long> connectionCount, Long vertex, Long value) {
        if(connectionCount.containsKey(vertex)) {
            connectionCount.put(vertex, connectionCount.get(vertex) + value);
        }
    }

    public static boolean pairPresent(List<Map.Entry<Long,Long>> pairs, Long a, Long b) {
        for (Map.Entry<Long, Long> pair : pairs) {
            if (Objects.equals(pair.getKey(), a) || Objects.equals(pair.getKey(), b)) {
                if (Objects.equals(pair.getValue(), a) || Objects.equals(pair.getValue(), b)) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: swap User[] to List<User>
    public static List<Map.Entry<Long, Long>> generateGraph(User[] users, long k) {
        List<Map.Entry<Long, Long>> result = new ArrayList<>();
        Map<Long, Long> verticesConnectionCount = new HashMap<>();

        for (User user : users) {
            verticesConnectionCount.put(user.getId(), 0L);
        }

        for (User user : users) {
            Long lowestVertexPosition = chooseRandomLowestVertexPositionWithoutTaken(verticesConnectionCount, null, result);
            if (verticesConnectionCount.get(lowestVertexPosition) >= k) {
                continue;
            }

            Long anotherLowestVertex = chooseRandomLowestVertexPositionWithoutTaken(verticesConnectionCount, lowestVertexPosition, result);
            if (verticesConnectionCount.get(anotherLowestVertex) >= k) {
                continue;
            }

            while (verticesConnectionCount.get(lowestVertexPosition) < k && verticesConnectionCount.get(anotherLowestVertex) < k) {
                //create new pair
                modifyConnectionCount(verticesConnectionCount,lowestVertexPosition,1L);
                modifyConnectionCount(verticesConnectionCount,anotherLowestVertex,1L);
                result.add(Map.entry(lowestVertexPosition, anotherLowestVertex));
                
                anotherLowestVertex = chooseRandomLowestVertexPositionWithoutTaken(verticesConnectionCount, lowestVertexPosition, result);
            }
            verticesConnectionCount.remove(lowestVertexPosition);

        }
        return result;
    }

    public static void printGraph() {
//        List<Map.Entry<Long, Long>> result = generateGraph(new User[] {
//                User.builder()
//                        .id(1L)
//                        .game(Game.builder()
//                            .id(1L)
//                            .userOneNumberOfImages(4)
//                            .userTwoNumberOfImages(4)
//                            .userOneTime(5)
//                            .userTwoTime(3)
//                            .symbolGroupsAmount(3)
//                            .symbolsInGroupAmount(4)
//                            .correctAnswerPoints(1)
//                            .wrongAnswerPoints(-1)
//                            .topology(Topology.builder().build())
//                            .createDateTime(LocalDateTime.now())
//                            .build())
//                        .score(11)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
//                        .cookie(JWTCookieHandler.createToken(1L, 1L))
//                        .build(),
//                User.builder()
//                        .id(2L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(13)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
//                        .cookie(JWTCookieHandler.createToken(2L, 1L))
//                        .build(),
//                User.builder()
//                        .id(3L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(7)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 21))
//                        .cookie(JWTCookieHandler.createToken(3L, 1L))
//                        .build(),
//                User.builder()
//                        .id(4L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(11)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
//                        .cookie(JWTCookieHandler.createToken(1L, 1L))
//                        .build(),
//                User.builder()
//                        .id(5L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(13)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
//                        .cookie(JWTCookieHandler.createToken(2L, 1L))
//                        .build(),
//                User.builder()
//                        .id(6L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(7)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 21))
//                        .cookie(JWTCookieHandler.createToken(3L, 1L))
//                        .build(),
//                User.builder()
//                        .id(7L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(11)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 16, 53))
//                        .cookie(JWTCookieHandler.createToken(1L, 1L))
//                        .build(),
//                User.builder()
//                        .id(8L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(13)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
//                        .cookie(JWTCookieHandler.createToken(2L, 1L))
//                        .build(),
//                User.builder()
//                        .id(9L)
//                        .game(Game.builder()
//                                .id(1L)
//                                .userOneNumberOfImages(4)
//                                .userTwoNumberOfImages(4)
//                                .userOneTime(5)
//                                .userTwoTime(3)
//                                .symbolGroupsAmount(3)
//                                .symbolsInGroupAmount(4)
//                                .correctAnswerPoints(1)
//                                .wrongAnswerPoints(-1)
//                                .topology(Topology.builder().build())
//                                .createDateTime(LocalDateTime.now())
//                                .build())
//                        .score(13)
//                        .generation(1)
//                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
//                        .cookie(JWTCookieHandler.createToken(2L, 1L))
//                        .build()
//        }, 4L);

        int n = 37;
        User[] userArray = new User[n];
        for(int i = 0;i<n;i++) {
            userArray[i] = User.builder()
                        .id((long) i)
                        .game(Game.builder()
                                .id(1L)
                                .userOneNumberOfImages(4)
                                .userTwoNumberOfImages(4)
                                .userOneTime(5)
                                .userTwoTime(3)
                                .symbolGroupsAmount(3)
                                .symbolsInGroupAmount(4)
                                .correctAnswerPoints(1)
                                .wrongAnswerPoints(-1)
                                .topology(Topology.builder().build())
                                .createDateTime(LocalDateTime.now())
                                .build())
                        .score(13)
                        .generation(1)
                        .lastSeen(LocalDateTime.of(2023, 4, 13, 17, 6))
                        .cookie(JWTCookieHandler.createToken(2L, 1L))
                        .build();
        }
        List<Map.Entry<Long, Long>> result = generateGraph(userArray,21L);

        for (Map.Entry<Long, Long> pair : result) {
            System.out.printf("(%d, %d), %n", pair.getKey(), pair.getValue());
        }
    }
}
