package de.uniol.inf.is.odysseus.fusion;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.fusion.metadata.FusionProbability;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;


public class Activator implements BundleActivator {

    private static BundleContext context;

	@SuppressWarnings("unchecked")
    @Override
    public void start(BundleContext context) throws Exception {
		Activator.context = context; 
		MetadataRegistry.addMetadataType(FusionProbability.class, IFusionProbability.class);
	    MetadataRegistry.addMetadataType(FusionProbability.class, ITimeInterval.class, IFusionProbability.class);
	}

	@SuppressWarnings("unchecked")
    @Override
    public void stop(BundleContext context) throws Exception {
        MetadataRegistry.removeCombinedMetadataType(ITimeInterval.class, IFusionProbability.class);
        MetadataRegistry.removeMetadataType(IFusionProbability.class);
		Activator.context = null;
    }

	public static BundleContext getContext() {
	    return context;
    }
	
}
