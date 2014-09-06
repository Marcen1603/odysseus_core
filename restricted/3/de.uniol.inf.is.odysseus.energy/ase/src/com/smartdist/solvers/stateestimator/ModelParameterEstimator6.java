/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import java.util.Vector;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.TuneableEightTerminalElement;
import com.smartdist.util.DevelopmentHelper;
import com.smartdist.util.SVDResultSet;
import com.smartdist.util.SingularValueDecomposer;

public class ModelParameterEstimator6 {

//	public void tuneModel(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, EstimationResultSet expansionPoint, Inputs inputs, double[] meanValues){
//		
//		Vector<Integer> indexOfTuneable = new Vector<Integer>();
//		Vector<TuneableEightTerminalElement> elementOfTuneable = new Vector<TuneableEightTerminalElement>();
//		Vector<int[]> localIndices = new Vector<int[]>();
//		int overallIndex = 0;
//		for (int i=0;i<tuneables.length;i++){
//			double[] tempTuneables = tuneables[i].getTuneables();
//			int[] indexMatrix = new int[tempTuneables.length];
//			for (int j=0;j<tempTuneables.length;j++){
//				indexOfTuneable.add(j);
//				elementOfTuneable.add(tuneables[i]);
//				indexMatrix[j] = overallIndex++;
//			}
//			localIndices.add(indexMatrix);
//		}
//		int numberOfTuneables = indexOfTuneable.size();
//		
//		Observers testOutputs = new Observers();
//		int numberOfConnectors = inputs.numberOfConnectors();
//		for (int i=0;i<numberOfConnectors;i++){
//			testOutputs.addConnector(inputs.getConnector(i));
//		}
//		
//		double[] stdDevs = new double[numberOfConnectors];
//		for (int i=0;i<numberOfConnectors;i++){
//			stdDevs[i] = inputs.stdDev.elementAt(i);
//		}
//
//		int numberOfNodes = network.getVertexCount();
//		
//		///////////////////////
//		// dh(x)/dp sensitivity matrix H_p(x)
//		/////////////////////
//		double[][] hSensitivity = new double[numberOfConnectors][numberOfTuneables];
//		
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfConnectors;j++){
//				hSensitivity[j][i] = inputs.getConnector(j).getModelSensitivity(network, expansionPoint.voltages, elementOfTuneable.elementAt(i), indexOfTuneable.elementAt(i));
//			}
//		}
//		
////		System.out.println("h(x) parameter sensitivity");
////		DevelopmentHelper.printToConsole(hSensitivity);
//		
//		
//		/////////////////////////////////
//		// System Jacobian H(x)
//		////////////////////////////////
//		double[][] jacobian = new double[numberOfConnectors][4*2*numberOfNodes];
//		for (int i=0;i<numberOfConnectors;i++){
//			jacobian[i] = inputs.getConnector(i).myJacobianRow(expansionPoint.voltages);
//		}
//		
//			//////////////////////////
//			//
//			//    Relative dangerous step
//			//
//				for (int i=0;i<numberOfConnectors;i++){
//					for (int j=0;j<4*2*numberOfNodes;j++){
//						jacobian[i][j] = jacobian[i][j]/stdDevs[i];
//					}
//				}
//			//
//			//    hope all went well
//			//
//			//////////////////////
//		
////		System.out.println("system jacobian H(x)");
////		DevelopmentHelper.printToConsole(jacobian);
//
//		
//		///////////////////////////////
//		// Left range of H(x)   U_{1,\dots,r}\cdot U_{1,\dots,r}^\dagger
//		//////////////////////////////
//		SVDResultSet jacobianSVD = SingularValueDecomposer.singuglarValueDecomposition(jacobian);
//		double[][] leftRange = new double[numberOfConnectors][numberOfConnectors];
//		for (int r=0;r<jacobianSVD.rank;r++){
//			for (int i=0;i<numberOfConnectors;i++){
//				for (int k=0;k<numberOfConnectors;k++){
//					leftRange[i][k] += jacobianSVD.leftSingularVectors[i][r]*jacobianSVD.leftSingularVectors[k][r];
//				}
//			}
//		}
//		
////		System.out.println("Left range of H(x)");
////		DevelopmentHelper.printToConsole(leftRange);
//		
//		
//		//////////////////////////////
//		// residuals sensitivity S
//		//////////////////////////////
//		double[][] residualsSensitivity = new double[numberOfConnectors][numberOfConnectors];
//		for (int i=0;i<numberOfConnectors;i++){
//			for (int j=0;j<numberOfConnectors;j++){
//				residualsSensitivity[i][j] = - leftRange[i][j];
//			}
//		}
//		for (int i=0;i<numberOfConnectors;i++){
//			residualsSensitivity[i][i] += 1;
//		}
//		
//			//////////////////////
//			//
//			//   Dangerous step! Weighting
//			//
//			//////////////////////
//			
//				for (int i=0;i<numberOfConnectors;i++){
//					for (int j=0;j<numberOfConnectors;j++){
//						//residualsSensitivity[i][j] = residualsSensitivity[i][j]/stdDevs[i];
//					}
//				}
//				
//			///////////////////////
//			//
//			//   End dangerous step!
//			//
//			//////////////////////
//		
////		System.out.println("Residuals Sensitivity S");
////		DevelopmentHelper.printToConsole(residualsSensitivity);
//		
//
//		
//		////////////////////////////
//		// model error sensitivity
//		////////////////////////////
//		double[][] modelErrorSensitivity = new double[numberOfConnectors][numberOfTuneables];
//		for (int i=0;i<numberOfConnectors;i++){
//			for (int j=0;j<numberOfTuneables;j++){
//				for (int k=0;k<numberOfConnectors;k++){
//					modelErrorSensitivity[i][j] += residualsSensitivity[i][k]*hSensitivity[k][j];
//				}
//			}
//		}
//		
////		System.out.println("Model Error Sensitivities");
////		DevelopmentHelper.printToConsole(modelErrorSensitivity);
//		
//		
//		
//		////////////////////////////
//		// weighted model error sensitivity
//		///////////////////////////
//		double[][] weightedModelErrorSensitivity = new double[numberOfConnectors][numberOfTuneables];
//		for (int i=0;i<numberOfConnectors;i++){
//			for (int j=0;j<numberOfTuneables;j++){
//				weightedModelErrorSensitivity[i][j] = modelErrorSensitivity[i][j]/stdDevs[i]; 
//			}
//		}
//		
////		System.out.println("Weighted Error Model Sensitivity");
////		DevelopmentHelper.printToConsole(weightedModelErrorSensitivity);
//		
//		
//		
//		////////////////////////////
//		// pseudo inverse of correction term
//		////////////////////////////
//		SVDResultSet correctionSVD = SingularValueDecomposer.singuglarValueDecomposition(weightedModelErrorSensitivity);
//		double[][] pInv = SingularValueDecomposer.pinv(correctionSVD);
//		
////		System.out.println("pseudo inverse of correction term");
////		DevelopmentHelper.printToConsole(pInv);
//		
//		
//		
//		////////////////////////////
//		// weighted residuals
//		///////////////////////////
//		double[] weightedResiduals = new double[numberOfConnectors];
//		for (int i=0;i<numberOfConnectors;i++){
//			weightedResiduals[i] = meanValues[i]/stdDevs[i];   //  <------ DANGEROUS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		}
//		
////		System.out.println("weighted residuals");
////		DevelopmentHelper.printToConsole(weightedResiduals);
//		
//		
//		
//		
//		//////////////////////////
//		// estimated model error
//		/////////////////////////
//		double[] modelError = new double[numberOfTuneables];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfConnectors;j++){
//				modelError[i] += pInv[i][j]*weightedResiduals[j];
//			}
//		}
//		
////		System.out.println("Estimated Model Error");
////		DevelopmentHelper.printToConsole(modelError);
//		
//		
//		
//		
//		
//				
////		double[] updatedTuneables = new double[numberOfTuneables];
////		for (int i=0;i<numberOfTuneables;i++){
////			for (int j=0;j<numberOfConnectors;j++){
////				updatedTuneables[i] += 0;//pInv[i][j]*meanValues[j]/stdDevs[j];//*(lengthsCorrections[i]/lengthDeratedMeansVector);
////			}
////		}
//		
//		//System.out.print("previous lengths: ");
//		for (int i=0;i<tuneables.length;i++){
//			double[] localTuneables = new double[localIndices.elementAt(i).length];
//			for (int j=0;j<localTuneables.length;j++){
//				double update = modelError[localIndices.elementAt(i)[j]];//*tuneables[i].getSensitivityAnalysisStepSize()[j];
//				double oldvalue = elementOfTuneable.elementAt(i).getTuneables()[indexOfTuneable.elementAt(i)];
//				localTuneables[j] = oldvalue-update;
//			}
//			tuneables[i].setTunables(localTuneables);
//			//System.out.print(((LengthTuneableLine)tuneables[i]).length + " ");
//		}
//		//System.out.println();
//		network.generateAdmittanceMatrices();	
//	}

	
	
	
	public double[] determineModelError(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, EstimationResultSet expansionPoint, Inputs inputs, double[] meanValues){
		
		Vector<Integer> indexOfTuneable = new Vector<Integer>();
		Vector<TuneableEightTerminalElement> elementOfTuneable = new Vector<TuneableEightTerminalElement>();
		Vector<int[]> localIndices = new Vector<int[]>();
		int overallIndex = 0;
		for (int i=0;i<tuneables.length;i++){
			double[] tempTuneables = tuneables[i].getTuneables();
			int[] indexMatrix = new int[tempTuneables.length];
			for (int j=0;j<tempTuneables.length;j++){
				indexOfTuneable.add(j);
				elementOfTuneable.add(tuneables[i]);
				indexMatrix[j] = overallIndex++;
			}
			localIndices.add(indexMatrix);
		}
		int numberOfTuneables = indexOfTuneable.size();
		
		Observers testOutputs = new Observers();
		int numberOfConnectors = inputs.numberOfConnectors();
		for (int i=0;i<numberOfConnectors;i++){
			testOutputs.addConnector(inputs.getConnector(i));
		}
		
		double[] stdDevs = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			stdDevs[i] = inputs.stdDev.elementAt(i);
		}

		int numberOfNodes = network.getVertexCount();

		
		
		
		
		
		
		///////////////////////
		// dh(x)/dp sensitivity matrix H_p(x)
		/////////////////////
		double[][] hSensitivity = new double[numberOfConnectors][numberOfTuneables];
		
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				hSensitivity[j][i] = inputs.getConnector(j).getModelSensitivity(network, expansionPoint.voltages,
						elementOfTuneable.elementAt(i), indexOfTuneable.elementAt(i));
			}
		}
		
