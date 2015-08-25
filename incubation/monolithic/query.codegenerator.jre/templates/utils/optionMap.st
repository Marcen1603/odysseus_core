optionMap(operatorVariable,optionMap)::=<<


OptionMap $operatorVariable$ParameterInfo = new OptionMap();
		
if(!Utils.isOperatorConfigAvailable("$operatorVariable$PO")){		
	$optionMap.keys:{k|
	  $operatorVariable$ParameterInfo.setOption("$k$", "$optionMap.(k)$");}
	$
}else{
	 $operatorVariable$ParameterInfo = Utils.getOperatorConfig("$operatorVariable$PO");
}


>>