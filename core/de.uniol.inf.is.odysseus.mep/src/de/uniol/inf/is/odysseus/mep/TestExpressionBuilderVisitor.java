package de.uniol.inf.is.odysseus.mep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestExpressionBuilderVisitor {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) throws NumberFormatException,
			IOException {
		BufferedReader inRead = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			IExpression<?> expression = MEP.parse(inRead.readLine());

			for (Variable curVar : expression.getVariables()) {
				System.out
						.println("Value for " + curVar.getIdentifier() + ": ");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(System.in));
				curVar.bind(Double.parseDouble(reader.readLine()));
			}
			System.out.println("expression [type= "
					+ expression.getType().getSimpleName() + "]: "
					+ expression.toString());
			System.out.println("value: " + expression.getValue());

		} catch (de.uniol.inf.is.odysseus.mep.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
