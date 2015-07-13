package de.uniol.inf.is.odysseus.query.transformation.java.operator;


import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class JavaCSVFileSinkOperator extends AbstractTransformationOperator{

	public JavaCSVFileSinkOperator(){
		this.implClass = SenderPO.class;
		this.name =  "CSVFILESINK";
		this.targetPlatform = "Java";
		defaultImports();
	}
	

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo csvFileSink = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		

		CodeFragmentInfo codeAccessFramwork = CreateDefaultCode.codeForAccessFrameworkNeu(operator);
		csvFileSink.addCodeFragmentInfo(codeAccessFramwork);
		
		
		//now create the SenderPO
		code.append("SenderPO "+operatorVariable+"PO = new SenderPO("+operatorVariable+"ProtocolHandler);");
		code.append("\n");
		code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("\n");
		

		csvFileSink.addImports(getNeededImports());
		csvFileSink.addCode(code.toString());
		
		return csvFileSink;
	}

	@Override
	public void defineImports() {
	
	}
	
	

	
	

}


