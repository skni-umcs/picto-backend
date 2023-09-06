package pl.umcs.workshop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.utils.CircularDoublyLinkedList;

public class CircularDoublyLinkedListTests {
  private CircularDoublyLinkedList list;

  @BeforeEach
  public void setUp() {
    list = new CircularDoublyLinkedList();
  }

  @Test
  public void testInsertNodeBegin() {
    User user = new User();
    user.setId(1L);
    list.insertNodeBegin(user);

    assertEquals(user, list.getStart().getValue());
    assertEquals(list.getStart(), list.getStart().getNext());
    assertEquals(list.getStart(), list.getStart().getPrevious());
  }

  @Test
  public void testInsertNodeAfter() {
    User user = new User();
    user.setId(1L);
    User user2 = new User();
    user.setId(2L);
    list.insertNodeBegin(user);
    list.insertNodeAfter(user2, list.getStart());

    assertEquals(user2, list.getStart().getNext().getValue());
    assertEquals(list.getStart(), list.getStart().getNext().getPrevious());
    assertEquals(list.getStart().getNext(), list.getStart().getPrevious());
  }

  @Test
  public void testInsertNodeEnd() {
    User user = new User();
    user.setId(1L);
    list.insertNodeEnd(user);

    assertEquals(user, list.getStart().getValue());
    assertEquals(list.getStart(), list.getStart().getNext());
    assertEquals(list.getStart(), list.getStart().getPrevious());
  }

  @Test
  public void testPrintList() {
    User user = new User();
    user.setId(1L);
    User user2 = new User();
    user.setId(2L);
    User user3 = new User();
    user.setId(3L);
    User user4 = new User();
    user.setId(4L);
    list.insertNodeBegin(user);
    list.insertNodeEnd(user3);
    list.insertNodeEnd(user2);
    list.printList();

    list.insertNodeBegin(user4);
    list.printList();
  }

  @Test
  public void testGetElementAtIndex() {
    User user = new User();
    user.setId(1L);
    User user2 = new User();
    user.setId(2L);
    User user3 = new User();
    user.setId(3L);
    list.insertNodeBegin(user);
    list.insertNodeEnd(user2);
    list.insertNodeEnd(user3);

    assertEquals(user, list.getElementAtIndex(0).getValue());
    assertEquals(user2, list.getElementAtIndex(1).getValue());
    assertEquals(user3, list.getElementAtIndex(2).getValue());
  }

  @Test
  public void testGetElementAtIndexEmptyList() {
    assertThrows(IndexOutOfBoundsException.class, () -> list.getElementAtIndex(0));
  }

  @Test
  public void testGetElementAtIndexOutOfBounds() {
    User user = new User();
    user.setId(1L);
    User user2 = new User();
    user.setId(2L);
    list.insertNodeBegin(user);
    list.insertNodeEnd(user2);

    assertThrows(IndexOutOfBoundsException.class, () -> list.getElementAtIndex(2));
  }

  @Test
  public void testSizeEmptyList() {
    assertEquals(0, list.size());
  }

  @Test
  public void testSizeNonEmptyList() {
    User user = new User();
    user.setId(1L);
    User user2 = new User();
    user.setId(2L);
    User user3 = new User();
    user.setId(3L);
    list.insertNodeBegin(user);
    list.insertNodeEnd(user2);
    list.insertNodeEnd(user3);

    assertEquals(3, list.size());
  }

  @Test
  public void testSizeSingleNodeList() {
    User user = new User();
    user.setId(1L);
    list.insertNodeBegin(user);

    assertEquals(1, list.size());
  }
}
