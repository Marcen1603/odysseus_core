package de.uniol.inf.is.odysseus.iql.qdl.parser;



import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.qdl.services.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;

public class OdysseusScriptGenerator {
	

	public String createOdysseusScript(IQDLQuery query, IDataDictionary dd, ISession session) {
		query.setDataDictionary(dd);
		query.setSession(session);		
		
		Collection<ILogicalOperator> roots = getRoots(query.execute());
		TopAO topAO = createTopAO(roots);
		String pql = QDLServiceBinding.getPQLGenerator().generatePQLStatement(topAO);
		
		return pql;
	}
	

	public TopAO createTopAO(Collection<ILogicalOperator> sources) {
		TopAO topAO = new TopAO();
		int i = 0; 
		for (ILogicalOperator op : sources) {
			topAO.subscribeToSource(op, i++, 0, op.getOutputSchema());
		}
		return topAO;
	}

	
	private Collection<ILogicalOperator> getRoots(Collection<ILogicalOperator> operators) {
		Collection<ILogicalOperator> roots = new HashSet<>();
		for (ILogicalOperator op : operators) {
			if (op.getSubscriptions().size() == 0) {
				roots.add(op);
			}
		}
		return roots;
	}
}
