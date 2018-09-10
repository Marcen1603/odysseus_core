package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IConnectionForWebservices;

public abstract class AbstractConnectionForWebservices implements IConnectionForWebservices {

	private List<Option> header = new ArrayList<>();

	@Override
	public void setHeaders(List<Option> header) {
		this.header = header;
	}

	protected List<Option> getHeader() {
		return header;
	}

}
