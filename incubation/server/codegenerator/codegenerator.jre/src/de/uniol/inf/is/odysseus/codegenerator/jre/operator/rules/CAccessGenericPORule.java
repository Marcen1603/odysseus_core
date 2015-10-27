package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCAccessGenericAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.codegenerator.utils.Utils;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class CAccessGenericPORule extends AbstractCAccessGenericAORule<AccessAO>{
	
	public CAccessGenericPORule() {
		super(CAccessGenericPORule.class.getName());
	}

	
	@Override
	public CodeFragmentInfo getCode(AccessAO operator) {
		
		CodeFragmentInfo accessPO = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		String transportHandler = operator.getTransportHandler();
		String dataHandler = operator.getDataHandler();
		String wrapper = operator.getWrapper();
		String protocolHandler = operator.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, operator.getOptionsMap(),operator, direction);
		accessPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		StringTemplate accessPOTemplate = new StringTemplate("operator","accessPO");
		accessPOTemplate.getSt().add("operatorVariable", operatorVariable);
		accessPOTemplate.getSt().add("getMaxTimeToWaitForNewEventMS", operator.getMaxTimeToWaitForNewEventMS());
		accessPOTemplate.getSt().add("readMetaData", operator.readMetaData());
		
		accessPO.addCode(accessPOTemplate.getSt().render());

		//important add timestamp op
		Utils.createTimestampAO(operator, operator.getDateFormat());
				
		accessPO.addFrameworkImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());	
		accessPO.addFrameworkImport(AccessPO.class.getName());
		accessPO.addFrameworkImport(IOException.class.getName());
		accessPO.addFrameworkImport(IMetaAttribute.class.getName());
		accessPO.addFrameworkImport(IMetadataInitializer.class.getName());
			
		return accessPO;
	}
}
