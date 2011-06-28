/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
					+ expression.getReturnType() + "]: "
					+ expression.toString());
			Object value = expression.getValue();
			if (value instanceof Double[][]) {
				Double[][]array = (Double[][]) value;
				for(Double[] line : array){
					System.out.println();
					boolean first =true;
					for(Double v : line ){
						if (first) {
							first = false;
						}else {
							System.out.print(", ");
						}
						System.out.print(v);
					}
				}

			} else {
				System.out.println("value: " + value.toString());
			}

		} catch (de.uniol.inf.is.odysseus.mep.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
