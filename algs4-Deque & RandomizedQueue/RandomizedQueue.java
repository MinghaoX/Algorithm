import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] items;
	private int size;

	// construct an empty randomized queue
   	public RandomizedQueue() {
   		items = (Item[]) new Object[2];
   		size = 0;
   	}                 
   	
   	// is the queue empty?
   	public boolean isEmpty() {
         return size == 0;
      }

   	// return the number of items on the queue               
   	public int size() {
         return size;
      }

      private void resize(int capacity) {
         assert capacity >= size;
         Item[] temp = (Item[]) new Object[capacity];
         for (int i = 0; i < size; i++) {
            temp[i] = items[i];
         }
         items = temp;
      }

   	// add the item                        
   	public void enqueue(Item item) throws NullPointerException {
         if (item == null) {
            throw new NullPointerException("Item cannot be null.");
         }
         if (size == items.length) {
            resize(size * 2);
         }
         items[size] = item;
         size++;
      }

      private void rearrange(int index) {
         for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
         }
         items[size - 1] = null;
      }

   	// remove and return a random item           
   	public Item dequeue() throws NoSuchElementException {
         if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
         }
         int i = StdRandom.uniform(size);
         Item result = items[i];
         rearrange(i);
         size--;
         if (size > 0 && size == items.length/4) {
            resize(items.length/2);
         }
         return result;
      }

   	// return (but do not remove) a random item                   
   	public Item sample() throws NoSuchElementException {
         if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
         }
         int i = StdRandom.uniform(size);
         return items[i];
      }

   	// return an independent iterator over items in random order                     
   	public Iterator<Item> iterator() {
         return new ReverseArrayIterator();
      }

      private class ReverseArrayIterator implements Iterator<Item> {
         private int left;
         private int[] shuffleIndex;

         public ReverseArrayIterator() {
            left = size;
            shuffleIndex = new int[size];
            for (int i = 0; i < size; i++) {
               shuffleIndex[i] = i;
            }
            StdRandom.shuffle(shuffleIndex);
         }

         public boolean hasNext() {
            return left > 0;
         }

         public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
         }

         public Item next() throws NoSuchElementException {
            if (!hasNext()) {
               throw new NoSuchElementException("No more item left.");
            }
            Item item = items[shuffleIndex[left - 1]];
            left--;
            return item;
         }
      }

   	// unit testing        
   	public static void main(String[] args) {
         RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
         for (int i = 0; i < 6; i++) {
            rq.enqueue(i);
         }
         StdOut.println(rq.sample());
         StdOut.println(rq.dequeue());
         for (Integer node : rq) {
            for (Integer node1 : rq) {
               StdOut.printf("%d ", node1);
            }
         StdOut.println();
         }     
         rq.dequeue();
         rq.dequeue();
         rq.dequeue();
         rq.dequeue();
         rq.dequeue();
         try {
            rq.dequeue();
         }
         catch(Exception e) {
            StdOut.println(e.getMessage());
         } 
      }  
}