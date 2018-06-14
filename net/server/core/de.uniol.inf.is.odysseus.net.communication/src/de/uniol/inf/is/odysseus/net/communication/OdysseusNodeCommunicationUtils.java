package de.uniol.inf.is.odysseus.net.communication;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public class OdysseusNodeCommunicationUtils {

	private static final long MAX_WAIT_TIME_MILLIS = 20 * 1000;

	@SuppressWarnings("unchecked")
	public static <T extends IMessage> T sendAndWaitForAnswer(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, Class<T> answerMessageType) throws OdysseusNodeCommunicationException {
		return (T) sendAndWaitForAnswer(communicator, destination, message, MAX_WAIT_TIME_MILLIS, answerMessageType);
	}

	@SafeVarargs
	public static IMessage sendAndWaitForAnswer(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, Class<? extends IMessage>... answerMessageTypes) throws OdysseusNodeCommunicationException {
		return sendAndWaitForAnswer(communicator, destination, message, MAX_WAIT_TIME_MILLIS, answerMessageTypes);
	}
	
	@SafeVarargs
	public static IMessage sendAndWaitForAnswer(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, long waitTimeMillis, Class<? extends IMessage>... answerMessageTypes) throws OdysseusNodeCommunicationException {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(destination, "destination must not be null!");
		Preconditions.checkNotNull(message, "message must not be null!");
		Preconditions.checkNotNull(answerMessageTypes, "answerMessageTypes must not be null!");
		Preconditions.checkArgument(answerMessageTypes.length > 0, "answerMessageTypes must not be empty");

		OdysseusNodeCommunicatorAnswerListener listener = new OdysseusNodeCommunicatorAnswerListener(answerMessageTypes, destination);
		try {
			for (Class<? extends IMessage> answerMessageType : answerMessageTypes) {
				communicator.addListener(listener, answerMessageType);
			}
			communicator.send(destination, message);

			return listener.waitForAnswer(waitTimeMillis);
		} catch (OdysseusNodeCommunicationException e) {
			throw e;
		} finally {
			for (Class<? extends IMessage> answerMessageType : answerMessageTypes) {
				communicator.removeListener(listener, answerMessageType);
			}
		}
	}

	@SafeVarargs
	public static void sendAndWaitForAnswerAsync(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, IGenericMessageAnswerCallback callback, Class<? extends IMessage>... answerMessageTypes) {
		sendAndWaitForAnswerAsync(communicator, destination, message, MAX_WAIT_TIME_MILLIS, callback, answerMessageTypes);
	}

	public static <T extends IMessage> void sendAndWaitForAnswerAsync(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, long waitMillis, IMessageAnswerCallback<T> callback, Class<T> answerMessageType) {
		sendAndWaitForAnswerAsync(communicator, destination, message, waitMillis, new IGenericMessageAnswerCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void answerReceived(IMessage message) {
				callback.answerReceived((T)message);
			}
			
			@Override
			public void answerFailed(OdysseusNodeCommunicationException cause) {
				callback.answerFailed(cause);
			}
		}, answerMessageType);
	}
	
	public static <T extends IMessage> void sendAndWaitForAnswerAsync(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, IMessageAnswerCallback<T> callback, Class<T> answerMessageType) {
		sendAndWaitForAnswerAsync(communicator, destination, message, MAX_WAIT_TIME_MILLIS, callback, answerMessageType);
	}

	@SafeVarargs
	public static void sendAndWaitForAnswerAsync(IOdysseusNodeCommunicator communicator, IOdysseusNode destination, IMessage message, long waitTimeMillis, IGenericMessageAnswerCallback callback, Class<? extends IMessage>... answerMessageTypes) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(destination, "destination must not be null!");
		Preconditions.checkNotNull(message, "message must not be null!");
		Preconditions.checkNotNull(answerMessageTypes, "answerMessageTypes must not be null!");
		Preconditions.checkArgument(answerMessageTypes.length > 0, "answerMessageTypes must not be empty");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				OdysseusNodeCommunicatorAnswerListener listener = new OdysseusNodeCommunicatorAnswerListener(answerMessageTypes, destination);
				try {
					for (Class<? extends IMessage> answerMessageType : answerMessageTypes) {
						communicator.addListener(listener, answerMessageType);
					}
					communicator.send(destination, message);

					callback.answerReceived(listener.waitForAnswer(waitTimeMillis));

				} catch (OdysseusNodeCommunicationException e) {
					callback.answerFailed(e);
				} finally {
					for (Class<? extends IMessage> answerMessageType : answerMessageTypes) {
						communicator.removeListener(listener, answerMessageType);
					}
				}
			}
		});
		t.setName("Node communication answer wait and process");
		t.setDaemon(true);
		t.start();
	}
}

class OdysseusNodeCommunicatorAnswerListener implements IOdysseusNodeCommunicatorListener {

	private final Class<? extends IMessage>[] answerMessageTypes;
	private final IOdysseusNode destinationNode;
	private final Object syncObject = new Object();

	private IMessage answer;

	public OdysseusNodeCommunicatorAnswerListener(Class<? extends IMessage>[] answerMessageTypes, IOdysseusNode destinationNode) {
		Preconditions.checkNotNull(answerMessageTypes, "answerMessageTypes must not be null!");
		Preconditions.checkArgument(answerMessageTypes.length > 0, "answerMessageTypes must not be empty");
		Preconditions.checkNotNull(destinationNode, "destinationNode must not be null!");

		this.answerMessageTypes = answerMessageTypes;
		this.destinationNode = destinationNode;
	}

	public IMessage waitForAnswer(long waitTime) throws OdysseusNodeCommunicationException {
		long currentWaitTime = 0;

		IMessage answered = null;
		do {
			synchronized (syncObject) {
				answered = answer;
			}

			if (answered == null) {
				waitSomeTime();
				currentWaitTime += 100;
			}
		} while (answered == null && currentWaitTime < waitTime);

		if (answered == null) {
			throw new OdysseusNodeCommunicationException("Waiting for answer took too long");
		}

		return answered;
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (!senderNode.equals(destinationNode)) {
			return;
		}

		synchronized (syncObject) {
			if (answer != null) {
				return;
			}
		}

		for (Class<? extends IMessage> answerMessageType : answerMessageTypes) {
			if (answerMessageType.isInstance(message)) {
				synchronized (syncObject) {
					answer = message;
					return;
				}
			}
		}
	}

	private void waitSomeTime() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}
}
