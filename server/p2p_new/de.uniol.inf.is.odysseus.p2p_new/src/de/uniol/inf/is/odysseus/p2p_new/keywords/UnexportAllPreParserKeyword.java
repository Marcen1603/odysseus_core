package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Map;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class UnexportAllPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "UNEXPORTALL";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {
		ImmutableList<SourceAdvertisement> exportedSources = P2PDictionary.getInstance().getExportedSources();
		for( SourceAdvertisement exportedSource : exportedSources ) {
			P2PDictionary.getInstance().removeSourceExport(exportedSource.getName());
		}
		return null;
	}

}
