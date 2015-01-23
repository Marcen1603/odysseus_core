package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.collection.Resource;


public class AccessAOSourceParameter extends AbstractParameter<Resource> {

	private static final long serialVersionUID = -1121595596743719918L;

	@Override
	protected void internalAssignment() {
		if( inputValue instanceof Resource ) {
			setValue( (Resource)inputValue );
		} else if (inputValue instanceof String){
			boolean marker = getDataDictionary().containsViewOrStream((String) inputValue, getCaller());
			Resource r = new Resource(getCaller().getUser(),(String) inputValue, marker);
			setValue(r);
		}
	}

	@Override
	protected String getPQLStringInternal() {
		if( inputValue instanceof Resource ) {
			Resource res = (Resource) inputValue;
			return "'" + res.getResourceName() + "'";
		}
		
		return "'" + inputValue + "'";
	}
}
