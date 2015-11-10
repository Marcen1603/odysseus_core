package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCCSVFileSinkRule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;


/**
 * This rule generate from a CSVFileSink the code for the 
 * SenderPO operator. 
 * 
 * template: operator/senderPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CCSVFileSinkRule extends AbstractCCSVFileSinkRule<CSVFileSink> {
	
	public CCSVFileSinkRule() {
		super(CCSVFileSinkRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(CSVFileSink operator) {
		CodeFragmentInfo csvFileSink = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		CSVFileSink csvFileSinkOP = (CSVFileSink) operator;

		String filename = "";
		//get needed access framework values
		String transportHandler = csvFileSinkOP.getTransportHandler();
		String dataHandler = csvFileSinkOP.getDataHandler();
		String wrapper = csvFileSinkOP.getWrapper();
		String protocolHandler = csvFileSinkOP.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		//generate code for access framework
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, csvFileSinkOP.getOptionsMap(),operator, direction);
		csvFileSink.addCodeFragmentInfo(codeAccessFramwork);
		
		//generate code for senderPO
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);
		//render template
		csvFileSink.addCode(senderPOTemplate.getSt().render());
		
		//add framework imports
		csvFileSink.addFrameworkImport(SenderPO.class.getName());

		
		return csvFileSink;
	}
	

}
