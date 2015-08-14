package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.Utils;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TCSVFileSourceRule extends AbstractRule{

	public TCSVFileSourceRule() {
		super("TCSVFileSourceRule", "java");
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof AbstractAccessAO){
			
			AbstractAccessAO operator = (AbstractAccessAO) logicalOperator;
			if (operator.getWrapper() != null) {
				if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
					return true;
				}
				if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator.getWrapper())) {
					return true;
				}
			}
		}
	
		return false;
	}


	@Override
	public Class<?> getConditionClass() {
		return AbstractAccessAO.class;
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo csvFileSource = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		CSVFileSource csvFileSourceOP = (CSVFileSource) operator;
	
		String filename = csvFileSourceOP.getFilename();
		String transportHandler = csvFileSourceOP.getTransportHandler();
		String dataHandler = csvFileSourceOP.getDataHandler();
		String wrapper = csvFileSourceOP.getWrapper();
		String protocolHandler = csvFileSourceOP.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		csvFileSource.addCodeFragmentInfo(CreateDefaultCode.codeForAccessFramework(protocolHandlerParameter, csvFileSourceOP.getOptionsMap(),operator, direction));
		
		TimestampAO timestampAO = Utils.createTimestampAO(operator, null);
		
		StringTemplate accessPOTemplate = new StringTemplate("operator","accessPO");
		accessPOTemplate.getSt().add("operatorVariable", operatorVariable);
		csvFileSource.addCode(accessPOTemplate.getSt().render());
	
		csvFileSource.addCodeFragmentInfo(CreateDefaultCode.codeForRelationalTimestampAttributeTimeIntervalMFactory(operator, timestampAO));

		//add imports
		csvFileSource.addImport(IMetaAttribute.class.getName());
		csvFileSource.addImport(TimeInterval.class.getName());
		csvFileSource.addImport(IOException.class.getName());
		csvFileSource.addImport(IMetadataInitializer.class.getName());
		csvFileSource.addImport(AccessPO.class.getName());
		
		return csvFileSource;
	}

}