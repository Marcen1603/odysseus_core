package de.uniol.inf.is.odysseus.nexmark.generator;

public class Categories {
	
	static final int CATEGORY_COUNT = 300;		// Anzahl zu generierender Kategorien
	public static final String XML_DECL = "<?xml version=\"1.0\"?>";
	public static final String XML_DECL_REGEX = "<\\?xml version=\\\"1.0\\\"\\?>";
	
	public static void main(String[] args) {
		System.out.println(generateCategoryData());
	}
	
	
	/**
	 * Erzeugt Kategorie-Daten und liefert diese als String zurueck.
	 * @return Die generierten Kategorie-Daten
	 */
	private static String generateCategoryData() {
		
		String data = "";
		data += XML_DECL + "\n";
		data += "<site datetime=\"0\">\n";
		data += "  <categories>\n";
		
		for (int i=0; i<CATEGORY_COUNT; i++) {
			data += "    <category>\n";
			data += "      <id>" + i + "</id>\n";
			data += "      <name>Category " + i + "</name>\n";
			data += "      <description>No description.</description>\n";
			data += "      <parentcategory>0</parentcategory>\n";
			data += "    </category>\n";
		}
		
		data += "  </categories>\n";
		data += "</site>\n";
		
		return data;
	}

}
