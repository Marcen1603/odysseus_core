package de.uniol.inf.is.odysseus.core.conversion;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CSVParser {

	static public char determineDelimiter(String v) {
		char ret;
		if (v.equals("\\t")) {
			ret = '\t';
		} else {
			ret = v.toCharArray()[0];
		}
		return ret;
	}

	static public List<String> parseCSV(final String line, final char textDelimiter, final char delimiter, final boolean trim) {
		List<String> ret = new LinkedList<String>();
		StringBuffer elem = new StringBuffer();
		boolean readInsideOfText = false;
		boolean readAfterText = false;

		for (char c : line.toCharArray()) {
			if (c == textDelimiter) {
				if (!readInsideOfText){
					elem = new StringBuffer();
				}else{
					// Flag ignore between textDelimiter and delimiter (e.g. 'abc' ,1)
					readAfterText = true;
				}
				readInsideOfText = !readInsideOfText;
				
				// elem.append(c);
			} else {
				if (readInsideOfText) {
					elem.append(c);
				} else {
					if (delimiter == c) {
						ret.add(elem.toString());
						elem = new StringBuffer();
						readAfterText = false;
					} else {
						if (!readAfterText){
							elem.append(c);
						}
					}
				}

			}
		}
		ret.add(elem.toString());
		if (trim){
			List<String> trimmed = new LinkedList<String>();
			for (String l: ret){
				trimmed.add(l.trim());
			}
			ret = trimmed;
		}
		return ret;
	}

	static public String[] parseCSV(final String line, final char delimiter, final boolean trim) {
		String[] ret = line.split(Pattern.quote("" + delimiter));
		if (trim) {
			String[] trimmed = new String[ret.length];
			for (int i = 0; i < ret.length; i++) {
				trimmed[i] = (ret[i].trim());
			}
			ret = trimmed;
		}
		return ret;
	}

	public static void main(String[] args) {
		String in = "4,  ' d' ,  ' e'";
		List<String> ret = CSVParser.parseCSV(in, '\'', ',', false);
		for (String string : ret) {
			System.out.println("\'" + string + "\'");
		}
	}

}