//		System.out.println("h(x) parameter sensitivity");
//		DevelopmentHelper.printToConsole(hSensitivity);
	
		
		Observers outputs = new Observers();
		for (int i=0;i<inputs.numberOfConnectors();i++){
			outputs.addConnector(inputs.getConnector(i));
		}
		outputs.calculateModelCoverages(expansionPoint);
//		for (int i=0;i<outputs.getConnectors().size();i++){
//			System.out.println("Connector " + i + " " + (outputs.modelCoverage.elementAt(i)<1.0 ? "observable" : "not observable"));
//		}

		
		
		
		
		
		
		/////////////////////////////////
		// System Jacobian H(x)
		////////////////////////////////
		double[][] jacobian = new double[numberOfConnectors][4*2*numberOfNodes];
		for (int i=0;i<numberOfConnectors;i++){
			jacobian[i] = inputs.getConnector(i).myJacobianRow(expansionPoint.voltages);
		}
		
		//////////////////////////
		//
		//    Relative dangerous step
		//
			for (int i=0;i<numberOfConnectors;i++){
				for (int j=0;j<4*2*numberOfNodes;j++){
					jacobian[i][j] = jacobian[i][j]/stdDevs[i];
				}
			}
		//
		//    hope all went well
		//
		//////////////////////

		
		
		
		
		
