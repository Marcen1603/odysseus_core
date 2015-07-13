package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class UnexportAllPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "UNEXPORTALL";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		ImmutableList<SourceAdvertisement> exportedSources = P2PDictionary.getInstance().getExportedSources();
		List<String> srcNames = Lists.newArrayList();
		for( SourceAdvertisement exportedSource : exportedSources ) {
			srcNames.add(exportedSource.getName());
		}
		
		P2PNetworkManagerService.waitForStart();
		P2PDictionary.getInstance().removeSourcesExport(srcNames);
		return null;
	}

}
