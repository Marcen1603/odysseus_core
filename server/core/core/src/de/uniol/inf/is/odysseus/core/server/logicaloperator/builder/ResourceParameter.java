package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.collection.Resource;

public class ResourceParameter extends AbstractParameter<Resource> {

	private static final long serialVersionUID = -3605838409249325020L;

	@Override
	protected void internalAssignment() {
		if (inputValue instanceof String) {
			int pos = ((String) inputValue).indexOf(".");
			if (pos > 0) {
				setValue(new Resource(((String) inputValue).substring(0, pos),
						((String) inputValue).substring(pos + 1)));
			} else {
				setValue(new Resource(getCaller().getUser(),
						(String) inputValue));
			}
		} else if (inputValue instanceof Resource) {
			setValue((Resource) inputValue);
		}
	}

	@Override
	protected String getPQLStringInternal() {
		return "'" + inputValue + "'";
	}
	
}
