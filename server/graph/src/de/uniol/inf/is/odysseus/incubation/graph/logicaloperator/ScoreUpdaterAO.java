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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

@LogicalOperator(name="ScoreUpdater", minInputPorts=1, maxInputPorts=1, doc="Update Score of each post for debs query", category={LogicalOperatorCategory.TRANSFORM})
public class ScoreUpdaterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7829300159849602634L;
	private String graphAttr;
	private Long timeIntervalMilli;

	public ScoreUpdaterAO() {
		super();
	}
	
	public ScoreUpdaterAO(ScoreUpdaterAO other) {
		super(other);
		this.graphAttr = other.graphAttr;
		this.timeIntervalMilli = other.timeIntervalMilli;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ScoreUpdaterAO(this);
	}
	
	@Parameter(type=StringParameter.class, name="GRAPHATTRIBUTE", optional=false, isList=false, doc="name of attribute containing graphstructure")
	public void setGraphAttribute(String graphAttribute) {
		this.graphAttr = graphAttribute;
	}
	
	@Parameter(type=LongParameter.class, name="TIMEINTERVAL", optional=false, isList=false, doc="size of time interval as long")
	public void setTimeInterval(Long timeInterval) {
		this.timeIntervalMilli = timeInterval;
	}
	
	@GetParameter(name="GRAPHATTRIBUTE")
	public String getGraphAttribute() {
		return this.graphAttr;
	}
	
	@GetParameter(name="TIMEINTERVAL")
	public Long getTimeInterval() {
		return this.timeIntervalMilli;
	}
		
	
	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute("graph", "graph", SDFGraphDatatype.GRAPH);
		SDFAttribute attr2 = new SDFAttribute("outdated", "outdated", SDFDatatype.STRING);
		attributeList.add(attr);
		attributeList.add(attr2);
		
		return SDFSchemaFactory.createNewSchema("graph", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, this.getInputSchema());
	}

}
