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

public class PMU extends NodeConnector {
	private Node myNode = null;
	public double trueValue = 0;
	public int myPhase = 0; // A:0, B:1, C:2, N:3
	public FourWireNetwork network = null;
	public boolean smoothening = true; // aims at preventing sudden sign-changes
										// at 180 degree, which stir up
										// iteration
	public double lastTheoreticalValue = 0.0;
	public double smootheningThreshold = 90.0;

	public PMU(FourWireNetwork network, Node myNode, int myPhase,
			double trueValue) {
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
				.getAbs() != 0) { // This might lead to 'false undetermined'
									// zero voltages
			myJacobianRow[2 * 4 * this.myNode.getModelNumber() + 2
					* this.myPhase + 0] = -assumedVoltages[4
					* this.myNode.getModelNumber() + this.myPhase].getImag()
					/ (assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getAbs() * assumedVoltages[4
							* this.myNode.getModelNumber() + this.myPhase]
								.getAbs()) / java.lang.Math.PI * 180;
			myJacobianRow[2 * 4 * this.myNode.getModelNumber() + 2
					* this.myPhase + 1] = assumedVoltages[4
					* this.myNode.getModelNumber() + this.myPhase].getReal()
					/ (assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getAbs() * assumedVoltages[4
							* this.myNode.getModelNumber() + this.myPhase]
								.getAbs()) / java.lang.Math.PI * 180;
		}
		return myJacobianRow;
	}

	@Override
	public Node myNode() {
		return this.myNode;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		if (smoothening) {
			double tempAngle = ((Math.atan2(
					assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getImag(),
					assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getReal()) / Math.PI) * 180.);
			if ((this.lastTheoreticalValue > this.smootheningThreshold)
					&& (tempAngle < -this.smootheningThreshold)) {
				// System.out.println("PMU.java: smoothened upwards: following "
				// + this.lastTheoreticalValue + " calculated value " +
				// tempAngle + " pushed up to " + (tempAngle+360.0));
				this.lastTheoreticalValue = (tempAngle + 360.0);
				return (tempAngle + 360.0);
			} else {
				if ((this.lastTheoreticalValue < -this.smootheningThreshold)
						&& (tempAngle > this.smootheningThreshold)) {
					// System.out.println("PMU.java: smoothened downwards: following "
					// + this.lastTheoreticalValue + " calculated value " +
					// tempAngle + " pushed down to " + (tempAngle-360.0));
					this.lastTheoreticalValue = (tempAngle - 360.0);
					return (tempAngle - 360.0);
				} else {
					this.lastTheoreticalValue = tempAngle;
					return tempAngle;
				}
			}
		} else {
			return ((Math.atan2(
					assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getImag(),
					assumedVoltages[4 * this.myNode.getModelNumber()
							+ this.myPhase].getReal()) / Math.PI) * 180.);
		}
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
		// It's not sensitive to model changes, only to state changes!
		return 0;
	}

}
