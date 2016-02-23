import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private static double[] getFraction(int N, int T){
		double[] result = new double[T];
		for(int i = 0; i < T; i++){
			Percolation temp = new Percolation(N);
			int counter = 0;
			while(!temp.percolates()){
				int m = StdRandom.uniform(1, N + 1);
				int n = StdRandom.uniform(1, N + 1);
				if(temp.isOpen(m, n) == false){
					counter++;
				}
				temp.open(m, n);
			}
			result[i] = (double)counter/(N * N);
		} 
		return result;
	}

	private final int unit;
	private final int times;
	private final double[] fractions;

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) throws IllegalArgumentException{
		if(N <= 0 || T <= 0) throw new IllegalArgumentException("N and T must be positive");
		unit = N;
		times = T;
		fractions = getFraction(N, T);
	}

	// sample mean of percolation threshold
	public double mean(){
		return StdStats.mean(fractions);
	}

	// sample standard deviation of percolation threshold
	public double stddev(){
		return StdStats.stddev(fractions);
	}

	// low  endpoint of 95% confidence interval
	public double confidenceLo(){
		return mean() - 1.96 * stddev() / Math.sqrt(times);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi(){
		return mean() + 1.96 * stddev() / Math.sqrt(times);
	}
	
	// test client (described below)
	public static void main(String[] args){
		PercolationStats test = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.println("mean = " + test.mean());
		System.out.println("stddev = " + test.stddev());
		System.out.println("95% confidence interval = " + test.confidenceLo() + "," + test.confidenceHi());
	}
}