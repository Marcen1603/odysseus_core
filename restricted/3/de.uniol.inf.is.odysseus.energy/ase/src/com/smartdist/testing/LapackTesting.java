/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

import com.smartdist.util.DevelopmentHelper;
import com.smartdist.util.LAPACK;
import com.smartdist.util.SVDResultSet;
import com.smartdist.util.SingularValueDecomposer;

public class LapackTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[][] A_2D = {{1,2},{3,4}};
		double[] A_1D = new double[A_2D.length*A_2D[0].length];
		for (int c=0;c<A_2D[0].length;c++){
			for (int r=0;r<A_2D.length;r++){
				A_1D[c*A_2D.length+r]=A_2D[r][c];
			}
		}
		
		System.out.println("----------------------------------");
		System.out.println("matrix 2D:");
		DevelopmentHelper.printToConsole(A_2D);
		SVDResultSet svd_2D = SingularValueDecomposer.singuglarValueDecomposition(A_2D);
		System.out.println("left singular values 2D:");
		DevelopmentHelper.printToConsole(svd_2D.leftSingularVectors);
		System.out.println("singular values 2D:");
		DevelopmentHelper.printToConsole(svd_2D.singularValues);
		System.out.println("right singular vectors 2D");
		DevelopmentHelper.printToConsole(svd_2D.rightSingularVectors);


		System.out.println("----------------------------------");
		System.out.println("matrix 1D:");
		DevelopmentHelper.printToConsole(A_1D);
		LAPACK.Dgesvd_full(A_1D, A_2D.length);
		

		
		
	}

}
