package pl.umcs.workshop.utils;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import pl.umcs.workshop.user.User;

@Getter
public class CircularDoublyLinkedList {
  private Node start;

  public static @NotNull CircularDoublyLinkedList listToCircular(@NotNull List<User> users) {
    CircularDoublyLinkedList result = new CircularDoublyLinkedList();

    for (User user : users) {
      result.insertNodeEnd(user);
    }

    return result;
  }

  public void insertNodeBegin(User value) {
    Node newNode = new Node(value);

    if (start == null) {
      start = newNode;
      newNode.next = newNode;
      newNode.previous = newNode;

      return;
    }

    this.start = insertNodeAfter(value, start.previous);
  }

  public Node insertNodeAfter(User value, Node nodeAfter) {
    Node newNode = new Node(value);

    if (start == null) {
      start = newNode;
      newNode.next = newNode;
      newNode.previous = newNode;

      return newNode;
    }

    newNode.previous = nodeAfter;
    newNode.next = nodeAfter.next;

    nodeAfter.next.previous = newNode;
    nodeAfter.next = newNode;

    return newNode;
  }

  public void insertNodeEnd(User value) {
    Node newNode = new Node(value);

    if (start == null) {
      start = newNode;
      newNode.next = newNode;
      newNode.previous = newNode;

      return;
    }

    insertNodeAfter(value, start.previous);
  }

  public Node getElementAtIndex(int index) {
    if (start == null) {
      throw new IndexOutOfBoundsException("The list is empty.");
    }

    Node currentNode = start;
    int currentIndex = 0;

    do {
      if (currentIndex == index) {
        return currentNode;
      }

      currentNode = currentNode.getNext();
      currentIndex++;
    } while (currentNode != start);

    throw new IndexOutOfBoundsException("Index out of bounds: " + index);
  }

  public int size() {
    if (start == null) {
      return 0;
    }

    int count = 0;
    Node currentNode = start;
    do {
      count++;
      currentNode = currentNode.getNext();
    } while (currentNode != start);

    return count;
  }

  public void printList() {
    if (start == null) {
      System.out.println("The list is empty.");
    } else {
      Node currentNode = start;

      do {
        System.out.print(currentNode.getValue() + " ");
        currentNode = currentNode.getNext();
      } while (currentNode != start);

      System.out.println();
    }
  }

  @Setter
  @Getter
  @RequiredArgsConstructor
  public static class Node {
    private User value;
    private Node next;
    private Node previous;

    public Node(User value) {
      this.value = value;
    }
  }
}
