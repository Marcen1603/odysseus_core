package de.uniol.inf.is.odysseus.condition.rest.datatypes;

public class Configuration {

	private ServerConfiguration serverConfiguration;
	private ClientConfiguration clientConfiguration;

	public ServerConfiguration getServerConfiguration() {
		return serverConfiguration;
	}

	public void setServerConfiguration(ServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	public ClientConfiguration getClientConfiguration() {
		return clientConfiguration;
	}

	public void setClientConfiguration(ClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}

}
