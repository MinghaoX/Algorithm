import java.util.*;

public class FastCollinearPoints {
    
    private final Point[] points;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
    	for (int m = 0; m < points.length; m++) {
            Point point = points[m];
            Point[] pointsAux = new Point[points.length - 1];
            for (int n = 0; n < m; n++) {
                pointsAux[n] = points[n];
            }
            for (int n = m; n < pointsAux.length; n++) {
                pointsAux[n] = points[n + 1];
            }
    		Arrays.sort(pointsAux, point.slopeOrder());
    		double currentSlope = point.slopeTo(pointsAux[0]);
    		int sameNum = 1;
    		for (int i = 1; i < pointsAux.length; i++) {
    			if (point.slopeTo(pointsAux[i]) == currentSlope) {
    				sameNum++;
    			}
    			if (((point.slopeTo(pointsAux[i]) != currentSlope) || (i == pointsAux.length - 1)) && sameNum > 2) {
    				Point[] subPoints = new Point[sameNum + 1];
    				subPoints[0] = point;
    				for (int j = 1; j < sameNum + 1; j++) {
    					subPoints[j] = pointsAux[i - 1 - sameNum + j];
    				}
    				Arrays.sort(subPoints);
    				LineSegment segment = new LineSegment(subPoints[0], subPoints[sameNum]);
    				if (! linkedSegment.contains(segment)) {
    					linkedSegment.add(segment);
    				}
    				break;
    			}
    			sameNum = 1;
    			currentSlope = point.slopeTo(pointsAux[i]);
    		}
    	}
    	LineSegment[] arraySegment = new LineSegment[linkedSegment.size()];
        return linkedSegment.toArray(arraySegment);
    }

    public static void main(String[] args) {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 1);
        Point p3 = new Point(2, 2);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(-1, 1);
        Point p6 = new Point(-2, 2);
        Point p7 = new Point(-3, 3);
        Point[] group = new Point[] {p1, p2, p3, p4, p5, p6, p7};
        BruteCollinearPoints test = new BruteCollinearPoints(group);
        System.out.println(test.numberOfSegments());
    }               
}
