joinTIPO(operatorVariable,input0MetaAttributeNamesCode, input1MetaAttributeNamesCode,input0MetaAttributeNamesVariable, input1MetaAttributeNamesVariable,areaName,predicateValue,transferFuntion,input0,input1)::=<<

		$input0MetaAttributeNamesCode$
		
		$input1MetaAttributeNamesCode$

		IMetadataMergeFunction<?> $operatorVariable$metaDataMerge = MetadataRegistry.getMergeFunction($input0MetaAttributeNamesVariable$,
						$input1MetaAttributeNamesVariable$);
		
		JoinTIPO $operatorVariable$PO = new JoinTIPO($operatorVariable$metaDataMerge);
		
		
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];
		try{
			areas[0] = (ITimeIntervalSweepArea) sweeparearegistry.getSweepArea("$areaName$");
			areas[1] = (ITimeIntervalSweepArea) sweeparearegistry.getSweepArea("$areaName$");
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		$operatorVariable$PO.setAreas(areas);
		
		$operatorVariable$PO.setTransferFunction(new $transferFuntion$());
		
		$operatorVariable$PO.setCreationFunction(new DefaultTIDummyDataCreation());
		
		
		DirectAttributeResolver $operatorVariable$DirectAttriuteResolver = new DirectAttributeResolver($operatorVariable$SDFSchema);
		RelationalPredicateBuilder $operatorVariable$RelationalPredicateBuilder = new  RelationalPredicateBuilder();
		RelationalPredicate $operatorVariable$Predicate = (RelationalPredicate)$operatorVariable$RelationalPredicateBuilder.createPredicate($operatorVariable$DirectAttriuteResolver, "$predicateValue$");
		$operatorVariable$Predicate.init($operatorVariable$leftSchemaSDFSchema, $operatorVariable$rightSchemaSDFSchema);
		
		$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
	
	
		$operatorVariable$PO.setJoinPredicate($operatorVariable$Predicate);
		$operatorVariable$PO.setDataMerge(new RelationalMergeFunction<ITimeInterval>($operatorVariable$PO.getOutputSchema().size()));
		
>>