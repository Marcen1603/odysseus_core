package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCSenderAORule extends AbstractRule {
	
	public AbstractCSenderAORule(String name) {
		super(name);
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
			if(logicalOperator instanceof SenderAO){
				SenderAO senderAO = (SenderAO) logicalOperator;
			
				ITenant tenant = UserManagementProvider.getDefaultTenant();
				ISink<?> sinkPO = DataDictionaryProvider.getDataDictionary(tenant).getSinkplan(senderAO.getSinkname());
				
				if(sinkPO != null){
					return true;
				}
			
			}else{
				return false;
			}
			
			return false;
	}
	
	
	@Override
	public Class<?> getConditionClass() {
		return SenderAO.class;
	}
	
	
	@Override
	public void analyseOperator(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		// TODO Auto-generated method stub
		
	}

}
