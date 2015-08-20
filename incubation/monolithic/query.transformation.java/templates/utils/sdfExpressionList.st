sdfExpressionList(operatorVariable, sdfExpressionList)::=<<

List<SDFExpression> $operatorVariable$SDFExpressionList = new ArrayList<SDFExpression>();

$sdfExpressionList : {sdfExpression |

$operatorVariable$SDFExpressionList.add(new SDFExpression("$sdfExpression.expressionString$", mep));
	
}$


>>