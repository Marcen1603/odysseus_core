package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

@LogicalOperator(name="ScoreUpdater", minInputPorts=1, maxInputPorts=1, doc="Update Score of each post for debs query", category={LogicalOperatorCategory.TRANSFORM})
public class ScoreUpdaterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7829300159849602634L;

	public ScoreUpdaterAO() {
		super();
	}
	
	public ScoreUpdaterAO(ScoreUpdaterAO other) {
		super(other);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ScoreUpdaterAO(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute(null, "graph", SDFGraphDatatype.GRAPH);
		SDFAttribute attr2 = new SDFAttribute(null, "postgraph", SDFGraphDatatype.GRAPH);
		SDFAttribute attr3 = new SDFAttribute(null, "outdated", SDFDatatype.STRING);
		attributeList.add(attr);
		attributeList.add(attr2);
		attributeList.add(attr3);
		
		return SDFSchemaFactory.createNewSchema("graphstructure", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, this.getInputSchema());
	}

}
