accessPO(operatorVariable)::=<<


AccessPO $operatorVariable$PO = new AccessPO($operatorVariable$ProtocolHandler,0);
$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
//set MetaData
IMetaAttribute $operatorVariable$MetaAttribute =  new TimeInterval();
((IMetadataInitializer) $operatorVariable$PO).setMetadataType($operatorVariable$MetaAttribute);

>>