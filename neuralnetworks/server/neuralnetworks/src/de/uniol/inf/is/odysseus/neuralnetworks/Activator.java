package de.uniol.inf.is.odysseus.neuralnetworks;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.NeuralNetworkFactory;
import de.uniol.inf.is.odysseus.neuralnetworks.strategy.IStrategy;
import de.uniol.inf.is.odysseus.neuralnetworks.strategy.StrategyFactory;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}

	private static IOperatorBuilderFactory builderfactory;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public void bindNeuralNetwork(INeuralNetwork<ITimeInterval> neuralnetwork)
	{
	    
        NeuralNetworkFactory.getInstance().addNeuralNetwork(neuralnetwork);
        
    }
	
    public void unbindNeuralNetwork(INeuralNetwork<ITimeInterval> neuralnetwork)
    {
        NeuralNetworkFactory.getInstance().removeNeuralNetwork(neuralnetwork);
    }
	
    public void bindStrategy(IStrategy<ITimeInterval> strategy)
    {
        StrategyFactory.getInstance().addStrategy(strategy);
    }
    
    public void unbindStrategy(IStrategy<ITimeInterval> strategy)
    {
        StrategyFactory.getInstance().removeStrategy(strategy);
    }
    
    public void bindOperatorBuilderFactory(IOperatorBuilderFactory obf){
        builderfactory = obf;
    }
    
    public void unbindOperatorBuilderFactory(IOperatorBuilderFactory obf){
        builderfactory = null;
    }

    public static IOperatorBuilderFactory getBuilderfactory() {
        return builderfactory;
    }   

    
}
