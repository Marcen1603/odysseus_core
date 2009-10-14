package de.uniol.inf.is.odysseus.parser.pql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;

public abstract class AbstractPQLActivator implements BundleActivator {

	private IOperatorBuilder builder;
	private String name;

	public AbstractPQLActivator(String name, IOperatorBuilder builder){
		this.builder = builder;
		this.name = name;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		PQLParserImpl.addOperatorBuilder(name, builder);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		PQLParserImpl.removeOperatorBuilder(name);
	}

}
