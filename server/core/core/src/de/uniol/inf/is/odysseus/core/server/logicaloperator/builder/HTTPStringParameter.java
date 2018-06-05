package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.net.URL;

import org.apache.commons.io.IOUtils;

public class HTTPStringParameter extends AbstractParameter<String> {
	private static final long serialVersionUID = 4730657051745577427L;

	@Override
	protected void internalAssignment() {		
		try {
			setValue(IOUtils.toString(new URL(inputValue.toString())));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	protected String getPQLStringInternal() {
		return getValue().toString();
	}

}
