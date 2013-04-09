/********************************************************************************** 
 * Copyright 2013 The Odysseus Team
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

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.DefaultFormattingStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition.MultiLineBracketRule;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition.OdysseusScriptPartitioner;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;

/**
 * @author Dennis Geesen
 * 
 */
public class PQLFormattingStrategy extends DefaultFormattingStrategy {

	private static final String TAB = "  ";

	// all between ( and )
	private static final String PARTITION_OPERATOR = "__pql_operator";
	// all between { and }
	private static final String PARTITION_PARAMETERS = "__pql_parameters";
	// all between ' and '
	private static final String PARTITION_STRING = "__pql_string";
	// all lines starting with ///
	private static final String PARTITION_COMMENT = "__pql_comment";
	// all between [ and ] in parameters
	private static final String PARTITION_PARAMLIST = "__pql_paramlist";
	// substring "name = <something>" in parameters
	private static final String PARTITION_ONEPARAM = "__pql_oneparam";
	// all between ${ and }
	private static final String PARTITION_REPLACEMENT = "__pql_replacement";

	private final IToken operatorToken = new Token(PARTITION_OPERATOR);
	private final IToken parametersToken = new Token(PARTITION_PARAMETERS);
	private final IToken stringToken = new Token(PARTITION_STRING);
	private final IToken commentToken = new Token(PARTITION_COMMENT);
	private final IToken paramlistToken = new Token(PARTITION_PARAMLIST);
	private final IToken oneParamToken = new Token(PARTITION_ONEPARAM);
	private final IToken replacementToken = new Token(PARTITION_REPLACEMENT);

	@Override
	public String format(String content, boolean isLineStart, String indentation, int[] positions) {
		final String originalContent = content;
		try {
			final IPredicateRule[] rules = new IPredicateRule[4];
			rules[0] = new EndOfLineRule("///", commentToken);
			rules[1] = new MultiLineBracketRule('(', ')', operatorToken);
			rules[2] = new MultiLineBracketRule('{', '}', parametersToken);
			rules[3] = new MultiLineBracketRule('\'', '\'', stringToken);

			final ITypedRegion[] partitions = calculatePartitions(content, rules, new String[] { PARTITION_COMMENT, PARTITION_OPERATOR, PARTITION_PARAMETERS, PARTITION_STRING });

			final String indent = indentation;
			String newContent = "";
			for (final ITypedRegion part : partitions) {
				String fPart = getPart(part, content);
				if (part.getType().equalsIgnoreCase(PARTITION_OPERATOR)) {
					final String lineAtOff = lineAtOffset(content, part.getOffset());
					String correctTab = indent;
					// is this the first operator?!
					if (lineAtOff.contains("=")) {
						int index = lineAtOff.indexOf("=") + 1;
						while (Character.isWhitespace(lineAtOff.charAt(index))) {
							index++;
						}
						if (!lineAtOff.startsWith(TAB)) {
							final int tabs = (int) Math.ceil(index / getTabSize());
							for (int i = 0; i < tabs; i++) {
								correctTab = correctTab + TAB;
							}
						}
					}
					fPart = formatOperator(fPart, correctTab);
				}
				newContent = newContent + fPart;
			}
			// names to upper case
			newContent = operatorNamesToUpperCase(newContent);
			if (isLineStart) {
				indentation = initialIndentation;
			}

			return newContent;
		} catch (final Exception e) {
			e.printStackTrace();
			return originalContent;
		}
	}

	protected String insertInstead(String text, String insert, int position, int length) {
		final String firstPart = text.substring(0, position);
		final String lastPart = text.substring(position + length, text.length());
		return firstPart + insert + lastPart;
	}

	protected String removeWhitespacesAndNewline(String content) {
		final String[] contentParts = content.split("\\s+|\r|\n");
		final StringBuffer buffer = new StringBuffer();
		for (final String contentPart : contentParts) {
			buffer.append(contentPart.trim()).append(" ");
		}
		buffer.delete(buffer.length() - 1, buffer.length());
		return buffer.toString();
	}

	protected String simpleReplace(String content, String toRepalce, String replaceWith) {
		final String[] parts = content.split(toRepalce);
		final StringBuffer buffer = new StringBuffer();
		for (final String part : parts) {
			buffer.append(part).append(replaceWith);
		}
		return buffer.toString();
	}

