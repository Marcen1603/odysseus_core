package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCCSVFileSourceRule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.Utils;

public class CCSVFileSourceRule extends AbstractCCSVFileSourceRule{

	public CCSVFileSourceRule() {
		super(CCSVFileSourceRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo csvFileSource = new CodeFragmentInfo();
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);
		
		CSVFileSource csvFileSourceOP = (CSVFileSource) operator;
	
		String filename = csvFileSourceOP.getFilename();
		String transportHandler = csvFileSourceOP.getTransportHandler();
		String dataHandler = csvFileSourceOP.getDataHandler();
		String wrapper = csvFileSourceOP.getWrapper();
		String protocolHandler = csvFileSourceOP.getProtocolHandler();
		
		
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		csvFileSource.addCodeFragmentInfo(CreateJavaDefaultCode.codeForAccessFramework(protocolHandlerParameter, csvFileSourceOP.getOptionsMap(),operator, direction));
	
		//important add a timestamp op to the source
		Utils.createTimestampAO(operator, csvFileSourceOP.getDateFormat());
		
		StringTemplate accessPOTemplate = new StringTemplate("operator","accessPO");
		accessPOTemplate.getSt().add("operatorVariable", operatorVariable);
		accessPOTemplate.getSt().add("getMaxTimeToWaitForNewEventMS", csvFileSourceOP.getMaxTimeToWaitForNewEventMS());
		accessPOTemplate.getSt().add("readMetaData", csvFileSourceOP.readMetaData());
		
		csvFileSource.addCode(accessPOTemplate.getSt().render());
	
		//add imports
		csvFileSource.addImport(IMetaAttribute.class.getName());
		csvFileSource.addImport(TimeInterval.class.getName());
		csvFileSource.addImport(IOException.class.getName());
		csvFileSource.addImport(IMetadataInitializer.class.getName());
		csvFileSource.addImport(AccessPO.class.getName());
		
		return csvFileSource;
	}




	



}