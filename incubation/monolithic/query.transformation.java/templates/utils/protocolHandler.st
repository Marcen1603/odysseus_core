protocolHandler(protocolHandlerParameter,operatorVariable, wrapper, openwrapper, direction)::=<<
	
	IProtocolHandler $operatorVariable$ProtocolHandler =  protocolhandlerregistry.getInstance("$protocolHandlerParameter.protocolHandler$", ITransportDirection.$direction$, $wrapper$, $operatorVariable$ParameterInfo,  datahandlerregistry.getDataHandler("$protocolHandlerParameter.dataHandler$",$operatorVariable$SDFSchema));
	ITransportHandler $operatorVariable$TransportHandler = transporthandlerregistry.getInstance("$protocolHandlerParameter.transportHandler$",$operatorVariable$ProtocolHandler,$operatorVariable$ParameterInfo);
	$operatorVariable$ProtocolHandler.setTransportHandler($operatorVariable$TransportHandler);
		
	$if(openwrapper)$
		$operatorVariable$TransportHandler.processInOpen();
	$endif$
		
>>