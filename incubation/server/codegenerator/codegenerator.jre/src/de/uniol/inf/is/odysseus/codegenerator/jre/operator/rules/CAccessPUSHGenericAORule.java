package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCAccessPUSHGenericAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.codegenerator.utils.Utils;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;

/**
 * This rule generate from a AccessAO (PUSH) the code for the 
 * receiverPO operator. 
 * 
 * template: operator/receiverPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CAccessPUSHGenericAORule extends AbstractCAccessPUSHGenericAORule<AccessAO>{
	
	public CAccessPUSHGenericAORule() {
		super(CAccessPUSHGenericAORule.class.getName());
	}

	
	@Override
	public CodeFragmentInfo getCode(AccessAO operator) {
		CodeFragmentInfo receiverPO = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);

		//get needed values for the access framework
		String transportHandler = operator.getTransportHandler();
		String dataHandler = operator.getDataHandler();
		String wrapper = operator.getWrapper();
		String protocolHandler = operator.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		//generate code for the access framework
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, operator.getOptionsMap(),operator, direction);
		receiverPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		//generate code for receiverPO
		StringTemplate receiverPOTemplate = new StringTemplate("operator","receiverPO");
		receiverPOTemplate.getSt().add("operatorVariable", operatorVariable);
		receiverPOTemplate.getSt().add("readMetaData", operator.getReadMetaData());
		
		//render template
		receiverPO.addCode(receiverPOTemplate.getSt().render());
		
		//important add timestamp op
		Utils.createTimestampAO(operator, operator.getDateFormat());
		
		//add framework imports
		receiverPO.addFrameworkImport(ReceiverPO.class.getName());
		receiverPO.addFrameworkImport(IOException.class.getName());
		receiverPO.addFrameworkImport(IMetaAttribute.class.getName());
		receiverPO.addFrameworkImport(IMetadataInitializer.class.getName());
			
		return receiverPO;
	}
}
