import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private static final int THRESHOLD = 1000;

    private static final Comparator<SearchNode> BY_MANHATTAN = new ByManhattan();

    private static class ByManhattan implements Comparator<SearchNode> {
        @Override public int compare(SearchNode a, SearchNode b) {
            return a.compareTo(b);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {

        private final SearchNode previous;

        private final Board value;

        private final int steps;

        public SearchNode(SearchNode previous, Board value, int steps) {
            this.previous = previous;
            this.value = value;
            this.steps = steps;
        }

        public Board getValue() {
            return value;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int getSteps() {
            return steps;
        }

        @Override public int compareTo(SearchNode that) {
            int thisManhattan = this.value.manhattan() + this.steps;
            int thatManhattan = that.value.manhattan() + that.steps;
            return thisManhattan - thatManhattan;
        }
    }

    private final SearchNode initial;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        this.initial = new SearchNode(null, initial, 0);
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        Solver twin = new Solver(initial.getValue().twin());
        return ((moves() > 0) && (twin.moves() == -1));

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solution() == null) {
            return -1;
        }
        int moveNum = -1;
        for (Board board : solution()) {
            moveNum++;
        }
        return moveNum;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Queue<Board> solution = new Queue<Board>();
        MinPQ<SearchNode> allNodes = new MinPQ<SearchNode>(BY_MANHATTAN); // store all search nodes
        allNodes.insert(initial);
        int counter = 0;
        while(!allNodes.min().getValue().isGoal()) {
            counter++;
            if (counter > THRESHOLD) {
                return null;
            }
            SearchNode currentNode = allNodes.delMin();
            solution.enqueue(currentNode.getValue());
            Iterable<Board> neighbors = currentNode.getValue().neighbors();
            for (Board neighbor : neighbors) {
                if (currentNode.getPrevious() == null || !neighbor.equals(currentNode.getPrevious().getValue())) {
                    SearchNode next = new SearchNode(currentNode, neighbor, currentNode.getSteps() + 1);
                    allNodes.insert(next);
                }
            }
        }
        SearchNode goal = allNodes.delMin();
        solution.enqueue(goal.getValue());
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}