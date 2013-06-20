package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class NaiveBayes<T extends IMetaAttribute> extends AbstractClassifier<T> {

	private final String algo_type = "NaiveBayes";

	static Map<String, Integer> trainingset = new HashMap<String, Integer>();

	static Map<String, Integer> positivewords = new HashMap<String, Integer>();
	static Map<String, Integer> negativewords = new HashMap<String, Integer>();

	public void trainClassifier(String trainingsetPath) {

		BufferedReader readneglines;
		try {
			readneglines = new BufferedReader(new FileReader(trainingsetPath));
			String lineneg = readneglines.readLine();

			while (lineneg != null) {
				String[] data = processCsvLine(lineneg);
				// System.out.println("Trainingset: " + data[0] + "|" +
				// data[1]);
				trainingset.put(data[0], Integer.parseInt(data[1]));
				lineneg = readneglines.readLine();
			}
			readneglines.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Positiv/Negativ Wortliste erstellen....");

		for (Map.Entry<String, Integer> e : trainingset.entrySet()) {

			if (e.getValue() == 1) {
				// positiv
				for (String singleword : e.getKey().split(" ")) {
					if (!positivewords.containsKey(singleword)) {
						positivewords.put(singleword, 1);
					} else {
						// vorhanden
						// ctr sichern
						int ctr = positivewords.get(singleword);
						// entfernen
						positivewords.remove(singleword);

						// mit neuem ctr hinzufügen
						positivewords.put(singleword, ctr + 1);

					}

				}
			} else {
				// negativ
				for (String singleword : e.getKey().split(" ")) {
					if (!negativewords.containsKey(singleword)) {
						negativewords.put(singleword, 1);
					} else {
						// vorhanden
						// ctr sichern
						int ctr = negativewords.get(singleword);
						// entfernen
						negativewords.remove(singleword);

						// mit neuem ctr hinzufügen
						negativewords.put(singleword, ctr + 1);

					}

				}
			}

			// System.out.println( e.getKey() + "="+ e.getValue() );
		}

		System.out.println("Classifier erfolgreich initialisiert!");

	}

	@Override
	public String getType() {
		return algo_type;
	}

	@Override
	public String startDetect(String text) {

		double decisionpos = 0.0;
		double decisionneg = 0.0;

		for (String singleword : text.split(" ")) {

			double a = 1.0;
			double b = 1.0;

			// Positive value
			if (positivewords.containsKey(singleword)) {
				a = a + positivewords.get(singleword);
			}

			// Positive value
			if (negativewords.containsKey(singleword)) {
				b = b + negativewords.get(singleword);
			}

			decisionpos += a / (a + b);
			decisionneg += b / (a + b);

		}
		System.out.println("----Auswertung----");
		System.out.println("Satz: " + text);

		System.out.println("Positive: " + decisionpos);
		System.out.println("Negative: " + decisionneg);

		int prediction = 1;

		if (decisionneg > decisionpos) {
			prediction = -1;
		}

		String erg = "";

		if (prediction == 1) {
			erg = "positive";
		} else {
			erg = "negative";
		}

		return erg;

	}

	public static String[] processCsvLine(final String data) {
		// System.out.println("+++++++++++++++++++++++++++++++++++++ Neue Zeile:");
		String[] output = new String[2];

		final StringTokenizer st = new StringTokenizer(data, ";");

		while (st.hasMoreTokens()) {
			output[0] = st.nextToken();
			output[1] = st.nextToken();
		}
		return output;
	}

	@Override
	public IClassifier<?> getInstance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
