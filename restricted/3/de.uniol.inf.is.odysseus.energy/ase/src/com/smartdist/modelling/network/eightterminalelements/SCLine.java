/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.eightterminalelements;

import com.smartdist.util.Complex;

public class SCLine extends Line {
	
	/**
	 * This class is a front end to Class Line(). It initializes Line based on the following arguments in Symmetrical Components.
	 * All arguments are per-length, with the length of the line stored in member variable double Line.length
	 * 
	 * @param r_1	positive sequence resistance per-length
	 * @param x_1	positive sequence reactance per-length
	 * @param r_0	zero sequence resistance per length
	 * @param x_0	zero sequence reactance per-length
	 * @param freq	nominal frequency reactances have been calculated for
	 */

	public SCLine(double r_1, double x_1, double r_0, double x_0, double freq){
		super();
		double d120 = 2d/3d*java.lang.Math.PI;
		double d240 = 4d/3d*java.lang.Math.PI;
		Complex a = new Complex(java.lang.Math.cos(d120),java.lang.Math.sin(d120));
		Complex a2 = new Complex(java.lang.Math.cos(d240),java.lang.Math.sin(d240));
		Complex c1 = new Complex(1,0);
		Complex c0 = new Complex(0,0);
		
		Complex[][] F = new Complex[][]{
				{c1.clone(), c1.clone(), c1.clone()},
				{a2.clone(), a.clone(), c1.clone()},
				{a.clone(), a2.clone(), c1.clone()}
		};
		
		Complex[][] Finv = new Complex[][]{
				{c1.clone(), a.clone(), a2.clone()},
				{c1.clone(), a2.clone(), a.clone()},
				{c1.clone(), c1.clone(), c1.clone()}
		};
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				Finv[i][j].divide(3);
			}
		}
		
		Complex[][] Zsc = new Complex[][]{
				{new Complex(r_1,x_1), c0.clone(), c0.clone()},
				{c0.clone(), new Complex(r_1,x_1), c0.clone()},
				{c0.clone(), c0.clone(), new Complex(r_0, x_0)}
		};
		
		Complex[][] Ztemp = new Complex[3][3];
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				Complex tempComplex = new Complex(0,0);
				for (int k=0;k<3;k++){
					tempComplex.add(Zsc[i][k].clone().multiply(Finv[k][j]));
				}
				Ztemp[i][j] = tempComplex;
			}
		}
		
		Complex[][] Z3ph = new Complex[3][3];
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				Complex tempComplex = new Complex(0,0);
				for (int k=0;k<3;k++){
					tempComplex.add(F[i][k].clone().multiply(Ztemp[k][j]));
				}
				Z3ph[i][j] = tempComplex;
			}
		}
		
		// calculating average of off-diagonal elements
		Complex Zneutral = new Complex(0,0);
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				if (i!=j){
					Zneutral.add(Z3ph[i][j]);
				}
			}
		}
		Zneutral.divide(6d);
		
		Complex[][] Z4ph = new Complex[4][4];
		for (int i=0;i<3;i++){
			for (int j=0;j<3;j++){
				Z4ph[i][j] = Z3ph[i][j].clone().sustract(Zneutral);
			}
			Z4ph[3][i] = new Complex(0,0);
			Z4ph[i][3] = new Complex(0,0);
		}
		Z4ph[3][3] = Zneutral.clone();
		
		double omega = 2*java.lang.Math.PI*freq;
		this.serialResistivityA = Z4ph[0][0].getReal();
		this.serialInductivityA = Z4ph[0][0].getImag()/omega;
		this.serialResistivityB = Z4ph[1][1].getReal();
		this.serialInductivityB = Z4ph[1][1].getImag()/omega;
		this.serialResistivityC = Z4ph[2][2].getReal();
		this.serialInductivityC = Z4ph[2][2].getImag()/omega;
		this.serialResistivityN = Z4ph[3][3].getReal();
		this.serialInductivityN = Z4ph[3][3].getImag()/omega;
		
	}
}
