import java.util.*;
import edu.princeton.cs.algs4.Queue;

public class Board {

    private final int[][] blocks;

    private final int[][] goal;

    private final int dimension;

    // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks.length < 2) {
            throw new IllegalArgumentException("dimension of blocks should be at least 2");
        }
        this.dimension = blocks.length;
        this.blocks = new int[dimension][dimension];
        this.goal = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.blocks[i][j] = blocks[i][j];
                this.goal[i][j] = i * dimension + j + 1;
            }
        }
        this.goal[dimension - 1][dimension - 1] = 0;
    }
    
    // board dimension N
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int actual = blocks[i][j];
                int expected = goal[i][j];
                if ((actual != expected) && (expected != 0)) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int actual = blocks[i][j];
                int expected = goal[i][j];
                if ((actual != expected) && (expected != 0)) {
                    int targetX = actual / dimension;
                    int targetY = actual % dimension - 1;
                    manhattan = manhattan + Math.abs(targetX - i) + Math.abs(targetX - j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;

    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int x = 0;
        Index index0 = indexOfZero();
        if (index0.x == 0) {
            x = 1;
        }
        Index index1 = new Index(x, 0);
        Index index2 = new Index(x, 1);
        int[][] twin = swap(index1, index2);
        return new Board(twin);
    }

    private Index indexOfZero() {
        Index index0 = new Index(0, 0);
        outerloop:
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    index0 = new Index(i, j);
                    break outerloop;
                }
            }
        }
        return index0;
    }

    private boolean isValid(Index index) {
        if ((index.x < 0) || (index.x > dimension - 1) || (index.y < 0) || (index.y > dimension - 1)) {
            return false;
        }
        return true;
    }

    private int[][] swap(Index index1, Index index2) {
        int n = dimension;
        int[][] newArray = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newArray[i][j] = blocks[i][j];
            }
        }
        int temp = newArray[index1.x][index1.y];
        newArray[index1.x][index1.y] = newArray[index2.x][index2.y];
        newArray[index2.x][index2.y] = temp;
        return newArray;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if ((y == null) || (this.getClass() != y.getClass())) {
            return false;
        }
        Board boardY = (Board) y;
        return this.toString().equals(boardY.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<Board>();
        Index index0 = indexOfZero();
        Index index1 = new Index(index0.x - 1, index0.y);
        Index index2 = new Index(index0.x + 1, index0.y);
        Index index3 = new Index(index0.x, index0.y - 1);
        Index index4 = new Index(index0.x, index0.y + 1);
        Index[] indexs = {index1, index2, index3, index4};
        for (Index index : indexs) {
            if (isValid(index)) {
                int[][] neighborArray = swap(index, index0);
                Board neighborBoard = new Board(neighborArray);
                neighbors.enqueue(neighborBoard);
            }
        }
        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                 s.append(String.format("%2d ", blocks[i][j]));
            }
        s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] at = {{1, 0}, {3, 2}};
        int[][] ct = {{1, 0}, {3, 2}};

        Board bt = new Board(at);
        Board dt = new Board(ct);
        System.out.println(bt.toString());
        System.out.println(bt.equals(dt));
        System.out.println(bt.manhattan());
        System.out.println(bt.twin().toString());
        Iterable<Board> it = bt.neighbors();
        for (Board neighbor : it) {
            System.out.println(neighbor.toString());
        }
        
    }

    private class Index {

        private final int x;

        private final int y;

        private Index(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}