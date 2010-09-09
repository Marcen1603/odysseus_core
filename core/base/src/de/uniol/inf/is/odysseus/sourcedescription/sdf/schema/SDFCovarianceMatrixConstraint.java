package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

public class SDFCovarianceMatrixConstraint extends SDFDatatypeConstraint {	
	
	private double[][] covMatrix;

	public SDFCovarianceMatrixConstraint(String URI, String type, double[][] covMatrix) {
		super(URI, type);
		this.covMatrix = covMatrix;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double[][] getCovMatrix(){
		return covMatrix;
	}

}
