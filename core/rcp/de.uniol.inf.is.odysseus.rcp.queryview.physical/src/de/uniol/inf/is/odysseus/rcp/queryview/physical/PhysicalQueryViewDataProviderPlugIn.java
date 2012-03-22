package de.uniol.inf.is.odysseus.rcp.queryview.physical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class PhysicalQueryViewDataProviderPlugIn implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryViewDataProviderPlugIn.class);
    
    private static IServerExecutor serverExecutor;
    
	@Override
    public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
    public void stop(BundleContext bundleContext) throws Exception {
	}

	public void bindExecutor( IExecutor executor ) {
	    
	    if( executor instanceof IServerExecutor ) {
	        if( serverExecutor != null ) {
	            LOG.error("More than one executor bound!");
	        }
	        
	        serverExecutor = (IServerExecutor)executor;
	        LOG.debug("ServerExecutor " + executor + " bound");
	    } else {
	        LOG.error("PhysicalQueryViewDataProvider needs IServerExecutor but not " + executor);
	    }
	}
	
	public void unbindExecutor( IExecutor executor ) {
	    if( executor == serverExecutor) {
	        serverExecutor = null;
	        LOG.debug("ServerExecutor unbound: " + executor);
	    } else {
	        LOG.error("Trying to unbound unbounded or unknown executor " + executor);
	        LOG.error("Nothing unbound.");
	    }
	}
	
	public static IServerExecutor getServerExecutor() {
	    return serverExecutor;
	}
}
