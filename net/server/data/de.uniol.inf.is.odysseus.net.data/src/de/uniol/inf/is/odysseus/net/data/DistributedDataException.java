package de.uniol.inf.is.odysseus.net.data;

public class DistributedDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public DistributedDataException() {
		super();
	}

	public DistributedDataException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public DistributedDataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DistributedDataException(String arg0) {
		super(arg0);
	}

	public DistributedDataException(Throwable arg0) {
		super(arg0);
	}
	
}
