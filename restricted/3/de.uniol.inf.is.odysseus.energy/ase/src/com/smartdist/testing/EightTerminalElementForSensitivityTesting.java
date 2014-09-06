/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.util.Complex;

public class EightTerminalElementForSensitivityTesting implements
		com.smartdist.modelling.network.TuneableEightTerminalElement {

	public double serialR = 0.0004;
	public double serialX = 0.0004;
	public double length = 500;
	public Complex[][] Y = new Complex[8][8];
	public Complex[][] dY = new Complex[8][8];
	
	public EightTerminalElementForSensitivityTesting(){
		super();
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				this.dY[i][j] = new Complex(0,0);
			}
		}
	}
	
	
	@Override
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency) {
		// TODO Auto-generated method stub
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				this.Y[i][j] = new Complex(0,0);
			}
		}
		for (int i=0;i<4;i++){
			this.Y[i][i].add(new Complex(serialR, serialX)).multiply(this.length).invert();
			this.Y[i+4][i+4].add(new Complex(serialR, serialX)).multiply(this.length).invert();
			this.Y[i][i+4].sustract(new Complex(serialR, serialX)).multiply(this.length).invert();
			this.Y[i+4][i].sustract(new Complex(serialR, serialX)).multiply(this.length).invert();
		}
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				this.Y[i][j].add(this.dY[i][j]);
			}
		}
		return this.Y;
	}

	@Override
	public double[] getTuneables() {
		
		return null;
	}

	@Override
	public void setTunables(double[] tuneables) {

	}

	@Override
	public double[] getSensitivityAnalysisStepSize() {

		return null;
	}

	@Override
	public Complex[][] getInfinitisimalAdmittanceChange(FourWireNetwork network, Complex[] expansionPoint, int tuneable) {

		return this.dY;
	}

}
