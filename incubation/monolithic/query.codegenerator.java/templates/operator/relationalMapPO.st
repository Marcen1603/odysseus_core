relationalMapPO(operatorVariable,isAllowNullValue,isEvaluateOnPunctuation,isSuppressErrors)::=<<

	RelationalMapPO $operatorVariable$PO = new RelationalMapPO<IMetaAttribute>($operatorVariable$InputSDFSchema,
					$operatorVariable$SDFExpressionList.toArray(new SDFExpression[0]),
					$isAllowNullValue$, $isEvaluateOnPunctuation$, $isSuppressErrors$);
	 $operatorVariable$PO.setOutputSchema($operatorVariable$SDFSchema);

>>