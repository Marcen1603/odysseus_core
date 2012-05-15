package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;


public class StringSocketInput extends SocketInput<String> {

	final private String objectDelimitter;
	final private boolean delimitterPartOfObject;
	private StringBuffer buffer = new StringBuffer();

	public StringSocketInput(String hostname, int port, String user,
			String password, String objectDelimitter, boolean delimitterPartOfObject) {
		super(hostname, port, user, password);
		this.objectDelimitter = objectDelimitter;
		this.delimitterPartOfObject = delimitterPartOfObject;
	}

	@Override
	public void init() {
		super.init();
		
	}
	
	@Override
	public boolean hasNext() {
		// Read data into buffer until objectDelimitter is read

		return false;
	}

	@Override
	public String getNext() {
		String ret = buffer.toString();
		buffer = new StringBuffer();
		return ret;
	}

}
