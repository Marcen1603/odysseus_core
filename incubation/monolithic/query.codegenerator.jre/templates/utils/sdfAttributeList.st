sdfAttributeList(sdfAttributes, operatorVariable, extendedVariableName)::=<<


//Create SDFAttribute for $operatorVariable$
	
List<SDFAttribute> $operatorVariable$SDFAttributeList$extendedVariableName; format="escape"$ = new ArrayList<SDFAttribute>();
 
$sdfAttributes : {attribute |
SDFAttribute $operatorVariable$$attribute.uRIWithoutQualName; format="escape"$_$attribute.qualName$SDFAttributeList$extendedVariableName; format="escape"$ = new SDFAttribute("$attribute.uRIWithoutQualName$","$attribute.qualName$", SDFDatatype.getType("$attribute.datatype$"), null, null,null);
$operatorVariable$SDFAttributeList$extendedVariableName; format="escape"$.add($operatorVariable$$attribute.uRIWithoutQualName; format="escape"$_$attribute.qualName$SDFAttributeList$extendedVariableName; format="escape"$);
	
}$


>>