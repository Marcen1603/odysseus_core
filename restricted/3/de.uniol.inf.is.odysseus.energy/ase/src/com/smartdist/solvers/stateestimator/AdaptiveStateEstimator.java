/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import java.util.Iterator;
import java.util.Vector;

import com.smartdist.modelconnecting.ModelConnector;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.util.Complex;
import com.smartdist.util.LAPACK;

public class AdaptiveStateEstimator {

	public EstimationResultSet estimateState(FourWireNetwork network,
			Inputs connectorContainer, double epsilon, int maxIterations) {

		boolean converged = false;
		int iterationCounter = 0;

		network.generateAdmittanceMatrices();

		/////////////////////////////
		// Generate initial voltage Vector
		Complex[] assumedVoltages = new Complex[4 * network.getVertexCount()];
		Iterator<Node> nodeIterator = network.getVertices().iterator();
		for (; nodeIterator.hasNext();) {
			Node tempNode = nodeIterator.next();
			Complex[] tempComplex = tempNode.getStartVoltage(
					tempNode.nominalVoltage, 0);
			assumedVoltages[4 * tempNode.getModelNumber()
					+ FourWireNetwork.phaseA] = tempComplex[0];
			assumedVoltages[4 * tempNode.getModelNumber()
					+ FourWireNetwork.phaseB] = tempComplex[1];
			assumedVoltages[4 * tempNode.getModelNumber()
					+ FourWireNetwork.phaseC] = tempComplex[2];
			assumedVoltages[4 * tempNode.getModelNumber()
					+ FourWireNetwork.phaseN] = tempComplex[3];
		}
		EstimationResultSet result = new EstimationResultSet();
		result.voltages = assumedVoltages;
		result.network = network;
		double[] mismatch = null;
		double[] previousMismatch = null;
		double change = 0;
		//double machineEPS = AdaptiveStateEstimator.calculateMachineEpsilonDouble();
		
		//////////////////////
		// parametrize fade-in 
		////////////////////
		int delayedFadeStartIteration = 1;//(int)java.lang.Math.round(0.1*maxIterations);
		int delayedFadeStopIteration = 4;//(int)java.lang.Math.round(0.2*maxIterations);
		int fadeInStopIteration = 3;//(int)java.lang.Math.round(0.15*maxIterations);
		//System.out.println("delayed fade start: " + delayedFadeStartIteration + ", delayed fade stop: " + delayedFadeStopIteration + ", fade in stop: " + fadeInStopIteration);
		double fadeInFactor = 0;
		double delayedFadeInFactor = 0;
		boolean delayedStarted = false;
		boolean delayedStopped = false;
		boolean fadeStopped = false;
		
		///////////////////////////
		// Main iterative loop
		/////////////////////////
		do {
			iterationCounter++;
			this.generateJacobianMatrix(network, connectorContainer, result.voltages, result);
			
			///////////////////////////
			// Determine fade factors
			/////////////////////////
			if (iterationCounter < fadeInStopIteration){
				fadeInFactor = 0.5*(1-java.lang.Math.cos(java.lang.Math.PI*(iterationCounter+0.5)/(fadeInStopIteration+1)));
				fadeStopped = false;
			}else{
				fadeStopped = true;
				fadeInFactor = 1.0;
			}
			if (iterationCounter >= delayedFadeStartIteration){
				if (iterationCounter <= delayedFadeStopIteration){
					delayedFadeInFactor = 0.5-0.5*java.lang.Math.cos(java.lang.Math.PI*(iterationCounter-delayedFadeStartIteration+0.5)/(delayedFadeStopIteration-delayedFadeStartIteration+1));
					delayedStarted = true;
					delayedStopped = false;
				}else{
					delayedFadeInFactor = 1.0;
					delayedStarted = true;
					delayedStopped = true;
				}
			}
			//System.out.println("Iteration: " + iterationCounter + " , fade in factor: " + fadeInFactor + " , delayed fade in factor: " + delayedFadeInFactor);
			
			///////////////////////
			// Apply fade factors to Jacobian 
			/////////////////////
			
			for (int i=0;i<connectorContainer.numberOfConnectors();i++){
				if ((connectorContainer.connectors.elementAt(i).wantsFadeIn())&&(!connectorContainer.connectors.elementAt(i).wantsDelayedStart())){
					if (!fadeStopped){
						for (int j=0;j<4*2*network.getVertexCount();j++){
							result.jacobian[i][j] = result.jacobian[i][j]/fadeInFactor;
						}
					}
				}
				if ((connectorContainer.connectors.elementAt(i).wantsFadeIn())&&(connectorContainer.connectors.elementAt(i).wantsDelayedStart())){
					if (delayedStarted){
						if (!delayedStopped){
							for (int j=0;j<4*2*network.getVertexCount();j++){
								result.jacobian[i][j] = result.jacobian[i][j]/delayedFadeInFactor;
							}
						}
					}else{
						for (int j=0;j<4*2*network.getVertexCount();j++){
							result.jacobian[i][j] = 0;
						}
					}
				}
				if ((connectorContainer.connectors.elementAt(i).wantsDelayedStart())&&(!connectorContainer.connectors.elementAt(i).wantsFadeIn())){
					if (!delayedStarted){
						for (int j=0;j<4*2*network.getVertexCount();j++){
							result.jacobian[i][j] = 0;
						}
					}
				}
			}
			
			this.updateJacobianAnalysis(result);
			previousMismatch = mismatch;
			mismatch = this.getMismatch(network, result,
					connectorContainer);
			//this.determineRank(result, singularValueThreshold);
			this.determineRank2(result);
			this.getPseudoInverse(result);
			Complex[] improvedVoltages = this.getImprovedVoltages(result,
					mismatch, result.pinv);
			change = 0;
			if (iterationCounter > 1){
				for (int i=0;i<mismatch.length;i++) change += (mismatch[i]-previousMismatch[i]);
				if (Math.abs(change) < epsilon) converged = true;
			}
			if (!converged) {
				result.voltages = improvedVoltages;
			}
		} while ((!converged) && (iterationCounter < maxIterations));

		result.usedIterrations = iterationCounter;
		result.allowedMaxIterations = maxIterations;
		return result;
	}

	
	
	
	@SuppressWarnings("unused")
	private void determineRank(EstimationResultSet result, double singularValueThreshold){
		int rank = 0;
		for (int i = 0; i < result.singularValues.length; i++) {
			if (result.singularValues[i] > singularValueThreshold)
				rank++;
		}
		result.rank = rank;
	}

