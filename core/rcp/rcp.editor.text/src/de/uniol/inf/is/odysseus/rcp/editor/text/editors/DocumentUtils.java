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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * @author Dennis Geesen
 * 
 */
public class DocumentUtils {

	/**
	 * Searches backwards from offset for a parser definition (#PARSER xyz) and returns the first one
	 * If there is no parser found, an empty string is returned
	 * @param document the document where to search in
	 * @param offset the position where the parser is valid
	 * @return the last set parser
	 */
	public static String findValidParserAtPosition(IDocument document, int offset) {
		if (offset <= 0) {
			return "";
		}
		try {
			int position = document.get(0, offset).toUpperCase().lastIndexOf("#PARSER");

			if (position == -1) {
				return "";
			}
			int start = position + 7;
			while (start < document.getLength()) {
				if (!Character.isWhitespace(document.getChar(start))) {
					break;
				}
				start++;
			}
			int end = start + 1;
			while (end < document.getLength()) {
				// if (Character.isWhitespace(document.getChar(end))) {
				if (document.getChar(end) == '#' || document.getChar(end) == '\n' || document.getChar(end) == '\r') {
					break;
				}
				end++;
			}
			String parser = document.get(start, end - start).trim();
			return parser;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

}
