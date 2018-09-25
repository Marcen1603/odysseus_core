package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="SAMPLE", doc="This operator can reduce load by throwing away tuples by numbers of elements or by time.",category={LogicalOperatorCategory.PROCESSING})
public class SampleAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2100883143405405327L;
	private int sampleRate = 0;
	private TimeValueItem timeValue;
	private TimeUnit baseTimeUnit = null;
	
	public SampleAO(){
		
	}
	
	public int getSampleRate() {
		return sampleRate;
	}
	
	@Parameter(type = IntegerParameter.class, name = "samplerate", doc="Set the number of objects that are removed (e.g. 2 means remove every second object)", optional=true)
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	public TimeValueItem getTimeValue() {
		return this.timeValue;	
	}
	
	@Parameter(type = TimeParameter.class, name = "timevalue", doc="If set, the output will contain only one element for this time period.",optional = true)
	public void setTimeValue(TimeValueItem timeValue) {
		this.timeValue = timeValue;
	}
	
	public SampleAO(SampleAO sampleAO) {
		super(sampleAO);
		this.sampleRate = sampleAO.sampleRate;
		this.timeValue = sampleAO.timeValue;
		this.baseTimeUnit = sampleAO.baseTimeUnit;
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SampleAO(this);
	}
	
	@Override
	public TimeUnit getBaseTimeUnit() {
		if (baseTimeUnit == null) {
			baseTimeUnit = TimeUnit.MILLISECONDS;

			SDFConstraint c = getInputSchema().getConstraint(
					SDFConstraint.BASE_TIME_UNIT);
			if (c != null) {
				baseTimeUnit = (TimeUnit) c.getValue();
			} else {

				// Find input schema attribute with type start timestamp
				// It provides the correct base unit
				// if not given use MILLISECONDS as default
				Collection<SDFAttribute> attrs = getInputSchema()
						.getSDFDatatypeAttributes(SDFDatatype.START_TIMESTAMP);
				if (attrs.isEmpty()) {
					attrs = getInputSchema().getSDFDatatypeAttributes(
							SDFDatatype.START_TIMESTAMP_STRING);
				}
				if (!attrs.isEmpty()) {
					SDFAttribute attr = attrs.iterator().next();
					SDFConstraint constr = attr
							.getDtConstraint(SDFConstraint.BASE_TIME_UNIT);
					if (constr != null) {
						baseTimeUnit = (TimeUnit) constr.getValue();
					}
				}
			}

		}
		return this.baseTimeUnit;
		
	}
	
	@Override
	public void initialize() {
		getBaseTimeUnit();
		super.initialize();
	}
	
	@Override
	public boolean isValid() {
		
		if (this.sampleRate != 0 && this.timeValue != null) {
			addError("You cannot use sampleRate and timeValue at once!");
			return false;
		}
		
		if (this.sampleRate == 0 && this.timeValue == null){
			addError("You must define sampleRate or timeValue!");
			return false;
		}
						
		return true;
		
	}

}
