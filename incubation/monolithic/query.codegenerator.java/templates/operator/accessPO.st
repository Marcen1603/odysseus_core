accessPO(operatorVariable,getMaxTimeToWaitForNewEventMS,readMetaData)::=<<


AccessPO $operatorVariable$PO = new AccessPO($operatorVariable$ProtocolHandler,$getMaxTimeToWaitForNewEventMS$,$readMetaData$);
$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
//set MetaData
IMetaAttribute $operatorVariable$MetaAttribute =  new TimeInterval();
((IMetadataInitializer) $operatorVariable$PO).setMetadataType($operatorVariable$MetaAttribute);

>>