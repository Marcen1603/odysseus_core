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
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.script.parser.activator.Activator;

public class IfController {

	private static final Logger LOG = LoggerFactory.getLogger(IfController.class);
	private static final InfoService INFO = InfoServiceFactory
			.getInfoService(IfController.class);
	
	
	public static final String PARAMETER_KEY = "#";

	public static final String DEFINE_KEY = "DEFINE";
	public static final String IFDEF_KEY = "IFDEF";
	public static final String IFNDEF_KEY = "IFNDEF";
	public static final String ELSE_KEY = "ELSE";
	public static final String ENDIF_KEY = "ENDIF";
	public static final String UNDEF_KEY = "UNDEF";

	public static final String SRCDEF_KEY = "IFSRCDEF";
	public static final String SRCNDEF_KEY = "IFSRCNDEF";
	public static final String QUERY_DEF_KEY = "IFQDEF";
	public static final String QUERY_NDEF_KEY = "IFQNDEF";

	public static final String IF_KEY = "IF";

	public static final String PRINT_KEY = "PRINT";

	private final String[] text;
	private final ISession caller;
	private final List<String> defined = Lists.newArrayList();
	private final Stack<Boolean> inIfClause = new Stack<Boolean>();

	private int currentLine = 0;

	public IfController(String[] text, ISession caller) {
		Preconditions.checkNotNull(text, "Text-array for IfController must not be null!");

		this.text = text;
		this.caller = caller;
	}

	public boolean canExecuteNextLine(ReplacementContainer replacements) throws OdysseusScriptException {
		try {

			if (this.currentLine > text.length) {
				throw new OdysseusScriptException("current line in if controller is greater than text length.\ncurrentline=" + this.currentLine + ", Text:\n" + text);
			}

			String currentLine = text[this.currentLine].trim();

			if (determineEndIf(currentLine)) {
				LOG.trace("Found #ENDIF: Line {}: {}", this.currentLine, currentLine);
				
				if (inIfClause.isEmpty()) {
					throw new OdysseusScriptException("ENDIF without IF/IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF!");
				}

				inIfClause.pop();
				LOG.trace("Stack: {}", inIfClause);
				return false;
			}

			if (determineElse(currentLine)) {
				LOG.trace("Found #ELSE: Line {}: {}", this.currentLine, currentLine);
				if (inIfClause.isEmpty()) {
					throw new OdysseusScriptException("ELSE without IF/IFDEF/IFNDEF/IFSRCDEF/IFSRCNDEF!");
				}

				Boolean old = inIfClause.pop();
				inIfClause.push(!old);
				LOG.trace("Stack: {}", inIfClause);
				return false;
			}

			Optional<String> optionalDefined = determineDefine(currentLine);
			if (optionalDefined.isPresent() && !defined.contains(optionalDefined.get())) {
				LOG.trace("Found #DEFINE: Line {}: {}", this.currentLine, currentLine);
				if( canExecuteNow() ) {
					defined.add(optionalDefined.get());
					return true;
				}
				return false;
			}

			Optional<String> optionalUndefined = determineUndefine(currentLine);
			if (optionalUndefined.isPresent()) {
				LOG.trace("Found #UNDEF: Line {}: {}", this.currentLine, currentLine);
				if( canExecuteNow() ) {
					defined.remove(optionalUndefined.get());
					return true;
				}
				return false;
			}

			Optional<String> optionalPrint = determinePrint(currentLine);
			if (optionalPrint.isPresent()) {
				LOG.trace("Found #PRINT: Line {}: {}", this.currentLine, currentLine);
				if( canExecuteNow() ) {
					executePrint(replacements, optionalPrint.get());
				}
				return false;
			}

			Optional<String> optionalIfDef = determineIfDef(currentLine);
			if (optionalIfDef.isPresent()) {
				LOG.trace("Found #IFDEF: Line {}: {}", this.currentLine, currentLine);

				Boolean value = defined.contains(optionalIfDef.get());
				inIfClause.push(value);
				LOG.trace("Stack: {}", inIfClause);

				return false;
			}

			Optional<String> optionalIfNDef = determineIfNDef(currentLine);
			if (optionalIfNDef.isPresent()) {
				LOG.trace("Found #IFNDEF: Line {}: {}", this.currentLine, currentLine);

				Boolean value = defined.contains(optionalIfNDef.get());
				inIfClause.push(!value);
				LOG.trace("Stack: {}", inIfClause);

				return false;
			}

			Optional<String> optionalSrcDef = determineIfSrcDef(currentLine);
			if (optionalSrcDef.isPresent()) {
				Boolean value = existsSource(optionalSrcDef.get(), caller);
				inIfClause.push(value);
				LOG.trace("Stack: {}", inIfClause);

				return false;
			}

			Optional<String> optionalSrcNotDef = determineIfSrcNDef(currentLine);
			if (optionalSrcNotDef.isPresent()) {
				LOG.trace("Found #IFSRCDEF: Line {}: {}", this.currentLine, currentLine);
				Boolean value = existsSource(optionalSrcNotDef.get(), caller);
				inIfClause.push(!value);
				LOG.trace("Stack: {}", inIfClause);

				return false;
			}
			
			
			Optional<String> optionalQDef = determineIfQDef(currentLine);
			if (optionalQDef.isPresent()) {
				Resource r = Resource.specialCreateResource(optionalQDef.get(), caller.getUser());
				Boolean value = existsQuery(r, caller);
				inIfClause.push(value);
				LOG.trace("Stack: {}", inIfClause);

				return false;
			}

			Optional<String> optionalQNotDef = determineIfQNDef(currentLine);
			if (optionalQNotDef.isPresent()) {
				Resource r = Resource.specialCreateResource(optionalQNotDef.get(), caller.getUser());
				Boolean value = existsQuery(r, caller);
				inIfClause.push(!value);
				return false;
			}

			Optional<String> optionalIf = determineIf(currentLine);
			if (optionalIf.isPresent()) {
				LOG.trace("Found #IF: Line {}: {}", this.currentLine, currentLine);
				
				if( canExecuteNow() ) {
					String stringExpression = optionalIf.get();
					SDFExpression expression = new SDFExpression(stringExpression, null, MEP.getInstance());
					
					List<SDFAttribute> attributes = expression.getAllAttributes();
					List<Object> values = Lists.newArrayList();
					Map<String, Serializable> replacementMap = replacements.toMap();
					for (SDFAttribute attribute : attributes) {
						String name = attribute.getAttributeName().toUpperCase();
						if (!replacementMap.containsKey(name)) {
							INFO.warning("Replacementkey " + name + " not known in #IF-statement");
							//throw new OdysseusScriptException("Replacementkey " + name + " not known in #IF-statement");
							inIfClause.push(false);
							return false;
						}
						
						values.add(replacementMap.get(name));
					}
					
					expression.bindVariables(values.toArray());
					
					inIfClause.push((Boolean) expression.getValue());
					LOG.trace("Stack: {}", inIfClause);
				} else {
					inIfClause.push(false);
				}
				
				return false;
			}

			return canExecuteNow();
		} finally {
			currentLine++;
		}
	}

