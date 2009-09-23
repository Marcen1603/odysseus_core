package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

public class DefaultSourceContent extends AbstractOperator{

	public DefaultSourceContent(String name, String typ, Image image,
			Collection<IParamConstruct<?>> constructParameters,
			Collection<IParamSetter<?>> setterParameters) {
		super(name, typ, image, constructParameters, setterParameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPipe() {
		return false;
	}

	@Override
	public boolean isOnlySink() {
		return false;
	}

	@Override
	public boolean isOnlySource() {
		return true;
	}
	
}
