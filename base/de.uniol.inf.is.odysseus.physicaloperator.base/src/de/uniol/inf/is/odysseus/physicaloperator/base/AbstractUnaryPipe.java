package de.uniol.inf.is.odysseus.physicaloperator.base;

public abstract class AbstractUnaryPipe<Read, Write> extends
		AbstractPipe<Read, Write> {

	protected AbstractUnaryPipe(){
		setNoOfInputPort(1);
	}
	
	@Override
	public final void subscribeTo(ISource<? extends Read> source, int port) {
		if (port != 0) {
			throw new IllegalArgumentException(
					"Subscription on illegal port (" + port + ") for unary operator");
		}
		super.subscribeTo(source, port);
	}
	
	public final void subscribeTo(ISource<Read> source){
		super.subscribeTo(source,0);
	}

}
