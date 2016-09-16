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

@LogicalOperator(name="BestPostsDebs", minInputPorts=1, maxInputPorts=1, doc="Calculate Best Posts for Query1 Debs Challenge", category={LogicalOperatorCategory.TRANSFORM})
public class BestPostsDebsAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -3522190343381081084L;

	public BestPostsDebsAO() {
		super();
	}
	
	public BestPostsDebsAO(BestPostsDebsAO other) {
		super(other);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new BestPostsDebsAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute(null, "post1", SDFDatatype.TUPLE);
		SDFAttribute attr1 = new SDFAttribute(null, "post2", SDFDatatype.TUPLE);
		SDFAttribute attr2 = new SDFAttribute(null, "post3", SDFDatatype.TUPLE);
		attributeList.add(attr);
		attributeList.add(attr1);
		attributeList.add(attr2);
		
		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("test", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, getInputSchema());
		return schema;
	}

}
