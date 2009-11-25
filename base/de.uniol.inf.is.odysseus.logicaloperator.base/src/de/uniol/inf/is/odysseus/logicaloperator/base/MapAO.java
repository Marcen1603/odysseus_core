package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class MapAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2120387285754464451L;
	private List<SDFExpression> expressions;
	private SDFAttributeList outputSchema = null;
	
	public MapAO() {
		super();
	}

	public MapAO(MapAO ao) {
		super(ao);
		this.expressions = ao.expressions;
	}

	public List<SDFExpression> getExpressions() {
		return expressions;
	}

	private void calcOutputSchema(){
		SDFAttributeList inputSchema = getInputSchema();
		if (inputSchema!=null){
			this.outputSchema = new SDFAttributeList();
			// Find existing Attributes
			for (SDFExpression expr: expressions){
				if (expr.isSingleAttribute()){
					outputSchema.add(expr.getSingleAttribute());
				}else{
					outputSchema.add(new SDFAttribute(expr.toString()));
				}
				
			}
		}
	}
	
	public void setExpressions(List<SDFExpression> expressions) {
		this.expressions = expressions;
		calcOutputSchema();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		if (outputSchema == null) calcOutputSchema();
		return outputSchema;
	}

	@Override
	public MapAO clone() {
		return new MapAO(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((expressions == null) ? 0 : expressions.hashCode());
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
		MapAO other = (MapAO) obj;
		if (expressions == null) {
			if (other.expressions != null)
				return false;
		} else if (!expressions.equals(other.expressions))
			return false;
		return true;
	}

}
