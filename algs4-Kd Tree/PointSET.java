import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class PointSET {

    private int size;

    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<Point2D>();
        size = 0;
    }

    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        set.add(p);
    }

    // does the set contain point p?          
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all points to standard draw      
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle                     
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        LinkedList<Point2D> inRect = new LinkedList<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                inRect.add(p);
            }
        }
        return inRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        Point2D nearestPoint = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Point2D point : set) {
        	double currentDistance = p.distanceTo(point);
            if (currentDistance < nearestDistance) {
                nearestPoint = point;
                nearestDistance = currentDistance;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args){

    }

}
