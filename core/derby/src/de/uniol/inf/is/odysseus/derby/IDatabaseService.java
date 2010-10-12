package de.uniol.inf.is.odysseus.derby;

import java.sql.Connection;

public interface IDatabaseService {

	public Connection getDatabaseConnection();
}
