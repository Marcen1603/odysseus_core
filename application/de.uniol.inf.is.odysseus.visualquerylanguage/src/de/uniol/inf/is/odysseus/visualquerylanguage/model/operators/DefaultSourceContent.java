package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

public class DefaultSourceContent extends AbstractOperator{

	public DefaultSourceContent(String name, String typ,
			Collection<IParam<?>> constructParameters,
			Collection<IParam<?>> setterParameters) {
		super(name, typ, constructParameters, setterParameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPipe() {
		return false;
	}

	@Override
	public boolean isSink() {
		return false;
	}

	@Override
	public boolean isSource() {
		return true;
	}
	
}
