package de.uniol.inf.is.odysseus.p2p_new;

import java.io.IOException;
import java.util.Enumeration;

import com.google.common.base.Optional;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;

public interface IJxtaServicesProvider {

	public void publish(Advertisement adv) throws IOException;

	public void publish(Advertisement adv, long lifetime, long expirationTime) throws IOException;

	public void publishInfinite(Advertisement adv) throws IOException;

	public void remotePublish(Advertisement adv);

	public void remotePublish(Advertisement adv, long expirationTime);
	
	public void remotePublishToPeer( Advertisement adv, PeerID peerID, long expirationTime );

	public void remotePublishInfinite(Advertisement adv);

	public void flushAdvertisement(Advertisement adv) throws IOException;

	public void getRemoteAdvertisements();

	public Enumeration<Advertisement> getLocalAdvertisements() throws IOException ;

	public void getRemotePeerAdvertisements();

	public Enumeration<Advertisement> getPeerAdvertisements() throws IOException ;

	public boolean isReachable(PeerID peerID);

	boolean isReachable(PeerID peerID, boolean tryToConnect);
	
	public Optional<String> getRemotePeerAddress(PeerID peerID);

	public InputPipe createInputPipe(PipeAdvertisement pipeAdv, PipeMsgListener listener) throws IOException ;

	public OutputPipe createOutputPipe(PipeAdvertisement pipeAdv, long timeoutMillis) throws IOException ;
}
