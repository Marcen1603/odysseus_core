package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.collection.Resource;

public class ResourceParameter extends AbstractParameter<Resource> {

	private static final long serialVersionUID = -3605838409249325020L;

	@Override
	protected void internalAssignment() {
		if (inputValue instanceof String){
			setValue( new Resource(getCaller().getUser(),(String) inputValue));
		} else if( inputValue instanceof Resource ) {
			setValue ((Resource)inputValue);
		}
	}

	@Override
	protected String getPQLStringInternal() {
		return "'" + inputValue + "'";
	}

}