//		System.out.println("system jacobian H(x)");
//		DevelopmentHelper.printToConsole(jacobian);

		
		///////////////////////////////
		// Left range of H(x)   U_{1,\dots,r}\cdot U_{1,\dots,r}^\dagger
		//////////////////////////////
		SVDResultSet jacobianSVD = SingularValueDecomposer.singuglarValueDecomposition(jacobian);
		double[][] leftRange = new double[numberOfConnectors][numberOfConnectors];
		for (int r=0;r<jacobianSVD.rank;r++){
			for (int i=0;i<numberOfConnectors;i++){
				for (int k=0;k<numberOfConnectors;k++){
					leftRange[i][k] += jacobianSVD.leftSingularVectors[i][r]*jacobianSVD.leftSingularVectors[k][r];
				}
			}
		}
		
//		System.out.println("Left range of H(x)");
//		DevelopmentHelper.printToConsole(leftRange);
		
		
			//////////////////////////
			//
			//  DANGEROUS !!!
			//
//			for (int i=0;i<numberOfConnectors;i++){
//				for (int j=0;j<numberOfConnectors;j++){
//					//leftRange[i][j] = leftRange[i][j]*stdDevs[i]/stdDevs[j];
//				}
//			}
		
			//
			//  End
			//
			//////////////////////////
		
		
		
		//////////////////////////////
		// residuals sensitivity S
		//////////////////////////////
		double[][] residualsSensitivity = new double[numberOfConnectors][numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfConnectors;j++){
				residualsSensitivity[i][j] = - leftRange[i][j];
			}
		}
		for (int i=0;i<numberOfConnectors;i++){
			residualsSensitivity[i][i] += 1;
		}
		
		//////////////////////
		//
		//   Dangerous step! Weighting
		//
		//////////////////////
		
