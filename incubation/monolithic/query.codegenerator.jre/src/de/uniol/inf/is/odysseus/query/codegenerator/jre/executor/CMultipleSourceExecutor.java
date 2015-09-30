package de.uniol.inf.is.odysseus.query.codegenerator.jre.executor;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.AbstractCExecutor;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;

public class CMultipleSourceExecutor extends AbstractCExecutor{

	public CMultipleSourceExecutor() {
		super("MultipleSourceExecutor", "JRE");
	}

	@Override
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> operatorList) {
		
		CodeFragmentInfo code = new CodeFragmentInfo();
		
		List<String> sourceOpList = new ArrayList<String>();
		
		for(ILogicalOperator sourceOp : operatorList){
			sourceOpList.add(JreCodegeneratorStatus.getInstance().getVariable(sourceOp));
		}
	
		StringTemplate startCodeTemplate = new StringTemplate("executor","multipleSourceExecutorStartCode");
		startCodeTemplate.getSt().add("operatorList", sourceOpList);
		
		code.addCode(startCodeTemplate.getSt().render());
		
		return code;
	}

	@Override
	public String getExecutorCode() {
		
		StringTemplate multipleSourceExecutorImplTemplate = new StringTemplate("executor","multipleSourceExecutorImpl");
	
		return multipleSourceExecutorImplTemplate.getSt().render();
	
	}
	

}
