package de.uniol.inf.is.odysseus.complexnumber;

import java.io.IOException;
import java.io.ObjectInputStream;
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
	public ComplexNumber readData(ObjectInputStream inputStream)
			throws IOException {
		ComplexNumber c;
		try {
			c = (ComplexNumber) inputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			c = null;
		}
		return c;
	}

	@Override
	public ComplexNumber readData(String string) {		
		return ComplexNumber.parseComplexNumber(string);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public int memSize(Object attribute) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class<?> createsType() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
