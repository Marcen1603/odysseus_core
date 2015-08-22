sdfAttributeList(sdfAttributes, operatorVariable)::=<<


//Create SDFAttribute for $operatorVariable$
	
List<SDFAttribute> $operatorVariable$SDFAttributeList = new ArrayList<SDFAttribute>();
 
$sdfAttributes : {attribute |
SDFAttribute $operatorVariable$$attribute.uRIWithoutQualName$_$attribute.qualName$ = new SDFAttribute("$attribute.uRIWithoutQualName$","$attribute.qualName$", SDFDatatype.getType("$attribute.datatype$"), null, null,null);
$operatorVariable$SDFAttributeList.add($operatorVariable$$attribute.uRIWithoutQualName$_$attribute.qualName$);
	
}$


>>