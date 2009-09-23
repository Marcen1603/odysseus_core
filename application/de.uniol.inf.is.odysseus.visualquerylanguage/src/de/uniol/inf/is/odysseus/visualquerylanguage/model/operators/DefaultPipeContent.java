package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

public class DefaultPipeContent extends AbstractOperator {



	public DefaultPipeContent(String name, String typ, Image image,
			Collection<IParamConstruct<?>> constructParameters,
			Collection<IParamSetter<?>> setterParameters) {
		super(name, typ, image, constructParameters, setterParameters);
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
