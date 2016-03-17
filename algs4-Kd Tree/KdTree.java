import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {

    private static final int VERTICAL = 0;

    private static final int HORIZONTAL = 1;

    private static class Node {

        private Point2D p;      // the point

        private RectHV rect;    // the axis-aligned rectangle corresponding to this node

        private int orient;

        private Node lb;        // the left/bottom subtree

        private Node rt;        // the right/top subtree

        private Node(Point2D p, RectHV rect, int orient) {
            this.p = p;
            this.rect = rect;
            this.orient = orient;
            this.lb = null;
            this.rt = null;
        }

    }

    private int size;

    private Node root;

    // construct an empty set of points
    public KdTree() {
        size = 0;
        root = null;
    }

    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }
    
    //orient is orientation of currentNode
    private static Node putPoint(Node currentNode, Node parentNode, Point2D p, int orient) {
        if (currentNode == null) {
            double side = compareWith(p, parentNode.p, parentNode.orient);
            RectHV currentRect = split(parentNode.rect, parentNode.p, parentNode.orient, side);
            return new Node(p, currentRect, orient);
        }
        double cmp = compareWith(p, currentNode.p, orient);
        if (cmp < 0) {
            currentNode.lb = putPoint(currentNode.lb, currentNode, p, 1 - orient);
        }
        else if (cmp >= 0) {
            currentNode.rt = putPoint(currentNode.rt, currentNode, p, 1 - orient);
        }
        return currentNode;
    }

    private static RectHV split(RectHV rectangle, Point2D p, int orient, double side) {
        RectHV result = rectangle;
        if (side < 0) {
            if (orient == VERTICAL) {
                result = new RectHV(rectangle.xmin(), rectangle.ymin(), p.x(), rectangle.ymax());
            }
            else {
                result = new RectHV(rectangle.xmin(), rectangle.ymin(), rectangle.xmax(), p.y());
            }
        }
        else {
            if (orient == VERTICAL) {
                result = new RectHV(p.x(), rectangle.ymin(), rectangle.xmax(), rectangle.ymax());
            }
            else {
                result = new RectHV(rectangle.xmin(), p.y(), rectangle.xmax(), rectangle.ymax());
            }
        }
        return result;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            root = new Node(p, new RectHV(0d, 0d, 1d, 1d), VERTICAL);
            size++;
            return;
        }
        if (!contains(p)) {
            root = putPoint(root, null, p, VERTICAL);
            size++;
        }
    }

    private static Point2D getPoint(Node x, Point2D p, int orient) {
        if (x == null) {
            return null;
        }
        if (x.p.equals(p)) {
            return x.p;
        }
        double cmp = compareWith(p, x.p, orient);
        if (cmp < 0) {
            return getPoint(x.lb, p, 1 - orient);
        }
        else {
            return getPoint(x.rt, p, 1 - orient);
        }

    }

    private static double compareWith(Point2D p, Point2D q, int orient) {
        if (orient == VERTICAL) {
            return p.x() - q.x();
        }
        else {
            return p.y() - q.y();
        }
    }

    // does the set contain point p?          
    public boolean contains(Point2D p) {
        return !(getPoint(root, p, VERTICAL) == null);
    }

    private static void drawPoint(Node x) {
        if (x == null) {
            return;
        }
        x.p.draw();
        drawPoint(x.lb);
        drawPoint(x.rt);
    }

    private static void drawSplit(Node x) {
        if (x == null) {
            return;
        }
        if (x.orient == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(.001);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else if (x.orient == HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(.001);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        drawSplit(x.lb);
        drawSplit(x.rt);
    }

    // draw all points to standard draw      
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        drawPoint(root);
        drawSplit(root);
    }

    private static void addPoint(Node x, RectHV rect, LinkedList<Point2D> insidePoints) {
        if (x == null) {
            return;
        }
        if (!x.rect.intersects(rect)) {
            return;
        }
        if (rect.contains(x.p)) {
            insidePoints.add(x.p);
        }
        addPoint(x.lb, rect, insidePoints);
        addPoint(x.rt, rect, insidePoints);
    }

    // all points that are inside the rectangle                     
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        LinkedList<Point2D> inRect = new LinkedList<Point2D>();
        addPoint(root, rect, inRect);
        return inRect;
    }

    private static Point2D searchNearest(Node x, Point2D p, Point2D currentNearest) {
        double currentDistance = p.distanceTo(currentNearest);
        if (x == null) {
            return currentNearest;
        }
        if (!x.rect.contains(p) && x.rect.distanceTo(p) > currentDistance) {
            return currentNearest;
        }
        double thisDistance = p.distanceTo(x.p);
        if (thisDistance < currentDistance) {
            currentNearest = x.p;
        }
        currentNearest = searchNearest(x.lb, p, currentNearest);
        currentNearest = searchNearest(x.rt, p, currentNearest);
        return currentNearest;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D nearestPoint = new Point2D(root.p.x(), root.p.y());
        nearestPoint = searchNearest(root, p, nearestPoint);
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}
