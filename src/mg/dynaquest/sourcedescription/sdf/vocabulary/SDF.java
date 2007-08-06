package mg.dynaquest.sourcedescription.sdf.vocabulary;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFExtensionalDescriptions;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDF {
	static ArrayList<SDF> sdffiles = null;

    /**
	 * @uml.property  name="baseUri"
	 */
    String baseUri = "";

	 /**
	 * @uml.property  name="namespaceName"
	 */
    static String baseDir = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/";
    /**
	 * @uml.property  name="namespaceName"
	 */
    String namespaceName = "";

    /**
     * 
     * @uml.property name="baseUri"
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * 
     * @uml.property name="namespaceName"
     */
    public String getNamespaceName() {
        return namespaceName;
    }

	protected SDF() {
	}

	public static void init() {
		sdffiles = new ArrayList<SDF>();
		sdffiles.add(new SDFDatatypes());
		sdffiles.add(new SDFDatatypeConstraints());
		sdffiles.add(new SDFDescriptions());
		sdffiles.add(new SDFExtensionalDescriptions());
		sdffiles.add(new SDFFunctions());
		sdffiles.add(new SDFIntensionalDescriptions());
		sdffiles.add(new SDFMappings());
		sdffiles.add(new SDFPredicates());
		sdffiles.add(new SDFQualityVoc());
		sdffiles.add(new SDFQualityDescriptions());
		sdffiles.add(new SDFQueryVoc());
		sdffiles.add(new SDFSchema());
		sdffiles.add(new SDFUnits());

		// Jetzt noch mal die ganzen Testdateien
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Global.sdf#",
						"testglobal"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source1.sdf#",
						"source1"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source2.sdf#",
						"source2"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source3.sdf#",
						"source3"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source4.sdf#",
						"source4"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source5.sdf#",
						"source5"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source6.sdf#",
						"source6"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source7.sdf#",
						"source7"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source8.sdf#",
						"source8"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source9.sdf#",
						"source9"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source10.sdf#",
						"source10"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source11.sdf#",
						"source11"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source12.sdf#",
						"source12"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Query1.sdf#",
						"testQuery1"));

		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/GlobalSchemaAutomarkt.sdf#",
						"globalSchemaAutomarkt"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Query1.sdf#",
						"AutoQuery1"));
		sdffiles
				.add(new SDFUniversal(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Source_CarVendor_1.sdf#",
						"AutoQuelle1"));
	}

	public static String getNamespaceForUri(String base) {
		String ret = null;
		for (int i = 0; i < sdffiles.size(); i++) {
			SDF file = (SDF) sdffiles.get(i);
			if (base.equals(file.getBaseUri())) {
				ret = file.getNamespaceName();
				break;
			}
		}
		return ret;
	}

	public static String getUriForNamespace(String namespace) {
		String ret = null;
		for (int i = 0; i < sdffiles.size(); i++) {
			SDF file = (SDF) sdffiles.get(i);
			if (namespace.equals(file.getNamespaceName())) {
				ret = file.getBaseUri();
				break;
			}
		}
		return ret;
	}
	
	public static String prettyPrintURI(String uri) {
		if (sdffiles == null)
			init();
		String ret = uri;
		if (uri != null) {
			String base = uri.substring(0, uri.lastIndexOf("#") + 1);
			String elem = uri.substring(uri.lastIndexOf("#") + 1);

			String namespaceName = getNamespaceForUri(base);
			if (namespaceName != null) {
				ret = namespaceName + ":" + elem;
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		System.out
				.println(mg.dynaquest.sourcedescription.sdf.vocabulary.SDF
						.prettyPrintURI("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_descriptions.sdf#SourceDescription"));
	}

}