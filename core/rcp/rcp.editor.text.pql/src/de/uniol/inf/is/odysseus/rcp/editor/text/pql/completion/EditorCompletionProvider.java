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
package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;

/**
 * @author Dennis Geesen
 * 
 */
public class EditorCompletionProvider implements
		IEditorLanguagePropertiesProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.editor.text.IEditorLanguagePropertiesProvider
	 * #getWords (java.lang.String, java.lang.String)
	 */
	@Override
	public List<ICompletionProposal> getCompletionSuggestions(
			String currentToken, String lastSplitters[], IExecutor executor,
			ISession session, IDocument document, int offset, Point selection) {
		List<ICompletionProposal> result = new ArrayList<>();
		currentToken = currentToken.trim().toLowerCase();
		String tokenbefore = "";
		if (lastSplitters.length > 0) {
			tokenbefore = lastSplitters[0].trim().toLowerCase();
		}

		// pre-build operator templates
		List<ICompletionProposal> operators = new ArrayList<ICompletionProposal>();
		for (LogicalOperatorInformation opBuilder : PQLEditorTextPlugIn
				.getOperatorInformations()) {
			if (opBuilder.getOperatorName().toLowerCase()
					.startsWith(currentToken)) {
				operators.add(buildCompletionProposalOperator(opBuilder,
						offset, currentToken.length(), document));
			}
		}

		Collections.sort(operators, new Comparator<ICompletionProposal>() {
			@Override
			public int compare(ICompletionProposal o1, ICompletionProposal o2) {
				return o1.getDisplayString().compareToIgnoreCase(
						o2.getDisplayString());
			}
		});

		// after comma it is
		String prefix = getPrefix(currentToken);
		currentToken = currentToken.substring(0,
				currentToken.length() - prefix.length()).trim();
		if (tokenbefore.endsWith(",") && lastSplitters.length > 1) {
			// is an operator, if the it also contains }
			if (lastSplitters[1].endsWith("}")) {
				return operators;
			}
			if (inListParam(document, offset)) {
				LogicalOperatorInformation builder = getOperatorAtPosition(document,
						offset);
				LogicalParameterInformation param = getParamAtPosition(document, offset,
						builder);
				if (param != null) {
					LogicalParameterInformation listP = (LogicalParameterInformation) param;
					String s = getParamValue(listP, -1, "", false);
					if (s.length() > 0) {
						if (!prefix.endsWith(s.substring(0, 1))) {
							List<ICompletionProposal> l = new ArrayList<>();
							l.add(buildCompletionProposal(s, offset));
							return l;
						}
					}
					return new ArrayList<>();
				}
			}
			// or it must be a parameter
			else {
				String op = findPreviousOperator(document, offset).trim();
				for (LogicalOperatorInformation opBuilder : PQLEditorTextPlugIn.getOperatorInformations()) {
					if (opBuilder.getOperatorName().equalsIgnoreCase(op)) {
						for (LogicalParameterInformation param : opBuilder.getParameters()) {
							result.add(buildCompletionProposalAttribute(param,
									offset, document));
						}
						break;
					}
				}
			}

		} else if (tokenbefore.endsWith("(")) {
			result.add(buildCompletionProposal("{", offset));
			result.add(buildCompletionProposal(")", offset));
			result.addAll(operators);
			return result;
		} else if (tokenbefore.endsWith("{")) {
			String op = findPreviousOperator(document, offset).trim();
			for (LogicalOperatorInformation opBuilder : PQLEditorTextPlugIn.getOperatorInformations()) {
				if (opBuilder.getOperatorName().equalsIgnoreCase(op)) {
					for (LogicalParameterInformation param : opBuilder.getParameters()) {
						result.add(buildCompletionProposalAttribute(param,
								offset, document));
					}
					break;
				}
			}
		} else if (tokenbefore.endsWith("=")) {
			if (isInParameterSection(document, offset - 1)) {
				LogicalOperatorInformation builder = getOperatorAtPosition(document,
						offset - 1);
				if (builder != null) {
					LogicalParameterInformation param = getParamAtPosition(document,
							offset - 1, builder);
					if (param != null) {
						String suffix = "";
						if (prefix.equals("'")) {
							try {
								char c = document.getChar(offset);
								if (c == '\'') {
									suffix = "'";
								}
							} catch (Exception e) {
							}
						}
						return buildParamValueProposal(param, offset, prefix,
								suffix, selection);
					}
				}
				return new ArrayList<>();
			}
			// no variable definition => stream definition, so we need an
			// operator
			return operators;
		}
		return result;
	}

	private boolean inListParam(IDocument document, int offset) {
		int n = offset;
		try {
			while (n > 0) {
				if (document.getChar(n) == '=') {
					return false;
				}
				if (document.getChar(n) == '[') {
					return true;
				}
				n--;
			}
		} catch (BadLocationException e) {
		}
		return false;
	}

	private LogicalParameterInformation getParamAtPosition(IDocument document, int offset,
			LogicalOperatorInformation builder) {
		if (builder == null) {
			return null;
		}
		try {
			int n = offset;
			String name = "";
			boolean read = false;
			while (n > 0) {
				char c = document.getChar(n);
				if (read
						&& (Character.isWhitespace(c) || c == ',' || c == '(' || c == '{')) {
					break;
				}
				if (read) {
					name = c + name;
				}
				if (c == '=') {
					read = true;
				}
				n--;
			}
			for (LogicalParameterInformation param : builder.getParameters()) {
				if (param.getName().equalsIgnoreCase(name)) {
					return param;
				}
			}
		} catch (BadLocationException e) {

		}
		return null;

	}

	private LogicalOperatorInformation getOperatorAtPosition(IDocument document,
			int offset) {
		try {
			int n = offset;
			String name = "";
			boolean read = false;
			while (n > 0) {
				char c = document.getChar(n);
				if (read
						&& (Character.isWhitespace(c) || getTokenSplitters()
								.contains(c))) {
					break;
				}
				if (read) {
					name = c + name;
				}
				if (c == '(') {
					read = true;
				}
				n--;
			}
			for (LogicalOperatorInformation builder : PQLEditorTextPlugIn
					.getOperatorInformations()) {
				if (builder.getOperatorName().equalsIgnoreCase(name)) {
					return builder;
				}
			}
		} catch (BadLocationException e) {

		}
		return null;
	}

	private boolean isInParameterSection(IDocument document, int offset) {
		try {
			int n = offset;
			while (n > 0) {
				char c = document.getChar(n);
				if (c == '}') {
					return false;
				}
				if (c == '{') {
					return true;
				}
				n--;
			}
		} catch (BadLocationException e) {

		}
		return false;
	}

	/**
	 * @param currentToken
	 * @return
	 */
	private String getPrefix(String currentToken) {
		for (int n = currentToken.length() - 1; n >= 0; n--) {
			if (getTokenSplitters().contains(currentToken.charAt(n))) {
				return currentToken.substring(n + 1);
			}
		}
		return currentToken;
	}

	private ICompletionProposal buildCompletionProposal(String word, int offset) {
		return new CompletionProposal(word, offset, 0, word.length());
	}

	private List<ICompletionProposal> buildParamValueProposal(
			LogicalParameterInformation param, int offset, String prefix, String suffix,
			Point selection) {
		List<ICompletionProposal> cp = new ArrayList<>();
		// for (String value : param.getPossibleValues()) {
		// if (selection.y > 0) {
		// cp.add(new CompletionProposal(value, selection.x, selection.y,
		// value.length()));
		// } else {
		// String pvalue = getParamValue(param, -1, value);
		// cp.add(new CompletionProposal(pvalue, offset - prefix.length(),
		// prefix.length() + suffix.length(), pvalue.length()));
		// }
		// }
		return cp;
	}

	private ICompletionProposal buildCompletionProposalAttribute(
			LogicalParameterInformation param, int offset, IDocument doc) {
		int length = 0;
		Region region = new Region(offset, length);
		String word = getParamString(param);
		Image image = PQLEditorTextPlugIn.getImageManager().get("attribute");
		String displayString = param.getName();
		Template template = new Template(displayString, param.getDoc(),
				"no-context", word, true);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext context = new DocumentTemplateContext(contextType, doc,
				offset, length);
		TemplateProposal tp = new TemplateProposal(template, context, region,
				image);
		return tp;
	}

	private ICompletionProposal buildCompletionProposalOperator(
			LogicalOperatorInformation op, int offset, int length, IDocument doc) {
		Image image = PQLEditorTextPlugIn.getImageManager().get("pqlOperator");
		Region region = new Region(offset - length, length);
		String tempString = buildTemplateString(op);
		Template template = new Template(op.getOperatorName().toUpperCase(),
				op.getDoc(), "no-context", tempString, true);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext context = new DocumentTemplateContext(contextType, doc,
				offset - length, length);
		TemplateProposal tp = new TemplateProposal(template, context, region,
				image);
		return tp;
	}

	private String buildTemplateString(LogicalOperatorInformation op) {
		String mandatoryParams = "";
		String sep = "";
		int i = 1;
		for (LogicalParameterInformation p : op.getParameters()) {
			if (p.isMandatory()) {
				mandatoryParams = mandatoryParams + sep + getParamString(p, i);
				sep = ", ";
				i++;
			}
		}

		StringBuilder tempString = new StringBuilder(op.getOperatorName()
				.toUpperCase() + "(");
		if (!mandatoryParams.isEmpty()) {
			tempString.append("{" + mandatoryParams + "}");
		}
		if (op.getMinPorts() > 0) {
			if (!mandatoryParams.isEmpty()) {
				tempString.append(", ");
			}
			tempString.append("${inputoperator}");
		}
		tempString.append(")");
		return tempString.toString();
	}

	/**
	 * @param document
	 * @param offset
	 */
	private String findPreviousOperator(IDocument doc, int offset) {
		boolean rawMode = false;
		boolean found = false;
		try {
			int start = offset;
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (c == '\'') {
					rawMode = !rawMode;
				}

				if (found && !Character.isJavaIdentifierPart(c)) {
					return doc.get(n, start - n);
				}

				if (!rawMode && c == '(') {
					start = n;
					found = true;
				}

			}
		} catch (BadLocationException e) {
		}
		return "";

	}

	private String getParamString(LogicalParameterInformation param) {
		return getParamString(param, -1);
	}
	
	private String getParamString(LogicalParameterInformation param, int nr) {
		String name = param.getName() + "="
				+ getParamValue(param, nr, "", false);
		return name;
	}

	private String getParamValue(LogicalParameterInformation param, int nr,
			String val, boolean nestedList) {
		if (nr >= 0) {
			val = "${value" + nr + "}";
		}
		String paramTypeName = param.getParameterClass().getName();
		if (paramTypeName.endsWith("builder.StringParameter")
				|| paramTypeName
						.endsWith("builder.ResolvedSDFAttributeParameter")) {
			return "'" + val + "'";
		} else if (param.isList() && !nestedList) {
			return "[" + getParamValue(param, nr, val, true) + "]";
		} else if (paramTypeName.endsWith("builder.AggregateItemParameter")) {
			if (nr >= 0) {
				return "['${aggregatefunction" + nr + "}', '${attribute" + nr
						+ "}', '${aggregatename" + nr
						+ "}', '${aggregatedatatype" + nr + "}']";
				// return "['${aggregatefunction" + nr + "}', '${attribute" + nr
				// + "}', '${aggregatename" + nr + "}', '${aggregate-datatype" +
				// nr + "}']";
			}
			return "['', '', '', '']";
		} else if (paramTypeName
				.endsWith("builder.CreateSDFAttributeParameter")) {
			return "['${name}','${datatype}']";
		}

		else if (paramTypeName.endsWith("builder.BooleanParameter")) {
			return val;
		} else {
			return val;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.text.IEditorCompletionProvider#
	 * getTokenSplitters()
	 */
	@Override
	public List<Character> getTokenSplitters() {
		List<Character> result = new ArrayList<Character>();
		result.add('=');
		result.add('{');
		result.add('}');
		result.add(' ');
		result.add('(');
		result.add(')');
		result.add('[');
		result.add(']');
		result.add(',');
		return result;
	}

	@Override
	public boolean ignoreWhitespaces() {
		return true;
	}

	@Override
	public String getSupportedParser() {
		return "PQL";
	}

	@Override
	public List<String> getTerminals() {
		List<String> names = new ArrayList<>();
		for (LogicalOperatorInformation loi : PQLEditorTextPlugIn
				.getOperatorInformations()) {
			names.add(loi.getOperatorName());
		}
		return names;
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		return new PQLFormattingStrategy();
	}

}
