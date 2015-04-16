package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TimerTransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "TIMER", category={LogicalOperatorCategory.SOURCE}, doc = "A trigger with time events")
public class TimerSource extends AbstractAccessAO {

	private static final long serialVersionUID = 1498973450585444521L;
	
	public TimerSource() {
		setTransportHandler(TimerTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
		SDFAttribute a = new SDFAttribute(getName(), "time", SDFDatatype.TIMESTAMP, null);
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		setAttributes(attributes);
	}
	
	public TimerSource(TimerSource other){
		super(other);
	}
		
	@Override
	public AbstractLogicalOperator clone() {
		return new TimerSource(this);
	}
	
	@Parameter(name = TimerTransportHandler.PERIOD, type = LongParameter.class, optional = false, doc = "The timer period in ms")
	public void setPeriod(long period){
		addOption(TimerTransportHandler.PERIOD,period+"");
	}
	
	public long getPeriod(){
		String p = getOption(TimerTransportHandler.PERIOD);
		if (p != null){
			return Long.parseLong(p);
		}else
			return -1;
	}
}
