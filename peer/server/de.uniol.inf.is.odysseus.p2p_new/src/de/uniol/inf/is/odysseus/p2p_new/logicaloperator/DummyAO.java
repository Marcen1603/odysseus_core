package de.uniol.inf.is.odysseus.p2p_new.logicaloperator;

import java.util.List;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;

@LogicalOperator(name = "DUMMY", doc = "Some dummy operator", minInputPorts = 0, maxInputPorts = 1, category=LogicalOperatorCategory.SINK)
public class DummyAO extends AbstractLogicalOperator {
	
	private DummyAO sink;
	private DummyAO src;
	private SDFSchema assignedSchema;
	
	private double dataRate;
	private double intervalLength;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6721640906028496606L;

    public DummyAO(){
        super();
    }
     
    public DummyAO(DummyAO dummyAO){
        super(dummyAO);
        this.sink = dummyAO.sink;
        this.src = dummyAO.src;
        if(this.assignedSchema!=null)
        	this.assignedSchema = dummyAO.assignedSchema.clone();
        this.setOutputSchema(dummyAO.getOutputSchema());
        setParameterInfos(dummyAO.getParameterInfos());
        
        this.dataRate = dummyAO.dataRate;
        this.intervalLength = dummyAO.intervalLength;
    }	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new DummyAO(this);
	}
	
	public void connectWithDummySink(DummyAO sink) {
		this.sink=sink;
	}
	
	public void connectWithDummySource(DummyAO source) {
		this.src = source;
	}
	
	public DummyAO getDummySink() {
		return sink;
	}
	
	public DummyAO getDummySource() {
		return src;
	}	
	
	@Parameter(name="SCHEMA", type = CreateSDFAttributeParameter.class, isList=true,optional=true)
	public void setSchema(List<SDFAttribute> outputSchema) {
		assignedSchema = new SDFSchema("", Tuple.class,outputSchema);
		addParameterInfo("SCHEMA", schemaToString(outputSchema));
	}
	
	public List<SDFAttribute> getSchema() {
		return assignedSchema != null ? assignedSchema.getAttributes() : null;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return assignedSchema;
	}	
	
	private static String schemaToString(List<SDFAttribute> outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("[");
			if( !Strings.isNullOrEmpty(attribute.getSourceName())) {
				sb.append("'").append(attribute.getSourceName()).append("',");
			}
			sb.append("'").append(attribute.getAttributeName());
			sb.append("', '");
			sb.append(attribute.getDatatype().getURI());
			sb.append("']");
			if (i < attributes.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Parameter(name="DATARATE", type = DoubleParameter.class, optional=true)
	public void setDataRate(double dataRate) {
		this.dataRate = dataRate;
		addParameterInfo("DATARATE", String.valueOf(dataRate));
	}	
	
	public double getDataRate() {
		return dataRate;
	}
	
	@Parameter(name="INTERVALLENGTH", type = DoubleParameter.class, optional=true)
	public void setIntervalLength(double intervalLength) {
		this.intervalLength = intervalLength;
		addParameterInfo("INTERVALLENGTH", String.valueOf(intervalLength));
	}
	
	public double getIntervalLength() {
		return intervalLength;
	}
}
