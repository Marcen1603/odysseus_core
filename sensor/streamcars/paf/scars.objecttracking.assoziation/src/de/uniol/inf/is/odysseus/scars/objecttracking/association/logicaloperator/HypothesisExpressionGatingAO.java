package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Volker Janz
 *
 * @param <M>
 */
public class HypothesisExpressionGatingAO<M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String predObjListPath;
	private String scanObjListPath;
	private String expressionString;

	public HypothesisExpressionGatingAO() {
		super();
	}

	public HypothesisExpressionGatingAO(HypothesisExpressionGatingAO<M> copy) {
		super(copy);
		this.predObjListPath = copy.predObjListPath;
		this.scanObjListPath = copy.scanObjListPath;
		this.expressionString = copy.expressionString;
	}

	@Override
	public HypothesisExpressionGatingAO<M> clone(){
		return new HypothesisExpressionGatingAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	public void initPaths(String predObjListPath, String scanObjListPath) {
		this.predObjListPath = predObjListPath;
		this.scanObjListPath = scanObjListPath;
	}

	public String getPredObjListPath() {
		return predObjListPath;
	}

	public void setPredObjListPath(String predObjListPath) {
		this.predObjListPath = predObjListPath;
	}

	public String getScanObjListPath() {
		return scanObjListPath;
	}

	public void setScanObjListPath(String scanObjListPath) {
		this.scanObjListPath = scanObjListPath;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
