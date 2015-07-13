package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class CreateDefaultCode {
	
	
	public static CodeFragmentInfo initOperator(ILogicalOperator operator){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		sdfSchema.addCodeFragmentInfo(TransformSDFSchema.getCodeForSDFSchema(operator.getOutputSchema(),operatorVariable));
				
		return sdfSchema;
	}

	public static CodeFragmentInfo codeForAccessFrameworkNeu(ILogicalOperator operator){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
	
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		ITransportDirection direction = ITransportDirection.IN;

		String filename = "";
		String transportHandler =  "";
		String dataHandler =  "";
		String wrapper = "";
		String protocolHandler = "";
		
		if(operator instanceof CSVFileSink){
			 CSVFileSink csvFileSink = (CSVFileSink) operator;
		
			 filename = csvFileSink.getFilename();
			 transportHandler = csvFileSink.getTransportHandler();
			 dataHandler = csvFileSink.getDataHandler();
			 wrapper = csvFileSink.getWrapper();
			 protocolHandler = csvFileSink.getProtocolHandler();
			 direction = ITransportDirection.OUT;
		}
		

		 
		if(operator instanceof CSVFileSource){
			CSVFileSource csvFileSource = (CSVFileSource) operator; 
			 
			 filename = csvFileSource.getFilename();
			 transportHandler = csvFileSource.getTransportHandler();
			 dataHandler = csvFileSource.getDataHandler();
			 wrapper = csvFileSource.getWrapper();
			 protocolHandler = csvFileSource.getProtocolHandler();
			 direction = ITransportDirection.IN;
			
		}
		
		//add import
		codeFragmentInfo.addImport(ITransportDirection.class.getName());

		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);	
		
		//generate code for options
		codeFragmentInfo.addCodeFragmentInfo(TransformCSVParameter.getCodeForParameterInfoNeu(operator.getParameterInfos(),operatorVariable));
		
		//setup transportHandler
		codeFragmentInfo.addCodeFragmentInfo(TransformProtocolHandler.getCodeForProtocolHandlerNeu(protocolHandlerParameter, operatorVariable, direction));
	

		return codeFragmentInfo;
	}
	
	
	
	public static CodeFragmentInfo codeForRelationalTimestampAttributeTimeIntervalMFactory(ILogicalOperator forOperator, TimestampAO timestampAO){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(forOperator);
		
		SDFSchema schema = timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		if (Tuple.class.isAssignableFrom(timestampAO.getInputSchema().getType())) {
			if (pos >= 0) {
				int posEnd = timestampAO.hasEndTimestamp() ? timestampAO
						.getInputSchema()
						.indexOf(timestampAO.getEndTimestamp()) : -1;
						
			code.append("RelationalTimestampAttributeTimeIntervalMFactory "+operatorVariable+"MetaUpdater = new RelationalTimestampAttributeTimeIntervalMFactory("+pos+", "+posEnd+","+ clearEnd+","+ timestampAO.getDateFormat()+","+timestampAO.getTimezone()+","+ timestampAO.getLocale()+","+timestampAO.getFactor()+","+ timestampAO.getOffset()+");");
			code.append("\n");
			code.append("((IMetadataInitializer) "+operatorVariable+"PO).addMetadataUpdater("+operatorVariable+"MetaUpdater);");
			}
			
		}
		codeFragmentInfo.addImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
		codeFragmentInfo.addImport(IMetadataInitializer.class.getName());
		
		codeFragmentInfo.addCode(code.toString());
		

	
		return codeFragmentInfo;
		
	}

}
