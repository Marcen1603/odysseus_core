package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.parser.pql.AbstractPQLActivator;

public class Activator extends AbstractPQLActivator{
	public Activator() {
		super("Benchmark", new BenchmarkBuilder());
	}
}
