selectPO(operatorVariable,predicateValue)::=<<


	DirectAttributeResolver $operatorVariable$DirectAttriuteResolver = new DirectAttributeResolver($operatorVariable$SDFSchema);
	RelationalPredicateBuilder $operatorVariable$RelationalPredicateBuilder = new  RelationalPredicateBuilder();
	RelationalPredicate $operatorVariable$Predicate = (RelationalPredicate)$operatorVariable$RelationalPredicateBuilder.createPredicate($operatorVariable$DirectAttriuteResolver, "$predicateValue$");
	$operatorVariable$Predicate.init($operatorVariable$SDFSchema, null);

	SelectPO $operatorVariable$PO = new SelectPO($operatorVariable$Predicate);
	
		
		
>>