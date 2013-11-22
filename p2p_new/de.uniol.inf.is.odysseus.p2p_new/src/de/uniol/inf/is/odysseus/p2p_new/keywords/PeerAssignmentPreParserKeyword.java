package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.parameter.PeerAssignmentParameter;
import de.uniol.inf.is.odysseus.p2p_new.provider.PeerAssignmentProvider;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * This class represents the keyword for the peer assignment strategies. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #PEERASSIGNMENT name
 * @author Michael Brand
 */
public class PeerAssignmentPreParserKeyword extends AbstractPreParserKeyword {
	
	/**
	 * The string representation of the keyword.
	 */
	public static final String KEYWORD = "PEERASSIGNMENT";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		
		if(!PeerAssignmentProvider.getInstance().getPeerAssignmentNames().contains(parameter))
			throw new OdysseusScriptException("Specified peer assignment strategy '" + parameter + 
					"' not found.");
		
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		
		if(!Strings.isNullOrEmpty(parameter))
			addSettings.add(new PeerAssignmentParameter(parameter));
		
		return null;
		
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return PeerAssignmentProvider.getInstance().getPeerAssignmentNames();
	}

}