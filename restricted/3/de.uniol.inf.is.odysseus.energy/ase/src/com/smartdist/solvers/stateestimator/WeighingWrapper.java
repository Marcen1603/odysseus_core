/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import com.smartdist.modelconnecting.ModelConnector;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.TuneableNetworkElement;
import com.smartdist.util.Complex;

public class WeighingWrapper extends ModelConnector {

	ModelConnector myConnector = null;
	double myConnectorsVariance = 0;

	public WeighingWrapper(ModelConnector connector, double abs1stStdDev) {
		super();
		this.myConnector = connector;
		this.myConnectorsVariance = abs1stStdDev;
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {
		double[] tempJacobian = this.myConnector.myJacobianRow(assumedVoltages);
		for (int i = 0; i < tempJacobian.length; i++) {
			tempJacobian[i] = tempJacobian[i] / this.myConnectorsVariance;
		}
		return tempJacobian;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		return this.myConnector.myTheroreticalValue(assumedVoltages)
				/ this.myConnectorsVariance;
	}

	@Override
	public double myTrueValue() {
		return this.myConnector.myTrueValue() / this.myConnectorsVariance;
	}

	@Override
	public void setMyTrueValue(double trueValue) {
		this.myConnector.setMyTrueValue(trueValue);
	}

	@Override
	public boolean wantsFadeIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wantsDelayedStart() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getModelSensitivity(FourWireNetwork network,
			Complex[] expansionPoint, TuneableNetworkElement element,
			int tuneable) {
		double temp = this.myConnector.getModelSensitivity(network, expansionPoint, element, tuneable) / this.myConnectorsVariance;
		return temp;
	}

}
