sdfAttributeList(sdfAttributes, operatorVariable, extendedVariableName)::=<<


//Create SDFAttribute for $operatorVariable$
	
List<SDFAttribute> $operatorVariable$SDFAttributeList$extendedVariableName$ = new ArrayList<SDFAttribute>();
 
$sdfAttributes : {attribute |
SDFAttribute $operatorVariable$$attribute.uRIWithoutQualName$_$attribute.qualName$SDFAttributeList$extendedVariableName$ = new SDFAttribute("$attribute.uRIWithoutQualName$","$attribute.qualName$", SDFDatatype.getType("$attribute.datatype$"), null, null,null);
$operatorVariable$SDFAttributeList$extendedVariableName$.add($operatorVariable$$attribute.uRIWithoutQualName$_$attribute.qualName$SDFAttributeList$extendedVariableName$);
	
}$


>>