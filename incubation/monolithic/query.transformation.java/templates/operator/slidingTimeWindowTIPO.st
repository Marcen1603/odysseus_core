slidingTimeWindowTIPO(baseTimeUnit, windowSize, operatorVariable)::=<<
		 SlidingTimeWindowTIPO $operatorVariable$PO = new SlidingTimeWindowTIPO(TimeUnit.$baseTimeUnit$,
		 new TimeValueItem($windowSize.time$,TimeUnit.valueOf("$windowSize.unit$")),
		 $operatorVariable$InputSchemaSDFSchema);		

>>