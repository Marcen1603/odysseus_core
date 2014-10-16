package de.uniol.inf.is.odysseus.wrapper.temper1.logicaloperator;


import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.access.Temper1TransportHandler;


@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "Temper1ACCESS", doc="Returns the value of a temperature sensor of the type TEMPer1.", category={LogicalOperatorCategory.SOURCE})
public class Temper1AccessAO extends AbstractAccessAO {
	
	private static final long serialVersionUID = 1L;
	private Integer tempNumber;
	
	public Temper1AccessAO() {
		super();
		setTransportHandler(Temper1TransportHandler.NAME);
		setWrapper(Constants.GENERIC_PULL);
		setDataHandler("Tuple");
		setProtocolHandler("none");
		
		
		List<SDFAttribute> schema = new LinkedList<>();
		schema.add(new SDFAttribute(null, "Temperature", SDFDatatype.DOUBLE, null));
		schema.add(new SDFAttribute(null, "Temperature", SDFDatatype.DOUBLE, null));
		//schema.add(new SDFAttribute(null, "Timestamp", SDFDatatype.TIMESTAMP, null));
		setAttributes(schema);
	}
	
	public Temper1AccessAO(Temper1AccessAO other) {
		super(other);
		this.tempNumber = other.tempNumber;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new Temper1AccessAO(this);
	}
	
	@Parameter(name = "tempnumber", doc = "The number of the temperature sensor", type = IntegerParameter.class, optional = false)
    public void setTempNumber(Integer tempNumber) {
		this.tempNumber = tempNumber;
	}
	
	public Integer getTempNumber(){
		return this.tempNumber;
	}
}
