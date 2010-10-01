package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.FileAccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.relational.FileAccessPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFileAccessAORule extends AbstractTransformationRule<FileAccessAO> {

	@Override
	public int getPriority() {		
		return 1;
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public void execute(FileAccessAO fileAccessAO, TransformationConfiguration transformConfig) {
		String fileAccessPOName = fileAccessAO.getSource().getURI(false);
		ISource fileAccessPO = new FileAccessPO(fileAccessAO.getPath(), fileAccessAO.getFileType(), fileAccessAO.getDelay());
		
		fileAccessPO.setOutputSchema(fileAccessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(fileAccessPOName, fileAccessPO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(fileAccessAO, fileAccessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(fileAccessAO);
		insert(fileAccessPO);
	
	}

	/**
	 * @TODO 
	 * 
	 * SourceType?!?
	 * 
	 */
	
	@Override
	public boolean isExecutable(FileAccessAO fileAccessAO, TransformationConfiguration trafo) {
		if(WrapperPlanFactory.getAccessPlan(fileAccessAO.getSource().getURI()) == null){
			if(fileAccessAO.getSourceType().equals("FileAccessPO")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "FileAccessAO -> FileAccessPO";
	}
	
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
