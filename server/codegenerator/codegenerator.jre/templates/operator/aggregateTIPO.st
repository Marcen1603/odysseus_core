aggregateTIPO(operatorVariable,fastGrouping)::=<<


AggregateAO $operatorVariable$dummyAggreagteAO = new AggregateAO();
$operatorVariable$dummyAggreagteAO.setAggregationItems($operatorVariable$AggregateItemList);



AggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval\>> $operatorVariable$PO = new AggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval\>>(
				$operatorVariable$InputSDFSchema,
				$operatorVariable$OutputSchemaInternSDFSchema,
				$operatorVariable$GroupingAttributeSDFAttributeList,
				$operatorVariable$dummyAggreagteAO.getAggregations(), $fastGrouping$, $operatorVariable$Mf);
				
				
				
		
		SDFSchema $operatorVariable$OutputSchema = $operatorVariable$PO.getInternalOutputSchema();
		List<SDFAttribute> $operatorVariable$OutAttributes = new ArrayList<>(
				$operatorVariable$OutputSchema.getAttributes());
		SDFSchema $operatorVariable$InputSchema = $operatorVariable$PO.getInputSchema();
		Map<SDFSchema, Map<AggregateFunction, SDFAttribute\>> $operatorVariable$Aggregations = $operatorVariable$PO
				.getAggregations();



		for (SDFSchema attrList : $operatorVariable$Aggregations.keySet()) {
			if (SDFSchema.subset(attrList, $operatorVariable$InputSchema)) {
				Map<AggregateFunction, SDFAttribute> funcs = $operatorVariable$Aggregations
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
						posArray[i] = $operatorVariable$InputSchema.indexOf(attr);
						if (attr.getDatatype().isPartialAggregate()) {
							partialAggregateInput = true;
						}
						// For most cases its the only datatype ... so keep one
						// of them
						datatype = attr.getDatatype().getURI();
					}
					
					
		
			
					
					IAggregateFunctionBuilder builder = aggregatefunctionbuilderregistry.getBuilder(
							$operatorVariable$InputSchema.getType(), p.getE2().getName());
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
				$operatorVariable$PO.getInputSchema(), $operatorVariable$SDFSchema,
				$operatorVariable$PO.getGroupingAttribute(),
				$operatorVariable$PO.getAggregations(), $operatorVariable$PO.isFastGrouping());
				
				
		$operatorVariable$PO.setGroupProcessor($operatorVariable$RGroupProcessor);
		$operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);
				


>>