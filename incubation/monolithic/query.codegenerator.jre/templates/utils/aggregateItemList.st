aggregateItemList(functionName,sdfAttributeListTest,outAttributeSourceName,operatorVariable,outAttributeSDFDataTypeName, outAttributeAttributeName)::=<<


List<AggregateItem> aggregateItemList = new ArrayList<AggregateItem>();

SDFAttribute outAttribute = new SDFAttribute("$outAttributeSourceName$","$outAttributeAttributeName$",SDFDatatype.getType("$outAttributeSDFDataTypeName$"), null);

$sdfAttributeList(sdfAttributeListTest,operatorVariable)$

aggregateItemList.add(new AggregateItem("$functionName$", $operatorVariable$SDFAttributeList, outAttribute));





>>