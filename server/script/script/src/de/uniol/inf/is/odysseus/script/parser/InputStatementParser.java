package de.uniol.inf.is.odysseus.script.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class InputStatementParser {

	public static final Collection<String> INPUT_KEYS = new ArrayList<>();
	static {
		INPUT_KEYS.add(OdysseusScriptParser.PARAMETER_KEY + "INPUT");
		INPUT_KEYS.add(OdysseusScriptParser.PARAMETER_KEY + "INCLUDE");
	}

	private static enum SourceType {
		HTTP, FILE
	}

	private final String[] textToParse;
	private final ReplacementContainer replacements;
	private final List<String> unwrappingFiles = Lists.newArrayList();

	public InputStatementParser(String[] textToParse,
			ReplacementContainer replacements) {
		Preconditions.checkNotNull(textToParse,
				"Text to check for %s must not be null!", INPUT_KEYS);
		Preconditions.checkNotNull(replacements,
				"Replacement container must not be null!");

		this.textToParse = textToParse;
		this.replacements = replacements;
	}

	public String[] unwrap() throws OdysseusScriptException,
			ReplacementException {
		return unwrapImpl(textToParse);
	}

	private String[] unwrapImpl(String[] text) throws OdysseusScriptException,
			ReplacementException {
		List<String> resultText = Lists.newLinkedList();

		for (String textLine : text) {

			String line = textLine.trim();
			boolean foundReplacement = false;
			for (String INPUT_KEY : INPUT_KEYS) {
				// Hack to allow inputs based on #define
				if (line.toLowerCase().startsWith("#define")){
					String[] lineParts = line.split(" ", 3);
					if (lineParts.length == 3){
						final String value;
						if (lineParts[2].startsWith("${")){
							value =  replacements.use(lineParts[2]);
						}else{
							value = lineParts[2];
						}
						
						replacements.put(lineParts[1], value);
					}
				}
				
				if (line.toLowerCase().startsWith(INPUT_KEY.toLowerCase())) {
					foundReplacement = true;
					line = replacements.use(line);

					String[] lineParts = line.split("\\ ", 2);

					if (lineParts.length != 2) {
						throw new OdysseusScriptException("Misused "
								+ INPUT_KEY + ": " + line);
					}

					String inputName = lineParts[1].trim();
					final String[] inputLines;

					if (unwrappingFiles.contains(inputName)) {
						throw new OdysseusScriptException("Cyclic " + INPUT_KEY
								+ " with file '" + inputName + "'");
					}

					if (inputName.toLowerCase().startsWith("http://")||
							inputName.toLowerCase().startsWith("ftp://")||
							inputName.toLowerCase().startsWith("https://")){
						inputLines = readFromInput(inputName, SourceType.HTTP);
					} else {
						inputLines = readFromInput(inputName, SourceType.FILE);
					}

					String[] unwrappedFileLines = unwrapImpl(inputLines);
					for (String unwrappedFileLine : unwrappedFileLines) {
						resultText.add(unwrappedFileLine);
					}
				}
			}
			if (!foundReplacement) {
				resultText.add(textLine);
			}

		}

		return resultText.toArray(new String[resultText.size()]);
	}

	private String[] readFromInput(String inputName, SourceType readType)
			throws OdysseusScriptException {

		final String[] inputLines;

		try {
			unwrappingFiles.add(inputName);
			switch (readType) {
			case FILE:

				inputLines = readTextLinesFromFile(inputName);
				break;
			case HTTP:
				inputLines = readTextLinesFromHttp(inputName);
				break;
			default:
				throw new OdysseusScriptException("Could not read from '"
						+ inputName + "'");
			}

		} catch (IOException e) {
			throw new OdysseusScriptException("Could not read from '"
					+ inputName + "'", e);

		} finally {
			unwrappingFiles.remove(inputName);
		}
		return inputLines;
	}

	private static String[] readTextLinesFromFile(String inputName)
			throws IOException, OdysseusScriptException {

		File includingFile = new File(inputName);
		if (!includingFile.exists()) {
			throw new OdysseusScriptException("Input-file '" + inputName
					+ "' does not exist!");
		}
		BufferedReader br = new BufferedReader(new FileReader(includingFile));
		return readFromReader(br);
	}

	private static String[] readTextLinesFromHttp(String inputName)
			throws IOException {

		URL source = new URL(inputName);
		URLConnection conn = source.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		return readFromReader(br);
	}

	private static String[] readFromReader(BufferedReader br)
			throws IOException {
		List<String> lines = Lists.newArrayList();

		try {
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}

			return lines.toArray(new String[lines.size()]);
		} finally {
			br.close();
		}
	}

}
