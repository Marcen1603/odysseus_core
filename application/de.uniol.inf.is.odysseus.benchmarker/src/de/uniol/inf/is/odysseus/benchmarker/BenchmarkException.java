package de.uniol.inf.is.odysseus.benchmarker;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkException extends Exception {
	private static final long serialVersionUID = 3965655511116642049L;

	public BenchmarkException() {
		super();
	}

	public BenchmarkException(Exception e) {
		super(e);
	}
}
