package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCCSVFileSinkRule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;

public class CCSVFileSinkRule extends AbstractCCSVFileSinkRule<CSVFileSink> {
	
	public CCSVFileSinkRule() {
		super(CCSVFileSinkRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(CSVFileSink operator) {
		CodeFragmentInfo csvFileSink = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		CSVFileSink csvFileSinkOP = (CSVFileSink) operator;

		String filename = "";
	
		String transportHandler = csvFileSinkOP.getTransportHandler();
		String dataHandler = csvFileSinkOP.getDataHandler();
		String wrapper = csvFileSinkOP.getWrapper();
		String protocolHandler = csvFileSinkOP.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, csvFileSinkOP.getOptionsMap(),operator, direction);
		csvFileSink.addCodeFragmentInfo(codeAccessFramwork);
		
		
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);

		csvFileSink.addFrameworkImport(SenderPO.class.getName());
		csvFileSink.addCode(senderPOTemplate.getSt().render());
		

		
		return csvFileSink;
	}
	

}
