receiverPO(operatorVariable,readMetaData)::=<<

	ReceiverPO $operatorVariable$PO = new ReceiverPO($operatorVariable$ProtocolHandler,$readMetaData$);
			
	$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
	
	//set MetaData
	IMetaAttribute $operatorVariable$MetaAttribute =  new TimeInterval();
	((IMetadataInitializer) $operatorVariable$PO).setMetadataType($operatorVariable$MetaAttribute);
			
>>	