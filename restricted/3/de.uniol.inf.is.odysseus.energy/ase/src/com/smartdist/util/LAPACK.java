/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.util;

import org.netlib.lapack.DGESDD;
import org.netlib.lapack.DGESVD;
import org.netlib.util.intW;

public class LAPACK {

	public static void main(String[] args) throws Exception {
		
		double[][] A = {{2.117079, 2.992092},{2.106365, 1.970807}, {2.131384, 3.130390}, {2.344235, 2.667219}, {1.838804, 3.243629}};
		double[] S = new double[Math.max(A.length, A[0].length)];
		double[][] U = new double[A.length][A.length];
		double[][] V = new double[A[0].length][A[0].length];
		LAPACK.DGESVD_full(A, S, U, V);
		DevelopmentHelper.printToConsole(U);
	}
	
	public static boolean DGESDD_full(double[][] A, double[] S, double[][] U, double[][] V){
			
		int rowsA = A.length;
		int colsA = A[0].length;
		org.netlib.util.intW info = new org.netlib.util.intW(2);
		int lwork = 4*Math.min(rowsA, colsA)*Math.min(rowsA, colsA)+Math.max(rowsA, colsA)+9*Math.min(rowsA, colsA);
		int liwork = 8*Math.min(rowsA, colsA);
		double[] work = new double[lwork];
		int[] IWORK = new int[liwork];

		double[][] transposedV = new double[colsA][colsA];		

		DGESDD.DGESDD("A", rowsA, colsA, A, S, U, transposedV, work, lwork, IWORK, info);
		
		for (int i=0;i<colsA;i++){
			for (int j=0;j<colsA;j++){
				V[i][j] = transposedV[j][i];
			}
		}
		
		return (info.val == 0? true:false);
	}
	
	
//    JOBVT is CHARACTER*1
//    Specifies options for computing all or part of the matrix
//    V**T:
//    = 'A':  all N rows of V**T are returned in the array VT;
//    = 'S':  the first min(m,n) rows of V**T (the right singular
//            vectors) are returned in the array VT;
//    = 'O':  the first min(m,n) rows of V**T (the right singular
//            vectors) are overwritten on the array A;
//    = 'N':  no rows of V**T (no right singular vectors) are
//            computed.
//
//    JOBVT and JOBU cannot both be 'O'.
	
	
	public static boolean DGESVD_full(double[][] A, double[] S, double[][] U, double[][] V){
		int rowsA = A.length;
		int colsA = A[0].length;
		org.netlib.util.intW info = new org.netlib.util.intW(2);
		
		int lwork = Math.max((3*Math.min(rowsA, colsA) + Math.max(rowsA, colsA)),5*Math.min(rowsA, colsA));
		double[] work = new double[lwork];
		
		double[][] transposedV = new double[colsA][colsA];
		
		DGESVD.DGESVD("A", "A", rowsA, colsA, A, S, U, transposedV, work, work.length, info);

		for (int i = 0; i < colsA; i++) {
			for (int j = 0; j < colsA; j++) {
				V[i][j] = transposedV[j][i];
			}
		}
		
		return (info.val == 0? true:false);
	}
			
	
	public static void Dgesvd_full(double[] A, int numberOfRows){
		
		String jobu = "A";
		String jobvt = "A";
		int m = numberOfRows;
		int n = A.length/numberOfRows;
		if ((A.length&numberOfRows)!=0) System.out.println("LAPACK.Dgesvd_full here: argument numberOfRows cannot be right");
		int _a_offset = 0;
		int lda = m;
		double[] s = new double[Math.min(m, n)];
		int _s_offset = 0;
		double[] u = new double[m*m];
		int _u_offset = 0;
		int ldu = m;
		double[] vt = new double[n*n];
		int _vt_offset = 0;
		int ldvt = n;
		int lwork = Math.max((3*Math.min(m, n) + Math.max(m, n)),5*Math.min(m, n));
		double[] work = new double[lwork];
		int _work_offset = 0;
		intW info = new intW(0);
	
		org.netlib.lapack.Dgesvd.dgesvd(jobu,
                jobvt,
                m,
                n,
                A,
                _a_offset,
                lda,
                s,
                _s_offset,
                u,
                _u_offset,
                ldu,
                vt,
                _vt_offset,
                ldvt,
                work,
                _work_offset,
                lwork,
                info);
		if (info.val!=0) System.out.println("org.netlib.lapack.Dgesvd failed with argument: " + info.val);
		System.out.println("left singular vectors 1D:");
		DevelopmentHelper.printToConsole(u);
		System.out.println("singular values 1D:");
		DevelopmentHelper.printToConsole(s);
		System.out.println("right singular vectors 1D:");
		DevelopmentHelper.printToConsole(vt);
		
		//pseudoInverse
		//org.netlib.lapack.Dgelss.dgelss(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16)
		
	}
	
}
