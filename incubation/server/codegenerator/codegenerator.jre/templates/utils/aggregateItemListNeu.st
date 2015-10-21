aggregateItemListNeu(cAggregateItemModelList,operatorVariable)::=<<


List<AggregateItem> $operatorVariable$AggregateItemList = new ArrayList<AggregateItem>();


$cAggregateItemModelList : {cAggregateItem |

$sdfAttributeList(cAggregateItem.inAttributes,operatorVariable,cAggregateItem.outAttributeAttributeName)$

SDFAttribute $cAggregateItem.outAttributeAttributeNameEscaped$OutAttribute = new SDFAttribute("$cAggregateItem.outAttributeSourceName$","$cAggregateItem.outAttributeAttributeName$",SDFDatatype.getType("$cAggregateItem.outAttributeSDFDataTypeName$"), null);
$operatorVariable$AggregateItemList.add(new AggregateItem("$cAggregateItem.functionName$", $operatorVariable$SDFAttributeList$cAggregateItem.outAttributeAttributeNameEscaped$, $cAggregateItem.outAttributeAttributeNameEscaped$OutAttribute));
	
}$







>>