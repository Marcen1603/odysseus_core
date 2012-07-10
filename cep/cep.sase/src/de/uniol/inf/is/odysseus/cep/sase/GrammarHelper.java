/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.cep.sase;

import java.util.Scanner;

public class GrammarHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		while (true){
			Scanner in = new Scanner(System.in);
			System.out.print(">");
			String toConvert  = in.nextLine();
			StringBuffer out = new StringBuffer();
			
			for (int i=0;i<toConvert.length();i++){
				String c = ""+toConvert.charAt(i);
				if (c.toLowerCase() != c.toUpperCase()) {
					out.append("('").append(c.toUpperCase()).append("'|'").append(c.toLowerCase()).append("')");
				}else{
					out.append("('").append(c).append("')");
				}
			}
			System.out.println(out.toString());
		}
	}

}
