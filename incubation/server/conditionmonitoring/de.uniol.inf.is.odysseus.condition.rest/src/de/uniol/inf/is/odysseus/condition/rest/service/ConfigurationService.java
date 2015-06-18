package de.uniol.inf.is.odysseus.condition.rest.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.uniol.inf.is.odysseus.condition.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.QueryInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ServerConfiguration;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class ConfigurationService {

	public static void runConfiguration(Configuration config, ISession session) {
		runServerConfiguration(config.getServerConfiguration(), session);
	}

	private static void runServerConfiguration(ServerConfiguration serverConfig, ISession session) {
		try {
			// First the sources from files
			for (QueryInformation sourcePath : serverConfig.getSourcePaths()) {
				String content = new String(Files.readAllBytes(Paths.get(sourcePath.getContent())));
				ExecutorServiceBinding.getExecutor().addQuery(content, sourcePath.getParser(), session, new Context());
			}

			// Then sources from strings
			for (QueryInformation source : serverConfig.getSources()) {
				ExecutorServiceBinding.getExecutor().addQuery(source.getContent(), source.getParser(), session,
						new Context());
			}

			// After the sources we can start the queries
			for (QueryInformation query : serverConfig.getQueryPaths()) {
				String content = new String(Files.readAllBytes(Paths.get(query.getContent())));
				ExecutorServiceBinding.getExecutor().addQuery(content, query.getParser(), session, new Context());
			}

			// Last but not least the queries from strings
			for (QueryInformation query : serverConfig.getQueries()) {
				ExecutorServiceBinding.getExecutor().addQuery(query.getContent(), query.getParser(), session,
						new Context());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
