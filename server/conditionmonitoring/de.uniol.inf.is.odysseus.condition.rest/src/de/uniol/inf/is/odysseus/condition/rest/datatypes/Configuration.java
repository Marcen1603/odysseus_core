package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Configuration {

	private UUID identifier = UUID.randomUUID();
	private String name;
	private String description;
	private List<String> users;
	private ServerConfiguration serverConfiguration;
	private ClientConfiguration clientConfiguration;

	public Configuration() {
		users = new ArrayList<String>();
	}
	
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

	/**
	 * The users which are allowed to get this configuration
	 * @return A list of usernames
	 */
	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	/**
	 * Adds a user to the list of allowed users
	 * @param username Name of the user
	 */
	public void addUser(String username) {
		users.add(username);
	}

}
