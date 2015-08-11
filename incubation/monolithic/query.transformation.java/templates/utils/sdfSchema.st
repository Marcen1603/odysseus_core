sdfSchema(operatorVariable,className,schemaURI,sdfAttributes)::=<<


//Create SDFAttribute for $operatorVariable$
	
List<SDFAttribute> $operatorVariable$SDFAttributeList = new ArrayList<SDFAttribute>();
 
$sdfAttributes : {attribute |
SDFAttribute $operatorVariable$$attribute.qualName$ = new SDFAttribute(null,"$attribute.qualName$", SDFDatatype.getType("$attribute.datatype$"), null, null,null);
$operatorVariable$SDFAttributeList.add($operatorVariable$$attribute.qualName$);
	
}$
	
	
//Create SDFSchema for $operatorVariable$
SDFSchema $operatorVariable$SDFSchema = SDFSchemaFactory.createNewSchema("$schemaURI$", $className$, $operatorVariable$SDFAttributeList);
	
>>
