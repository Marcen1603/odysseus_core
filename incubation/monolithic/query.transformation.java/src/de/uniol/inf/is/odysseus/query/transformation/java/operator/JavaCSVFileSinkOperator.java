package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;

public class JavaCSVFileSinkOperator extends AbstractTransformationOperator{
	
	private final String name =  "CSVFILESINK";
	private final String targetPlatform = "Java";
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		code.append(CreateDefaultCode.codeForAccessFramework(operator));
	
		//now create the SenderPO
		code.append("SenderPO "+operatorVariable+"PO = new SenderPO("+operatorVariable+"ProtocolHandler);");
		code.append("\n");
		code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
	}

	@Override
	public Set<String> getNeededImports() {
		Set<String> imoportList = new HashSet<String>();
		imoportList.add(SenderPO.class.getPackage().getName()+"."+SenderPO.class.getSimpleName());
		return imoportList;
	}
}


