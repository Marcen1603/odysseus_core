package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;

public class HasRoleFunction extends AbstractBooleanStringFunction {

	private static final long serialVersionUID = -4724952242276852639L;

	@Override
	public String getSymbol() {
		return "hasRole";
	}

	@Override
	public Boolean getValue() {
		return UserManagement.getUsermanagement().hasRole((String)getInputValue(0), (String)getInputValue(1));
	}

}
