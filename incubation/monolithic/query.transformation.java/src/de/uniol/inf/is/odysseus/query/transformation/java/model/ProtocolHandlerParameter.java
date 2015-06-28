package de.uniol.inf.is.odysseus.query.transformation.java.model;

public class ProtocolHandlerParameter {
	
	private String filename = "";
	private String transportHandler = "";
	private String dataHandler = "";
	private String wrapper = "";
	private String protocolHandler = "";
	
	public ProtocolHandlerParameter(String filename, String transportHandler,
			String dataHandler, String wrapper, String protocolHandler) {
		super();
		this.filename = filename;
		this.transportHandler = transportHandler;
		this.dataHandler = dataHandler;
		this.wrapper = wrapper;
		this.protocolHandler = protocolHandler;
	}

	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTransportHandler() {
		return transportHandler;
	}

	public void setTransportHandler(String transportHandler) {
		this.transportHandler = transportHandler;
	}

	public String getDataHandler() {
		return dataHandler;
	}

	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}

	public String getWrapper() {
		return wrapper;
	}

	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}

	public String getProtocolHandler() {
		return protocolHandler;
	}

	public void setProtocolHandler(String protocolHandler) {
		this.protocolHandler = protocolHandler;
	}
	

}
