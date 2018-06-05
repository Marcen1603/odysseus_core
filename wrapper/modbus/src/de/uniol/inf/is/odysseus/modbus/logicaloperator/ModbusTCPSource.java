package de.uniol.inf.is.odysseus.modbus.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.ghgande.j2mod.modbus.Modbus;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.NoProtocolHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BitVectorParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.modbus.ModbusTCPTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "ModbusTCPSource", category={LogicalOperatorCategory.SOURCE}, doc = "Allows to read from a Modbus TCP connections.")
public class ModbusTCPSource extends AbstractAccessAO {

	private static final long serialVersionUID = -7787237523086155235L;
	private int functionCode;
	
	
	public ModbusTCPSource(){
		super();
		setTransportHandler(ModbusTCPTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PULL);
		setProtocolHandler(NoProtocolHandler.NAME);
	}

	public ModbusTCPSource(ModbusTCPSource modbusTCPSource) {
		super(modbusTCPSource);
		this.functionCode = modbusTCPSource.functionCode;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ModbusTCPSource(this);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.COUNT, optional = false)
	public void setCount(int count){
		addOption(ModbusTCPTransportHandler.COUNT, ""+count);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.FUNCTION_CODE, optional = false)
	public void setFunctionCode(int functionCode){
		addOption(ModbusTCPTransportHandler.FUNCTION_CODE, ""+functionCode);
		this.functionCode = functionCode;
	}
	
	private void determineAndSetOutputSchema(int functionCode) {
		SDFAttribute outputAttribute = null;
		switch (functionCode){
			case Modbus.READ_INPUT_DISCRETES:
			case Modbus.READ_COILS:
				outputAttribute = new SDFAttribute(getName(),"value",SDFDatatype.BITVECTOR);
				break;
			case Modbus.READ_INPUT_REGISTERS:
			case Modbus.READ_MULTIPLE_REGISTERS:
				outputAttribute = new SDFAttribute(getName(),"value",SDFDatatype.LIST_INTEGER);
				break;
			default:
				// Do nothing, schema must be set by hand
		}

		if (outputAttribute != null){
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			setAttributes(outputAttributes);
		}
		
		
	}

	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.PORT, optional = true)
	public void setPort(int port){
		addOption(ModbusTCPTransportHandler.PORT, ""+port);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.REF, optional = false)
	public void setRef(int ref){
		addOption(ModbusTCPTransportHandler.REF, ""+ref);
	}
	
	@Parameter(type = StringParameter.class, name = ModbusTCPTransportHandler.SLAVE, optional = false)
	public void setSlave(String slave){
		addOption(ModbusTCPTransportHandler.SLAVE, ""+slave);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.UNIT_ID, optional = true)
	public void setUnitId(int unitID){
		addOption(ModbusTCPTransportHandler.UNIT_ID, ""+unitID);
	}
	
	@Parameter(type = BitVectorParameter.class, name = ModbusTCPTransportHandler.WRITE_BITVECTOR, optional = true)
	public void setWriteBitvector(BitVector value){
		addOption(ModbusTCPTransportHandler.WRITE_BITVECTOR, ""+value);
	}
	
	@Parameter(type = BooleanParameter.class, name = ModbusTCPTransportHandler.WRITE_BOOLEAN, optional = true)
	public void setWriteBoolean(Boolean value){
		addOption(ModbusTCPTransportHandler.WRITE_BOOLEAN, ""+value);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.WRITE_FUNCTION_CODE, optional = true)
	public void setWriteFunctionCode(int value){
		addOption(ModbusTCPTransportHandler.WRITE_FUNCTION_CODE, ""+value);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.WRITE_REF, optional = true)
	public void setWriteRef(int writeRef){
		addOption(ModbusTCPTransportHandler.WRITE_REF, ""+writeRef);
	}
	
	@Parameter(type = IntegerParameter.class, name = ModbusTCPTransportHandler.WRITE_REGISTERS, optional = true, isList=true)
	public void setWriteRegisters(List<Integer> writeRegisters){
		addOption(ModbusTCPTransportHandler.WRITE_REGISTERS, buildString(writeRegisters,","));
	}
	
	@Override
	public boolean isValid() {
		boolean valid = super.isValid();
		
		determineAndSetOutputSchema(functionCode);
		
		return valid;
	}

	
	
}
