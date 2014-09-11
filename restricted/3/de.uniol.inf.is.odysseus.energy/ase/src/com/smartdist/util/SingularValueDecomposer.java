/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.util;

import org.netlib.lapack.DGESVD;

public class SingularValueDecomposer {

	//public static double machineEpsilon = SingularValueDecomposer.calculateMachineEpsilonDouble(1.0d);
	
	public static double[][] pinv(double[][] matrix) {

		// TODO check and compare to SVD in State Estimator and add SVD partial
		// results.

		int rows = matrix.length;
		int columns = matrix[0].length;

		double[] singularValues = new double[rows];
		double[][] leftSingularVectors = new double[rows][rows];
		double[][] rightSingularVectors = new double[columns][columns];
		double[] work = new double[Math.max(
				3 * Math.min(rows, columns) + Math.max(rows, columns),
				5 * Math.min(rows, columns))];
		org.netlib.util.intW info = new org.netlib.util.intW(2);
		double[][] transposedRightSingularVectors = new double[columns][columns];
		DGESVD.DGESVD("A", "A", rows, columns, matrix, singularValues,
				leftSingularVectors, transposedRightSingularVectors, work,
				work.length, info);
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < columns; j++) {
				rightSingularVectors[i][j] = transposedRightSingularVectors[j][i];
			}
		}
		
		
		
		
		int maxSize = (matrix.length > matrix[0].length ? matrix.length : matrix[0].length);
		double largestSV = 0.0;
		for (int i=0;i<singularValues.length;i++){
			if (singularValues[i]>largestSV){
				largestSV = singularValues[i];
			}
		}
		

		double machEps = SingularValueDecomposer.calculateMachineEpsilonDouble(largestSV);
		double singularValueThreshold = maxSize*machEps;
		
		int rank = 0;
		for (int i = 0; i < singularValues.length; i++) {
			if (singularValues[i] > singularValueThreshold)
				rank++;
		}

		double[][] pinv = new double[columns][rows];

		for (int row = 0; row < columns; row++) {
			for (int col = 0; col < rows; col++) {
				for (int r = 0; r < rank; r++) {
					pinv[row][col] += rightSingularVectors[row][r]
							* (1 / singularValues[r])
							* leftSingularVectors[col][r];
				}
			}
		}
		return pinv;
	}
	
	public static Complex[][] pinv(Complex[][] matrix){
		double[][] tempMatrix = new double[2*matrix.length][2*matrix[0].length];
		Complex[][] result = new Complex[2*matrix[0].length][2*matrix.length];
		for (int i=0;i<matrix.length;i++){
			for (int j=0;j<matrix[0].length;j++){
				tempMatrix[2*i+0][2*j+0]=matrix[i][j].getReal();
				tempMatrix[2*i+0][2*j+1]=-matrix[i][j].getImag();
				tempMatrix[2*i+1][2*j+0]=matrix[i][j].getImag();
				tempMatrix[2*i+1][2*j+1]=matrix[i][j].getReal();
			}
		}
		double[][] tempInv = pinv(tempMatrix);
		double tempReal = 0;
		double tempImag = 0;
		for (int i=0;i<matrix[0].length;i++){
			for (int j=0;j<matrix.length;j++){
				tempReal = 0.5*(tempInv[2*i+0][2*j+0]+tempInv[2*i+1][2*j+1]);
				tempImag = 0.5*(tempInv[2*i+1][2*j+0]-tempInv[2*i+0][2*j+1]);
				result[i][j] = new Complex(tempReal, tempImag);
			}
		}
		return result;
	}
	
	public static SVDResultSet singuglarValueDecomposition(double[][] matrix) {
		// Get the SVD of matrix
		SVDResultSet result = new SVDResultSet();
		int rowsJ = matrix.length;
		int colsJ = matrix[0].length;
		if (result.singularValues == null) result.singularValues = new double[Math.max(rowsJ, colsJ)];
		if (result.leftSingularVectors == null) result.leftSingularVectors = new double[rowsJ][rowsJ];
		if (result.rightSingularVectors == null) result.rightSingularVectors = new double[colsJ][colsJ];
		LAPACK.DGESVD_full(matrix, result.singularValues, result.leftSingularVectors, result.rightSingularVectors);
		
		// RAAAANK !!!
		int maxSize = (matrix.length > matrix[0].length ? matrix.length : matrix[0].length);
		double largestSV = 0.0;
		for (int i=0;i<result.singularValues.length;i++){
			if (result.singularValues[i]>largestSV){
				largestSV = result.singularValues[i];
			}
		}
		double singularValueThreshold = maxSize*SingularValueDecomposer.calculateMachineEpsilonDouble(largestSV);
		result.rank = 0;
		for (int i = 0; i < result.singularValues.length; i++) {
			if (result.singularValues[i] > singularValueThreshold)
				result.rank++;
		}
		return result;
	}


    public static double calculateMachineEpsilonDouble(double center) {
        double machEps = 1.0d;
        do
           machEps /= 2.0d;
        while (center + (machEps / 2.0) != center);
        System.out.println("Machine Epsilon claculated");
        return machEps;
    }
    
    public static double[][] pinv(SVDResultSet svd){
    	int rows = svd.leftSingularVectors.length;
    	int columns = svd.rightSingularVectors.length;
		double[][] pinv = new double[columns][rows];
		for (int row = 0; row < columns; row++) {
			for (int col = 0; col < rows; col++) {
				for (int r = 0; r < svd.rank; r++) {
					pinv[row][col] += svd.rightSingularVectors[row][r]
							* (1 / svd.singularValues[r])
							* svd.leftSingularVectors[col][r];
				}
			}
		}
		return pinv;
    }



}
