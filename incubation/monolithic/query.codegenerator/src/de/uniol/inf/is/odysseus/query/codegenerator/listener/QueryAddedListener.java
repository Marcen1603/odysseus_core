package de.uniol.inf.is.odysseus.query.codegenerator.listener;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.queryBuildSetting.TestSetting;
import de.uniol.inf.is.odysseus.query.codegenerator.CAnalyseServiceBinding;

public class QueryAddedListener implements IQueryAddedListener{

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds,
			QueryBuildConfiguration buildConfig, String parserID,
			ISession user, Context context) {
		if(buildConfig.get(TestSetting.class) != null){

			TransformationParameter transformationParameter= buildConfig.get(TestSetting.class).getValue();
			transformationParameter.setQueryId(queryIds.get(0));
			
			CAnalyseServiceBinding.getAnalyseComponent().startQueryTransformation(transformationParameter, null);

	
		}
	
		
	}

}
