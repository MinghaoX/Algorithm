import java.util.*;

public class BruteCollinearPoints {

    private final Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
    	if (points == null) {
    		throw new NullPointerException();
    	}
    	for (Point point1 : points) {
    		if (point1 == null) {
    			throw new NullPointerException();
    		}
    		for (Point point2 : points) {
    			if (point2 != point1 && point2.compareTo(point1) == 0) {
    				throw new IllegalArgumentException();
    			}
    		}
    	}
        this.points = points;
    }

    // the number of line segments    
    public int numberOfSegments() {
        return segments().length;
    }

    // the line segments        
    public LineSegment[] segments() {
        LinkedList<LineSegment> linkedSegment = new LinkedList<LineSegment>();
        for (int i = 0; i < points.length - 3; i++) {
        	Point p = points[i];
         	for (int j = i + 1; j < points.length - 2; j++) {
         		Point q = points[j];
         		for (int m = j + 1; m < points.length - 1; m++) {
         			Point r = points[m];
         			for (int n = m + 1; n < points.length; n++) {
         				Point s = points[n];
         				Point[] group = {p, q, r, s};
         				Arrays.sort(group);
         				if (p.slopeTo(q) == q.slopeTo(r) && q.slopeTo(r) == r.slopeTo(s)) {
         					LineSegment segment = new LineSegment(group[0], group[3]);
                            linkedSegment.add(segment);
         				}
         			}
         		}
         	}
        }
        LineSegment[] arraySegment = new LineSegment[linkedSegment.size()];
        return linkedSegment.toArray(arraySegment);
    }

    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 0);
        Point p3 = new Point(0, 1);
        Point p4 = new Point(2, 2);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(4, 4);
        Point[] group = new Point[] {p1, p2, p3, p4, p5, p6};
        BruteCollinearPoints test = new BruteCollinearPoints(group);
        System.out.println(test.numberOfSegments());
    }

}