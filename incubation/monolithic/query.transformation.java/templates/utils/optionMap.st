optionMap(operatorVariable,optionMap)::=<<


OptionMap $operatorVariable$ParameterInfo = new OptionMap();
		
$optionMap.keys:{k|
  $operatorVariable$ParameterInfo.setOption("$k$", "$optionMap.(k)$");}
$
	
	


>>