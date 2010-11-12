package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class MapAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2120387285754464451L;
	private List<SDFExpression> expressions;
	private SDFAttributeList outputSchema = new SDFAttributeList();

	public MapAO() {
		super();
	}

	public MapAO(MapAO ao) {
		super(ao);
		this.setExpressions(ao.expressions);
	}

	public List<SDFExpression> getExpressions() {
		return Collections.unmodifiableList(expressions);
	}

	private void calcOutputSchema() {
		outputSchema.clear();
		if (expressions != null) {
			for (SDFExpression expr : expressions) {
				SDFAttribute attr = null;
				if (expr.isOnlyAttribute()) {
					attr = new SDFAttribute(expr.getSingleAttribute());
				} else {
					attr = new SDFAttribute(expr.getExpression());
					// FIXME stimmt natuerlich nicht im allgemeinen, aber atm
					// ist datatype unbekannt
					attr.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
				}
				outputSchema.add(attr);

				// Alles Quatsch :-)
				//
				// if (expr.isSingleAttribute()){
				// outputSchema.add(expr.getSingleAttribute());
				// }else if (inputSchema.contains(attr)){
				// outputSchema.add(attr);
				// } else{
				// outputSchema.add(new SDFAttribute(expr.toString()));
				// }
			}
		}
	}

	public void setExpressions(List<SDFExpression> expressions) {
		this.expressions = expressions;
		calcOutputSchema();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
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