//			for (int i=0;i<numberOfConnectors;i++){
//				for (int j=0;j<numberOfConnectors;j++){
//					//residualsSensitivity[i][j] = residualsSensitivity[i][j];//stdDevs[i];
//				}
//			}
			
		///////////////////////
		//
		//   End dangerous step!
		//
		//////////////////////

		
		
		
		
		
//		System.out.println("Residuals Sensitivity S");
//		DevelopmentHelper.printToConsole(residualsSensitivity);
		

		
		////////////////////////////
		// model error sensitivity
		////////////////////////////
		double[][] modelErrorSensitivity = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfTuneables;j++){
				for (int k=0;k<numberOfConnectors;k++){
					modelErrorSensitivity[i][j] += residualsSensitivity[i][k]*hSensitivity[k][j]/stdDevs[k];
				}
			}
		}
		
//		System.out.println("Model Error Sensitivities");
//		DevelopmentHelper.printToConsole(modelErrorSensitivity);
		
		
		
		////////////////////////////
		// weighted model error sensitivity
		///////////////////////////
		double[][] weightedModelErrorSensitivity = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfTuneables;j++){
				weightedModelErrorSensitivity[i][j] = modelErrorSensitivity[i][j];//stdDevs[i];
			}
		}
		
//		System.out.println("Weighted Error Model Sensitivity");
//		DevelopmentHelper.printToConsole(weightedModelErrorSensitivity);
		
		
		
		////////////////////////////
		// pseudo inverse of correction term
		////////////////////////////
		SVDResultSet correctionSVD = SingularValueDecomposer.singuglarValueDecomposition(weightedModelErrorSensitivity);
		double[][] pInv = SingularValueDecomposer.pinv(correctionSVD);
		
//		System.out.println("pseudo inverse of correction term");
//		DevelopmentHelper.printToConsole(pInv);
		
		
		
		////////////////////////////
		// weighted residuals
		///////////////////////////
		double[] weightedResiduals = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			weightedResiduals[i] = meanValues[i]/stdDevs[i];		// <<<<<<<<<<<<<<<<<<<<< Dangerous Step !!!
		}
		
//		System.out.println("weighted residuals");
//		DevelopmentHelper.printToConsole(weightedResiduals);
		
		
		
		//////////////////////////
		// estimated model error
		/////////////////////////
		double[] modelError = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				if (outputs.modelCoverage.elementAt(j) < 1.0){
					modelError[i] += pInv[i][j]*weightedResiduals[j];
				}else{
					System.out.println("Connector " + j + " wasn't observable!");
				}
			}
		}
		
//		System.out.println("Estimated Model Error");
//		DevelopmentHelper.printToConsole(modelError);

		SVDResultSet hSensitivitySVD = SingularValueDecomposer.singuglarValueDecomposition(hSensitivity);
		double[][] hSensitivityPInv = SingularValueDecomposer.pinv(hSensitivitySVD);

