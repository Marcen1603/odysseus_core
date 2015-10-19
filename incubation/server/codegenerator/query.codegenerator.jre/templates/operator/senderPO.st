senderPO(operatorVariable)::=<<

		//now create the SenderPO
		SenderPO $operatorVariable$PO = new SenderPO($operatorVariable$ProtocolHandler);
		$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
		
>>