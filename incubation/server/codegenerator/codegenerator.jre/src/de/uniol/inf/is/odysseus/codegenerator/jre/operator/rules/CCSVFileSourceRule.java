package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;


import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCCSVFileSourceRule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.codegenerator.utils.Utils;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;

/**
 * This rule generate from a CSVFileSource the code for the 
 * AccessPO operator. 
 * 
 * template: operator/accessPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CCSVFileSourceRule extends AbstractCCSVFileSourceRule<CSVFileSource>{

	public CCSVFileSourceRule() {
		super(CCSVFileSourceRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(CSVFileSource operator) {
		CodeFragmentInfo csvFileSource = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		CSVFileSource csvFileSourceOP = (CSVFileSource) operator;
	
		String filename = csvFileSourceOP.getFilename();
		String transportHandler = csvFileSourceOP.getTransportHandler();
		String dataHandler = csvFileSourceOP.getDataHandler();
		String wrapper = csvFileSourceOP.getWrapper();
		String protocolHandler = csvFileSourceOP.getProtocolHandler();
		
		
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		csvFileSource.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, csvFileSourceOP.getOptionsMap(),operator, direction));
	
		//important add a timestamp op to the source
		Utils.createTimestampAO(operator, csvFileSourceOP.getDateFormat());
		
		StringTemplate accessPOTemplate = new StringTemplate("operator","accessPO");
		accessPOTemplate.getSt().add("operatorVariable", operatorVariable);
		accessPOTemplate.getSt().add("getMaxTimeToWaitForNewEventMS", csvFileSourceOP.getMaxTimeToWaitForNewEventMS());
		accessPOTemplate.getSt().add("readMetaData", csvFileSourceOP.readMetaData());
		
		csvFileSource.addCode(accessPOTemplate.getSt().render());
	
		//add imports
		csvFileSource.addFrameworkImport(IMetaAttribute.class.getName());
		csvFileSource.addFrameworkImport(TimeInterval.class.getName());
		csvFileSource.addFrameworkImport(IOException.class.getName());
		csvFileSource.addFrameworkImport(IMetadataInitializer.class.getName());
		csvFileSource.addFrameworkImport(AccessPO.class.getName());
		
		return csvFileSource;
	}




	



}