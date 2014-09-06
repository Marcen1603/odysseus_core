/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import java.util.Iterator;

import org.netlib.lapack.DGETRF;
import org.netlib.lapack.DGETRI;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.util.Complex;

public class TraditionalStateEstimator {

	public Complex[] estimateState(FourWireNetwork network,
			Inputs connectorContainer, double epsilon, int maxIterations) {
		
		/**
		 * This method implements the traditional Least-Mean-Square-Error State Estimation published by Schweppe in 
		 * [1]	F. C. Schweppe and D. B. Rom, "Power System Static-State Estimation, Part II: Approximate Model," Power Apparatus and Systems, IEEE Transactions on, no. 1, pp. 125-130, 1970.
		 * [2]	F. C. Schweppe and J. Wildes, "Power System Static-State Estimation, Part I: Exact Model," Power Apparatus and Systems, IEEE Transactions on, no. 1, pp. 120-125, 1970.
		 * [3]	F. C. Schweppe, "Power System Static-State Estimation, Part III: Implementation," Power Apparatus and Systems, IEEE Transactions on, no. 1, pp. 130-135, 1970.
		 * 
		 * Comments and nomenclature base on
		 * [4]	O. Krause and S. Lehnhoff, "Generalized static-state estimation," presented at the Universities Power Engineering Conference (AUPEC), 2012 22nd Australasian.
		 */
		
		boolean converged = false;
		int iteration = 0;
		
		//////////////////////////////////////////////////////
		// Having the network preparing the scene by generating the overall admittance matrix and enumerate all nodes.
		// Has to be done before start vector can be generated.
		////////////////////////////////////////////////////
		
		network.generateAdmittanceMatrices();
		
		//////////////////////////////////////////////////////
		// Generate start voltage vector, assuming all voltages are at their nominal voltage with respective 0, 120, -120 degree angle.
		////////////////////////////////////////////////////
		
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
		
		//////////////////////////////////////////////////////
		/////////////////////////////////////////////////////
		//
		//	Entering the iterative loop as outlined in equation (16) in [4]
		//
		/////////////////////////////////////////////////
		////////////////////////////////////////////////
		
		do {
			
			//////////////////////////////////////////////////////
			// Matrix H(x) containing the Jacobian row vectors of the used input variables. Equation (11) in [4]
			////////////////////////////////////////////////////
			
			double[][] H = new double[connectorContainer.connectors.size()][4*2*network.getVertexCount()]; 
			for (int i=0;i<connectorContainer.connectors.size();i++){
				H[i] = connectorContainer.getConnector(i).myJacobianRow(assumedVoltages);
			}
			
			
			//////////////////////////////////////////////////////
			// Matrix R containing the standard deviations for all used input variables. Equation (8) in [4]
			// variable R will be reused for the inverse R^-1 
			////////////////////////////////////////////////////
			
			double[][] R = new double[connectorContainer.connectors.size()][connectorContainer.connectors.size()];
			for (int i=0;i<connectorContainer.connectors.size();i++){
				R[i][i] = connectorContainer.stdDev.elementAt(i);
			}
			
			
			//////////////////////////////////////////////////////
			// Inversion of R
			////////////////////////////////////////////////////
			/*
			 *       SUBROUTINE DGETRI( N, A, LDA, IPIV, WORK, LWORK, INFO )
			 *
				*  -- LAPACK routine (version 3.2) --
				*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
				*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
				*     November 2006
				*
				*  Purpose
				*  =======
				*
				*  DGETRI computes the inverse of a matrix using the LU factorization
				*  computed by DGETRF.
				
				*  =====================================================================
			 */
			int[] IPIV = new int[R.length];
			double[] WORK = new double[R.length*R.length];
			org.netlib.util.intW info = new org.netlib.util.intW(2);
			DGETRF.DGETRF(R.length, IPIV.length, R, IPIV, info);
			DGETRI.DGETRI(R.length, R, IPIV, WORK, WORK.length, info);//( N, A, LDA, IPIV, WORK, LWORK, INFO )

			
			//////////////////////////////////////////////////////
			// Calculation of e(x) as in equation (13) in [4]
			////////////////////////////////////////////////////
			double[] e = new double[connectorContainer.connectors.size()];
			for (int i=0;i<connectorContainer .connectors.size();i++){
				e[i] = connectorContainer.connectors.elementAt(i).myTrueValue()-connectorContainer.connectors.elementAt(i).myTheroreticalValue(assumedVoltages);
			}
			
			
			//////////////////////////////////////////////////////
			// Computation of g(x)=-2*H^T(x)*R^-1*e(x) as in equation (13) in [4]
			////////////////////////////////////////////////////
			double[] g = new double[4*2*network.getVertexCount()];
			// calculating R^-1*e first
			double[] Re = new double[connectorContainer.connectors.size()];
			for (int i=0;i<connectorContainer.connectors.size();i++){
				Re[i] = 0;
				for (int j=0;j<connectorContainer.connectors.size();j++){
					Re[i]+=R[i][j]*e[j];
				}
			}
			for (int i=0;i<4*2*network.getVertexCount();i++){
				g[i] = 0;
				for (int j=0;j<connectorContainer.connectors.size();j++){
					g[i] += -2*H[j][i]*Re[j];
				}
			}
			
			//////////////////////////////////////////////////////
			// Computation of matrix G(x) = 2*H^T(x)*R^-1*H(x) as in equation (17) in [4], which has the error of transposing the wrong matrix.
			////////////////////////////////////////////////////
			double[][] G = new double[4*2*network.getVertexCount()][4*2*network.getVertexCount()];
			// calculating R^-1(x)*H(x) first
			double[][] RH = new double[connectorContainer.connectors.size()][4*2*network.getVertexCount()];
			for (int i=0;i<connectorContainer.connectors.size();i++){
				for (int j=0;j<4*2*network.getVertexCount();j++){
					RH[i][j] = 0;
					for (int k=0;k<connectorContainer.connectors.size();k++){
						RH[i][j] += R[i][k]*H[k][j];
					}
				}
			}
			for (int i=0;i<4*2*network.getVertexCount();i++){
				for (int j=0;j<4*2*network.getVertexCount();j++){
					G[i][j] = 0;
					for (int k=0;k<connectorContainer.connectors.size();k++){
						G[i][j] += 2*H[k][i]*RH[k][j];
					}
				}
			}
			
			//////////////////////////////////////////////////////
			// Generate the inverse of G(x) needed in equation (16) in [4]
			////////////////////////////////////////////////////
			/*
			 *       SUBROUTINE DGETRI( N, A, LDA, IPIV, WORK, LWORK, INFO )
			 *
				*  -- LAPACK routine (version 3.2) --
				*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
				*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
				*     November 2006
				*
				*  Purpose
				*  =======
				*
				*  DGETRI computes the inverse of a matrix using the LU factorization
				*  computed by DGETRF.
				
				*  =====================================================================
			 */
			IPIV = new int[G.length];
			WORK = new double[G.length*G.length];
			info = new org.netlib.util.intW(2);
			DGETRF.DGETRF(G.length, IPIV.length, G, IPIV, info);
			DGETRI.DGETRI(G.length, G, IPIV, WORK, G.length, info);//( N, A, LDA, IPIV, WORK, LWORK, INFO )
			
	
			//////////////////////////////////////////////////////
			// Calculate voltage correction for x like in equation (16) in [4]
			////////////////////////////////////////////////////
			// first generate the currently assumed voltage vector in real-valued representation, where c=(e+i*f) in order [e_1, f_1,...,e_n,f_n]
			double[] x = new double[4*2*network.getVertexCount()];
			for (int i=0;i<4*network.getVertexCount();i++){
				x[2*i] = assumedVoltages[i].getReal();
				x[2*i+1] = assumedVoltages[i].getImag();
			}
			// calculate G^-1(x)*g(x) first. Note that variable G now contains G^-1 after the inversion
			double[] Gg = new double[4*2*network.getVertexCount()];
			for (int i=0;i<4*2*network.getVertexCount();i++){
				Gg[i] = 0;
				for (int j=0;j<4*2*network.getVertexCount();j++){
					Gg[i] += G[i][j]*g[j];
				}
			}
			// generate the improved voltage vector
			double[] xk = new double[4*2*network.getVertexCount()];
			for (int i=0;i<4*2*network.getVertexCount();i++){
				xk[i] = x[i]-Gg[i];
			}
			// update the assumedVoltages vector
			for (int i=0;i<4*network.getVertexCount();i++){
				assumedVoltages[i] = new Complex(xk[2*i], xk[2*i+1]);
			}
		
			
			//////////////////////////////////////////////////////
			// check for convergence
			////////////////////////////////////////////////////
			double dx = 0;
			for (int i=0;i<4*2*network.getVertexCount();i++){
				dx += Gg[i]*Gg[i];
			}
			dx = java.lang.Math.sqrt(dx);
			if (dx<=epsilon) converged = true;
			
		} while ((!converged)&&(iteration++<maxIterations));
	
		return assumedVoltages;
	}
	
	
}
