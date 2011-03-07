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
package de.uniol.inf.is.odysseus.scars.rcp;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserConstants;
import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;

public class PQLHackKeywords implements IKeywordGroup {

	private String[] keys;
	
	public PQLHackKeywords() {
		String[] pqlHackKeys = ProceduralExpressionParserConstants.tokenImage;
		
		ArrayList<String> k = new ArrayList<String>();
		for( int i = 5; i < pqlHackKeys.length - 22; i++ ) {
			
			String pqlHackKey = pqlHackKeys[i];
			
			// Manche Steuerzeichen beginnen mit '<', die sollen nicht hervorgehoben werden
			if( pqlHackKey.startsWith("<"))
				continue;
			
			// pqlHackKey beinhaltet am Anfang und am Ende Anführungszeichen
			// die müssen weg
			String str = pqlHackKey.substring(1, pqlHackKey.length()-1);
			
			// Spezielle Zeichen entfernen
			if( pqlHackKey.equals("\t")) continue;
			if( pqlHackKey.equals("\n")) continue;
			if( pqlHackKey.equals("\r")) continue;
			if( pqlHackKey.equals(":=")) continue;
			if( pqlHackKey.equals(" ")) continue;
			
			if( str.length() > 1 ) {
				k.add(str);
			}
		}
		
		keys = k.toArray(new String[k.size()]);
	}

	@Override
	public String[] getKeywords() {
		return keys;
	}

}
