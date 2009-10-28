package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Andre Bolles
 */
public class ProjectMVAO extends ProjectAO {
	private static final long serialVersionUID = 5487345119018834806L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(projectMatrix);
		result = prime * result + Arrays.hashCode(projectVector);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectMVAO other = (ProjectMVAO) obj;
		if (!Arrays.equals(projectMatrix, other.projectMatrix))
			return false;
		if (!Arrays.equals(projectVector, other.projectVector))
			return false;
		return true;
	}
	
	/**
	 * only used in multivariate case
	 */
	private double[][] projectMatrix;
	
	/**
	 * only used in multivariate case
	 */
	private double[] projectVector;

	public ProjectMVAO(ProjectMVAO projectMVAO) {
		super(projectMVAO);
		this.projectMatrix = projectMVAO.projectMatrix;
		this.projectVector = projectMVAO.projectVector;
	}

	public ProjectMVAO() {
		super();
	}

	public ProjectMVAO(SDFAttributeList queryAttributes) {
		super();
		setOutputSchema(queryAttributes);
	}

	public @Override
	ProjectAO clone() {
		return new ProjectAO(this);
	}
	
	public double[][] getProjectMatrix(){
		return this.projectMatrix;
	}
	
	public void setProjectMatrix(double[][] matrix){
		this.projectMatrix = matrix;
	}
	
	public double[] getProjectVector(){
		return this.projectVector;
	}
	
	public void setProjectVector(double[] vector){
		this.projectVector = vector;
	}
	
	public static int[] calcRestrictList(SDFAttributeList in, SDFAttributeList out){
		int[] ret = new int[out.size()];
		int i=0;
		for (SDFAttribute a:out){
			ret[i++] = in.indexOf(a);
		}
		return ret;
	}
}
