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
		boolean overreadModus1 = false;
		boolean overreadModus2 = false;

		for (char c : line.toCharArray()) {

			if (c == textDelimiter) {
				overreadModus1 = !overreadModus1;
				// elem.append(c);
			} else {
				if (overreadModus1 || overreadModus2) {
					elem.append(c);
				} else {
					if (delimiter == c) {
						ret.add(elem.toString());
						elem = new StringBuffer();
					} else {
						elem.append(c);
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
		String[] ret = line.split(Pattern.quote(""+delimiter));
		if (trim) {
			String[] trimmed = new String[ret.length];
			for (int i = 0; i < ret.length; i++) {
				trimmed[i] = (ret[i].trim());
			}
			ret = trimmed;
		}
		return ret;
	}

	
}
