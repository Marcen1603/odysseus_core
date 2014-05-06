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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.script.parser.activator.Activator;

public class IfController {

	private static final String PARAMETER_KEY = "#";

	public static final String DEFINE_KEY = "DEFINE";
	public static final String IFDEF_KEY = "IFDEF";
	public static final String IFNDEF_KEY = "IFNDEF";
	public static final String ELSE_KEY = "ELSE";
	public static final String ENDIF_KEY = "ENDIF";
	public static final String UNDEF_KEY = "UNDEF";
	
	public static final String SRCDEF_KEY = "IFSRCDEF";
	public static final String SRCNDEF_KEY = "IFSRCNDEF";
	
	public static final String IF_KEY = "IF";
	
	public static final String PRINT_KEY = "PRINT";
	
	private final String[] text;
	private final ISession caller;
	private final List<String> defined = Lists.newArrayList();

	private int currentLine = 0;
	
	private Boolean inIfClause = null;

	public IfController( String[] text, ISession caller ) {
		Preconditions.checkNotNull(text, "Text-array for IfController must not be null!");
		
		this.text = text;
		this.caller = caller;
	}

	public boolean canExecuteNextLine(ReplacementContainer replacements) throws OdysseusScriptException {
		try {
			
			if(this.currentLine>text.length){
				
				throw new OdysseusScriptException("current line in if controller is greater than text length.\ncurrentline="+this.currentLine+", Text:\n"+text);
			}
			
			String currentLine = text[this.currentLine].trim();
			
			if( determineEndIf(currentLine) ) {
				if( inIfClause == null ) {
					throw new OdysseusScriptException("ENDIF without IF/IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF!");
				}
				
				inIfClause = null;
				return false;
			}
			
			if( determineElse(currentLine) ) {
				if( inIfClause == null ) {
					throw new OdysseusScriptException("ELSE without IF/IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF!");
				}
				
				inIfClause = !inIfClause;
				return false;
			}
			
			
			if( inIfClause != null && inIfClause == false ) {
				return false;
			}
			
			Optional<String> optionalDefined = determineDefined( currentLine );
			if( optionalDefined.isPresent() && !defined.contains(optionalDefined.get())) {
				defined.add(optionalDefined.get());
				return true;
			}
			
			Optional<String> optionalUndefined = determineUndefined( currentLine);
			if( optionalUndefined.isPresent() ) {
				defined.remove(optionalUndefined.get());
				return false;
			}
			
            Optional<String> optionalPrint = determinePrint(currentLine);
            if (optionalPrint.isPresent()) {
                String stringExpression = optionalPrint.get();
                SDFExpression expression = new SDFExpression(stringExpression, MEP.getInstance());
                
                List<SDFAttribute> attributes = expression.getAllAttributes();
                List<Object> values = Lists.newArrayList();
                Map<String, Serializable> replacementMap = replacements.toMap();
                for( SDFAttribute attribute : attributes ) {
                    String name = attribute.getAttributeName().toUpperCase();
                    if( !replacementMap.containsKey(name)) {
                        throw new OdysseusScriptException("Replacementkey " + name + " not known in #PRINT-statement");
                    } 
                    
                    values.add(replacementMap.get(name));
                }
                expression.bindVariables(values.toArray());
                System.out.println(expression.getValue());
                return false;
            }
	            
			Optional<String> optionalIfDef = determineIfDef( currentLine );
			if(optionalIfDef.isPresent()) {
				
				if( inIfClause != null ) {
					throw new OdysseusScriptException("Nested IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF-clauses not allowed (now)!");
				}
				
				inIfClause = defined.contains(optionalIfDef.get());
				return false;
			}
			
			Optional<String> optionalIfNDef = determineIfNDef( currentLine );
			if(optionalIfNDef.isPresent()) {
				
				if( inIfClause != null ) {
					throw new OdysseusScriptException("Nested IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF-clauses not allowed (now)!");
				}
				
				inIfClause = !defined.contains(optionalIfNDef.get());
				return false;
			}
			
			Optional<String> optionalSrcDef = determineIfSrcDef(currentLine);
			if( optionalSrcDef.isPresent() ) {
				if( inIfClause != null ) {
					throw new OdysseusScriptException("Nested IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF-clauses not allowed (now)!");
				}
				
				inIfClause = existsSource(optionalSrcDef.get(), caller);
				
				return false;
			}
			
			Optional<String> optionalSrcNotDef = determineIfSrcNDef(currentLine);
			if( optionalSrcNotDef.isPresent() ) {
				if( inIfClause != null ) {
					throw new OdysseusScriptException("Nested IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF-clauses not allowed (now)!");
				}
				
				inIfClause = !existsSource(optionalSrcNotDef.get(), caller);
				
				return false;
			}			
			
			Optional<String> optionalIf = determineIf(currentLine);
			if( optionalIf.isPresent() ) {
				String stringExpression = optionalIf.get();
				SDFExpression expression = new SDFExpression(stringExpression, MEP.getInstance());
				
				List<SDFAttribute> attributes = expression.getAllAttributes();
				List<Object> values = Lists.newArrayList();
				Map<String, Serializable> replacementMap = replacements.toMap();
				for( SDFAttribute attribute : attributes ) {
					String name = attribute.getAttributeName().toUpperCase();
					if( !replacementMap.containsKey(name)) {
						throw new OdysseusScriptException("Replacementkey " + name + " not known in #IF-statement");
					} 
					
					values.add(replacementMap.get(name));
				}
				
				expression.bindVariables(values.toArray());
				
				inIfClause = expression.getValue();
				return false;
			}
			
			return true;
		} finally {
			currentLine++;
		}
	}

	private static boolean existsSource(String sourceName, ISession caller) {
		return Activator.getExecutor().containsViewOrStream(new Resource(caller.getUser(), sourceName), caller);
	}

	private static boolean determineElse(String trim) {
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
	
	private static Optional<String> determineIfSrcDef(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + SRCDEF_KEY, textLine);
	}	
	
	private static Optional<String> determineIfSrcNDef(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + SRCNDEF_KEY, textLine);
	}	
	
    private static Optional<String> determinePrint(String textLine) {
        String strToFind = PARAMETER_KEY + PRINT_KEY + " ";
        int pos = textLine.indexOf(strToFind);
        if( pos != -1 ) {
            return Optional.of(textLine.substring(pos + strToFind.length() ));
        }
        return Optional.absent();
    }
	   
	private static Optional<String> determineIf(String textLine) {
		String strToFind = PARAMETER_KEY + IF_KEY + " ";
		int pos = textLine.indexOf(strToFind);
		if( pos != -1 ) {
			return Optional.of(textLine.substring(pos + strToFind.length() ));
		}
		return Optional.absent();
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
