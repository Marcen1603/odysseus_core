import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.latency.pql.LatencyCalculatorAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;


public class Activator implements BundleActivator {

	final static String CALCLATENCY = "CALCLATENCY";
	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putOperatorBuilderType(CALCLATENCY, LatencyCalculatorAOBuilder.class);		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType(CALCLATENCY);
	}

}
