package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

public class DefaultPipeContent extends AbstractOperator {



	public DefaultPipeContent(String name, String typ,
			Collection<IParamConstruct<?>> constructParameters,
			Collection<IParamSetter<?>> setterParameters) {
		super(name, typ, constructParameters, setterParameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPipe() {
		return true;
	}

	@Override
	public boolean isOnlySink() {
		return false;
	}

	@Override
	public boolean isOnlySource() {
		return false;
	}
}