	private String addAfter(String content, String search, String add, boolean trimAfter) {
		final String[] parts = content.split(search);
		String sep = "";
		content = "";
		for (String part : parts) {
			if (!sep.equals("")) {
				part = part.trim();
			}
			content = content + sep + part;
			sep = search + add;
		}
		return content;
	}

	/**
	 * @param partText
	 * @param string
	 * @param string2
	 * @return
	 */
	private String addNewlineAfter(String partText, String search, String indent) {
		final String[] parts = partText.split(search);
		String sep = "";
		partText = "";
		for (String part : parts) {
			if (!sep.equals("")) {
				part = indent + part.trim();
			}
			partText = partText + sep + part;
			sep = search + System.lineSeparator();
		}
		return partText;
	}

	private ITypedRegion[] calculatePartitions(String content, IPredicateRule[] rules, String[] legalPartitions) {
		final Document queryDocument = new Document(content);
		final RuleBasedPartitionScanner partitionscanner = new RuleBasedPartitionScanner();

		partitionscanner.setPredicateRules(rules);
		final OdysseusScriptPartitioner partitioner = new OdysseusScriptPartitioner(partitionscanner, legalPartitions);
		partitioner.connect(queryDocument);
		queryDocument.setDocumentPartitioner(partitioner);
		partitioner.printPartitions(queryDocument);
		return partitioner.computePartitioning(0, queryDocument.getLength());
	}

	private String formatOperator(String fPart, String indent) {

		boolean startRemoved = false;
		boolean endRemoved = false;
		if (fPart.startsWith("(")) {
			fPart = fPart.substring(1);
			startRemoved = true;
		}
		if (fPart.endsWith(")")) {
			fPart = fPart.substring(0, fPart.length() - 1);
			endRemoved = true;
		}

		final String newIndent = indent + TAB;

		fPart = removeNewLinesWithoutComment(fPart);

		final IPredicateRule[] rules = new IPredicateRule[3];
		rules[0] = new MultiLineBracketRule('(', ')', operatorToken);
		rules[1] = new MultiLineRule("${", "}", replacementToken);
		rules[2] = new MultiLineBracketRule('{', '}', parametersToken);

		final ITypedRegion[] partitions = calculatePartitions(fPart, rules, new String[] { PARTITION_PARAMETERS, PARTITION_REPLACEMENT, PARTITION_OPERATOR });

		String result = "";
		for (final ITypedRegion part : partitions) {
			String partText = getPart(part, fPart);
			if (part.getType().equalsIgnoreCase(PARTITION_PARAMETERS)) {
				partText = formatParameters(partText, indent);
			}
			if (part.getType().equalsIgnoreCase(PARTITION_OPERATOR)) {
				partText = formatOperator(partText, indent + TAB);
			}
			// default content must be an operator name or a source-name
			if (part.getType().equalsIgnoreCase(IDocument.DEFAULT_CONTENT_TYPE)) {
				// starts with , indicates a subsequent operator
				if (partText.trim().startsWith(",")) {
					partText = addNewlineAfter(partText, ",", newIndent);
					// partText = addAfter(partText, ",", System.lineSeparator()
					// + newIndent+"\t");
				}
			}
			result = result + partText;
		}

		if (startRemoved) {
			result = "(" + result;
		}
		if (endRemoved) {
			result = result + System.lineSeparator() + indent + ")";
		}
		return result;
	}

