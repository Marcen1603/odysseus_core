package de.uniol.inf.is.odysseus.complexnumber;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class ComplexNumberDataHandler extends
		AbstractDataHandler<ComplexNumber> {

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFComplexNumberDatatype.COMPLEX_NUMBER.getURI());
	}
	
	public ComplexNumberDataHandler() {
		super(null);
	}
	
	@Override
	protected IDataHandler<ComplexNumber> getInstance(SDFSchema schema) {
		return new ComplexNumberDataHandler();
	}
	
	@Override
	public ComplexNumber readData(ByteBuffer buffer) {
		double r = buffer.getDouble();
		double i = buffer.getDouble();
		return new ComplexNumber(r,i);
	}

	@Override
	public ComplexNumber readData(String string) {		
		return ComplexNumber.parseComplexNumber(string);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		ComplexNumber n = (ComplexNumber)data;
		buffer.putDouble(n.getReal());
		buffer.putDouble(n.getImaginary());
	}

	@Override
	public int memSize(Object attribute) {
		return Double.SIZE/4; // 2*Double.SIZE/8
	}

	@Override
	public Class<?> createsType() {
		return ComplexNumber.class;
	}



	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
