/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelconnecting.eightterminalelementconnectors;

import com.smartdist.modelconnecting.EightTerminalElementConnector;
import com.smartdist.modelling.network.EightTerminalElement;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.TuneableEightTerminalElement;
import com.smartdist.modelling.network.TuneableFourTerminalElement;
import com.smartdist.modelling.network.TuneableNetworkElement;
import com.smartdist.util.Complex;

public class AbsoluteEightTerminalElementPhaseCurrent extends
		EightTerminalElementConnector {
	private EightTerminalElement myEightTerminalElement = null;
	private FourWireNetwork network = null;
	private Node myNode = null;
	private int myPhase = 0;
	private double myTrueValue = 0;

	public AbsoluteEightTerminalElementPhaseCurrent(FourWireNetwork myNetwork,
			EightTerminalElement myEightTerminalElement, Node myNode,
			int myPhase, double myTrueValue) {
		this.myEightTerminalElement = myEightTerminalElement;
		this.network = myNetwork;
		this.myNode = myNode;
		this.myPhase = myPhase;
		this.myTrueValue = myTrueValue;
	}

	@Override
	public void setMyTrueValue(double trueValue) {
		this.myTrueValue = trueValue;
	}

	@Override
	public double myTrueValue() {
		return this.myTrueValue;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		Complex[][] admittanceMatrix = this.myEightTerminalElement
				.generateAdmittanceMatrix(this.network.nominalFrequency);
		Complex tempCurrent = new Complex();
		int myNodeNumber = this.myNode.getModelNumber();
		Node sourceNode = this.network.getSource(this.myEightTerminalElement);
		Node targetNode = this.network.getDest(this.myEightTerminalElement);
		boolean atSource = false;
		if (sourceNode.getModelNumber() == myNodeNumber) {
			atSource = true;
		} else {
			atSource = false;
		}
		if (atSource) {
			for (int i = 0; i < 4; i++) {
				tempCurrent.add(admittanceMatrix[4 * 0 + this.myPhase][i]
						.clone().multiply(
								assumedVoltages[4 * sourceNode.getModelNumber()
										+ i]));
				tempCurrent.add(admittanceMatrix[4 * 0 + this.myPhase][4 + i]
						.clone().multiply(
								assumedVoltages[4 * targetNode.getModelNumber()
										+ i]));
			}
		} else {
			for (int i = 0; i < 4; i++) {
				tempCurrent.add(admittanceMatrix[4 * 1 + this.myPhase][i]
						.clone().multiply(
								assumedVoltages[4 * sourceNode.getModelNumber()
										+ i]));
				tempCurrent.add(admittanceMatrix[4 * 1 + this.myPhase][4 + i]
						.clone().multiply(
								assumedVoltages[4 * targetNode.getModelNumber()
										+ i]));
			}
		}
		return tempCurrent.getAbs();
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {

		double[] tempJacobianRow = new double[4 * 2 * this.network
				.getVertexCount()];
		Complex[][] admittanceMatrix = this.myEightTerminalElement
				.generateAdmittanceMatrix(this.network.nominalFrequency);

		// First of all gain orientation. On which side am I?
		Node sourceNode = this.network.getSource(this.myEightTerminalElement);
		Node targetNode = this.network.getDest(this.myEightTerminalElement);
		boolean atSource = false;
		if (this.myNode.getModelNumber() == sourceNode.getModelNumber()) {
			atSource = true;
		}

		// Calculate absolute value for outer derivative.
		double absI = this.myTheroreticalValue(assumedVoltages);
		@SuppressWarnings("unused")
		double tempReEi = 0;
		int k = 0;
		if (atSource) {
			k = this.myPhase;
		} else {
			k = 4 + this.myPhase;
		}
		Complex[] voltages = new Complex[8];

		for (int j = 0; j < 4; j++) {
			voltages[4 * 0 + j] = assumedVoltages[4
					* sourceNode.getModelNumber() + j].clone();
			voltages[4 * 1 + j] = assumedVoltages[4
					* targetNode.getModelNumber() + j].clone();
		}
		for (int i = 0; i < 8; i++) {
			double dIdEi = 0;
			double temp = 0;
			for (int j = 0; j < 8; j++) {
				if (j != i) {
					temp += admittanceMatrix[k][j].getReal()
							* voltages[j].getReal();
					temp -= admittanceMatrix[k][j].getImag()
							* voltages[j].getImag();
				}
			}
			temp = 2 * temp * admittanceMatrix[k][i].getReal();
			temp += 2 * admittanceMatrix[k][i].getReal()
					* admittanceMatrix[k][i].getReal() * voltages[i].getReal();
			temp -= 2 * admittanceMatrix[k][i].getReal()
					* admittanceMatrix[k][i].getImag() * voltages[i].getImag();
			dIdEi = temp;
			temp = 0;
			for (int j = 0; j < 8; j++) {
				if (j != i) {
					temp += admittanceMatrix[k][j].getImag()
							* voltages[j].getReal();
					temp += admittanceMatrix[k][j].getReal()
							* voltages[j].getImag();
				}
			}
			temp = 2 * temp * admittanceMatrix[k][i].getImag();
			temp += 2 * admittanceMatrix[k][i].getImag()
					* admittanceMatrix[k][i].getImag() * voltages[i].getReal();
			temp += 2 * admittanceMatrix[k][i].getReal()
					* admittanceMatrix[k][i].getImag() * voltages[i].getImag();
			dIdEi += temp;

			double dIdFi = 0;
			temp = 0;
			for (int j = 0; j < 8; j++) {
				if (j != i) {
					temp += admittanceMatrix[k][j].getReal()
							* voltages[j].getReal();
					temp -= admittanceMatrix[k][j].getImag()
							* voltages[j].getImag();
				}
			}
			temp = -2 * temp * admittanceMatrix[k][i].getImag();
			temp += 2 * admittanceMatrix[k][i].getImag()
					* admittanceMatrix[k][i].getImag() * voltages[i].getImag();
			temp -= 2 * admittanceMatrix[k][i].getReal()
					* admittanceMatrix[k][i].getImag() * voltages[i].getReal();
			dIdFi = temp;
			temp = 0;
			for (int j = 0; j < 8; j++) {
				if (j != i) {
					temp += admittanceMatrix[k][j].getImag()
							* voltages[j].getReal();
					temp += admittanceMatrix[k][j].getReal()
							* voltages[j].getImag();
				}
			}
			temp = 2 * temp * admittanceMatrix[k][i].getReal();
			temp += 2 * admittanceMatrix[k][i].getReal()
					* admittanceMatrix[k][i].getReal() * voltages[i].getImag();
			temp += 2 * admittanceMatrix[k][i].getReal()
					* admittanceMatrix[k][i].getImag() * voltages[i].getReal();
			dIdFi += temp;

			if (absI == 0)
				absI = 0.001;

			if (i < 4) {
				tempJacobianRow[4 * 2 * sourceNode.getModelNumber() + 2 * i + 0] = dIdEi
						/ 2. / absI;
				tempJacobianRow[4 * 2 * sourceNode.getModelNumber() + 2 * i + 1] = dIdFi
						/ 2. / absI;
			} else {
				tempJacobianRow[4 * 2 * targetNode.getModelNumber() + 2
						* (i - 4) + 0] = dIdEi / 2. / absI;
				tempJacobianRow[4 * 2 * targetNode.getModelNumber() + 2
						* (i - 4) + 1] = dIdFi / 2. / absI;
			}

		}
		return tempJacobianRow;
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
				int myNodeNumber = 0;//sourceNode.getModelNumber();
				int remoteNodeNumber = 0;//destinationNode.getModelNumber();
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

				Complex[] myAdmittanceRow = tempElement.generateAdmittanceMatrix(this.network.nominalFrequency)[4*here+this.myPhase];
				
//				System.out.println("MyNode: " + myNodeNumber + ", RemoteNodeNumber: " + remoteNodeNumber);
//				System.out.println("Here: " + here + ", there: " + there);
				
				double I = this.myTheroreticalValue(expansionPoint);
				if (I != 0.0){
					Complex current = new Complex(0,0);
					for (int i=0;i<4;i++){
						current.add((new Complex(myAdmittanceRow[4*here+i])).multiply(expansionPoint[4*myNodeNumber+i]));
						current.add((new Complex(myAdmittanceRow[4*there+i])).multiply(expansionPoint[4*remoteNodeNumber+i]));
					}
					//System.out.println("I: " + I + ", |I|: " + current.getAbs());
					for (int i=0;i<4;i++){
						//g_ij,RX
						sensitivity += expansionPoint[4*myNodeNumber+i].getReal()*current.getReal()*dY[4*here+this.myPhase][4*here+i].getReal();
						sensitivity += expansionPoint[4*remoteNodeNumber+i].getReal()*current.getReal()*dY[4*here+this.myPhase][4*there+i].getReal();						
						sensitivity += expansionPoint[4*myNodeNumber+i].getImag()*current.getImag()*dY[4*here+this.myPhase][4*here+i].getReal();
						sensitivity += expansionPoint[4*remoteNodeNumber+i].getImag()*current.getImag()*dY[4*here+this.myPhase][4*there+i].getReal();
						
						//b_ij,RX
						sensitivity += -expansionPoint[4*myNodeNumber+i].getImag()*current.getReal()*dY[4*here+this.myPhase][4*here+i].getImag();
						sensitivity += -expansionPoint[4*remoteNodeNumber+i].getImag()*current.getReal()*dY[4*here+this.myPhase][4*there+i].getImag();
						sensitivity += expansionPoint[4*myNodeNumber+i].getReal()*current.getImag()*dY[4*here+this.myPhase][4*here+i].getImag();
						sensitivity += expansionPoint[4*remoteNodeNumber+i].getReal()*current.getImag()*dY[4*here+this.myPhase][4*there+i].getImag();
					}
					sensitivity = sensitivity/I;
				}

			}
		}
		if (element instanceof TuneableFourTerminalElement){
			if (this.myNode.myFourTerminalElements.contains(element)){
				// If it's a FourTerminalElement
				System.out.println("AbsoluteNodalPhaseCurrent doesn't support tuning of FourTerminalElements yet!");
			}
		}	
		return sensitivity;
	}

}
