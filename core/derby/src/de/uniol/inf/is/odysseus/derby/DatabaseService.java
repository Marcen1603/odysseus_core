package de.uniol.inf.is.odysseus.derby;

import java.sql.Connection;

public class DatabaseService implements IDatabaseService{

	@Override
	public Connection getDatabaseConnection() {
		return Activator.getConnection();
	}

}
