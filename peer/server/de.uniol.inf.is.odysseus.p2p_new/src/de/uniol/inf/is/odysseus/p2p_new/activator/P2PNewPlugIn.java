package de.uniol.inf.is.odysseus.p2p_new.activator;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.broadcast.BroadcastRequestListener;
import de.uniol.inf.is.odysseus.p2p_new.broadcast.BroadcastRequestSender;
import de.uniol.inf.is.odysseus.p2p_new.communication.CommunicationAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.communication.CommunicationAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveMultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.MultipleSourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.RemoveMultipleSourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.RemoveSourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.SourceAdvertisementInstantiator;

public class P2PNewPlugIn implements BundleActivator {

	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);
	private static final int BROADCAST_INTERVAL = 5000;

	private static Bundle bundle;

	private BroadcastRequestListener listener;
	private BroadcastRequestSender broadcastSender;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		registerAdvertisementTypes();
		
		bundle = bundleContext.getBundle();
		
		listener = new BroadcastRequestListener();
		Thread t = new Thread(listener);
		t.setName("Peer Broadcast listener");
		t.setDaemon(true);
		t.start();
		
		broadcastSender = new BroadcastRequestSender(BROADCAST_INTERVAL);
		broadcastSender.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		broadcastSender.stopRunning();
		
		listener.stopListener();

		bundle = null;
	}
	
	public static Bundle getBundle() {
		return bundle;
	}

	private static void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(CommunicationAdvertisement.getAdvertisementType(), new CommunicationAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(RemoveSourceAdvertisement.getAdvertisementType(), new RemoveSourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(MultipleSourceAdvertisement.getAdvertisementType(), new MultipleSourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(RemoveMultipleSourceAdvertisement.getAdvertisementType(), new RemoveMultipleSourceAdvertisementInstantiator());
	}
}
