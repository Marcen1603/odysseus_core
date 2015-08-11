package de.uniol.inf.is.odysseus.query.transformation.java.operator;


import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class JavaCSVFileSinkOperator extends AbstractTransformationOperator{

	public JavaCSVFileSinkOperator(){
		super(SenderPO.class, "CSVFILESINK","Java");
	}
	

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo csvFileSink = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
	
		CSVFileSink csvFileSinkOP = (CSVFileSink) operator;
			
		String filename = csvFileSinkOP.getFilename();
		String transportHandler = csvFileSinkOP.getTransportHandler();
		String dataHandler = csvFileSinkOP.getDataHandler();
		String wrapper = csvFileSinkOP.getWrapper();
		String protocolHandler = csvFileSinkOP.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateDefaultCode.codeForAccessFrameworkNeu(protocolHandlerParameter, csvFileSinkOP.getOptionsMap(),operator, direction);
		csvFileSink.addCodeFragmentInfo(codeAccessFramwork);
		
		
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);

		csvFileSink.addImports(getNeededImports());
		csvFileSink.addCode(senderPOTemplate.getSt().render());
		
		return csvFileSink;
	}

	@Override
	public void defineImports() {
	
	}
	
	

	
	

}


