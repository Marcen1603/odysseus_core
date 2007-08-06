package mg.dynaquest.sourcedescription;

import mg.dynaquest.xml.*;

import org.w3c.dom.Document;
import java.io.*;

public class SubstXMLEntities {

	public static void doIt(String InFile, String OutFile) throws Exception {
		Document doc = DOMHelp.parse(InFile, false);
		DOMHelp.dumpNode(doc, new FileWriter(OutFile), true);
	}

	public static void main(String[] args) {
		try {
			if (args.length != 2) {
				System.out.println("SubstXMLEntities InFile OutFile");
			} else {
				doIt(args[0], args[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}