package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
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
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);

		String transportHandler = operator.getTransportHandler();
		String dataHandler = operator.getDataHandler();
		String wrapper = operator.getWrapper();
		String protocolHandler = operator.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, operator.getOptionsMap(),operator, direction);
		receiverPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		StringTemplate receiverPOTemplate = new StringTemplate("operator","receiverPO");
		receiverPOTemplate.getSt().add("operatorVariable", operatorVariable);
		receiverPOTemplate.getSt().add("readMetaData", operator.getReadMetaData());
		
			
		receiverPO.addCode(receiverPOTemplate.getSt().render());
		
		//important add timestamp op
		Utils.createTimestampAO(operator, operator.getDateFormat());
		
		
		receiverPO.addFrameworkImport(ReceiverPO.class.getName());
		receiverPO.addFrameworkImport(IOException.class.getName());
		
		
		receiverPO.addFrameworkImport(IMetaAttribute.class.getName());
		receiverPO.addFrameworkImport(IMetadataInitializer.class.getName());
			
		return receiverPO;
	}
}
