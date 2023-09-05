package pl.umcs.workshop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.umcs.workshop.utils.CircularDoublyLinkedList;

public class CircularDoublyLinkedListTests {
  private CircularDoublyLinkedList list;

  @BeforeEach
  public void setUp() {
    list = new CircularDoublyLinkedList();
  }

  @Test
  public void testInsertNodeBegin() {
    list.insertNodeBegin(1L);

    assertEquals(1L, list.getStart().getValue());
    assertEquals(list.getStart(), list.getStart().getNext());
    assertEquals(list.getStart(), list.getStart().getPrevious());
  }

  @Test
  public void testInsertNodeAfter() {
    list.insertNodeBegin(1L);
    list.insertNodeAfter(2L, list.getStart());

    assertEquals(2L, list.getStart().getNext().getValue());
    assertEquals(list.getStart(), list.getStart().getNext().getPrevious());
    assertEquals(list.getStart().getNext(), list.getStart().getPrevious());
  }

  @Test
  public void testInsertNodeEnd() {
    list.insertNodeEnd(1L);

    assertEquals(1L, list.getStart().getValue());
    assertEquals(list.getStart(), list.getStart().getNext());
    assertEquals(list.getStart(), list.getStart().getPrevious());
  }

  @Test
  public void testPrintList() {
    list.insertNodeBegin(1L);
    list.insertNodeEnd(3L);
    list.insertNodeEnd(2L);
    list.printList();

    list.insertNodeBegin(4L);
    list.printList();
  }

  @Test
  public void testGetElementAtIndex() {
    list.insertNodeBegin(1L);
    list.insertNodeEnd(2L);
    list.insertNodeEnd(3L);

    assertEquals(1L, list.getElementAtIndex(0));
    assertEquals(2L, list.getElementAtIndex(1));
    assertEquals(3L, list.getElementAtIndex(2));
  }

  @Test
  public void testGetElementAtIndexEmptyList() {
    assertThrows(IndexOutOfBoundsException.class, () -> list.getElementAtIndex(0));
  }

  @Test
  public void testGetElementAtIndexOutOfBounds() {
    list.insertNodeBegin(1L);
    list.insertNodeEnd(2L);

    assertThrows(IndexOutOfBoundsException.class, () -> list.getElementAtIndex(2));
  }

  @Test
  public void testSizeEmptyList() {
    assertEquals(0, list.size());
  }

  @Test
  public void testSizeNonEmptyList() {
    list.insertNodeBegin(1L);
    list.insertNodeEnd(2L);
    list.insertNodeEnd(3L);

    assertEquals(3, list.size());
  }

  @Test
  public void testSizeSingleNodeList() {
    list.insertNodeBegin(1L);

    assertEquals(1, list.size());
  }
}
