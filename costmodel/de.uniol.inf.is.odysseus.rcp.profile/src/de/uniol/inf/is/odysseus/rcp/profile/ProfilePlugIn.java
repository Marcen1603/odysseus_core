package de.uniol.inf.is.odysseus.rcp.profile;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;

public class ProfilePlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(ProfilePlugIn.class);
	
	private static OperatorCostModel<? extends IPhysicalOperator> operatorCostModel;
	private static IServerExecutor serverExecutor;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	@SuppressWarnings("unchecked")
	public void bindCostModel( ICostModel<?> costModel ) {
		try {
			if( costModel instanceof OperatorCostModel ) {
				operatorCostModel = (OperatorCostModel<? extends IPhysicalOperator>)costModel;
				
				LOG.debug("Operator cost model bound: {}", costModel);
			} else {
				LOG.error("Profiling is only with operator cost model possible");
			}
		} catch( Throwable t ) {
			LOG.error("Could not bind cost model", t);
		}
	}
	
	public void bindExecutor( IExecutor executor ) {
		if( executor instanceof IServerExecutor ) {
			serverExecutor = (IServerExecutor) executor;
			
			LOG.debug("ServerExecutor bound: {}", executor);
		}
	}
	
	public void unbindCostModel( ICostModel<?> costModel ) {
		if( costModel == operatorCostModel ) {
			operatorCostModel = null;
			
			LOG.debug("Operator cost model unbound: {}", costModel);
		}
	}
	
	public void unbindExecutor( IExecutor executor ) {
		if( executor == serverExecutor ) {
			serverExecutor = null;
			
			LOG.debug("ServerExecutor unbound: {}", executor);
		}
	}
	
	public static OperatorCostModel<? extends IPhysicalOperator> getOperatorCostModel() {
		return operatorCostModel;
	}
	
	public static boolean hasOperatorCostModel() {
		return getOperatorCostModel() != null;
	}
	
	public static IServerExecutor getServerExecutor() {
		return serverExecutor;
	}
	
	public static boolean hasServerExecutor() {
		return getServerExecutor() != null;
	}
}
