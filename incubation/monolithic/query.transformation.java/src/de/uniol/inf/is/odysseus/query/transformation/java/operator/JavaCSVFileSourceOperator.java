package de.uniol.inf.is.odysseus.query.transformation.java.operator;


import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.Utils;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class JavaCSVFileSourceOperator extends AbstractTransformationOperator {
	
	public JavaCSVFileSourceOperator(){
		 this.implClass = AccessPO.class;
		 this.name = "CSVFileSource";
		 this.targetPlatform = "Java";
			defaultImports();
	}
	  
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo csvFileSource = new CodeFragmentInfo();
	
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		csvFileSource.addCodeFragmentInfo(CreateDefaultCode.codeForAccessFrameworkNeu(operator));
		
	
		TimestampAO timestampAO = Utils.createTimestampAO(operator, null);
		
		//now create the AccessPO
		code.append("AccessPO "+operatorVariable+"PO = new AccessPO("+operatorVariable+"ProtocolHandler,0);");
		code.append("\n");
		code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("\n");
		//set MetaData
		code.append("IMetaAttribute "+operatorVariable+"MetaAttribute =  new TimeInterval();");
		code.append("\n");
		code.append("((IMetadataInitializer) "+operatorVariable+"PO).setMetadataType("+operatorVariable+"MetaAttribute);");
		code.append("\n");
		csvFileSource.addCode(code.toString());
	
		csvFileSource.addCodeFragmentInfo(CreateDefaultCode.codeForRelationalTimestampAttributeTimeIntervalMFactory(operator, timestampAO));

	
		csvFileSource.addImport(IMetaAttribute.class.getName());
		csvFileSource.addImport(TimeInterval.class.getName());
		csvFileSource.addImport(IOException.class.getName());
		
		
		return csvFileSource;
	}

	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}
	

	

}
