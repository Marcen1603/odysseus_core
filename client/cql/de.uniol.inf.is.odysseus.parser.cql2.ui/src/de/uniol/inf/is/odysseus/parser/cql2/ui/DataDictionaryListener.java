package de.uniol.inf.is.odysseus.parser.cql2.ui;

import java.util.Set;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql2.typing.ExpressionsTypeProvider;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class DataDictionaryListener implements IUpdateEventListener {

	public static DataDictionaryListener instance = null;

	public static DataDictionaryListener getInstance() {
		return instance == null ? (instance = new DataDictionaryListener()) : instance;
	}

	
	
	private DataDictionaryListener() {}

	@Override
	public void eventOccured(String type) {
		if (type == IUpdateEventListener.SESSION) {
			Activator.registerDataDictionaryListener();
		}
		ISession user = OdysseusRCPPlugIn.getActiveSession();
		if(user != null) {
			Set<SDFSchema> schema = OdysseusRCPPlugIn.getExecutor().getStreamsAndViewsInformation(user).stream()
					.map(e -> e.getOutputSchema()).collect(Collectors.toSet());
			ExpressionsTypeProvider.setOutputSchema(schema);
		}
	}
}
