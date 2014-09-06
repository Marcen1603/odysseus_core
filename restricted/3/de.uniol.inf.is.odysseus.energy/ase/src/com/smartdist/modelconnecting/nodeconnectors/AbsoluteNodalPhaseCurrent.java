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

public class AbsoluteNodalPhaseCurrent extends NodeConnector {
	private Node myNode = null;
	public double trueValue = 0;
	public int myPhase = 0; // A:0, B:1, C:2, N:3
	public FourWireNetwork network = null;

	public AbsoluteNodalPhaseCurrent(FourWireNetwork network, Node myNode,
			int myPhase, double trueValue) {
		this.myNode = myNode;
		this.myPhase = myPhase;
		this.trueValue = trueValue;
		this.network = network;
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {
		double absI = this.myTheroreticalValue(assumedVoltages);
		double[] myJacobianRow = new double[2 * 4 * network.getVertexCount()];
		Complex[] myAdmittanceRow = network.networkAdmittanceMatrix[4
				* this.myNode.getModelNumber() + this.myPhase];
		double tempDouble = 0;
		
//		double relativeTestStep = 0.01;
//		Collection<Node> nodes = network.getVertices();
//		Iterator<Node> nodeIterator = nodes.iterator();
//		Node tempNode = null;
//		int tempModelNumber = 0;
//		double tempNominalVoltage = 0;
//		for (;nodeIterator.hasNext();){
//			tempNode = nodeIterator.next();
//			tempModelNumber = tempNode.getModelNumber();
//			tempNominalVoltage = tempNode.nominalVoltage;
//			for (int i=0;i<4;i++){
//				assumedVoltages[4*tempModelNumber+i].add(new Complex(relativeTestStep*tempNominalVoltage,0));
//				myJacobianRow[2*4*tempModelNumber+2*i+0] = (this.myTheroreticalValue(assumedVoltages) - absI)/(relativeTestStep*tempNominalVoltage);
//				assumedVoltages[4*tempModelNumber+i].sustract(new Complex(relativeTestStep*tempNominalVoltage,-relativeTestStep*tempNominalVoltage));
//				myJacobianRow[2*4*tempModelNumber+2*i+1] = (this.myTheroreticalValue(assumedVoltages) - absI)/(relativeTestStep*tempNominalVoltage);
//				assumedVoltages[4*tempModelNumber+i].sustract(new Complex(0.0,relativeTestStep*tempNominalVoltage));
//			}
//		}
		
		
		if (absI>1e-4){
			for (int i=0;i<4*network.getVertexCount();i++){
				tempDouble = 0;
				for (int j=0;j<4*network.getVertexCount();j++){
					if (j!=i){
						tempDouble += myAdmittanceRow[j].getReal()*assumedVoltages[j].getReal()-myAdmittanceRow[j].getImag()*assumedVoltages[j].getImag();
					}
				}
				myJacobianRow[2*i+0] += myAdmittanceRow[i].getReal()*tempDouble;
				tempDouble = 0;
				for (int j=0;j<4*network.getVertexCount();j++){
					if (j!=i){
						tempDouble += myAdmittanceRow[j].getImag()*assumedVoltages[j].getReal()+myAdmittanceRow[j].getReal()*assumedVoltages[j].getImag();
					}
				}
				myJacobianRow[2*i+0] += myAdmittanceRow[i].getImag()*tempDouble;
				tempDouble = 0;
				myJacobianRow[2*i+0] += myAdmittanceRow[i].getReal()*myAdmittanceRow[i].getReal()*assumedVoltages[i].getReal();
				myJacobianRow[2*i+0] += myAdmittanceRow[i].getImag()*myAdmittanceRow[i].getImag()*assumedVoltages[i].getReal();
				myJacobianRow[2*i+0] = myJacobianRow[2*i+0]/absI;
			}
			for (int i=0;i<4*network.getVertexCount();i++){
				tempDouble = 0;
				for (int j=0;j<4*network.getVertexCount();j++){
					if (j!=i){
						tempDouble += myAdmittanceRow[j].getReal()*assumedVoltages[j].getReal()-myAdmittanceRow[j].getImag()*assumedVoltages[j].getImag();
					}
				}
				myJacobianRow[2*i+1] += -myAdmittanceRow[i].getImag()*tempDouble;
				tempDouble = 0;
				for (int j=0;j<4*network.getVertexCount();j++){
					if (j!=i){
						tempDouble += myAdmittanceRow[j].getImag()*assumedVoltages[j].getReal()+myAdmittanceRow[j].getReal()*assumedVoltages[j].getImag();
					}
				}
				myJacobianRow[2*i+1] += myAdmittanceRow[i].getReal()*tempDouble;
				tempDouble = 0;
				myJacobianRow[2*i+1] += myAdmittanceRow[i].getReal()*myAdmittanceRow[i].getReal()*assumedVoltages[i].getImag();
				myJacobianRow[2*i+1] += myAdmittanceRow[i].getImag()*myAdmittanceRow[i].getImag()*assumedVoltages[i].getImag();
				myJacobianRow[2*i+1] = myJacobianRow[2*i+1]/absI;
			}
		}
		
		
		
//		for (int i = 0; i < 4 * network.getVertexCount(); i++) {
//			// d|I|/dej
//			// First inner bracket
//			tempDouble = 0;
//			for (int j = 0; j < 4 * network.getVertexCount(); j++) {
//				if (!(j == i)) {
//					tempDouble += myAdmittanceRow[j].getReal()
//							* assumedVoltages[j].getReal();
//					tempDouble -= myAdmittanceRow[j].getImag()
//							* assumedVoltages[j].getImag();
//				}
//			}
//			tempDouble = 2 * tempDouble * myAdmittanceRow[i].getReal();
//			tempDouble += 2 * myAdmittanceRow[i].getReal()
//					* myAdmittanceRow[i].getReal()
//					* assumedVoltages[i].getReal();
//			tempDouble -= 2 * myAdmittanceRow[i].getReal()
//					* myAdmittanceRow[i].getImag()
//					* assumedVoltages[i].getImag();
//			myJacobianRow[2 * i + 0] = tempDouble;
//			// Second inner bracket
//			tempDouble = 0;
//			for (int j = 0; j < 4 * network.getVertexCount(); j++) {
//				if (!(j == i)) {
//					tempDouble += myAdmittanceRow[j].getImag()
//							* assumedVoltages[j].getReal();
//					tempDouble += myAdmittanceRow[j].getReal()
//							* assumedVoltages[j].getImag();
//				}
//			}
//			tempDouble = 2 * tempDouble * myAdmittanceRow[i].getImag();
//			tempDouble += 2 * myAdmittanceRow[i].getImag()
//					* myAdmittanceRow[i].getImag()
//					* assumedVoltages[i].getReal();
//			tempDouble += 2 * myAdmittanceRow[i].getReal()
//					* myAdmittanceRow[i].getImag()
//					* assumedVoltages[i].getImag();
//			myJacobianRow[2 * i + 0] += tempDouble;
//			// Outer derivative
//			if (absI != 0) {
//				myJacobianRow[2 * i + 0] = (myJacobianRow[2 * i + 0] / 2.)
//						/ absI;
//			} else {
//				// myJacobianRow[2*i+0] = 0;
//				myJacobianRow[2 * i + 0] = (myJacobianRow[2 * i + 0] / 2.) / 0.001;
//			}
//
//			// d|I|/dfj
//			// First inner bracket
//			tempDouble = 0;
//			for (int j = 0; j < 4 * network.getVertexCount(); j++) {
//				if (!(j == i)) {
//					tempDouble += myAdmittanceRow[j].getReal()
//							* assumedVoltages[j].getReal();
//					tempDouble -= myAdmittanceRow[j].getImag()
//							* assumedVoltages[j].getImag();
//				}
//			}
//			tempDouble = -2. * tempDouble * myAdmittanceRow[i].getImag();
//			tempDouble += 2 * myAdmittanceRow[i].getImag()
//					* myAdmittanceRow[i].getImag()
//					* assumedVoltages[i].getImag();
//			tempDouble -= 2 * myAdmittanceRow[i].getReal()
//					* myAdmittanceRow[i].getImag()
//					* assumedVoltages[i].getReal();
//			myJacobianRow[2 * i + 1] = tempDouble;
//			// Second inner bracket
//			tempDouble = 0;
//			for (int j = 0; j < 4 * network.getVertexCount(); j++) {
//				if (!(j == i)) {
//					tempDouble += myAdmittanceRow[j].getImag()
//							* assumedVoltages[j].getReal();
//					tempDouble += myAdmittanceRow[j].getReal()
//							* assumedVoltages[j].getImag();
//				}
//			}
//			tempDouble = 2 * tempDouble * myAdmittanceRow[i].getReal();
//			tempDouble += 2 * myAdmittanceRow[i].getReal()
//					* myAdmittanceRow[i].getReal()
//					* assumedVoltages[i].getImag();
//			tempDouble += 2 * myAdmittanceRow[i].getReal()
//					* myAdmittanceRow[i].getImag()
//					* assumedVoltages[i].getReal();
//			myJacobianRow[2 * i + 1] += tempDouble;
//			// Outer derivative
//			if (absI != 0) {
//				myJacobianRow[2 * i + 1] = (myJacobianRow[2 * i + 1] / 2.)
//						/ absI;
//			} else {
//				// myJacobianRow[2*i+1] = 0;
//				myJacobianRow[2 * i + 1] = (myJacobianRow[2 * i + 1] / 2.) / 0.001;
//			}
//		}
//
//		// System.out.print("I: " + absI);
//		// DevelopmentHelper.printToConsole(myJacobianRow);
		return myJacobianRow;
	}

	@Override
	public Node myNode() {
		return this.myNode;
	}

	@Override
	public double myTheroreticalValue(Complex[] assumedVoltages) {
		Complex[] myAdmittanceRow = network.networkAdmittanceMatrix[4
				* myNode.getModelNumber() + this.myPhase];
		Complex phaseCurrent = new Complex(0, 0);
		Complex tempComplex = null;
		for (int i = 0; i < assumedVoltages.length; i++) {
			tempComplex = myAdmittanceRow[i].clone();
			phaseCurrent.add(tempComplex.multiply(assumedVoltages[i]));
		}
		// System.out.println("I_th: " + phaseCurrent.getAbs());
		return phaseCurrent.getAbs();
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

				Complex[] myAdmittanceRow = this.network.networkAdmittanceMatrix[4*myNodeNumber+this.myPhase];
				
				double I = this.myTheroreticalValue(expansionPoint);
				if (I != 0.0){
					Complex current = new Complex(0,0);
					for (int i=0;i<4*this.network.getVertexCount();i++){
						current.add((new Complex(myAdmittanceRow[i])).multiply(expansionPoint[i]));
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
