package de.uniol.inf.is.odysseus.script.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
				if (line.toLowerCase().startsWith(INPUT_KEY.toLowerCase())) {
					foundReplacement = true;
					line = replacements.use(line);

					String[] lineParts = line.split("\\ ", 2);

					if (lineParts.length != 2) {
						throw new OdysseusScriptException("Misused "
								+ INPUT_KEY + ": " + line);
					}

					String fileName = lineParts[1].trim();

					File includingFile = new File(fileName);
					if (!includingFile.exists()) {
						throw new OdysseusScriptException("File for "
								+ INPUT_KEY + " '" + fileName
								+ "' does not exist!");
					}

					if (unwrappingFiles.contains(fileName)) {
						throw new OdysseusScriptException("Cyclic " + INPUT_KEY
								+ " with file '" + fileName + "'");
					}

					try {
						unwrappingFiles.add(fileName);
						String[] fileLines = readTextLinesFromFile(includingFile);

						String[] unwrappedFileLines = unwrapImpl(fileLines);
						for (String unwrappedFileLine : unwrappedFileLines) {
							resultText.add(unwrappedFileLine);
						}

					} catch (IOException e) {
						throw new OdysseusScriptException("Could not "
								+ INPUT_KEY + " file '" + fileName + "'", e);

					} finally {
						unwrappingFiles.remove(fileName);
					}
				}
			}
			if (!foundReplacement) {
				resultText.add(textLine);
			}

		}

		return resultText.toArray(new String[resultText.size()]);
	}

	private static String[] readTextLinesFromFile(File includingFile)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(includingFile));
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
