import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
   private Node first;
   private Node last;
   private int size;

   private class Node {
      private Item value;
      private Node former;
      private Node next;

      private Node(Item value, Node former, Node next) {
         this.value = value;
         this.former = former;
         this.next = next;
      }

      public Item getValue() {
         return value;
      }
   }

   // construct an empty deque
   public Deque() {
      first = null;
   }

   // is the deque empty?
   public boolean isEmpty() {
      return first == null;
   }

   // return the number of items on the deque
   public int size() {
      return size;
   }

   // add the item to the front        
   public void addFirst(Item item) throws NullPointerException {
      if (item == null) {
         throw new NullPointerException();
      }
      if (isEmpty()) {
         first = new Node(item, null, null);
         last = first;
      }
      else {
         Node oldfirst = first;
         first = new Node(item, null, oldfirst);
         oldfirst.former = first;
      }
      size++;
   }

   // add the item to the end        
   public void addLast(Item item) throws NullPointerException {
      if (item == null) {
         throw new NullPointerException();
      }
      if (isEmpty()) {
         last = new Node(item, null, null);
         first = last;
      }
      else {
         Node oldlast = last;
         last = new Node(item, oldlast, null);
         oldlast.next = last;
      }
      size++;
   }

   // remove and return the item from the front      
   public Item removeFirst() throws NoSuchElementException {
      if (isEmpty()) {
         throw new NoSuchElementException("Deque is empty.");
      }
      if (size == 1) {
         Item result = first.value;
         first = null;
         last = null;
         size--;
         return result;
      }
      Item result = first.value;
      first = first.next;
      first.former = null;
      size--;
      return result;
   }

   // remove and return the item from the end             
   public Item removeLast() throws NoSuchElementException {
      if (isEmpty()) {
         throw new NoSuchElementException("Deque is empty.");
      }
      if (size == 1) {
         Item result = last.value;
         last = null;
         first = null;
         size--;
         return result;
      }
      Item result = last.value;
      last = last.former;
      last.next = null;
      size--;
      return result;
   }

   // return an iterator over items in order from front to end                
   public Iterator<Item> iterator() {
      return new ListIterator();
   }

   private class ListIterator implements Iterator<Item> {
      private Node current = first;

      public boolean hasNext() {
         return current != null;
      }

      public void remove() throws UnsupportedOperationException {
         throw new UnsupportedOperationException();
      }

      public Item next() throws NoSuchElementException {
         if (!hasNext()) {
            throw new NoSuchElementException("Deque is empty.");
         }
         Item item = current.value;
         current = current.next;
         return item;
      }
   }
   
   // unit testing        
   public static void main(String[] args) {
      Deque<Integer> deque = new Deque<Integer>();
      deque.addFirst(1);
      deque.addLast(2);
      deque.addFirst(3);
      for (Integer node : deque) {
         StdOut.printf("%d ", node);
      }
      StdOut.println();
      deque.removeLast();
      for (Integer node : deque) {
         for (Integer item : deque) {
         StdOut.printf("%d ", item);
         }
      }
      StdOut.println();
      deque.removeFirst();
      deque.removeFirst();
      StdOut.println();
      try {
         deque.removeLast();
      }
      catch(Exception e) {
         StdOut.println(e.getMessage());
      }
   }  
}
