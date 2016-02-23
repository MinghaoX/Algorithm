import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation{
	private WeightedQuickUnionUF grid;
	private boolean[] open;
	private boolean[] full;
	private final int size;
	private final int unit;

	//create N-by-N grid, with all sites blocked
	public Percolation(int N) throws IllegalArgumentException{
		if(N <= 0) throw new IllegalArgumentException("N must be positive");
		unit = N;
		size = N * N + 2;
		grid = new WeightedQuickUnionUF(size);
		for(int i = 1; i < N + 1; i++){
			grid.union(0, this.getIndex(1, i));
			grid.union(size - 1, this.getIndex(N, i));
		}
		open = new boolean[size];
		for(boolean i : open){
			i = false;
		}
		open[0] = true;
		open[size - 1] = true;
		full = new boolean[size];
		for(boolean j : open){
			j = false;
		}
		full[0] = true;
		full[size - 1] = true;
	}

	//parse (row,column) to index 
	private int getIndex(int i, int j){
		return (i - 1) * unit + j;
	}

   //valid index (row,column) is not out of bound
	private boolean validIndex(int i, int j){
		if(i <= 0 || i > unit || j <= 0 || j > unit) return false;
		return true;
	}

	//open site (row i, column j) if it is not open already
   	public void open(int i, int j) throws IndexOutOfBoundsException{
   		if (i <= 0 || i > unit) throw new IndexOutOfBoundsException("row index i out of bounds");
   		if (j <= 0 || j > unit) throw new IndexOutOfBoundsException("column index j out of bounds");
   		if (!this.isOpen(i,j)) {
   			open[this.getIndex(i,j)] = true;
   			this.bind(i,j);
   		}
   		for (int row = 1; row <= unit; row++) {
            for (int col = 1; col <= unit; col++) {
   				if(this.isOpen(row, col) && grid.connected(0, this.getIndex(row, col))){
   					this.fill(row, col);
   				}
   			}
   		}
   		return;
   	}

      //union site with sites next to it which are open
   	private void bind(int i, int j){
   		if(this.validIndex(i - 1, j) && this.isOpen(i - 1, j)){
   			grid.union(this.getIndex(i - 1, j), this.getIndex(i, j));
   		}
   		if(this.validIndex(i + 1, j) && this.isOpen(i + 1, j)){
   			grid.union(this.getIndex(i + 1, j), this.getIndex(i, j));
   		}
   		if(this.validIndex(i, j - 1) && this.isOpen(i, j - 1)){
   			grid.union(this.getIndex(i, j - 1), this.getIndex(i, j));
   		}
   		if(this.validIndex(i, j + 1) && this.isOpen(i, j + 1)){
   			grid.union(this.getIndex(i, j + 1), this.getIndex(i, j));
   		}
   		return;
   	}

      //fill site (row i, column j)
   	private void fill(int i, int j){
   		full[this.getIndex(i, j)] = true;
   		return;
   	}

   	//is site (row i, column j) open?
   	public boolean isOpen(int i, int j) throws IndexOutOfBoundsException{
   		if (i <= 0 || i > unit) throw new IndexOutOfBoundsException("row index i out of bounds");
   		if (j <= 0 || j > unit) throw new IndexOutOfBoundsException("column index j out of bounds");
   		return open[this.getIndex(i,j)];
   	}
   	//is site (row i, column j) full?
   	public boolean isFull(int i, int j) throws IndexOutOfBoundsException{
   		if (i <= 0 || i > unit) throw new IndexOutOfBoundsException("row index i out of bounds");
   		if (j <= 0 || j > unit) throw new IndexOutOfBoundsException("column index j out of bounds");
   		return full[this.getIndex(i,j)];
   	}
   	//does the system percolate?
   	public boolean percolates(){
   		return grid.connected(0, size - 1);
   	}
}