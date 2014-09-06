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
import com.smartdist.modelling.network.TuneableEightTerminalElement;
import com.smartdist.modelling.network.TuneableFourTerminalElement;
import com.smartdist.modelling.network.TuneableNetworkElement;
import com.smartdist.util.Complex;

public class LambdaConnectionRealPart extends NodeConnector {
	private Node myNode = null;
	@SuppressWarnings("unused")
	private double myTrueValue = 0;
	private FourWireNetwork myNetwork = null;

	public LambdaConnectionRealPart(FourWireNetwork network, Node myNode) {
		this.myNode = myNode;
		this.myNetwork = network;
	}

	@Override
	public double myTrueValue() {
		/**
		 * The concept here is to use the neutral current that derives from the
		 * 'neutral's' row in the admittance matrix as the real value and the
		 * sum of all other phase currents as the theoretical value. The problem
		 * is, that by design myTrueValue is called without assumedVoltages.
		 * Workaround is to calculate myTrueValue every time
		 * myTheoreticalValue(assumedVoltages) is called, store it in the member
		 * variable myTrueValue and return this whenever myTrueValue() is
		 * invoked.
		 */
		return 0;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		// Update this.myTrueValue first. It is the negative sum of all other
		// phase current, since the balance of all has to equal zero.
		Complex[][] admittanceMatrix = this.myNetwork.networkAdmittanceMatrix;
		int myNodeNumber = this.myNode.getModelNumber();
		int numberOfNodes = this.myNetwork.getVertexCount();

		Complex tempValue = new Complex();
		Complex tempNeutralCurrent = new Complex();
		Complex tempPhaseACurrent = new Complex();
		Complex tempPhaseBCurrent = new Complex();
		Complex tempPhaseCCurrent = new Complex();
		for (int i = 0; i < 4 * numberOfNodes; i++) {
			tempPhaseACurrent.add(admittanceMatrix[4 * myNodeNumber
					+ FourWireNetwork.phaseA][i].clone().multiply(
					assumedVoltages[i]));
			tempPhaseBCurrent.add(admittanceMatrix[4 * myNodeNumber
					+ FourWireNetwork.phaseB][i].clone().multiply(
					assumedVoltages[i]));
			tempPhaseCCurrent.add(admittanceMatrix[4 * myNodeNumber
					+ FourWireNetwork.phaseC][i].clone().multiply(
					assumedVoltages[i]));
			tempNeutralCurrent.add(admittanceMatrix[4 * myNodeNumber
			        + FourWireNetwork.phaseN][i].clone().multiply(
			        assumedVoltages[i]));
		}
//		tempNeutralCurrent.sustract(tempPhaseACurrent);
//		tempNeutralCurrent.sustract(tempPhaseBCurrent);
//		tempNeutralCurrent.sustract(tempPhaseCCurrent);
		
		tempValue.add(tempPhaseACurrent);
		tempValue.add(tempPhaseBCurrent);
		tempValue.add(tempPhaseCCurrent);
		tempValue.add(tempNeutralCurrent);

//		this.myTrueValue = tempValue.getReal();
//
//		// the theoretical value is just what is given by the neutral's
//		// admittance matrix row multiplied by all voltages
//		tempNeutralCurrent = new Complex();
//		for (int i = 0; i < 4 * numberOfNodes; i++) {
//			tempNeutralCurrent.add(admittanceMatrix[4 * myNodeNumber
//					+ FourWireNetwork.phaseN][i].clone().multiply(
//					assumedVoltages[i]));
//		}

		return tempValue.getReal();
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {
		int myNodeNumber = this.myNode.getModelNumber();
		int numberOfNodes = this.myNetwork.getVertexCount();
		Complex[][] admittanceMatrix = this.myNetwork.networkAdmittanceMatrix;

		// Since everything is linear, it simply splits into g*e-b*f for the
		// real part of the current
		double[] tempJacobianRow = new double[8 * numberOfNodes];
		double tempG = 0;
		double tempB =0;
		for (int i = 0; i < 4 * numberOfNodes; i++) {
			tempG = 0;
			tempB = 0;
			for (int p=0;p<4;p++){
				tempG += admittanceMatrix[4 * myNodeNumber 
				         + p][i].getReal();
				tempB += -admittanceMatrix[4 * myNodeNumber
				         + p][i].getImag();
			}
			tempJacobianRow[2 * i + 0] = tempG;
			tempJacobianRow[2 * i + 1] = tempB;
		}
		return tempJacobianRow;
	}

	@Override
	public void setMyTrueValue(double trueValue) {
		// Nothing to be set here... It's automatically done in
		// myTheoreticalValue()
	}

	@Override
	public Node myNode() {
		return this.myNode;
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
		double sensitivity = 0.0;
		if (element instanceof TuneableEightTerminalElement){
			if (network.isIncident(this.myNode, (TuneableEightTerminalElement)element)){
				// If it's an EightTerminalElement
				TuneableEightTerminalElement tempElement = (TuneableEightTerminalElement)element;
				Complex[][] dY = tempElement.getInfinitisimalAdmittanceChange(network, expansionPoint, tuneable);
				
				Node sourceNode = network.getSource(tempElement);
				Node destinationNode = network.getDest(tempElement);
				int myNodeNumber = sourceNode.getModelNumber();
				int remoteNodeNumber = destinationNode.getModelNumber();
				int here = 0;
				int there = 0;
				
				if (sourceNode == this.myNode){
					here = 0;
					there = 1;
					myNodeNumber = sourceNode.getModelNumber();
					remoteNodeNumber = destinationNode.getModelNumber();
				}else{
					here = 1;
					there = 0;
					myNodeNumber = destinationNode.getModelNumber();
					remoteNodeNumber = sourceNode.getModelNumber();
				}

				for (int i=0;i<4;i++){
						for (int p=0;p<4;p++){
							sensitivity += dY[4*here+p][4*here+i].getReal()*expansionPoint[4*myNodeNumber+i].getReal();
							sensitivity += dY[4*here+p][4*there+i].getReal()*expansionPoint[4*remoteNodeNumber+i].getReal();
							sensitivity += -dY[4*here+p][4*here+i].getImag()*expansionPoint[4*myNodeNumber+i].getImag();
							sensitivity += -dY[4*here+p][4*there+i].getImag()*expansionPoint[4*remoteNodeNumber+i].getImag();
						}
//					sensitivity += dY[4*here+FourWireNetwork.phaseN][4*here+i].getReal()*expansionPoint[4*myNodeNumber+i].getReal();
//					sensitivity += dY[4*here+FourWireNetwork.phaseN][4*there+i].getReal()*expansionPoint[4*remoteNodeNumber+i].getReal();
//					sensitivity += -dY[4*here+FourWireNetwork.phaseN][4*here+i].getImag()*expansionPoint[4*myNodeNumber+i].getImag();
//					sensitivity += -dY[4*here+FourWireNetwork.phaseN][4*there+i].getImag()*expansionPoint[4*remoteNodeNumber+i].getImag();
				}
				
			}
		}
		if (element instanceof TuneableFourTerminalElement){
			if (this.myNode.myFourTerminalElements.contains(element)){
				// If it's a FourTerminalElement
				System.out.println("StartConnectionRealPart doesn't support tuning of FourTerminalElements yet!");
			}
		}	
		return sensitivity;
	}

}
