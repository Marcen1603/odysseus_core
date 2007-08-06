package mg.dynaquest.evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class SplittData {

	static String inFilename = "E:\\dissertation\\db\\plz\\Postleitzahlen.txt";

	static String outFilename = "E:\\dissertation\\db\\plz\\Postleitzahlen_neu.txt";

	public static void doIt() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(inFilename));
		BufferedWriter out = new BufferedWriter(new FileWriter(outFilename));
		String line = null;

		while ((line = in.readLine()) != null) {
			StringTokenizer tokenize = new StringTokenizer(line, ";");
			String first = tokenize.nextToken();
			String second = tokenize.nextToken();
			StringTokenizer secTok = new StringTokenizer(second, " \",");
			while (secTok.hasMoreTokens()) {
				out.write(first + ";" + secTok.nextToken() + "\r\n");
			}
		}

	}

	public static void main(String[] args) {
		try {
			SplittData.doIt();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}

	}

}