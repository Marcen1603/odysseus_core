slidingAdvanceTimeWindowTIPO(baseTimeUnit, windowSize, windowAdvance, operatorVariable)::=<<


SlidingAdvanceTimeWindowTIPO $operatorVariable$PO = new SlidingAdvanceTimeWindowTIPO(TimeUnit.$baseTimeUnit$,
		 new TimeValueItem($windowSize.time$,TimeUnit.valueOf("$windowSize.unit$")),
		 new TimeValueItem($windowAdvance.time$,TimeUnit.valueOf("$windowAdvance.unit$")),
		 $operatorVariable$InputSchemaSDFSchema);		
		
>>