	private boolean canExecuteNow() {
		for( Boolean b : inIfClause ) {
			if( b == false ) {
				//LOG.trace("Line : false");
				return false;
			}
		}
		//LOG.trace("Line : true");
		return true;
	}

	private static void executePrint(ReplacementContainer replacements, String stringExpression) throws OdysseusScriptException {
		SDFExpression expression = new SDFExpression(stringExpression, null, MEP.getInstance());

		List<SDFAttribute> attributes = expression.getAllAttributes();
		List<Object> values = Lists.newArrayList();
		Map<String, Serializable> replacementMap = replacements.toMap();
		for (SDFAttribute attribute : attributes) {
			String name = attribute.getAttributeName().toUpperCase();
			if (!replacementMap.containsKey(name)) {
				throw new OdysseusScriptException("Replacementkey " + name + " not known in #PRINT-statement");
			}

			values.add(replacementMap.get(name));
		}
		expression.bindVariables(values.toArray());
		System.out.println(String.valueOf(expression.getValue()));
	}

	private static boolean existsSource(String sourceName, ISession caller) {
		return Activator.getExecutor().containsViewOrStream(new Resource(caller.getUser(), sourceName), caller);
	}

	private static boolean existsQuery(Resource queryName, ISession caller) {
	
		return Activator.getExecutor().getLogicalQueryByName(queryName , caller) != null;
		///return Activator.getExecutor().getLogicalQueryByName(new Resource(caller.getUser(), queryName).toString(), caller) != null;
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

	private static Optional<String> determineDefine(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + DEFINE_KEY, textLine);
	}

	private static Optional<String> determineUndefine(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + UNDEF_KEY, textLine);
	}

	private static Optional<String> determineIfSrcDef(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + SRCDEF_KEY, textLine);
	}

	private static Optional<String> determineIfSrcNDef(String textLine) throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + SRCNDEF_KEY, textLine);
	}
	
	private static Optional<String> determineIfQDef(String textLine)throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + QUERY_DEF_KEY, textLine);
	}

	private static Optional<String> determineIfQNDef(String textLine)throws OdysseusScriptException {
		return determineReplacement(PARAMETER_KEY + QUERY_NDEF_KEY, textLine);
	}

	private static Optional<String> determinePrint(String textLine) {
		String strToFind = PARAMETER_KEY + PRINT_KEY + " ";
		int pos = textLine.toUpperCase().indexOf(strToFind.toUpperCase());
		if (pos != -1) {
			return Optional.of(textLine.substring(pos + strToFind.length()));
		}
		return Optional.absent();
	}

	private static Optional<String> determineIf(String textLine) {
		String strToFind = PARAMETER_KEY + IF_KEY + " ";
		int pos = textLine.toUpperCase().indexOf(strToFind.toUpperCase());
		if (pos != -1) {
			return Optional.of(textLine.substring(pos + strToFind.length()));
		}
		return Optional.absent();
	}

	private static boolean hasPreParserKeyword(String keyword, String textLine) {
		return textLine.toUpperCase().indexOf(keyword.toUpperCase()) != -1;
	}

	private static Optional<String> determineReplacement(String keyword, String textLine) throws OdysseusScriptException {
		int definePos = textLine.toUpperCase().indexOf(keyword.toUpperCase());
		if (definePos != -1) {
			String[] parts = textLine.split(" |\t", 3);
			if (parts.length < 2) {
				throw new OdysseusScriptException("Name for " + keyword + " expected.");
			}
			return Optional.of(parts[1]);
		}

		return Optional.absent();
	}

	public void checkState() throws OdysseusScriptException {
		if (!inIfClause.isEmpty()) {
			throw new OdysseusScriptException("Missing #ENDIF at end of file");
		}
	}
}