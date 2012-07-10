/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.script.parser;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class IfController {

	private static final String PARAMETER_KEY = "#";

	public static final String DEFINE_KEY = "DEFINE";
	public static final String IFDEF_KEY = "IFDEF";
	public static final String IFNDEF_KEY = "IFNDEF";
	public static final String ELSE_KEY = "ELSE";
	public static final String ENDIF_KEY = "ENDIF";
	public static final String UNDEF_KEY = "UNDEF";

	private final String[] text;
	private final List<String> defined = Lists.newArrayList();

	private int currentLine = 0;
	
	private Boolean inIfClause = null;

	public IfController( String[] text ) {
		Preconditions.checkNotNull(text, "Text-array for IfController must not be null!");
		
		this.text = text;
	}

	public boolean canExecuteNextLine() throws OdysseusScriptException {
		try {
			
			if( determineEndIf(text[currentLine].trim()) ) {
				if( inIfClause == null ) {
					throw new OdysseusScriptException("ENDIF without IFDEF/IFNDEF!");
				}
				
				inIfClause = null;
				return false;
			}
			
			if( determineElse(text[currentLine].trim()) ) {
				if( inIfClause == null ) {
					throw new OdysseusScriptException("ELSE without IFDEF/IFNDEF!");
				}
				
				inIfClause = !inIfClause;
				return false;
			}
			
			
			if( inIfClause != null && inIfClause == false ) {
				return false;
			}
			
			Optional<String> optionalDefined = determineDefined( text[currentLine].trim() );
			if( optionalDefined.isPresent() && !defined.contains(optionalDefined.get())) {
				defined.add(optionalDefined.get());
				return true;
			}
			
			Optional<String> optionalUndefined = determineUndefined( text[currentLine].trim());
			if( optionalUndefined.isPresent() ) {
				defined.remove(optionalUndefined.get());
				return false;
			}
			
			Optional<String> optionalIfDef = determineIfDef( text[currentLine].trim() );
			if(optionalIfDef.isPresent()) {
				
				if( inIfClause != null ) {
					throw new OdysseusScriptException("Nested IFDEF/IFNDEF-clauses not allowed (now)!");
				}
				
				inIfClause = defined.contains(optionalIfDef.get());
				return false;
			}
			
			Optional<String> optionalIfNDef = determineIfNDef( text[currentLine].trim() );
			if(optionalIfNDef.isPresent()) {
				
				if( inIfClause != null ) {
					throw new OdysseusScriptException("Nested IFDEF/IFNDEF-clauses not allowed (now)!");
				}
				
				inIfClause = !defined.contains(optionalIfNDef.get());
				return false;
			}
			
			return true;
		} finally {
			currentLine++;
		}
	}
	
	private boolean determineElse(String trim) {
		return hasPreParserKeyword(PARAMETER_KEY + ELSE_KEY, trim);
	}

	private static Optional<String> determineIfNDef(String trim) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + IFNDEF_KEY, trim);
	}

	private static boolean determineEndIf(String textline) {
		return hasPreParserKeyword(PARAMETER_KEY + ENDIF_KEY, textline);
	}

	private static Optional<String> determineIfDef(String textline) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + IFDEF_KEY, textline);
	}

	private static Optional<String> determineDefined( String textLine ) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + DEFINE_KEY, textLine);
	}
	
	private static Optional<String> determineUndefined(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + UNDEF_KEY, textLine);
	}
	
	private static boolean hasPreParserKeyword( String keyword, String textLine ) {
		return textLine.indexOf(keyword) != -1;
	}

	private static Optional<String> determineReplacement(String keyword, String textLine) throws OdysseusScriptException {
		int definePos = textLine.indexOf(keyword);
		if( definePos != -1 ) {
			String[] parts = textLine.split(" |\t", 3);
			if( parts.length < 2) {
				throw new OdysseusScriptException("Name for " + keyword + " expected.");
			}
			return Optional.of(parts[1]);
		}
		
		return Optional.absent();
	}
}
