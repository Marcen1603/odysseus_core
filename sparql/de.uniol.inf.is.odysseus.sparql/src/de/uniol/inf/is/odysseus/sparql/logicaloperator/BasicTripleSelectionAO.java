package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Triple;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;

public class BasicTripleSelectionAO extends SelectAO implements
		OutputSchemaSettable {

	private static final long serialVersionUID = -8187061597335204197L;
	protected Triple triple;
	private SDFSchema outputSchema;

	public BasicTripleSelectionAO(Triple t) {
		this.triple = t;
		calcOutElements();
	}

	public void calcOutElements() {
		List<SDFAttribute> l = new ArrayList<SDFAttribute>();

		if (triple.getSubject().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#"
					+ triple.getSubject().getName(), SDFDatatype.STRING));
		}
		if (triple.getPredicate().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#"
					+ triple.getPredicate().getName(), SDFDatatype.STRING));
		}
		if (triple.getObject().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#"
					+ triple.getObject().getName(), SDFDatatype.STRING));
		}
		if (getInputAO() != null && getInputSchema() != null) {
			l.addAll(getInputSchema().getAttributes());
		}
		SDFSchema schema = new SDFSchema("", l);
		this.setOutputSchema(schema);
	}

	public BasicTripleSelectionAO(BasicTripleSelectionAO selection) {
		super();
		this.triple = selection.triple;
	}

	public Triple getTriple() {
		return triple;
	}

	public void setT(Triple t) {
		this.triple = t;
	}

	@Override
	public BasicTripleSelectionAO clone() {
		return new BasicTripleSelectionAO(this);
	}

	@Override
	public String toString() {
		return "BasicTripleSelection (" + this.triple.toString() + ")";
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		this.outputSchema = outputSchema;
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
		setOutputSchema(outputSchema);
	}

	@Override
	public SDFSchema getOutputSchema() {
		return outputSchema;
	}
}
