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
package de.uniol.inf.is.odysseus.rcp.editor.text.pql.textmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Geesen
 * 
 */
public class PQLDocumentModel {

	private List<PQLDocumentOperator> rootOperators = new ArrayList<>();

	public PQLDocumentModel(String content) {
		parse(content);

	}

	/**
	 * @param content
	 */
	private void parse(String content) {
		// TODO Auto-generated method stub
		
	}

	public List<PQLDocumentOperator> rootOperators() {
		return this.rootOperators;
	}

	// private void oldparse(String content) {
	// // we only want all root operators...
	// for (IOperatorBuilder b :
	// PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder()) {
	// String op = b.getName().toUpperCase();
	// int index = content.toUpperCase().indexOf(op);
	// while (index >= 0) {
	// System.out.println("check: " + op);
	// String before = getTokenBefore(index, content);
	// if (before.endsWith("=")) {
	// System.out.println("... is a root!");
	// // parse it and its child stuff...
	// String startAtOp = content.substring(index);
	// PQLDocumentOperator opNode = parseOperator(startAtOp);
	// rootOperators.add(opNode);
	// }
	// index = content.toUpperCase().indexOf(op, index + 1);
	// }
	// }
	// }
	//
	// private PQLDocumentOperator parseOperatorOLD(String text) {
	// // there are three possibilities: 1. nothing, 2. an attribute-section,
	// // 3. one or more child operators
	//
	// // 1. nothing: this means, between ( and ) brackets may only be empty
	// // content
	// System.out.println("text is: " + text);
	// int startOperatorBracket = text.indexOf("(");
	//
	// if (startOperatorBracket == -1) {
	// // no content, because there is no opening bracket
	// // it must be only a name
	// String name = text.trim();
	// PQLDocumentOperator op = new PQLDocumentOperator(name);
	// return op;
	// } else {
	// // else, there was an opening bracket, so we have to parse the
	// // contents
	// int endOperatorBracket = getClosingBracketAfter(startOperatorBracket + 1,
	// text, "(", ")");
	// if (noTextBetween(startOperatorBracket, endOperatorBracket, text)) {
	// // there is no content? --> no childs or attributes
	// System.out.println("there is no input for the op!");
	// // create the name from beginning till open bracket
	// String name = text.substring(0, startOperatorBracket).trim();
	// PQLDocumentOperator op = new PQLDocumentOperator(name);
	// return op;
	// } else {
	// // first, we cut out the text for the op and remove the operator
	// // (last) bracket
	// text = text.substring(0, endOperatorBracket);
	// // the name is from beginning till first ( bracket
	// String name = text.substring(startOperatorBracket).trim();
	// // now, we may have either case 2. or 3....
	// // case 2 is an attribute section, which means, the next
	// // character must be an {
	// int startAttributes = text.indexOf("{", startOperatorBracket + 1);
	// if (startAttributes != -1 && noTextBetween(startOperatorBracket + 1,
	// startAttributes, text)) {
	// // no text between first ( and a { --> start of the
	// // attributes-section
	// int endAttributes = getClosingBracketAfter(startAttributes + 1, text,
	// "{", "}");
	// String attributes = text.substring(startAttributes, endAttributes + 1);
	// System.out.println("attributes: " + attributes);
	// // after attributes, there could also be other input
	// // operators
	// PQLDocumentOperator op = new PQLDocumentOperator(name);
	// // if there are input ops, there must be an ,
	// int indexComma = text.indexOf(",", endAttributes + 1);
	// if (indexComma >= 0) {
	// String inputText = text.substring(indexComma).trim();
	// // input text must be input operators...
	// // we have to split them and parse them isolated,
	// // and they are the current childs!
	// for (String part : splitToChilds(inputText)) {
	// op.addChild(parseOperator(part));
	// }
	// }
	// return op;
	// } else {
	// // there are other input operators
	// System.out.println("only handle childs...");
	// PQLDocumentOperator op = new PQLDocumentOperator(name);
	// String nextThings = text.substring(startOperatorBracket + 1);
	// if (!nextThings.isEmpty()) {
	// // next thing must be input operators...
	// // we have to split them and parse them isolated,
	// // and they are the current childs!
	// for (String part : splitToChilds(nextThings)) {
	// op.addChild(parseOperator(part));
	// }
	// }
	// return op;
	// }
	//
	// }
	// }
	//
	// }
	//
	// /**
	// * @param nextThings
	// * @return
	// */
	// private List<String> splitToChilds(String text) {
	// text = text.trim();
	// System.out.println("to split: " + text);
	// // two possibilities: name (next is "," or ")") or op
	// intCo
	//
	// List<String> parts = new ArrayList<>();
	// return parts;
	// }
	//
	// /**
	// * @param i
	// * @param startAttributes
	// * @param text
	// * @return
	// */
	// private boolean noTextBetween(int from, int to, String text) {
	// String part = text.substring(from, to);
	// part = removeNewLine(part);
	// part = part.trim();
	// return part.isEmpty();
	// }
	//
	// private int getClosingBracketAfter(int startOffset, String text, String
	// openBracket, String closeBracket) {
	// int open = 1;
	// for (int position = startOffset; position < text.length(); position++) {
	// String part = text.substring(position);
	// if (part.startsWith(openBracket)) {
	// open++;
	// }
	// if (part.startsWith(closeBracket)) {
	// open--;
	// if (open == 0) {
	// return position;
	// }
	// }
	// }
	// return -1;
	// }
	//
	// /**
	// * @param childsafter
	// * @return
	// */
	//
	// /**
	// * @param attributesString
	// * @return
	// */
	// private List<PQLDocumentAttribute> parseAtttributes(String atts) {
	// List<PQLDocumentAttribute> attributes = new ArrayList<>();
	// atts = atts.replace("{", "");
	// atts = atts.replace("}", "");
	// PQLDocumentAttribute attribute = new PQLDocumentAttribute("all", atts, 0,
	// atts.length());
	// attributes.add(attribute);
	// return attributes;
	// }
	//
	// private String getTokenBefore(int offset, String content) {
	// if (offset <= 0) {
	// return "";
	// } else {
	// String partbefore = content.substring(0, offset);
	// String[] withoutSpaces = partbefore.split("\\s|\n|\r");
	// if (withoutSpaces.length > 0) {
	// return withoutSpaces[withoutSpaces.length - 1];
	// }
	// return "";
	// }
	// }
	//
	// private String getTokenBehind(int offset, String content) {
	// if (offset >= content.length()) {
	// return "";
	// } else {
	// String partbehind = content.substring(offset);
	// String[] withoutSpaces = partbehind.split("\\s|\n|\r");
	// if (withoutSpaces.length > 0) {
	// return withoutSpaces[0];
	// }
	// return "";
	// }
	// }
	//
	// private String removeNewLine(String s) {
	// s = s.replaceAll("\\n", "");
	// s = s.replaceAll("\\t", "");
	// return s;
	// }
}
