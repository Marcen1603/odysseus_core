package de.uniol.inf.is.odysseus.rcp.editor.text.pqlhack;

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
