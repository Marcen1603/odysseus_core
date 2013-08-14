package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ExportAllPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "EXPORTALL";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String transCfgName = (String) variables.get("TRANSCFG");
		
		if( Strings.isNullOrEmpty(transCfgName)) {
			throw new OdysseusScriptException("Cannot export: Name of transformation configuration not set!");
		}
		
		if( !ServerExecutorService.isBound() ) {
			throw new OdysseusScriptException("Cannot export: Server executor is not bound");
		}
		
		if( ServerExecutorService.getServerExecutor().getQueryBuildConfiguration(transCfgName) == null) {
			throw new OdysseusScriptException("Cannot export: TransformationConfiguration '" + transCfgName + "' not found");
		}

	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		final String transCfgName = (String) variables.get("TRANSCFG");

		List<String> sources = determineCurrentSourceNames();
		for( String source : sources ) {
			if( !P2PDictionary.getInstance().isExported(source) && !P2PDictionary.getInstance().isImported(source)) {
				try {
					P2PDictionary.getInstance().exportSource(source, transCfgName);
				} catch (PeerException e) {
					throw new OdysseusScriptException("Could not export " + source, e);
				}
			}
		}
		
		return null;
	}

	private static List<String> determineCurrentSourceNames() {
		Set<Entry<String, ILogicalOperator>> streamsAndViews = ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getStreamsAndViews(SessionManagementService.getActiveSession());
		List<String> sourceNames = Lists.newArrayList();
		for( Entry<String, ILogicalOperator> streamOrView : streamsAndViews ) {
			sourceNames.add(streamOrView.getKey());
		}
		return sourceNames;
	}

}
