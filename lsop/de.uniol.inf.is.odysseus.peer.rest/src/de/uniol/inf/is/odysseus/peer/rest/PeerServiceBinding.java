package de.uniol.inf.is.odysseus.peer.rest;

import net.jxta.document.AdvertisementFactory;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisementListener;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisementSender;

public class PeerServiceBinding {

	private static IServerExecutor executor;	
	private static IQueryPartController queryPartController;
	private static IPeerDictionary peerDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;
	
	private static WebserviceAdvertisementListener webserviceAdvListener = new WebserviceAdvertisementListener();

	
	public void bindExecutor(IExecutor ex) {
		executor = (IServerExecutor) ex;
	}

	public void unbindExecutor(IExecutor ex) {
		executor = null;
	}
	
	public static IServerExecutor getExecutor() {
		return executor;
	}
	
	
	public static void bindQueryPartController(IQueryPartController controller) {
		queryPartController = controller;
	}
	
	public static void unbindQueryPartController(IQueryPartController controller) {
		queryPartController = null;
	}
	
	
	public static IQueryPartController getQueryPartController() {
		return queryPartController;
	}
	
	public static void bindPeerDictionary(IPeerDictionary dictionary) {
		peerDictionary = dictionary;
	}
	
	public static void unbindPeerDictionary(IPeerDictionary dictionary) {
		peerDictionary = null;
	}
	
	
	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}

	
	
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;

		
		p2pNetworkManager.addListener(new IP2PNetworkListener() {
			
			@Override
			public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void networkStarted(IP2PNetworkManager sender) {
				AdvertisementFactory.registerAdvertisementInstance(WebserviceAdvertisement.getAdvertisementType(), new WebserviceAdvertisementInstantiator());
				new WebserviceAdvertisementSender().publishWebserviceAdvertisement(jxtaServicesProvider, p2pNetworkManager.getLocalPeerID(),p2pNetworkManager.getLocalPeerGroupID());
				PeerServiceBinding.getP2PNetworkManager().addAdvertisementListener(webserviceAdvListener);
			}
		});
	}
	
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = null;
	}	

	public static IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}
	

	
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}
	
	public static IJxtaServicesProvider getJxtaServicesProvider() {
		return jxtaServicesProvider;
	}

	public static WebserviceAdvertisementListener getWebserviceAdvListener() {
		return webserviceAdvListener;
	}
	
}
