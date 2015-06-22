package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.UUID;

public class Configuration {

	private UUID identifier = UUID.randomUUID();
	private String name;
	private String description;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getIdentifier() {
		return identifier;
	}

}
