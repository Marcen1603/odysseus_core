package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ExportAllPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "EXPORTALL";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		final String transCfgName = (String) variables.get("TRANSCFG");

		if (Strings.isNullOrEmpty(transCfgName)) {
			throw new OdysseusScriptException("Cannot export: Name of transformation configuration not set!");
		}

		if (!ServerExecutorService.isBound()) {
			throw new OdysseusScriptException("Cannot export: Server executor is not bound");
		}

		if (ServerExecutorService.getServerExecutor().getQueryBuildConfiguration(transCfgName) == null) {
			throw new OdysseusScriptException("Cannot export: TransformationConfiguration '" + transCfgName + "' not found");
		}

	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		List<String> sources = determineCurrentSourceNames();
		List<String> exportableSources = determineExportableSources(sources);
		
		P2PNetworkManager.waitForStart();
		try {
			P2PDictionary.getInstance().exportSources(exportableSources);
		} catch (PeerException e) {
			throw new OdysseusScriptException("Could not export " + exportableSources, e);
		}

		return null;
	}

	private static List<String> determineExportableSources(List<String> sources) {
		List<String> exportableSources = Lists.newArrayList();
		for (String source : sources) {
			if (!P2PDictionary.getInstance().isExported(source)) {
				exportableSources.add(source);
			}
		}
		return exportableSources;
	}

	private static List<String> determineCurrentSourceNames() {
		Set<Entry<Resource, ILogicalOperator>> streamsAndViews = ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getStreamsAndViews(SessionManagementService.getActiveSession());
		List<String> sourceNames = Lists.newArrayList();
		for (Entry<Resource, ILogicalOperator> streamOrView : streamsAndViews) {
			// FIXME: Use Resources
			sourceNames.add(streamOrView.getKey().toString());
		}
		return sourceNames;
	}

}
