package de.uniol.inf.is.odysseus.query.transformation.java.operator;


import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.Utils;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;

public class JavaCSVFileSourceOperator extends AbstractTransformationOperator {
	

	private final String name =  "CSVFileSource";
	private final String targetPlatform = "Java";
	  
	  
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}
	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

	
		

		code.append(CreateDefaultCode.codeForAccessFramework(operator));

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
		code.append(CreateDefaultCode.codeForRelationalTimestampAttributeTimeIntervalMFactory(operator, timestampAO));
		

		return code.toString();
	}
	
	
	@Override
	public Set<String> getNeededImports() {
		Set<String> importList = new HashSet<String>();
		importList.add(AccessPO.class.getPackage().getName()+"."+AccessPO.class.getSimpleName());
		return importList;
	}
	
	


}
