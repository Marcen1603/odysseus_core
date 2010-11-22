package de.uniol.inf.is.odysseus.scars.emep.functions;

public class DoubleMatrixConverter {
	
	private static DoubleMatrixConverter instance = new DoubleMatrixConverter();
	
	private DoubleMatrixConverter() {
		
	}
	
	public static DoubleMatrixConverter getInstance() {
		return instance;
	}
	
	public double[][] convertMatrix(Double[][] mat) {
		double[][] dMatrix = new double[mat[0].length][mat.length];  
		for (int i = 0; i < dMatrix[0].length; i++) {
			for (int j = 0; j < dMatrix.length; j++) {
				dMatrix[i][j] = mat[i][j];
			}
		}
		return dMatrix;
	}

}
