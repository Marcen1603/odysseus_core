slidingElementWindowTIPO(operatorVariable,windowSize, windowAdvance, windowSlide,isWindowSlideNull, baseTimeUnit)::=<<

SlidingElementWindowTIPO $operatorVariable$PO= new SlidingElementWindowTIPO(
			TimeUnit.$baseTimeUnit$, 
			$timeValueItem(windowSize)$,
			$timeValueItem(windowAdvance)$,
			$timeValueItem(windowSlide)$,
			$operatorVariable$PartitionSDFAttributeList, 
			$operatorVariable$InputSchemaSDFSchema);
$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);





>>