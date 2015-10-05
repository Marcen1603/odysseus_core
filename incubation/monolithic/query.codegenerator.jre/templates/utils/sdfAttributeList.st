sdfAttributeList(sdfAttributes, operatorVariable, extendedVariableName)::=<<


//Create SDFAttribute for $operatorVariable$
	
List<SDFAttribute> $operatorVariable$SDFAttributeList$extendedVariableName; format="replace"$ = new ArrayList<SDFAttribute>();
 
$sdfAttributes : {attribute |
SDFAttribute $operatorVariable$$attribute.uRIWithoutQualName; format="replace"$_$attribute.qualName$SDFAttributeList$extendedVariableName; format="replace"$ = new SDFAttribute("$attribute.uRIWithoutQualName$","$attribute.qualName$", SDFDatatype.getType("$attribute.datatype$"), null, null,null);
$operatorVariable$SDFAttributeList$extendedVariableName; format="replace"$.add($operatorVariable$$attribute.uRIWithoutQualName; format="replace"$_$attribute.qualName$SDFAttributeList$extendedVariableName; format="replace"$);
	
}$


>>