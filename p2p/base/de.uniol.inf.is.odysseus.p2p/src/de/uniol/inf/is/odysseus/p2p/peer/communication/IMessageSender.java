package de.uniol.inf.is.odysseus.p2p.peer.communication;

public interface IMessageSender<N,M,R> {
	public void sendMessage(N network,M message, R to);
}
