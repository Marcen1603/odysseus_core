/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelconnecting.nodeconnectors;

import com.smartdist.modelconnecting.NodeConnector;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.TuneableNetworkElement;
import com.smartdist.util.Complex;

public class PhaseToEarthVoltage extends NodeConnector {
	private Node myNode = null;
	public double trueValue = 0;
	public int myPhase = 0; // A:0, B:1, C:2, N:3
	public FourWireNetwork network = null;

	public PhaseToEarthVoltage(FourWireNetwork network, Node myNode,
			int myPhase, double trueValue) {
		this.myNode = myNode;
		this.myPhase = myPhase;
		this.trueValue = trueValue;
		this.network = network;
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {

		double[] myJacobianRow = new double[2 * 4 * this.network
				.getVertexCount()];
		if (assumedVoltages[4 * this.myNode.getModelNumber() + this.myPhase]
				.getAbs() != 0) {
			myJacobianRow[4 * 2 * this.myNode.getModelNumber() + 2
					* this.myPhase + 0] = assumedVoltages[4
					* this.myNode.getModelNumber() + this.myPhase].getReal()
					/ assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getAbs();
			myJacobianRow[4 * 2 * this.myNode.getModelNumber() + 2
					* this.myPhase + 1] = assumedVoltages[4
					* this.myNode.getModelNumber() + this.myPhase].getImag()
					/ assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getAbs();
		}
		return myJacobianRow;
	}

	@Override
	public Node myNode() {
		return this.myNode;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		return assumedVoltages[4 * this.myNode.getModelNumber() + this.myPhase]
				.getAbs();
	}

	@Override
	public double myTrueValue() {
		return this.trueValue;
	}

	@Override
	public void setMyTrueValue(double trueValue) {
		this.trueValue = trueValue;
	}

	@Override
	public boolean wantsFadeIn() {
		return false;
	}

	@Override
	public boolean wantsDelayedStart() {
		return false;
	}

	@Override
	public double getModelSensitivity(FourWireNetwork network,
			Complex[] expansionPoint, TuneableNetworkElement element,
			int tuneable) {
		// It's not sensitive to model changes, but only to state changes!
		return 0;
	}

}
