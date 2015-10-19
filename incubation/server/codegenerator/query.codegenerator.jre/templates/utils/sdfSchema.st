sdfSchema(operatorVariable,className,schemaURI,sdfAttributes)::=<<
//Create SDFAttribute for $operatorVariable$
	
List<SDFAttribute> $operatorVariable$SDFAttributeList = new ArrayList<SDFAttribute>();
 
$sdfAttributes : {attribute |

SDFAttribute $operatorVariable$$attribute.uRIWithoutQualName; format="replace"$_$attribute.qualName; format="replace"$ = new SDFAttribute("$attribute.uRIWithoutQualName$","$attribute.qualName$", SDFDatatype.getType("$attribute.datatype$"), null, null,null);
$operatorVariable$SDFAttributeList.add($operatorVariable$$attribute.uRIWithoutQualName; format="replace"$_$attribute.qualName; format="replace"$);
	
}$
//Create SDFSchema for $operatorVariable$
SDFSchema $operatorVariable$SDFSchema = SDFSchemaFactory.createNewSchema("$schemaURI$", $className$, $operatorVariable$SDFAttributeList);
	
	
>>

