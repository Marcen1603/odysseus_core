/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.util;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.solvers.stateestimator.EstimationResultSet;

public class DevelopmentHelper {

	static public void printToConsole(Complex[][] matrix) {
		if (matrix != null) {
			DecimalFormat format = new DecimalFormat("0.##E0");
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					if (matrix[i][j].getImag() < 0) {
						System.out.print(format.format(matrix[i][j].getReal())
								+ "" + format.format(matrix[i][j].getImag())
								+ "i \t");
					} else {
						System.out.print(format.format(matrix[i][j].getReal())
								+ "+" + format.format(matrix[i][j].getImag())
								+ "i \t");
					}
				}
				System.out.println();
			}
		}
	}

	static public void printToConsole(Complex[] vector) {
		if (vector != null) {
			DecimalFormat format = new DecimalFormat("0.##E0");
			for (int j = 0; j < vector.length; j++) {
				if (vector[j].getImag() < 0) {
					System.out.println(format.format(vector[j].getReal()) + " "
							+ format.format(vector[j].getImag()) + "i \t");
				} else {
					System.out.println(format.format(vector[j].getReal()) + "+"
							+ format.format(vector[j].getImag()) + "i \t");
				}
			}
			System.out.println();
		}
	}

	static public void printToConsole2(Complex[] vector) {
		if (vector != null) {
			DecimalFormat format = new DecimalFormat("0.##E0");
			for (int j = 0; j < vector.length; j++) {
				System.out.println(format.format(vector[j].getAbs()) + "<"
						+ (vector[j].getArg() / java.lang.Math.PI * 180)
						+ "deg \t");
			}
			System.out.println();
		}
	}

	static public void printToConsole(double[][] matrix) {
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				DevelopmentHelper.printToConsole(matrix[i]);
			}
		}
	}

	static public void printToConsole(double[] vector) {
		if (vector != null) {
			DecimalFormat format = new DecimalFormat("0.##E0");
			for (int j = 0; j < vector.length; j++) {
				System.out.print(format.format(vector[j]) + "\t ");
			}
			System.out.println();
		}
	}

	static public void printEstimationResultToConsole(FourWireNetwork network,
			EstimationResultSet result) {
		DecimalFormat format = new DecimalFormat("##0.000");
		DecimalFormat format2 = new DecimalFormat("##0.0");
		String[] output = new String[4 * network.getVertexCount()];
		Collection<Node> nodes = network.getVertices();
		Iterator<Node> iterator = nodes.iterator();
		Node tempNode = null;
		for (; iterator.hasNext();) {
			tempNode = iterator.next();
			// phase A
			output[4 * tempNode.getModelNumber() + FourWireNetwork.phaseA] = "Node "
					+ (tempNode.getModelNumber() + 1)
					+ " - phase A: "
					+ format.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseA].getAbs())
					+ "V < "
					+ format2.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseA].getArg()
							/ Math.PI * 180)
					+ "deg";
			/*\t - abs coverage: "
					+ format2.format(result.absCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseA])
					+ "deg ,arg coverage: "
					+ format2.format(result.argCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseA])
					+ "deg \t - principal angle 1: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseA][0])
					+ "deg ,principal angle 2: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseA][1]) + "deg";
*/
			// phase B
			output[4 * tempNode.getModelNumber() + FourWireNetwork.phaseB] = "Node "
					+ (tempNode.getModelNumber() + 1)
					+ " - phase B: "
					+ format.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseB].getAbs())
					+ "V < "
					+ format2.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseB].getArg()
							/ Math.PI * 180)
					+ "deg";
			/*\t - abs coverage: "
					+ format2.format(result.absCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseB])
					+ "deg ,arg coverage: "
					+ format2.format(result.argCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseB])
					+ "deg \t - principal angle 1: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseB][0])
					+ "deg ,principal angle 2: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseB][1]) + "deg";
*/
			// phase C
			output[4 * tempNode.getModelNumber() + FourWireNetwork.phaseC] = "Node "
					+ (tempNode.getModelNumber() + 1)
					+ " - phase C: "
					+ format.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseC].getAbs())
					+ "V < "
					+ format2.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseC].getArg()
							/ Math.PI * 180)
					+ "deg";
			/*\t - abs coverage: "
					+ format2.format(result.absCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseC])
					+ "deg ,arg coverage: "
					+ format2.format(result.argCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseC])
					+ "deg \t - principal angle 1: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseC][0])
					+ "deg ,principal angle 2: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseC][1]) + "deg";
*/
			// phase N
			output[4 * tempNode.getModelNumber() + FourWireNetwork.phaseN] = "Node "
					+ (tempNode.getModelNumber() + 1)
					+ " - phase N: "
					+ format.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseN].getAbs())
					+ "V < "
					+ format2.format(result.voltages[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseN].getArg()
							/ Math.PI * 180)
					+ "deg";
			/*\t - abs coverage: "
					+ format2.format(result.absCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseN])
					+ "deg ,arg coverage: "
					+ format2.format(result.argCoverage[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseN])
					+ "deg \t - principal angle 1: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseN][0])
					+ "deg ,principal angle 2: "
					+ format2.format(result.principalAngles[4
							* tempNode.getModelNumber()
							+ FourWireNetwork.phaseN][1]) + "deg";
							*/
		}
		for (int i = 0; i < output.length; i++) {
			System.out.println(output[i]);
		}
	}

}
