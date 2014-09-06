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

public class SinglePhaseActivePower_backup extends NodeConnector {
	private Node myNode = null;
	public double trueValue = 0;
	public int myPhase = 0; // the phase the current is measured -- A:0 B:1 C:2
							// N:3
	public int counterPhase = 3; // the phase the current flows to - typically N
									// -- A:0 B:1 C:2 N:3
	public FourWireNetwork network = null;

	public SinglePhaseActivePower_backup(FourWireNetwork network, Node myNode,
			int primaryPhase, int counterPhase, double trueValue) {
		this.myNode = myNode;
		this.myPhase = primaryPhase;
		this.counterPhase = counterPhase;
		this.trueValue = trueValue;
		this.network = network;
	}

	public SinglePhaseActivePower_backup(String uId, FourWireNetwork network,
			Node myNode, int primaryPhase, int counterPhase, double trueValue) {
		this.myNode = myNode;
		this.myPhase = primaryPhase;
		this.counterPhase = counterPhase;
		this.trueValue = trueValue;
		this.network = network;
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {

		int totalNumberOfNodes = network.getVertexCount();
		int myNodesNumber = myNode.getModelNumber();
		Complex[] myAdmittanceVector = network.networkAdmittanceMatrix[4
				* myNodesNumber + this.myPhase];
		double[] myJacobianVector = new double[4 * 2 * totalNumberOfNodes];

		// dPi/dej
		for (int j = 0; j < 4 * totalNumberOfNodes; j++) {
			myJacobianVector[2 * j + 0] = myAdmittanceVector[j].getReal()
					* assumedVoltages[4 * myNodesNumber + this.myPhase]
							.getReal();
			myJacobianVector[2 * j + 0] -= myAdmittanceVector[j].getReal()
					* assumedVoltages[4 * myNodesNumber + this.counterPhase]
							.getReal();
			myJacobianVector[2 * j + 0] += myAdmittanceVector[j].getImag()
					* assumedVoltages[4 * myNodesNumber + this.myPhase]
							.getImag();
			myJacobianVector[2 * j + 0] -= myAdmittanceVector[j].getImag()
					* assumedVoltages[4 * myNodesNumber + this.counterPhase]
							.getImag();
		}
		// dPi/dfj
		for (int j = 0; j < 4 * totalNumberOfNodes; j++) {
			myJacobianVector[2 * j + 1] = -myAdmittanceVector[j].getImag()
					* assumedVoltages[4 * myNodesNumber + this.myPhase]
							.getReal();
			myJacobianVector[2 * j + 1] += myAdmittanceVector[j].getImag()
					* assumedVoltages[4 * myNodesNumber + this.counterPhase]
							.getReal();
			myJacobianVector[2 * j + 1] += myAdmittanceVector[j].getReal()
					* assumedVoltages[4 * myNodesNumber + this.myPhase]
							.getImag();
			myJacobianVector[2 * j + 1] -= myAdmittanceVector[j].getReal()
					* assumedVoltages[4 * myNodesNumber + this.counterPhase]
							.getImag();
		}
		// dPi/dei
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 0] = 0;
		for (int j = 0; j < 4 * totalNumberOfNodes; j++) {
			if (j != 4 * myNodesNumber + this.myPhase) {
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 0] += myAdmittanceVector[j]
						.getReal() * assumedVoltages[j].getReal();
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 0] -= myAdmittanceVector[j]
						.getImag() * assumedVoltages[j].getImag();
			}
		}
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 0] += 2
				* myAdmittanceVector[4 * myNodesNumber + this.myPhase]
						.getReal()
				* assumedVoltages[4 * myNodesNumber + this.myPhase].getReal();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 0] -= myAdmittanceVector[4
				* myNodesNumber + this.myPhase].getReal()
				* assumedVoltages[4 * myNodesNumber + this.counterPhase]
						.getReal();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 0] -= myAdmittanceVector[4
				* myNodesNumber + this.myPhase].getImag()
				* assumedVoltages[4 * myNodesNumber + this.counterPhase]
						.getImag();

		// dPi/dfi
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 1] = 0;
		for (int j = 0; j < 4 * totalNumberOfNodes; j++) {
			if (j != 4 * myNodesNumber + this.myPhase) {
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 1] += myAdmittanceVector[j]
						.getImag() * assumedVoltages[j].getReal();
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 1] += myAdmittanceVector[j]
						.getReal() * assumedVoltages[j].getImag();
			}
		}
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 1] += 2
				* myAdmittanceVector[4 * myNodesNumber + this.myPhase]
						.getReal()
				* assumedVoltages[4 * myNodesNumber + this.myPhase].getImag();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 1] += myAdmittanceVector[4
				* myNodesNumber + this.myPhase].getImag()
				* assumedVoltages[4 * myNodesNumber + this.counterPhase]
						.getReal();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.myPhase + 1] -= myAdmittanceVector[4
				* myNodesNumber + this.myPhase].getReal()
				* assumedVoltages[4 * myNodesNumber + this.counterPhase]
						.getImag();

		// dPi/dei Counterphase
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 0] = 0;
		for (int j = 0; j < 4 * totalNumberOfNodes; j++) {
			if (j != 4 * myNodesNumber + this.counterPhase) {
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase
						+ 0] -= myAdmittanceVector[j].getReal()
						* assumedVoltages[j].getReal();
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase
						+ 0] += myAdmittanceVector[j].getImag()
						* assumedVoltages[j].getImag();
			}
		}
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 0] -= 2
				* myAdmittanceVector[4 * myNodesNumber + this.counterPhase]
						.getReal()
				* assumedVoltages[4 * myNodesNumber + this.counterPhase]
						.getReal();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 0] += myAdmittanceVector[4
				* myNodesNumber + this.counterPhase].getReal()
				* assumedVoltages[4 * myNodesNumber + this.myPhase].getReal();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 0] += myAdmittanceVector[4
				* myNodesNumber + this.counterPhase].getReal()
				* assumedVoltages[4 * myNodesNumber + this.myPhase].getImag();

		// dPi/dfi Counterphase
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 1] = 0;
		for (int j = 0; j < 4 * totalNumberOfNodes; j++) {
			if (j != 4 * myNodesNumber + this.counterPhase) {
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase
						+ 1] -= myAdmittanceVector[j].getImag()
						* assumedVoltages[j].getReal();
				myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase
						+ 1] -= myAdmittanceVector[j].getReal()
						* assumedVoltages[j].getImag();
			}
		}
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 1] -= 2
				* myAdmittanceVector[4 * myNodesNumber + this.counterPhase]
						.getReal()
				* assumedVoltages[4 * myNodesNumber + this.counterPhase]
						.getImag();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 1] -= myAdmittanceVector[4
				* myNodesNumber + this.counterPhase].getImag()
				* assumedVoltages[4 * myNodesNumber + this.myPhase].getReal();
		myJacobianVector[2 * 4 * myNodesNumber + 2 * this.counterPhase + 1] += myAdmittanceVector[4
				* myNodesNumber + this.counterPhase].getReal()
				* assumedVoltages[4 * myNodesNumber + this.myPhase].getImag();

		return myJacobianVector;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {

		int myNodesNumber = myNode.getModelNumber();
		int totalNumberOfNodes = network.getVertices().size();
		Complex[] myAdmittanceVector = network.networkAdmittanceMatrix[4
				* myNodesNumber + myPhase];
		Complex tempComplex = null;
		Complex nodalCurrent = new Complex(0, 0);
		Complex phaseToCounterPhaseVoltage = new Complex(0, 0);

		for (int i = 0; i < 4 * totalNumberOfNodes; i++) {
			tempComplex = new Complex(0, 0);
			tempComplex.add(new Complex(myAdmittanceVector[i]).multiply(assumedVoltages[i]));
			nodalCurrent.add(tempComplex);
		}

		phaseToCounterPhaseVoltage.add(
				assumedVoltages[4 * myNodesNumber + this.myPhase]).sustract(
				assumedVoltages[4 * myNodesNumber + this.counterPhase]);
		// System.out.print("I: " + nodalCurrent.getAbs() + " V: " +
		// phaseToNVoltage.getAbs());
		double P = (nodalCurrent.conjugate()
				.multiply(phaseToCounterPhaseVoltage)).getReal();
		// System.out.println(" Psoll: " + this.trueValue + " Pist: " + P);
		return P;
	}

	@Override
	public double myTrueValue() {
		return this.trueValue;
	}

	@Override
	public Node myNode() {
		return this.myNode;
	}

	@Override
	public void setMyTrueValue(double trueValue) {
		this.trueValue = trueValue;
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
					
				//g_ik,RX;k\neq i, X\in{A,...,N}
				for (int p=0;p<4;p++){
					sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.counterPhase].getReal())
							*expansionPoint[4*remoteNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*there+p].getReal();
					sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.counterPhase].getImag())
							*expansionPoint[4*remoteNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*there+p].getReal();
				}
				//b_ik,RX;k\neq i; X\in {A,...,N}
				for (int p=0;p<4;p++){
					sensitivity += (-1)*(expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.counterPhase].getReal())
							*expansionPoint[4*remoteNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*there+p].getImag();
					sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.counterPhase].getImag())
							*expansionPoint[4*remoteNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*there+p].getImag();
				}
				for (int p=0;p<4;p++){
					if (p == this.myPhase){
						//g_ii,RR
						sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getReal()*expansionPoint[4*myNodeNumber+this.myPhase].getReal()
								+ expansionPoint[4*myNodeNumber+this.myPhase].getImag()*expansionPoint[4*myNodeNumber+this.myPhase].getImag())
								*dY[4*here+this.myPhase][4*here+this.myPhase].getReal();
					}
					if (p == this.counterPhase){
						//g_ii,RS
						sensitivity += (-expansionPoint[4*myNodeNumber+this.counterPhase].getReal()*expansionPoint[4*myNodeNumber+this.counterPhase].getReal()
								-expansionPoint[4*myNodeNumber+this.counterPhase].getImag()*expansionPoint[4*myNodeNumber+this.counterPhase].getImag())
								*dY[4*here+this.myPhase][4*here+this.counterPhase].getReal();
					}
					if (!(p == this.myPhase)&&!(p == this.counterPhase)){
						//g_ii,RX;X\neq R,S
						sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.counterPhase].getReal())
								*expansionPoint[4*myNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*here+p].getReal();
						sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.counterPhase].getImag())
								*expansionPoint[4*myNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*here+p].getReal();
						//b_ii,RX;X\neq R,S
						sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.counterPhase].getImag())
								*expansionPoint[4*myNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*here+p].getImag();
						sensitivity += (-1)*(expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.counterPhase].getReal())
								*expansionPoint[4*myNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*here+p].getImag();
					}
				}
			}
		}
		if (element instanceof TuneableFourTerminalElement){
			if (this.myNode.myFourTerminalElements.contains(element)){
				// If it's a FourTerminalElement
				System.out.println("SinglePhaseActivePower doesn't support tuning of FourTerminalElements yet!");
			}
		}	
		return sensitivity;
	}

	@Override
	public boolean wantsFadeIn() {
		return false;
	}

	@Override
	public boolean wantsDelayedStart() {
		return false;
	}

}
