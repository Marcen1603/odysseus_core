package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class MapAO extends ProjectAO {
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

	private static final long serialVersionUID = -2120387285754464451L;
	public List<SDFExpression> expressions;

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

	public void setExpressions(List<SDFExpression> expressions) {
		this.expressions = expressions;
		checkSchema();
	}

	private void checkSchema() throws RuntimeException {
		if (this.expressions != null && this.getOutputSchema() != null
				&& this.expressions.size() != this.getOutputSchema().size()) {
			throw new RuntimeException(
					"output schema and expressions do not match");
		}
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outElements) {
		super.setOutputSchema(outElements);
		checkSchema();
	}

	@Override
	public MapAO clone() {
		return new MapAO(this);
	}

}
