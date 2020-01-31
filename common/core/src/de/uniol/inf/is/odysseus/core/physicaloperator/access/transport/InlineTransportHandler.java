package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class InlineTransportHandler extends AbstractPushTransportHandler {

	public static final String NAME = "Inline";
	public static final String PERIOD = NAME + ".period";
	public static final String CONTENT = NAME + ".Content";
	private Timer timer;
	private long period;
	private String content;
	private int current;

	public InlineTransportHandler() {
	}

	public InlineTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		options.checkRequiredException(CONTENT);
		period = options.getLong(PERIOD, 1000);
		content = options.get(CONTENT);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new InlineTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		fireOnConnect();
		startTimer();
	}

	private void startTimer() {
		current = 0;
		final String lines[] = content.split("&&");
		if (lines.length > 0) {
			timer = new Timer();
			final TimerTask task = new TimerTask() {
				@Override
				public void run() {
					if (current < lines.length) {
						InlineTransportHandler.this.fireProcess(lines[current++]);
					} else {
						try {
							InlineTransportHandler.this.processInClose();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			timer.schedule(task, 0, this.period);
		} else {
			try {
				processInClose();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopTimer() {
		timer.cancel();
		timer.purge();
		timer = null;
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void processInClose() throws IOException {
		stopTimer();
		fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
