package de.uniol.inf.is.odysseus.peer.jxta;

import java.io.IOException;
import java.util.Collection;

import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.endpoint.EndpointService;
import net.jxta.peer.PeerID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import com.google.common.base.Optional;

public interface IJxtaServicesProvider {

	public boolean isActive();
	
	public void publish(Advertisement adv) throws IOException;

	public void publish(Advertisement adv, long lifetime, long expirationTime) throws IOException;

	public void publishInfinite(Advertisement adv) throws IOException;

	public void remotePublish(Advertisement adv);

	public void remotePublish(Advertisement adv, long expirationTime);
	
	public void remotePublishToPeer( Advertisement adv, PeerID peerID, long expirationTime );

	public void remotePublishInfinite(Advertisement adv);

	public void flushAdvertisement(Advertisement adv) throws IOException;

	public void getRemoteAdvertisements();
	
	public void getRemoteAdvertisements(DiscoveryListener listener);

	public Collection<Advertisement> getLocalAdvertisements();
	
	public <T extends Advertisement> Collection<T> getLocalAdvertisements( Class<T> advertisementClass);

	public void getRemotePeerAdvertisements();

	public void getRemotePeerAdvertisements(DiscoveryListener listener);
	
	public Collection<PeerAdvertisement> getPeerAdvertisements();
	
	public boolean isReachable(PeerID peerID);

	boolean isReachable(PeerID peerID, boolean tryToConnect);
	
	public Optional<String> getRemotePeerAddress(PeerID peerID);

	public InputPipe createInputPipe(PipeAdvertisement pipeAdv, PipeMsgListener listener) throws IOException ;

	public OutputPipe createOutputPipe(PipeAdvertisement pipeAdv, long timeoutMillis) throws IOException ;

	public void addDiscoveryListener(DiscoveryListener listener);

	public void removeDiscoveryListener(DiscoveryListener listener);

	public EndpointService getEndpointService();

}
