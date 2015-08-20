relationalMapPO(operatorVariable,isAllowNullValue,isEvaluateOnPunctuation,isSuppressErrors)::=<<

	RelationalMapPO $operatorVariable$PO = new RelationalMapPO<IMetaAttribute>($operatorVariable$SDFSchema,
					$operatorVariable$SDFExpressionList.toArray(new SDFExpression[0]),
					$isAllowNullValue$, $isEvaluateOnPunctuation$, $isSuppressErrors$);
	 $operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);

>>