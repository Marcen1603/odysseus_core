slidingElementWindowTIPO(operatorVariable,windowSize, windowAdvance, windowSlide,isWindowSlideNull, baseTimeUnit)::=<<

SlidingElementWindowTIPO $operatorVariable$PO= new SlidingElementWindowTIPO(
			TimeUnit.$baseTimeUnit$,
			new TimeValueItem($windowSize.time$,TimeUnit.valueOf("$windowSize.unit$")), 
			new TimeValueItem($windowAdvance.time$,TimeUnit.valueOf("$windowAdvance.unit$")),
			$if(isWindowSlideNull)$
				 null,
			$else$
				new TimeValueItem($windowSlide.time ; null="null"$,TimeUnit.valueOf("$windowSlide.unit; null="null"$")), 
			$endif$		
			$operatorVariable$PartitionSDFAttributeList, 
			$operatorVariable$InputSchemaSDFSchema);
$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);





>>