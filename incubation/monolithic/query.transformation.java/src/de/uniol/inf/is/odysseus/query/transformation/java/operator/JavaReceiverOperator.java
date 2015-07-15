package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class JavaReceiverOperator extends AbstractTransformationOperator{
	
	public JavaReceiverOperator(){
		 this.implClass = ReceiverPO.class;
		 this.name = "StreamAO";
		 this.targetPlatform = "Java";
			defaultImports();
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo receiverPO = new CodeFragmentInfo();
		StringBuilder code = new StringBuilder();
		StreamAO streamAO = (StreamAO)operator;
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(streamAO.getStreamname(),  null);

		AccessAO accessAO = (AccessAO)logicalPlan;
		
	
		 String transportHandler = accessAO.getTransportHandler();
		 String dataHandler = accessAO.getDataHandler();
		 String wrapper = accessAO.getWrapper();
		 String protocolHandler = accessAO.getProtocolHandler();
		 ITransportDirection direction = ITransportDirection.IN;
		 
		 ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		 CodeFragmentInfo codeAccessFramwork = CreateDefaultCode.codeForAccessFrameworkNeu(protocolHandlerParameter, accessAO.getOptionsMap(),operator, direction);
		 receiverPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		 
		//now create the AccessPO
			code.append("ReceiverPO "+operatorVariable+"PO = new ReceiverPO("+operatorVariable+"ProtocolHandler);");
			code.append("\n");
			code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
			code.append("\n");
			code.append("\n");
		 
			
			receiverPO.addCode(code.toString());
	
			receiverPO.addImport(ReceiverPO.class.getName());

			receiverPO.addImport(IOException.class.getName());
			
			
		return receiverPO;
	}

	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}

}