	private void determineRank2(EstimationResultSet result){
		int maxSize = (result.jacobian.length > result.jacobian[0].length ? result.jacobian.length : result.jacobian[0].length);
		double largestSV = 0.0;
		for (int i=0;i<result.singularValues.length;i++){
			if (result.singularValues[i]>largestSV){
				largestSV = result.singularValues[i];
			}
		}
		double singularValueThreshold = maxSize*AdaptiveStateEstimator.calculateMachineEpsilonDouble(largestSV);
		int rank = 0;
		for (int i = 0; i < result.singularValues.length; i++) {
			if (result.singularValues[i] > singularValueThreshold)
				rank++;
		}
		result.rank = rank;
	}
	
	private void generateJacobianMatrix(FourWireNetwork network,
			Inputs connectorContainer, Complex[] voltage, EstimationResultSet result) {
		// if necessary, generate Jacobian matrix
		if (result.jacobian == null) result.jacobian = new double[connectorContainer.numberOfConnectors()][8*network.getVertexCount()];
		for (int i=0;i<connectorContainer.numberOfConnectors();i++){
			Vector<ModelConnector> connectors = connectorContainer
					.getWeighedInputs();
			result.jacobian[i] = connectors.elementAt(i).myJacobianRow(voltage);
		}
	}
	
	private void updateJacobianAnalysis(EstimationResultSet result) {
		// Get the SVD of Jacobian
		int rowsJ = result.jacobian.length;
		int colsJ = result.jacobian[0].length;
		if (result.singularValues == null) result.singularValues = new double[Math.max(rowsJ, colsJ)];
		if (result.leftSingularVectors == null) result.leftSingularVectors = new double[rowsJ][rowsJ];
		if (result.rightSingularVectors == null) result.rightSingularVectors = new double[colsJ][colsJ];
		LAPACK.DGESVD_full(result.jacobian, result.singularValues, result.leftSingularVectors, result.rightSingularVectors);
		//LAPACK.DGESDD_full(result.jacobian, result.singularValues, result.leftSingularVectors, result.rightSingularVectors);
	}

	private double[] getMismatch(FourWireNetwork network,
			EstimationResultSet result, Inputs connectorContainer) {
		double[] mismatch = new double[connectorContainer.numberOfConnectors()];
		// Node Connectors
		Vector<ModelConnector> connectors = connectorContainer
				.getWeighedInputs();
		for (int i = 0; i < connectors.size(); i++) {
			mismatch[i] = connectors.elementAt(i).myTheroreticalValue(
					result.voltages)
					- connectors.elementAt(i).myTrueValue();
		}
		return mismatch;
	}

	private void getPseudoInverse(EstimationResultSet result) {
		if (result.pinv == null) result.pinv = new double[result.jacobian[0].length][result.jacobian.length];
		if (result.rank == 0) this.determineRank2(result);
		for (int pinvx = 0; pinvx < result.jacobian[0].length; pinvx++) {
			for (int pinvy = 0; pinvy < result.jacobian.length; pinvy++) {
				result.pinv[pinvx][pinvy] = 0;
				for (int s = 0; s < result.rank; s++) {
					result.pinv[pinvx][pinvy] += result.rightSingularVectors[pinvx][s]
							* result.leftSingularVectors[pinvy][s]
							/ result.singularValues[s];
				}
			}
		}
	}
	
	private Complex[] getImprovedVoltages(EstimationResultSet result,
			double[] mismatch, double[][] pinv) {
		double iterationDamping = 1;
		double[] realValuedImprovement = new double[pinv.length];
		Complex[] improvedVoltages = new Complex[result.voltages.length];
		for (int i = 0; i < improvedVoltages.length; i++) {
			improvedVoltages[i] = result.voltages[i].clone();
		}

		for (int i = 0; i < pinv.length; i++) {
			realValuedImprovement[i] = 0;
			for (int j = 0; j < pinv[0].length; j++) {
				realValuedImprovement[i] += pinv[i][j] * mismatch[j];
			}
		}
		for (int i = 0; i < improvedVoltages.length; i++) {
			improvedVoltages[i].sustract(new Complex(iterationDamping
					* realValuedImprovement[2 * i + 0], iterationDamping
					* realValuedImprovement[2 * i + 1]));
		}
		return improvedVoltages;
	}
	
    @SuppressWarnings("unused")
	private static double calculateMachineEpsilonDouble() {
        double machEps = 1.0d;
 
        do
           machEps /= 2.0d;
        while (1.0 + (machEps / 2.0) != 1.0);
 
        return machEps;
    }
    private static double calculateMachineEpsilonDouble(double center) {
        double machEps = 1.0d;
 
        do
           machEps /= 2.0d;
        while (center + (machEps / 2.0) != center);
 
        return machEps;
    }

}
