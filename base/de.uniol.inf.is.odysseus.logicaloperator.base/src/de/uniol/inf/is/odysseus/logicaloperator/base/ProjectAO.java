/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */
public class ProjectAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 5487345119018834806L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(projectMatrix);
		result = prime * result + Arrays.hashCode(projectVector);
		result = prime * result + Arrays.hashCode(restrictList);
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
		ProjectAO other = (ProjectAO) obj;
		if (!Arrays.equals(projectMatrix, other.projectMatrix))
			return false;
		if (!Arrays.equals(projectVector, other.projectVector))
			return false;
		if (!Arrays.equals(restrictList, other.restrictList))
			return false;
		return true;
	}

	private int[] restrictList = new int[0];
	
	/**
	 * only used in multivariate case
	 */
	private double[][] projectMatrix;
	
	/**
	 * only used in multivariate case
	 */
	private double[] projectVector;

	public ProjectAO(ProjectAO projectAO) {
		super(projectAO);
		if (restrictList != null){
			int length = projectAO.restrictList.length;
			restrictList = new int[length];
			System.arraycopy(projectAO.restrictList, 0, restrictList, 0, length);
		}
		this.projectMatrix = projectAO.projectMatrix;
		this.projectVector = projectAO.projectVector;
	}

	public ProjectAO() {
		super();
	}

	public ProjectAO(SDFAttributeList queryAttributes) {
		super();
		setOutputSchema(queryAttributes);
	}

	public @Override
	ProjectAO clone() {
		return new ProjectAO(this);
	}

	public int[] getRestrictList() {
		return this.restrictList;
	}
	
	public void setRestrictList(int[] list){
		this.restrictList = list;
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
	
	public void updateRestrictList(){
		restrictList = calcRestrictList(getInputSchema(), getOutputSchema());
	}
	
	public static int[] calcRestrictList(SDFAttributeList in, SDFAttributeList out){
		int[] ret = new int[out.size()];
		int i=0;
		for (SDFAttribute a:out){
			ret[i++] = in.indexOf(a);
		}
		return ret;
	}

	//@Override
	public void calcOutputSchema() {
		// TODO: Works only in relational case
		SDFAttribute[] out = new SDFAttribute[restrictList.length];
		SDFAttributeList in = getInputSchema();
		
		int pos = 0;
		for (int i: restrictList){
			out[pos++] = in.get(i); 
		}
		
		setOutputSchema(new SDFAttributeList(out));
		
	}
}
