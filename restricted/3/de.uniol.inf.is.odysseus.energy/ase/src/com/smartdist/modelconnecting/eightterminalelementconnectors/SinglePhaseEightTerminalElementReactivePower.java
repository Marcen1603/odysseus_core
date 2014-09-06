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

public class SinglePhaseEightTerminalElementReactivePower extends
EightTerminalElementConnector {

	double myTrueValue = 0;
	FourWireNetwork myNetwork = null;
	EightTerminalElement myElement = null;
	Node myNode = null;
	int myPhase = 0;
	int myCounterPhase = 0;
	
	public SinglePhaseEightTerminalElementReactivePower(FourWireNetwork network, EightTerminalElement element, Node node, int phase, int counterPhase, double value){
		super();
		this.myNetwork = network;
		this.myElement = element;
		this.myNode = node;
		this.myPhase = phase;
		this.myCounterPhase = counterPhase;
		this.myTrueValue = value;
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
		Node startNode = this.myNode;
		Node endNode = this.myNetwork.getOpposite(startNode, this.myElement);
		@SuppressWarnings("unused")
		boolean forward = false;
		Complex[][] admittanceMatrix = this.myElement.generateAdmittanceMatrix(this.myNetwork.nominalFrequency);
		int startNodeIndex = startNode.getModelNumber();
		int endNodeIndex = endNode.getModelNumber();
		int localStart = 0;
		int localEnd = 0;
		
		if (startNode == this.myNetwork.getSource(this.myElement)){
			forward = true;
			localStart = 0;
			localEnd = 1;
		}else{
			forward = false;
			localStart = 1;
			localEnd = 0;
		}
		
		Complex tempCurrent = new Complex(0,0);
		for (int i=0;i<4;i++){
			tempCurrent.add(new Complex(admittanceMatrix[4*localStart+this.myPhase][4*localStart+i]).multiply(assumedVoltages[4*startNodeIndex+i]));
			tempCurrent.add(new Complex(admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i]).multiply(assumedVoltages[4*endNodeIndex+i]));
		}
		Complex apparentPower = new Complex(assumedVoltages[4*startNodeIndex+this.myPhase]).sustract(assumedVoltages[4*startNodeIndex+this.myCounterPhase]).multiply(new Complex(tempCurrent).conjugate());
		
		return apparentPower.getImag();
	}

	@Override
	public double[] myJacobianRow(Complex[] assumedVoltages) {
		// TODO Auto-generated method stub
		Node startNode = this.myNode;
		Node endNode = this.myNetwork.getOpposite(startNode, this.myElement);
		@SuppressWarnings("unused")
		boolean forward = false;
		Complex[][] admittanceMatrix = this.myElement.generateAdmittanceMatrix(this.myNetwork.nominalFrequency);
		int startNodeIndex = startNode.getModelNumber();
		int endNodeIndex = endNode.getModelNumber();
		int localStart = 0;
		int localEnd = 0;
		
		if (startNode == this.myNetwork.getSource(this.myElement)){
			forward = true;
			localStart = 0;
			localEnd = 1;
		}else{
			forward = false;
			localStart = 1;
			localEnd = 0;
		}
		double[] jacobianRow = new double[2*4*this.myNetwork.getVertexCount()];
		
		
		for (int i=0;i<4;i++){
			// dQ_12/de_1,X
			jacobianRow[2*4*startNodeIndex+2*i+0] += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
			jacobianRow[2*4*startNodeIndex+2*i+0] += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
			jacobianRow[2*4*startNodeIndex+2*i+0] += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
			jacobianRow[2*4*startNodeIndex+2*i+0] += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();
			
			// dQ_12/de_2,X
			jacobianRow[2*4*endNodeIndex+2*i+0] += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
			jacobianRow[2*4*endNodeIndex+2*i+0] += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
			jacobianRow[2*4*endNodeIndex+2*i+0] += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
			jacobianRow[2*4*endNodeIndex+2*i+0] += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();

			// dQ_12/df_1,X
			jacobianRow[2*4*startNodeIndex+2*i+1] += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
			jacobianRow[2*4*startNodeIndex+2*i+1] += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
			jacobianRow[2*4*startNodeIndex+2*i+1] += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
			jacobianRow[2*4*startNodeIndex+2*i+1] += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();
			
			// dP_12/df_2,X
			jacobianRow[2*4*endNodeIndex+2*i+1] += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
			jacobianRow[2*4*endNodeIndex+2*i+1] += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
			jacobianRow[2*4*endNodeIndex+2*i+1] += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
			jacobianRow[2*4*endNodeIndex+2*i+1] += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();
			
		}
		
		double temp = 0;
		// dQ_12/de_1,R
		for (int i=0;i<4;i++){
			if (i!=this.myPhase){
				temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+i].getReal();
				temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+i].getImag();
			}
		}
		for (int i=0;i<4;i++){
			temp += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*endNodeIndex+i].getReal();
			temp += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*endNodeIndex+i].getImag();
		}
		temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myPhase].getReal()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
		temp += +admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();
		temp += -2*admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
		jacobianRow[2*4*startNodeIndex+2*this.myPhase+0] = temp;
		
		temp = 0;
		// dQ_12/df_1,R
		for (int i=0;i<4;i++){
			if (i!=this.myPhase){
				temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+i].getReal();
				temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+i].getImag();
			}
		}
		for (int i=0;i<4;i++){
			temp += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*endNodeIndex+i].getReal();
			temp += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*endNodeIndex+i].getImag();
		}
		temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
		temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myPhase].getReal()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();
		temp += -2*admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
		jacobianRow[2*4*startNodeIndex+2*this.myPhase+1] = temp;

		
		temp = 0;
		// dQ_12/de_1,S
		for (int i=0;i<4;i++){
			if (i!=this.myCounterPhase){
				temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+i].getReal();
				temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+i].getImag();
			}
		}
		for (int i=0;i<4;i++){
			temp += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*endNodeIndex+i].getReal();
			temp += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*endNodeIndex+i].getImag();
		}
		temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myCounterPhase].getReal()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
		temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myCounterPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
		temp += 2*admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myCounterPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getReal();
		jacobianRow[2*4*startNodeIndex+2*this.myCounterPhase+0] = temp;
		
		temp = 0;
		// dP_12/df_1,S
		for (int i=0;i<4;i++){
			if (i!=this.myCounterPhase){
				temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getReal()*assumedVoltages[4*startNodeIndex+i].getReal();
				temp += admittanceMatrix[4*localStart+this.myPhase][4*localStart+i].getImag()*assumedVoltages[4*startNodeIndex+i].getImag();
			}
		}
		for (int i=0;i<4;i++){
			temp += -admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getReal()*assumedVoltages[4*endNodeIndex+i].getReal();
			temp += admittanceMatrix[4*localStart+this.myPhase][4*localEnd+i].getImag()*assumedVoltages[4*endNodeIndex+i].getImag();
		}
		temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myCounterPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myPhase].getImag();
		temp += -admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myCounterPhase].getReal()*assumedVoltages[4*startNodeIndex+this.myPhase].getReal();
		temp += 2*admittanceMatrix[4*localStart+this.myPhase][4*localStart+this.myCounterPhase].getImag()*assumedVoltages[4*startNodeIndex+this.myCounterPhase].getImag();
		jacobianRow[2*4*startNodeIndex+2*this.myCounterPhase+1] = temp;

		//DevelopmentHelper.printToConsole(admittanceMatrix);
		return jacobianRow;
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

				for (int p=0;p<4;p++){
					//g_ik,RY
					sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getImag())
							*expansionPoint[4*myNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*here+p].getReal();
					sensitivity += -(expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getReal())
							*expansionPoint[4*myNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*here+p].getReal();
					sensitivity += (expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getImag())
							*expansionPoint[4*remoteNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*there+p].getReal();
					sensitivity += -(expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getReal())
							*expansionPoint[4*remoteNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*there+p].getReal();
					
					//b_ik,RY
					sensitivity += -(expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getImag())
							*expansionPoint[4*myNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*here+p].getImag();
					sensitivity += -(expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getReal())
							*expansionPoint[4*myNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*here+p].getImag();
					sensitivity += -(expansionPoint[4*myNodeNumber+this.myPhase].getImag()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getImag())
							*expansionPoint[4*remoteNodeNumber+p].getImag()*dY[4*here+this.myPhase][4*there+p].getImag();
					sensitivity += -(expansionPoint[4*myNodeNumber+this.myPhase].getReal()-expansionPoint[4*myNodeNumber+this.myCounterPhase].getReal())
							*expansionPoint[4*remoteNodeNumber+p].getReal()*dY[4*here+this.myPhase][4*there+p].getImag();
					
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
