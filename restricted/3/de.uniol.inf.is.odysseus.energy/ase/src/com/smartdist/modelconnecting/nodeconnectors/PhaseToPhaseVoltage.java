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

public class PhaseToPhaseVoltage extends NodeConnector {
	private Node myNode = null;
	public double trueValue = 0;
	public int myPhase = 0; // A:0, B:1, C:2, N:3
	public int counterPhase = 3; // A:0, B:1, C:2, N:3
	public FourWireNetwork network = null;

	public PhaseToPhaseVoltage(FourWireNetwork network, Node myNode,
			int myPhase, int counterPhase, double trueValue) {
		this.myNode = myNode;
		this.myPhase = myPhase;
		this.counterPhase = counterPhase;
		this.trueValue = trueValue;
		this.network = network;
	}

	public PhaseToPhaseVoltage(String uId, FourWireNetwork network,
			Node myNode, int myPhase, int counterPhase, double trueValue) {
		this.myNode = myNode;
		this.myPhase = myPhase;
		this.counterPhase = counterPhase;
		this.trueValue = trueValue;
		this.network = network;
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {
		double[] myJacobianRow = new double[2 * 4 * this.network
				.getVertexCount()];
		Complex tempComplex = assumedVoltages[4 * this.myNode.getModelNumber()
				+ this.myPhase].clone();
		tempComplex.sustract(assumedVoltages[4 * this.myNode.getModelNumber()
				+ this.counterPhase]);
		if (tempComplex.getAbs() != 0) {
			myJacobianRow[2 * 4 * this.myNode.getModelNumber() + 2
					* this.myPhase + 0] = tempComplex.getReal()
					/ tempComplex.getAbs(); // d/de_a
			myJacobianRow[2 * 4 * this.myNode.getModelNumber() + 2
					* this.counterPhase + 0] = -tempComplex.getReal()
					/ tempComplex.getAbs(); // d/de_b
			myJacobianRow[2 * 4 * this.myNode.getModelNumber() + 2
					* this.myPhase + 1] = tempComplex.getImag()
					/ tempComplex.getAbs(); // d/df_a
			myJacobianRow[2 * 4 * this.myNode.getModelNumber() + 2
					* this.counterPhase + 1] = -tempComplex.getImag()
					/ tempComplex.getAbs(); // d/df_b
		}
		return myJacobianRow;
	}

	@Override
	public Node myNode() {
		return this.myNode;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		Complex tempComplex = assumedVoltages[4 * this.myNode.getModelNumber()
				+ this.myPhase].clone();
		tempComplex.sustract(assumedVoltages[4 * this.myNode.getModelNumber()
				+ this.counterPhase]);
		return tempComplex.getAbs();
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
