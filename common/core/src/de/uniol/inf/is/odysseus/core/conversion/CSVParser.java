package de.uniol.inf.is.odysseus.core.conversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CSVParser {

	static public List<String> parseCSV(final String line, final char textDelimiter, final char delimiter, final boolean trim) {
		List<String> ret = new ArrayList<String>();
		StringBuffer elem = new StringBuffer();
		boolean readInsideOfText = false;
		boolean readAfterText = false;
		boolean readInsideOfList = false; 
		
		for (char c : line.toCharArray()) {
			if (c == textDelimiter){
				if (!readInsideOfText){
					elem = new StringBuffer();
				}else{
					// Flag ignore between textDelimiter and delimiter (e.g. 'abc' ,1)
					readAfterText = true;
				}
				readInsideOfText = !readInsideOfText;
				
				// elem.append(c);
			} else if(!readInsideOfText && c == '['){
				readInsideOfList = true;
				elem = new StringBuffer();
				elem.append(c);
			} else if (readInsideOfList && c == ']'){
				elem.append(c);
				ret.add(elem.toString());
				elem = new StringBuffer();
				readInsideOfList = false;
			}else {
				if (readInsideOfText || readInsideOfList) {
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

	static public List<String> parseCSV(final String line, final char delimiter, final boolean trim) {
		String[] ret = line.split(Pattern.quote("" + delimiter));
		if (trim) {
			String[] trimmed = new String[ret.length];
			for (int i = 0; i < ret.length; i++) {
				trimmed[i] = (ret[i].trim());
			}
			ret = trimmed;
		}
		return Arrays.asList(ret);
	}

	public static void main(String[] args) {
		String in = "4,  ' d' ,  ' e[2]',[1,2,3], 'a'";
		List<String> ret = CSVParser.parseCSV(in, '\'', ',', false);
		for (String string : ret) {
			System.out.println("\'" + string + "\'");
		}
	}

}
