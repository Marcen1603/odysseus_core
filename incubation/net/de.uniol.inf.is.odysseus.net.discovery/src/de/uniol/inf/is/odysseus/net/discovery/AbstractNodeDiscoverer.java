package de.uniol.inf.is.odysseus.net.discovery;

public abstract class AbstractNodeDiscoverer implements INodeDiscoverer {

	private boolean started = false;

	@Override
	public void start() {
		started = true;
	}

	@Override
	public void stop() {
		started = false;
	}

	@Override
	public final boolean isStarted() {
		return started;
	}

	@Override
	public final boolean isStopped() {
		return !isStarted();
	}

}
