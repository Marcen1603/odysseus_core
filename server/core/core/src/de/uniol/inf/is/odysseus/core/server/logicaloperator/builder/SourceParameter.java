package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public class SourceParameter extends AbstractParameter<AccessAO> {

	private static final long serialVersionUID = 1464553400244793989L;

	@Override
	protected void internalAssignment() {
		if( inputValue instanceof AccessAO ) {
			setValue((AccessAO)inputValue);
			return;
		}
		
		ILogicalPlan source = getDataDictionary().getViewOrStream((String) this.inputValue, getCaller());
		AccessAO accessAO = new AccessAO();
		if (Resource.containsUser(getCaller().getUser(), (String) this.inputValue)) {
			accessAO.setAccessAOName(new Resource((String) this.inputValue));
		} else {
			accessAO.setAccessAOName(new Resource(getCaller().getUser(), ((String) this.inputValue)));
		}
		accessAO.setOutputSchema(source.getOutputSchema());
		setValue(accessAO);
	}

	@Override
	protected String getPQLStringInternal() {
		return "'" + getValue().getAccessAOName() + "'";
	}

}