	/**
	 * @param fPart
	 * @param indent
	 * @return
	 */
	private String formatParameters(String fPart, String indent) {
		final String newIndent = indent + TAB;

		fPart = removeNewLinesWithoutComment(fPart);

		final IPredicateRule[] rules = new IPredicateRule[5];
		rules[0] = new MultiLineBracketRule('\'', '\'', stringToken);
		rules[1] = new MultiLineBracketRule('[', ']', paramlistToken);
		rules[2] = new MultiLineBracketRule(',', '=', oneParamToken);
		rules[3] = new MultiLineBracketRule('$', '}', replacementToken);
		rules[4] = new MultiLineBracketRule('{', '=', oneParamToken);

		final ITypedRegion[] partitions = calculatePartitions(fPart, rules, new String[] { PARTITION_ONEPARAM, PARTITION_PARAMLIST, PARTITION_REPLACEMENT, PARTITION_STRING });

		String result = "";
		for (final ITypedRegion part : partitions) {
			String partText = getPart(part, fPart);
			if (part.getType().equalsIgnoreCase(PARTITION_ONEPARAM)) {
				// partText = addAfter(partText, "=", " = ");
				partText = partText.toLowerCase();
				if (partText.trim().startsWith(",")) {
					partText = addAfter(partText, ",", System.lineSeparator() + newIndent + TAB, true);
				}
				if (partText.trim().startsWith("{")) {
					partText = "{" + System.lineSeparator() + newIndent + TAB + partText.substring(1).trim();
				}
			} else if (part.getType().equalsIgnoreCase(PARTITION_PARAMLIST)) {
				partText = formatParamlist(partText, indent + TAB);
			}
			// if(!part.getType().equalsIgnoreCase(PARTITION_PARAMLIST)){
			// partText = addAfter(partText, ",",
			// System.lineSeparator()+newIndent);
			// }

			result = result + partText;
		}
		if (result.endsWith("}")) {
			result = result.substring(0, result.length() - 1) + System.lineSeparator() + newIndent + "}";
		}
		return result;
	}

	/**
	 * @param partText
	 * @param indent
	 * @return
	 */
	private String formatParamlist(String text, String indent) {

		// if paramlist is less than 50, we put all in one line
		if (text.length() < 50) {
			return removeNewLinesWithoutComment(text);
		}
		boolean startRemoved = false;
		boolean endRemoved = false;
		if (text.startsWith("[")) {
			text = text.substring(1);
			startRemoved = true;
		}
		if (text.endsWith("]")) {
			text = text.substring(0, text.length() - 1);
			endRemoved = true;
		}

		final String newIndent = indent + TAB;

		final IPredicateRule[] rules = new IPredicateRule[1];
		rules[0] = new MultiLineBracketRule('[', ']', paramlistToken);

		final ITypedRegion[] partitions = calculatePartitions(text, rules, new String[] { PARTITION_PARAMLIST });

		String result = "";
		for (final ITypedRegion part : partitions) {
			// a list in a list
			String partText = getPart(part, text);
			if (part.getType().equalsIgnoreCase(PARTITION_PARAMLIST)) {
				partText = newIndent + TAB + partText.trim();
			} else {
				if (partText.trim().equals(",")) {
					partText = "," + System.lineSeparator();
				}
			}
			if (!partText.trim().isEmpty()) {
				result = result + partText;
			}
		}

		if (startRemoved) {
			result = "[" + System.lineSeparator() + result;
		}
		if (endRemoved) {
			result = result + System.lineSeparator() + newIndent + "]";
		}
		return result;

	}

	private String getPart(ITypedRegion region, String content) {
		return content.substring(region.getOffset(), region.getOffset() + region.getLength());
	}

	private int getTabSize() {
		if (TAB.equals("\t")) {
			return 8;
		}
		
		return TAB.length();
	}

	/**
	 * @param content
	 * @param offset
	 * @return
	 */
	private String lineAtOffset(String text, int offset) {
		int pos = offset - 1;
		String line = text.substring(pos, offset);
		while (pos >= 0) {
			if (line.startsWith("\n")) {
				return line;
			}
			pos--;
			line = text.substring(pos, offset);
		}
		return text;
	}

	private String operatorNamesToUpperCase(String partText) {
		// print all operator build in upper case
		final String upperContent = partText.toUpperCase();
		for (final IOperatorBuilder b : PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder()) {
			final String name = b.getName().toUpperCase();
			int index = upperContent.indexOf(name, 0);
			while (index >= 0 && index <= partText.length()) {
				partText = insertInstead(partText, name, index, name.length());
				index = upperContent.indexOf(name, index + name.length());
			}
		}
		return partText;
	}

	private String removeNewLinesWithoutComment(String text) {
		final ArrayList<String> lines = new ArrayList<>();
		try {
			final BufferedReader reader = new BufferedReader(new StringReader(text));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return text;
		}
		String result = "";
		for (final String line : lines) {
			result = result + line;
			if (line.trim().startsWith("///")) {
				result = result + System.lineSeparator();
			}
		}
		return result;
	}

}
