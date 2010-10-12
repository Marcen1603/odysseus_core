package de.uniol.inf.is.odysseus.mep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.mep.impl.ParseException;

public class TestExpressionBuilderVisitor {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		MEPImpl parser = new MEPImpl(new InputStreamReader(System.in));
		ExpressionBuilderVisitor v = new ExpressionBuilderVisitor();
		try {
			IExpression expression = (IExpression) parser.Expression().jjtAccept(v, null);
			for(Variable curVar : expression.getVariables()){
				System.out.println("Value for " + curVar.getIdentifier() + ": ");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				curVar.bind(Double.parseDouble(reader.readLine()));
			}
			System.out.println("expression: " + expression.toString());
			System.out.println("value: " + expression.getValue());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
