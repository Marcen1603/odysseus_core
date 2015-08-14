slidingPeriodicWindowTIPO(baseTimeUnit, windowSize, windowSlide, operatorVariable)::=<<


SlidingPeriodicWindowTIPO $operatorVariable$PO = new SlidingPeriodicWindowTIPO(TimeUnit.$baseTimeUnit$,
		 new TimeValueItem($windowSize.time$,TimeUnit.valueOf("$windowSize.unit$")),
		 new TimeValueItem($windowSlide.time$,TimeUnit.valueOf("$windowSlide.unit$")),
		 $operatorVariable$InputSchemaSDFSchema);		

>>