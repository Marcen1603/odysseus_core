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

public class DeltaConnectionRealPart extends NodeConnector {
	private Node myNode = null;
	private FourWireNetwork myNetwork = null;

	public DeltaConnectionRealPart(FourWireNetwork network, Node myNode) {
		this.myNode = myNode;
		this.myNetwork = network;
	}

	@Override
	public double myTrueValue() {
		// Always has to equal zero, since neutral is disconnected in Delta
		// connection
		return 0;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		// the theoretical value is just what is given by the neutral's
		// admittance matrix row multiplied by all voltages
		int numberOfNodes = this.myNetwork.getVertexCount();
		Complex[][] admittanceMatrix = this.myNetwork.networkAdmittanceMatrix;
		int myNodeNumber = this.myNode.getModelNumber();
		Complex tempNeutralCurrent = new Complex();
		for (int i = 0; i < 4 * numberOfNodes; i++) {
			tempNeutralCurrent.add(admittanceMatrix[4 * myNodeNumber
					+ FourWireNetwork.phaseN][i].clone().multiply(
					assumedVoltages[i]));
		}

		return tempNeutralCurrent.getReal();
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {
		int myNodeNumber = this.myNode.getModelNumber();
		int numberOfNodes = this.myNetwork.getVertexCount();
		Complex[][] admittanceMatrix = this.myNetwork.networkAdmittanceMatrix;

		// Since everything is linear, it simply splits into g*e-b*f for the
		// real part of the current
		double[] tempJacobianRow = new double[8 * numberOfNodes];
		for (int i = 0; i < 4 * numberOfNodes; i++) {
			tempJacobianRow[2 * i + 0] = admittanceMatrix[4 * myNodeNumber
					+ FourWireNetwork.phaseN][i].getReal(); // g*e
			tempJacobianRow[2 * i + 1] = -admittanceMatrix[4 * myNodeNumber
					+ FourWireNetwork.phaseN][i].getImag(); // -b*f
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
		return true;
	}

	@Override
	public boolean wantsDelayedStart() {
		return true;
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
					sensitivity += dY[4*here+FourWireNetwork.phaseN][4*here+i].getReal()*expansionPoint[4*myNodeNumber+i].getReal();
					sensitivity += dY[4*here+FourWireNetwork.phaseN][4*there+i].getReal()*expansionPoint[4*remoteNodeNumber+i].getReal();
					sensitivity += -dY[4*here+FourWireNetwork.phaseN][4*here+i].getImag()*expansionPoint[4*myNodeNumber+i].getImag();
					sensitivity += -dY[4*here+FourWireNetwork.phaseN][4*there+i].getImag()*expansionPoint[4*remoteNodeNumber+i].getImag();
				}
				
			}
		}
		if (element instanceof TuneableFourTerminalElement){
			if (this.myNode.myFourTerminalElements.contains(element)){
				// If it's a FourTerminalElement
				System.out.println("DeltaConnectionRealPart doesn't support tuning of FourTerminalElements yet!");
			}
		}	
		return sensitivity;
	}

}
