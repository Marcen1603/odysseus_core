package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

/**
 * Logical operator for score updater.
 * 
 * @author Kristian Bruns
 */
@LogicalOperator(name="ScoreUpdater", minInputPorts=1, maxInputPorts=1, doc="Update Score of each post for debs query", category={LogicalOperatorCategory.TRANSFORM})
public class ScoreUpdaterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7829300159849602634L;
	private String graphAttr;
	private TimeValueItem timeInterval;

	public ScoreUpdaterAO() {
		super();
	}
	
	public ScoreUpdaterAO(ScoreUpdaterAO other) {
		super(other);
		this.graphAttr = other.graphAttr;
		this.timeInterval = other.timeInterval;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ScoreUpdaterAO(this);
	}
	
	@Parameter(type=StringParameter.class, name="GRAPHATTRIBUTE", optional=false, isList=false, doc="name of attribute containing graphstructure")
	public void setGraphAttribute(String graphAttribute) {
		this.graphAttr = graphAttribute;
	}
	
	@Parameter(type=TimeParameter.class, name="TIMEINTERVAL", optional=false, isList=false, doc="size of time interval as long")
	public void setTimeInterval(TimeValueItem timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	@GetParameter(name="GRAPHATTRIBUTE")
	public String getGraphAttribute() {
		return this.graphAttr;
	}
	
	public TimeValueItem getTimeInterval() {
		return this.timeInterval;
	}
	
	@GetParameter(name="TIMEINTERVAL")
	public Long getTimeIntervalMillis() {
		return TimeUnit.MILLISECONDS.convert(getTimeInterval().getTime(), getTimeInterval().getUnit());
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
