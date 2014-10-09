package de.uniol.inf.is.odysseus.sports.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * Variant of SampleAO to reduce Load while grouping by an attribute.
 * @author Carsten Cordes
 *
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="REDUCELOAD", doc="This operator can reduce load by throwing away tuples.",category={LogicalOperatorCategory.PROCESSING})
public class ReduceLoadAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2100883143405405327L;
	
	/**
	 * Sample rate (if sampling per element)
	 */
	private int sampleRate = 0;
	
	/**
	 * Time value to sample via time.
	 */
	private TimeValueItem timeValue;
	
	/**
	 * Base time unit.
	 */
	private TimeUnit baseTimeUnit = null;
	
	/**
	 * List of Grouping attributes.
	 */
	private List<SDFAttribute> groupingAttributes = new ArrayList<>();
	
	/**
	 * Constructor
	 */
	public ReduceLoadAO(){
		
	}
	
	/**
	 * gets sample Rate.
	 * @return
	 */
	public int getSampleRate() {
		return sampleRate;
	}
	
	/***
	 * Sets sample rate.
	 * @param sampleRate
	 */
	@Parameter(type = IntegerParameter.class, name = "samplerate", optional=true)
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	/**
	 * Gets Time value
	 * @return
	 */
	public TimeValueItem getTimeValue() {
		return this.timeValue;	
	}
	
	/**
	 * Sets time value.
	 * @param timeValue
	 */
	@Parameter(type = TimeParameter.class, name = "timevalue", optional = true)
	public void setTimeValue(TimeValueItem timeValue) {
		this.timeValue = timeValue;
	}
	
	
	/**
	 * Constructor
	 * @param ao Logical Operator
	 */
	public ReduceLoadAO(ReduceLoadAO ao) {
		super(ao);
		this.sampleRate = ao.sampleRate;
		this.timeValue = ao.timeValue;
		this.baseTimeUnit = ao.baseTimeUnit;
		this.groupingAttributes = new ArrayList<>(ao.getGroupingAttributes());
		
	}
	
	/***
	 * Sets group-by attribtues.
	 * @param attributes
	 */
	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}
	
	/***
	 * Sets basetime Unit manually (needed if Odysseus fails to preserve parameter)
	 * @param btuString BaseTimeUnit as String
	 */
	@Parameter(name = "ASSUMEBTU", optional = true, type=StringParameter.class)
	public void setAssumedBaseTimeUnit(String btuString) {
		TimeUnit btu = TimeUnit.valueOf(btuString);
		if (btu != null){
			this.baseTimeUnit = btu;
		}
		
	}
	
	public String getAssumedBaseTimeUnit() {
		return this.baseTimeUnit.toString();
	}
	
	
	/***
	 * Returns group-by attributes
	 * @return
	 */
	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}


	/**
	 * Clones Operato
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new ReduceLoadAO(this);
	}
	
	/**
	 * Returns base time unit.
	 * @return
	 */
	public TimeUnit getBaseTimeUnit() {
		baseTimeUnit = getInputSchema().determineTimeUnit();
		if(baseTimeUnit==null) {
			baseTimeUnit = TimeUnit.MILLISECONDS;
		}
		return this.baseTimeUnit;
		
	}
	
	@Override
	/***
	 * Initializes Operator
	 */
	public void initialize() {
		getBaseTimeUnit();
		super.initialize();
	}
	
	@Override
	/***
	 * True if all needed parameters are set.
	 */
	public boolean isValid() {
		
		if((this.sampleRate == 0 && this.timeValue != null) || (this.sampleRate > 0 && this.timeValue == null))
			return super.isValid();
		
		return false;
		
	}

}
