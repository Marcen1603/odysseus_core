aggregateTIPO(operatorVariable,fastGrouping)::=<<


AggregateAO $operatorVariable$dummyAggreagteAO = new AggregateAO();
$operatorVariable$dummyAggreagteAO.setAggregationItems($operatorVariable$AggregateItemList);



AggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval\>> $operatorVariable$PO = new AggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval\>>(
				$operatorVariable$InputSDFSchema,
				$operatorVariable$OutputSchemaInternSDFSchema,
				$operatorVariable$GroupingAttributeSDFAttributeList,
				$operatorVariable$dummyAggreagteAO.getAggregations(), $fastGrouping$, $operatorVariable$Mf);
				
				
				
		
		SDFSchema outputSchema = $operatorVariable$PO.getInternalOutputSchema();
		List<SDFAttribute> outAttributes = new ArrayList<>(
				outputSchema.getAttributes());
		SDFSchema inputSchema = $operatorVariable$PO.getInputSchema();
		Map<SDFSchema, Map<AggregateFunction, SDFAttribute\>> aggregations = $operatorVariable$PO
				.getAggregations();


		AggregateFunctionBuilderRegistry aggregateFunctionBuilderRegistry =  new AggregateFunctionBuilderRegistry();
		aggregateFunctionBuilderRegistry.registerAggregateFunctionBuilder(new RelationalAggregateFunctionBuilder());

		for (SDFSchema attrList : aggregations.keySet()) {
			if (SDFSchema.subset(attrList, inputSchema)) {
				Map<AggregateFunction, SDFAttribute> funcs = aggregations
						.get(attrList);
				for (Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					FESortedClonablePair<SDFSchema, AggregateFunction> p = new FESortedClonablePair<SDFSchema, AggregateFunction>(
							attrList, e.getKey());
					int[] posArray = new int[p.getE1().size()];
					boolean partialAggregateInput = false;
					String datatype = null;
					for (int i = 0; i < p.getE1().size(); ++i) {
						SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
						if (attr.getDatatype().isPartialAggregate()) {
							partialAggregateInput = true;
						}
						// For most cases its the only datatype ... so keep one
						// of them
						datatype = attr.getDatatype().getURI();
					}
					
					
		
			
					
					IAggregateFunctionBuilder builder = aggregateFunctionBuilderRegistry.getBuilder(
							inputSchema.getType(), p.getE2().getName());
					if (builder == null) {
						throw new RuntimeException(
								"Could not find a builder for "
										+ p.getE2().getName());
					}
					IAggregateFunction aggFunction = builder.createAggFunction(
							p.getE2(), p.getE1(), posArray, partialAggregateInput,
							datatype);
					$operatorVariable$PO.setInitFunction(p, aggFunction);
					$operatorVariable$PO.setMergeFunction(p, aggFunction);
					$operatorVariable$PO.setEvalFunction(p, aggFunction);
				}
			}
		}



		
		
		
				

RelationalGroupProcessor $operatorVariable$RGroupProcessor = new RelationalGroupProcessor(
				aggregate2PO.getInputSchema(), $operatorVariable$SDFSchema,
				aggregate2PO.getGroupingAttribute(),
				aggregate2PO.getAggregations(), aggregate2PO.isFastGrouping());
				
				
		$operatorVariable$PO.setGroupProcessor($operatorVariable$RGroupProcessor);
		$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
				


>>