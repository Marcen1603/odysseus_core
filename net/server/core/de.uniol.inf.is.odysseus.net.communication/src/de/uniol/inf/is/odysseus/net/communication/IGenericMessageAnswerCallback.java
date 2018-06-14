package de.uniol.inf.is.odysseus.net.communication;

public interface IGenericMessageAnswerCallback {

	public void answerReceived(IMessage message);
	public void answerFailed(OdysseusNodeCommunicationException cause);

}
