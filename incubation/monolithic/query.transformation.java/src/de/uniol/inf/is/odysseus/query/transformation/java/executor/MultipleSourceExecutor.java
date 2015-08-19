package de.uniol.inf.is.odysseus.query.transformation.java.executor;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.executor.AbstractExecutor;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class MultipleSourceExecutor extends AbstractExecutor{

	public MultipleSourceExecutor() {
		super("MultipleSourceExecutor", "Java");
	}

	@Override
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> operatorList) {
		
		CodeFragmentInfo code = new CodeFragmentInfo();
		
		List<String> sourceOpList = new ArrayList<String>();
		
		for(ILogicalOperator sourceOp : operatorList){
		
			sourceOpList.add(JavaTransformationInformation.getInstance().getVariable(sourceOp));
		}
		

		StringTemplate startCodeTemplate = new StringTemplate("executor","multipleSourceExecutorStartCode");
		startCodeTemplate.getSt().add("operatorList", sourceOpList);
		
		code.addCode(startCodeTemplate.getSt().render());
		
		return code;
	}

	
	public String getExecutorCode() {
		StringTemplate multipleSourceExecutorImplTemplate = new StringTemplate("executor","multipleSourceExecutorImpl");
	
		return multipleSourceExecutorImplTemplate.getSt().render();
	
	}
	

}
