package de.uniol.inf.is.odysseus.net.communication;

public interface IMessageAnswerCallback<T extends IMessage> {

	public void answerReceived(T message);
	public void answerFailed(OdysseusNodeCommunicationException cause);

}
