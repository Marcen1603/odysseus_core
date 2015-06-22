package de.uniol.inf.is.odysseus.condition.rest.serverresources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.restlet.resource.Post;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.ClientConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.dto.request.RunConfigurationRequestDTO;
import de.uniol.inf.is.odysseus.condition.rest.service.ConfigurationService;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

public class RunConfigurationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "RunConfiguration";

	@Post
	public ClientConfiguration runConfiguration(RunConfigurationRequestDTO runRequest) {
		ISession session = loginWithToken(runRequest.getToken());
		Configuration config = null;
		try {
			String content = new String(Files.readAllBytes(Paths
					.get("D:\\Dropbox\\Studium\\Master\\Masterarbeit\\SVN\\GUIConfigs\\cohortConfig.txt")));
			Gson gson = new Gson();
			config = gson.fromJson(content, Configuration.class);
			ConfigurationService.runConfiguration(config, session);
//			SDFSchema out = ExecutorServiceBinding.getExecutor().getOutputSchema(4, session);
//			Map<Integer, SDFSchema> outputSchemaMap = ExecutorServiceBinding.getExecutor().getLogicalQueryByName("cohort1", session).getLogicalPlan().getOutputSchemaMap();
//			System.out.println(out);
//			System.out.println(outputSchemaMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config.getClientConfiguration();
	}

}
