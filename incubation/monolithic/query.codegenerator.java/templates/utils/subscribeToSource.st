subscribeToSource(operatorVariable,targetOpMap)::=<<


$targetOpMap.keys:{k|

$operatorVariable$PO.subscribeToSource($k$PO, $targetOpMap.(k).sinkInPort$, $targetOpMap.(k).sourceOutPort$, $k$PO.getOutputSchema());
  }
  

$
	

>>



