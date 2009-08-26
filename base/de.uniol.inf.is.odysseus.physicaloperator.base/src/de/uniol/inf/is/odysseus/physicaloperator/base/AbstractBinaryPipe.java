package de.uniol.inf.is.odysseus.physicaloperator.base;


public abstract class AbstractBinaryPipe<Read, Write> extends AbstractPipe<Read, Write>
		implements IPipe<Read, Write> {
	
	int LEFT = 0;
	int RIGHT = 1;
	
	protected AbstractBinaryPipe(){
		setNoOfInputPort(2);
	}

	@Override
	public final void subscribeTo(ISource<? extends Read> source, int port) {
		if (port != 0 && port != 1) {
			throw new IllegalArgumentException("Subscription on illegal port ("
					+ port + ") for binary opperator");
		}
		super.subscribeTo(source, port);
	}
	
	public void close(){};
}