//		System.out.println("hSensitivities pseudo-inverse");
//		DevelopmentHelper.printToConsole(hSensitivityPInv);

		
		double[][] hSensitivityInterdependencies = new double[numberOfTuneables][numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfTuneables;j++){
				for (int k=0;k<numberOfConnectors;k++){
					hSensitivityInterdependencies[i][j] += hSensitivityPInv[i][k]*hSensitivity[k][j];
				}
			}
		}
		
//		System.out.println("h-sensitivity interdependencies");
//		DevelopmentHelper.printToConsole(hSensitivityInterdependencies);
		
		SVDResultSet hSensitivityInterdependeciesSVD = SingularValueDecomposer.singuglarValueDecomposition(hSensitivityInterdependencies);
		@SuppressWarnings("unused")
		double[][] hSensitivityInterdependenciesPInv = SingularValueDecomposer.pinv(hSensitivityInterdependeciesSVD);
		
//		System.out.println("pseudo-inverse of h-sensitivity interdependencies");
//		DevelopmentHelper.printToConsole(hSensitivityInterdependenciesPInv);

		double[][] interdependencyCorrectionMatrix = new double[numberOfTuneables][numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfTuneables;j++){
				interdependencyCorrectionMatrix[i][j] = -hSensitivityInterdependencies[i][j];
			}
			interdependencyCorrectionMatrix[i][i] += 1;
		}
		
		SVDResultSet interdependecyCorrectionMatrixSVD = SingularValueDecomposer.singuglarValueDecomposition(interdependencyCorrectionMatrix);
		@SuppressWarnings("unused")
		double[][] interdependencyCorrectionMatrixPInv = SingularValueDecomposer.pinv(interdependecyCorrectionMatrixSVD);
		
		double[] weightedModelErrors = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfTuneables;j++){
				weightedModelErrors[i] += hSensitivityInterdependencies[i][j]*modelError[j];
			}
		}

		
		
		return modelError;
		//return weightedModelErrors;
	}
	
	
	
	
	
	public void tuneModel2(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, double[] modelError){		
		Vector<Integer> indexOfTuneable = new Vector<Integer>();
		Vector<TuneableEightTerminalElement> elementOfTuneable = new Vector<TuneableEightTerminalElement>();
		Vector<int[]> localIndices = new Vector<int[]>();
		int overallIndex = 0;
		for (int i=0;i<tuneables.length;i++){
			double[] tempTuneables = tuneables[i].getTuneables();
			int[] indexMatrix = new int[tempTuneables.length];
			for (int j=0;j<tempTuneables.length;j++){
				indexOfTuneable.add(j);
				elementOfTuneable.add(tuneables[i]);
				indexMatrix[j] = overallIndex++;
			}
			localIndices.add(indexMatrix);
		}
		@SuppressWarnings("unused")
		int numberOfTuneables = indexOfTuneable.size();
		for (int i=0;i<tuneables.length;i++){
			double[] localTuneables = new double[localIndices.elementAt(i).length];
			for (int j=0;j<localTuneables.length;j++){
				double update = modelError[localIndices.elementAt(i)[j]];//*tuneables[i].getSensitivityAnalysisStepSize()[j];
				double oldvalue = elementOfTuneable.elementAt(i).getTuneables()[indexOfTuneable.elementAt(i)];
				localTuneables[j] = oldvalue-update;
			}
			tuneables[i].setTunables(localTuneables);
			//System.out.print(((LengthTuneableLine)tuneables[i]).length + " ");
		}
		//System.out.println();
		network.generateAdmittanceMatrices();	
	}
	
	
	
	
	
	
	
	
	
	
	public void tuneModel(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, Inputs inputs, double[][] timeSeries, int iterations){
		int numberOfSamples = timeSeries[0].length;
		@SuppressWarnings("unused")
		double[][] deviations = new double[timeSeries.length][timeSeries[0].length];
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		EstimationResultSet result = null;
		
		double epsilon = 0.01; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 20; // maximum number of iterations
		//double singularValueThreshold = 1e-8; // minimum value for a singular value to be considered significant
		
		for (int iter=0;iter<iterations;iter++){
			boolean[] converged = new boolean[numberOfSamples];
			int numberOfConverged = 0;
			double[][] modelError = new double[numberOfSamples][8];/// <<<-------- Hier UNBEDINGT noch die Gesamtanzahl der tuneables einsetzen!!!
			double[] errors = new double[inputs.numberOfConnectors()];
			double[][] allErrors = new double[numberOfSamples][inputs.numberOfConnectors()];
			System.out.print("No. iterations: ");
			for (int i=0;i<numberOfSamples;i++){
				for (int j=0;j<inputs.numberOfConnectors();j++){
					inputs.getConnector(j).setMyTrueValue(timeSeries[j][i]);
				}
				result = estimator.estimateState(network, inputs, epsilon, maxIterations);
				if (result.usedIterrations<result.allowedMaxIterations){
					converged[i] = true;
					numberOfConverged++;
					for (int j=0;j<inputs.numberOfConnectors();j++){
						errors[j] = inputs.getConnector(j).myTrueValue() - inputs.getConnector(j).myTheroreticalValue(result.voltages);
						allErrors[i][j] = errors[j];
					}
					modelError[i] = this.determineModelError(network, tuneables, result, inputs, errors);
				}else{
					converged[i] = false;
				}
				
				////////////////
				// output
				//DevelopmentHelper.printEstimationResultToConsole(network, result);
//				for (int j=0;j<inputs.numberOfConnectors();j++){
//					deviations[j][i] = inputs.getConnector(j).myTrueValue() - inputs.getConnector(j).myTheroreticalValue(result.voltages);
//				}
//				
				System.out.print(result.usedIterrations + ", ");
			}
			double[] averageError = new double[inputs.numberOfConnectors()];
			double[] averageModelError = new double[modelError[0].length];
			for (int i=0;i<numberOfSamples;i++){
				if (converged[i]){//(true){//(converged[i]){
					for (int j=0;j<modelError[0].length;j++){
						averageModelError[j] += -modelError[i][j];
					}
					for (int j=0;j<inputs.numberOfConnectors();j++){
						averageError[j] += allErrors[i][j];
					}
				}
			}
			for (int i=0;i<modelError[0].length;i++){
				averageModelError[i] = averageModelError[i]/numberOfConverged;//numberOfSamples;//numberOfConverged;
			}
			for (int i=0;i<inputs.numberOfConnectors();i++){
				averageError[i] = averageError[i]/numberOfConverged;
			}
			
			//this.tuneModel2(network, tuneables, this.determineModelError(network, tuneables, result, inputs, averageError));
			
			DevelopmentHelper.printToConsole(averageModelError);
			this.tuneModel2(network, tuneables, averageModelError);
			
			
			
			
			
			//			System.out.println();
//			//DevelopmentHelper.printToConsole(deviations);
//			double[] meanValues = new double[inputs.numberOfConnectors()];
//			for (int i=0;i<inputs.numberOfConnectors();i++){
//				for (int j=0;j<numberOfSamples;j++){
//					if (converged[j]){
//						meanValues[i] += -deviations[i][j];
//					}
//				}
//				meanValues[i] = meanValues[i]/numberOfConverged;//numberOfSamples;
////				if ((inputs.getConnector(i).wantsDelayedStart())&&(iterations<5)){
////					meanValues[i]=0;
////				}
//			}
//			
////			for (int l=0;l<tuneables.length;l++){
////				System.out.print(" line " + l + ": " + ((LengthTuneableLine)tuneables[l]).length);
////			}
////			System.out.println();
//			DevelopmentHelper.printToConsole(meanValues);
//			
//			double averageMean = 0.0;
//			for (int i=0;i<inputs.numberOfConnectors();i++){
//				averageMean += meanValues[i]*meanValues[i];
//			}
//			averageMean = Math.sqrt(averageMean);
//			//System.out.println(" -- average mean: " + averageMean);
//			
//			////////////////////////////////////////////////
//			EstimationResultSet expansionPoint = result;  // TO BE THOUGHT ABOUT !!!
//			////////////////////////////////////////////////
//			
//			this.tuneModel(network, tuneables, expansionPoint, inputs, meanValues);
		}
	}
}
