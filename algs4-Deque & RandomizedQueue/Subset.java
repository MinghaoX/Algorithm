import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
	public static void main(String[] args) {
		int i = Integer.parseInt(args[0]);
		RandomizedQueue<String> s = new RandomizedQueue<String>();
		int index = 0;
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
            if (s.size() < i) {
            	s.enqueue(item);
            }
            else {
            	int randomNum = StdRandom.uniform(index + 1);
            	if (randomNum < i) {
            		s.dequeue();
            		s.enqueue(item);
            	}
            }
            index++;
        }
        for (String temp : s) {
        	StdOut.println(temp);
        }
	}
}