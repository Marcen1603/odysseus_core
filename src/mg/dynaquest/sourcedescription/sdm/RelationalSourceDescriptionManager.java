	package mg.dynaquest.sourcedescription.sdm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import mg.dynaquest.sourcedescription.sdf.description.SDFAccessPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeBindung;
import mg.dynaquest.sourcedescription.sdf.description.SDFExtensionalSourceDescription;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputAttributeBinding;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFIntensionalSourceDescription;
import mg.dynaquest.sourcedescription.sdf.description.SDFNecessityState;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputAttributeBindingFactory;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFQualitativeSourceDescription;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;
import mg.dynaquest.sourcedescription.sdf.description.SDFSourceDescription;
import mg.dynaquest.sourcedescription.sdf.function.SDFFunction;
import mg.dynaquest.sourcedescription.sdf.function.SDFFunctionFactory;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFSchemaMapping;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFSchemaMappingFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicateFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicateFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDatatype;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDatatypeFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFEntity;
import mg.dynaquest.sourcedescription.sdf.schema.SDFGranularityConstraint;
import mg.dynaquest.sourcedescription.sdf.schema.SDFIntervall;
import mg.dynaquest.sourcedescription.sdf.schema.SDFMaxLengthConstraint;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFRangeConstraint;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElementSet;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.unit.SDFUnit;
import mg.dynaquest.sourcedescription.sdf.unit.SDFUnitFactory;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDescriptions;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFFunctions;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;
import mg.dynaquest.support.RDFHelper;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFException;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.QueryExecution;
import com.hp.hpl.jena.rdql.QueryResults;
import com.hp.hpl.jena.rdql.ResultBinding;

/**
 * Diese Klasse kapselt Quellenbeschreibungen, die in einem relationalen DBMS
 * abgelegt sind. Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:13 $
 * Version: $Revision: 1.4 
 */

public class RelationalSourceDescriptionManager implements
		SourceDescriptionManager {
	
	static Logger logger = Logger.getLogger("mg.dynaquest.sourcedescription.sdm.RelationalSourceDescriptionManager");

	static Connection dbConnection = null;

	// Cachen von erzeugten Objekten
	static Map<Integer, SDFDataschema> dataschemaCache = new HashMap<Integer, SDFDataschema>();
	static Map<Integer, SDFSchemaElement> attrConstCache = new HashMap<Integer, SDFSchemaElement>();
	static Map<Integer, SDFSchemaElementSet> elementSetCache = new HashMap<Integer, SDFSchemaElementSet>();
	static Map<Integer, SDFEntity> entityCache = new HashMap<Integer, SDFEntity>();
	static Map<Integer, SDFAccessPattern> accessPatternCache = new HashMap<Integer, SDFAccessPattern>();
	static Map<Integer, SDFInputPattern> intputPatternCache = new HashMap<Integer, SDFInputPattern>();
	static Map<Integer, SDFOutputPattern> outputPatternCache = new HashMap<Integer, SDFOutputPattern>();
	static Map<Integer, SDFNecessityState> necessityStateCache = new HashMap<Integer, SDFNecessityState>();
	static Map operatorCache = new HashMap();
	static Map<Integer,SDFPredicate> predicateCache = new HashMap<Integer,SDFPredicate>();
	static Map constSetSelectionCache = new HashMap();
	static PreparedStatement queryIDFromURI = null;
	static PreparedStatement queryURIFromID = null;
	// TODO: Verwenden
	static Map<String, PreparedStatement> predQueryStmts = new HashMap<String, PreparedStatement>();
    
    // Für die DeleteStatements
    static ArrayList<PreparedStatement> prepStatements = new ArrayList<PreparedStatement>();
    static ArrayList<String> prepStatementsString = new ArrayList<String>();
    static ArrayList<Integer> numberOfParamsInStatement = new ArrayList<Integer>();

	private static Map<String, RelationalSourceDescriptionManager> instance = new HashMap<String, RelationalSourceDescriptionManager>();

    
	static void initDB(String user, String password, String jdbcString,
			String driverClass) throws ClassNotFoundException, SQLException {
		// Connection zur DB aufbauen		
		if (dbConnection == null){
			logger.debug("Connecting to: "+jdbcString);
			Class.forName(driverClass);
			dbConnection = DriverManager.getConnection(jdbcString, user, password);
			queryIDFromURI = dbConnection
					.prepareStatement("Select id from SDFURIIDMapping where uri=?");
			queryURIFromID = dbConnection
					.prepareStatement("Select uri from SDFURIIDMapping where id=?");
			logger.debug("Connecting to: "+jdbcString+" success");
		}
    }
	
	static java.sql.Statement getNewUpdateStatement() throws SQLException{
		return dbConnection.createStatement();
	}
	
    private void doExecuteUpdate(String sql) throws SQLException {
    	java.sql.Statement stmt = getNewUpdateStatement();
    	logger.debug(sql);
    	stmt.executeUpdate(sql);
		stmt.close();
	}
    
    private void executeUpdate(String sql, boolean ignoreException) throws SQLException {
    	try {
			doExecuteUpdate(sql);
		} catch (SQLException e) {
			if (ignoreException){
				logger.error("Ignored :"+e.getErrorCode()+" "+e.getMessage());
			}else{
				throw e;
			}
		}    
    }
    
    public static RelationalSourceDescriptionManager getInstance(String name, String user, String password,
			String jdbcString, String driverClass, boolean createSDMSchema, String sqlFilename){
    	RelationalSourceDescriptionManager ret = instance.get(name);    	
    	if (ret == null){
    		ret = new RelationalSourceDescriptionManager(user, password, jdbcString, driverClass,
    				createSDMSchema, sqlFilename);
    		instance.put(name, ret);
    	}
    	return ret;
    }
    
	/**
	 * @param createSDMSchema 
	 *  
	 */
	private RelationalSourceDescriptionManager(String user, String password,
			String jdbcString, String driverClass, boolean createSDMSchema, String sqlFilename) {
		try {
			if (dbConnection == null){
				initDB(user, password, jdbcString, driverClass);
			}
			if (createSDMSchema){
				createSDMSchema(sqlFilename);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

    // ***********************************************************************
    // Daten laden --> war früher in SimpleTestLoader
    // ***********************************************************************


    void createSDMSchema(String sqlFilename) throws Exception{
        //if (dbConnection == null) initDB();
        BufferedReader reader = new BufferedReader(new FileReader(sqlFilename));
        // Zum einfachen Verarbeiten der Import-Skripts,
        // dieses vollständig einlesen (wg. der ;)
        StringBuffer sqlFile = new StringBuffer(); 
        String line = "";
        while ((line = reader.readLine()) != null){
            sqlFile.append(line+" ");
        }
        // und dann die einzelnen Statements auslesen und an die DB schicken
        StringTokenizer sqlStmts = new StringTokenizer(sqlFile.toString(),";");
        java.sql.Statement stmt = dbConnection.createStatement();
        while (sqlStmts.hasMoreElements()){
           String sql = sqlStmts.nextToken().trim();
           if (sql.length() > 1){
               logger.debug("Führe aus: "+sql);
               try{
                   stmt.executeUpdate(sql);
               }catch(Exception e){
                       logger.error("Ignored-->: "+e.getMessage());
               }
           }
        }       
    }
   

    void initDeleteStatements() throws ClassNotFoundException,
            SQLException {
        java.sql.Statement dbStmt = dbConnection.createStatement();
        //if (dbStmt == null)
        //    initDB();

        ResultSet trs = dbStmt
                .executeQuery("select Table_name from user_tables where table_name like 'SDF%'");
        java.sql.Statement dbStmt2 = dbConnection.createStatement();
        while (trs.next()) {
            // Jetzt für jede Tabelle schauen, welche Attribute auf ID enden
            // und vom Typ Number sind
            StringBuffer sql = new StringBuffer("Delete from "
                    + trs.getString("Table_name") + " where ");
            ResultSet idAttribs = dbStmt2
                    .executeQuery("select column_name from cols where table_name='"
                            + trs.getString("Table_name")
                            + "' and "
                            + "DATA_TYPE ='NUMBER' AND column_name like '%ID'");

            int noOfParams = 0;
            while (idAttribs.next()) {
                sql.append(idAttribs.getString("column_name") + " = ? OR ");
                noOfParams++;
            }
            idAttribs.close();
            String sql2 = sql.substring(0, sql.length() - 4);
            //.println(sql2);
            prepStatements.add(dbConnection.prepareStatement(sql2));
            prepStatementsString.add(sql2);
            numberOfParamsInStatement.add(new Integer(noOfParams));
        }
        trs.close();
        dbStmt2.close();
        dbStmt.close();
    }

    String getURIForResource(Resource r, String baseURI) {
        if (r.isAnon()) {
            return baseURI + "#" + r.toString() + "_anon";
        } else {
            return r.toString();
        }
    }

    // Diese Methode dient dazu, zu einem String eine passende ID zu finden
    // dazu könnte man sich unterschiedliche Dinge vorstellen
    // hier wird die DB-gestützt gemacht, d.h. es wird zunächst geschaut, ob die
    // URI bereits vorhanden ist und dann deren ID zurückgeliefert, oder es wird
    // eine neue ID erzeugt, in die DB zusammen mit der URI eingetragen zu dann
    // zurück
    // geliefert.
    public synchronized int getIDForResource(String uri,
            boolean createIfNotFound) throws SQLException {
        int resultID = -1;
        java.sql.Statement dbStmt = dbConnection.createStatement();
        ResultSet rs = dbStmt
                .executeQuery("Select id from SDFURIIDMapping where uri='"
                        + uri + "'");
        if (rs.next()) {
            resultID = rs.getInt("ID");
            rs.close();
        } else {
            rs.close();
            if (createIfNotFound) {
                resultID = createNewIdForResource(uri);
            }
        }
        dbStmt.close();
        return resultID;
    }

    public synchronized int getIDForResource(Resource r, String baseURI,
            boolean createIfNotFound) throws SQLException {
        return getIDForResource(getURIForResource(r, baseURI).trim(),
                createIfNotFound);
    }

    int createNewIdForResource(String uri) throws SQLException {
        dbConnection.setAutoCommit(false);
        java.sql.Statement dbStmt = dbConnection.createStatement();
        ResultSet rs = dbStmt.executeQuery("Select maxId from SDFURIIDMaxId");
        int resultID = 0;
        if (rs.next()) {
            resultID = rs.getInt("maxId");
        } else {
            executeUpdate("Insert into SDFURIIDMaxId values("+ resultID + ")", false);
        }
        resultID++;
        rs.close();
        executeUpdate("Update SDFURIIDMaxId set maxID=" + resultID, false);
        boolean isAnon = uri.endsWith("_anon");
        executeUpdate("Insert into SDFURIIDMapping values("
                + resultID + ",'" + uri + "','" + isAnon + "')", false);
        dbConnection.commit();
        dbConnection.setAutoCommit(true);
        dbStmt.close();
        return resultID;
    }



	int createNewIdForResource(Resource r, String baseURI)
            throws SQLException {
        String uri = getURIForResource(r, baseURI).trim();
        return createNewIdForResource(uri);
    }

    public void printTriple(Resource subject, Property predicate,
            RDFNode object, String baseURI) {
        String printSubject = getURIForResource(subject, baseURI);
        String printPredicate = predicate.toString();
        String printObject = object.toString();
    
        //try{
        //  getURIForResource((Resource)object, baseURI);
        //}catch(Exception e){
        // ignore
        //  printObject = object.toString();
        //}

        logger.info(printSubject + " " + printPredicate + " " + printObject + " .");
    }

    // --------------------------------------------------------------------------
    // Schema-Aktionen
    // --------------------------------------------------------------------------

    // Die folgenden Methoden zum Initialisieren kann man
    // bei Gelegenheit noch mal überarbeiten. Die unterscheiden sich ja
    // nur im Aufruf der process-Methode

    void initUnits(String baseURI, String superClass)
            throws RDFException, SQLException, IOException {
        Model model = RDFHelper.readModel(baseURI);
        List<Resource> nodes = RDFHelper.findNodes(model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA,
                superClass);
        for (int i = 0; i < nodes.size(); i++) {
            Resource node = (Resource) nodes.get(i);
            processUnit(node, baseURI);
            // Rekursiver Aufruf
            initUnits(baseURI, node.toString());
        }
    }

    void initDatatypes(String baseURI, String superClass)
            throws RDFException, SQLException, IOException {
        Model model = RDFHelper.readModel(baseURI);
        List<Resource> nodes = RDFHelper.findNodes(model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA,
                superClass);
        for (int i = 0; i < nodes.size(); i++) {
            Resource node = (Resource) nodes.get(i);
            processDatatype(node, baseURI);
            // Rekursiver Aufruf
            initDatatypes(baseURI, node.toString());
        }
    }

    void initFunctionTypes(String baseURI, String superClass)
            throws RDFException, SQLException, IOException {
        Model model = RDFHelper.readModel(baseURI);
        List<Resource> nodes = RDFHelper.findNodes(model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA,
                superClass);
        for (int i = 0; i < nodes.size(); i++) {
            Resource node = (Resource) nodes.get(i);
            processFunctionType(node, baseURI);
            // Rekursiver Aufruf
            initFunctionTypes(baseURI, node.toString());
        }
    }

    void initMappings(String baseURI, String superClass)
            throws RDFException, SQLException, IOException {
        Model model = RDFHelper.readModel(baseURI);
        List<Resource> nodes = RDFHelper.findNodes(model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA,
                superClass);
        for (int i = 0; i < nodes.size(); i++) {
            Resource node = (Resource) nodes.get(i);
            processMappingType(node, baseURI);
            // Rekursiver Aufruf
            initMappings(baseURI, node.toString());
        }
    }

    void initPredicates(String baseURI, String superClass)
            throws RDFException, SQLException, IOException {
        Model model = RDFHelper.readModel(baseURI);
        List<Resource> nodes = RDFHelper.findNodes(model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA,
                superClass);
        for (int i = 0; i < nodes.size(); i++) {
            Resource node = (Resource) nodes.get(i);
            processPredicateType(node, baseURI);
            // Rekursiver Aufruf
            initPredicates(baseURI, node.toString());
        }
    }

    //  static void initQuality(String baseURI, String superClass)
    //    throws RDFException,SQLException, IOException{
    //    Model model = readModel(baseURI);
    //    ArrayList nodes = findNodes(model,SDFSchema.isA,superClass);
    //    for(int i=0;i<nodes.size();i++){
    //      Resource node = (Resource) nodes.get(i);
    //      processQualityType(node, baseURI);
    //      // Rekursiver Aufruf
    //      initQuality(baseURI,node.toString());
    //    }
    // }

    void initSDFSchema() throws RDFException, SQLException, IOException {
        // Erst mal hartcodiert (ist aber eigentlich nicht so schlimm. Sind ja
        // fixe
        // Namespaces

        // Sollte man nicht eigentlich erst einmal die SDF-Schema-Dateien in
        // ein großes Model einlesen und dann die Informationen suchen?
        // Die folgenden Methode funktioniert, so lange die Infos sich jeweils
        // in
        // einer Datei befinden ... eventuell später

        initDatatypes(
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.filename,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.SDFDatatype);
        initUnits(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFUnits.filename,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFUnits.SDFUnit);
        initFunctionTypes(
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFFunctions.filename,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFFunctions.SDFFunction);
        initMappings(
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFMappings.filename,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFMappings.SDFMapping);
        initPredicates(
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.filename,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.Predicate);
        // Operatoren??
        //initQuality(sdfBaseDir+"sdf_quality.sdf",sdfBaseDir+"sdf_quality.sdf#SDFQuality");
        // Qualitätsebenen?
    }

    // --------------------------------------------------------------------------
    // SDF-Datei einlesen
    // --------------------------------------------------------------------------

    public int processFunctionType(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die FunctionType-Information: " + uri);
        int functionTypeID = getIDForResource(uri, false);
        if (functionTypeID == -1) {
            functionTypeID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFFunctionType(FunctionTypeID,FunctionTypeURI) values("
                            + functionTypeID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
 //           Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            // printTriple(subject, predicate, object, baseURI);
            if (predicate.getURI().equals(
                    mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA)) {
                int superFunctionID = processFunctionType((Resource) object,
                        baseURI);
                executeUpdate("Update SDFFunctionType set superFunctionID="
                                + superFunctionID
                                + " where FunctionTypeID="
                                + functionTypeID, false);
            }

        }
        return functionTypeID;
    }

    public int processMappingFunction(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die MappungFunktion-Information: " + uri);
        int functionID = getIDForResource(uri, false);
        if (functionID == -1) {
            functionID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFMappingFunction(MappingFunctionID,MappingFunctionURI) values("
                            + functionID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        if (!iter.hasNext()){
            logger.error("Keine Eigenschaften bei "+uri);
        }
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            // printTriple(subject, predicate, object, baseURI);
            if (predicate.getURI().equals(
                    "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                int functionTypeID = processFunctionType((Resource) object,
                        baseURI);
                executeUpdate("Update SDFMappingFunction set FunctionTypeID="
                                + functionTypeID
                                + " where MappingFunctionID="
                                + functionID, false);
            }
            if (predicate
                    .getURI()
                    .equals(SDFFunctions.hasReverse)) {
                int reverseID = getIDForResource((Resource) object, baseURI,
                        false);
                logger.debug("hasReverse ");
                // Nur wenn die Funktion noch nicht definiert ist noch eintragen
                // zur Vermeidung einer Endlosschleife
                if (reverseID == -1) {
                    reverseID = processMappingFunction((Resource) object,
                            baseURI);
                }
                if (reverseID != -1) {
                    executeUpdate("Update SDFMappingFunction set hasReverseFunction="
                                    + reverseID
                                    + " where MappingFunctionID="
                                    + functionID, false);
                }
            }
        }
        return functionID;
    }

    public int processMappingType(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die MappingType-Information: " + uri);
        int mappingTypeID = getIDForResource(uri, false);
        if (mappingTypeID == -1) {
            mappingTypeID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFMappingType(MappingTypeID,MappingTypeURI) values("
                            + mappingTypeID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            // printTriple(subject, predicate, object, baseURI);
            if (predicate.getURI().equals(
                    mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA)) {
                int superMappingID = processMappingType((Resource) object,
                        baseURI);
                executeUpdate("Update SDFMappingType set superMappingID="
                                + superMappingID
                                + " where MappingTypeID="
                                + mappingTypeID, false);
            }
        }
        return mappingTypeID;
    }

    public int processSchemaMapping(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die SchemaMapping-Information: " + uri);
        int schemaMappingID = getIDForResource(uri, false);
        if (schemaMappingID == -1) {
            schemaMappingID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFSchemaMapping(MappingID,MappingURI) values("
                            + schemaMappingID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        // fuer die Positionen der Mapping Attribute
        int inPos = 0;
        int outPos = 0;
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            // printTriple(subject, predicate, object, baseURI);
            if (predicate.getURI().equals(
                    "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                int mappingTypeID = processMappingType((Resource) object,
                        baseURI);
                executeUpdate("Update SDFSchemaMapping set MappingTypeID="
                                + mappingTypeID
                                + " where MappingID="
                                + schemaMappingID, false);
            }
            if (predicate.getURI().equals(
                    mg.dynaquest.sourcedescription.sdf.vocabulary.SDFMappings.hasIn)) {
                //int hasInAttrConstID =
                // getIDForResource((Resource)object,baseURI,true);
                int hasInAttrConstID = processAttrConst((Resource) object,
                        baseURI);
                try {
                    String sql = "Insert into SDFSchemaMapHasInAttribute(MappingID,AttrConstID,pos) values("
                            + schemaMappingID
                            + ","
                            + hasInAttrConstID
                            + ","
                            + (inPos++) + ")";
                    logger.debug(sql);
                    executeUpdate(sql, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFMappings.hasOut)) {
                int hasOutAttrConstID = processAttrConst((Resource) object,
                        baseURI);
                try {
                    String sql = "Insert into SDFSchemaMapHasOutAttribute(MappingID,AttrConstID,pos) values("
                            + schemaMappingID
                            + ","
                            + hasOutAttrConstID
                            + ","
                            + (outPos++) + ")";
                    logger.debug(sql);
                    executeUpdate(sql, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFMappings.usesFunction)) {
                int mappingFunctionID = processMappingFunction(
                        (Resource) object, baseURI);
                executeUpdate("Update SDFSchemaMapping set MappingFunctionID="
                                + mappingFunctionID
                                + " where MappingID="
                                + schemaMappingID, false);
            }

            //ToDo Conditional Mapping!!

        }
        return schemaMappingID;
    }

    public int processOutputAttributeBinding(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die OutputAttributeBinding-Information: " + uri);
        int id = getIDForResource(uri, false);
        if (id == -1) {
            id = createNewIdForResource(uri);
            executeUpdate("Insert into SDFOutputAttributeBinding values("
                    + id + ",'" + uri + "')", false);
        }
        return id;
    }

    public  void processOutputPatternElement(Resource r, String baseURI,
            int outputPatternID) throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die OutputPatternElement-Information: " + uri);
        int outputPatternElementID = getIDForResource(uri, false);
        if (outputPatternElementID == -1) {
            outputPatternElementID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFOutputPatternElement(OutputPatternElementID,OutputPatternElementURI,OutputPatternID) values("
                            + outputPatternElementID
                            + ",'"
                            + uri
                            + "',"
                            + outputPatternID + ")", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
 //           Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.concernsAttribute)) {
                int attrConstID = processAttrConst((Resource) object, baseURI);
                executeUpdate("Update SDFOutputPatternElement set AttrConstID="
                                + attrConstID
                                + " where OutputPatternElementID="
                                + outputPatternElementID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasOutputAttributeBinding)) {
                int outputAttributeBindingID = processOutputAttributeBinding(
                        (Resource) object, baseURI);
                executeUpdate("Update SDFOutputPatternElement set OutputAttributeBindingID="
                                + outputAttributeBindingID
                                + " where OutputPatternElementID="
                                + outputPatternElementID, false);
            }
        }
    }

    public  int processOutputPattern(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die OutputPattern-Information: " + uri);
        int outputPatternID = getIDForResource(uri, false);
        if (outputPatternID == -1) {
            outputPatternID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFOutputPattern(OutputPatternID,OutputPatternURI) values("
                            + outputPatternID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasAttributeAttributeBindingPair)) {
                processOutputPatternElement((Resource) object, baseURI,
                        outputPatternID);
            }
        }
        return outputPatternID;
    }

    public  int processNecessity(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Notwendigkeits-Information: " + uri);
        int necID = getIDForResource(uri, false);
        if (necID == -1) {
            necID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFNecessity values("
                    + necID + ",'" + uri + "')", false);
        }
        return necID;
    }

    public  int processCompareOperator(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Operator-Information: " + uri);
        int ID = getIDForResource(uri, false);
        if (ID == -1) {
            ID = createNewIdForResource(uri);
           executeUpdate("Insert into SDFCompareOp values(" + ID
                    + ",'" + uri + "')", false);
        }
        return ID;
    }

    public  int processConstantSetSelectionType(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die constantSetSelectionType-Information: " + uri);
        int ID = getIDForResource(uri, false);
        if (ID == -1) {
            ID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFConstantSetSelectionType values("
                            + ID + ",'" + uri + "')", false);
        }
        return ID;
    }

    public  int processAttrConstSet(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die AttrConstSet-Information: " + uri);
        int ID = getIDForResource(uri, false);
        if (ID == -1) {
            ID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFAttrConstSet(SetID,SetURI) values("
                            + ID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasConstant)) {
                int attrConstID = processAttrConst((Resource) object, baseURI);
                try {
                    executeUpdate("Insert into SDFAttrConstSetElements values("
                                    + attrConstID + "," + ID + ")", false);
                } catch (Exception e) {
                    if (e.getLocalizedMessage().startsWith(
                            "ORA-00001: unique constraint")) {
                        logger.error("IGNORED --> "+ e.getMessage());
                    } else {
                        logger.error(e);
                    }
                }
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasAttribute)) {
                int attrConstID = processAttrConst((Resource) object, baseURI);
                try {
                    executeUpdate("Insert into SDFAttrConstSetElements values("
                                    + attrConstID + "," + ID + ")", false);
                } catch (Exception e) {
                    if (e.getLocalizedMessage().startsWith(
                            "ORA-00001: unique constraint")) {
                    	logger.error("IGNORED --> "+e.getMessage());
                    } else {
                        logger.error(e);
                    }
                }
            }
        }
        return ID;
    }

    public  int processConstantSelection(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die ConstantSelection-Information: " + uri);
        int ID = getIDForResource(uri, false);
        if (ID == -1) {
            ID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFConstantSetSelection(ConstantSetSelectionID,ConstantSetSelectionURI) values("
                            + ID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
 //           Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasConstantSelectionType)) {
                int constantSetSelectionTypeID = processConstantSetSelectionType(
                        (Resource) object, baseURI);
                executeUpdate("Update SDFConstantSetSelection set ConstantSetSelectionTypeID="
                                + constantSetSelectionTypeID
                                + " where ConstantSetSelectionID=" + ID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasConstantSet)) {
                int attConstSetID = processAttrConstSet((Resource) object,
                        baseURI);
                executeUpdate("Update SDFConstantSetSelection set SetID="
                                + attConstSetID
                                + " where ConstantSetSelectionID=" + ID, false);
            }
        }
        return ID;
    }

    public  void processInputPatternElement(Resource r, String baseURI,
            int inputPatternID) throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die InputPatternElement-Information: " + uri);
        int inputPatternElementID = getIDForResource(uri, false);
        if (inputPatternElementID == -1) {
            inputPatternElementID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFInputPatternElement(InputPatternElementID,InputPatternElementURI,InputPatternID) values("
                            + inputPatternElementID
                            + ",'"
                            + uri
                            + "',"
                            + inputPatternID + ")", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.concernsAttribute)) {
                int attrConstID = processAttrConst((Resource) object, baseURI);
                executeUpdate("Update SDFInputPatternElement set AttrConstID="
                                + attrConstID
                                + " where InputPatternElementID="
                                + inputPatternElementID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.necessity)) {
                int necessityID = processNecessity((Resource) object, baseURI);
                executeUpdate("Update SDFInputPatternElement set NecessityId="
                                + necessityID
                                + " where InputPatternElementID="
                                + inputPatternElementID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasCompareOperator)) {
                int compareOpID = processCompareOperator((Resource) object,
                        baseURI);
                // Hier jetzt eine Änderung, da es eine Menge von CompareOperatoren geben kann
                //executeUpdate("Update SDFInputPatternElement set CompareOpId="
                //                + compareOpID
                //                + " where InputPatternElementID="
                //                + inputPatternElementID, false);
                executeUpdate("Insert into SDFInputPatternCompareOperator (InputPatternElementID, CompareOpId) values("+
                		inputPatternElementID+","+compareOpID
                		+")",false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.valueFromConstantSet)) {
                int constantSetSelectionID = processConstantSelection(
                        (Resource) object, baseURI);
                executeUpdate("Update SDFInputPatternElement set constantSetSelectionID="
                                + constantSetSelectionID
                                + " where InputPatternElementID="
                                + inputPatternElementID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.dependsOn)) {
                int attrConstSetID = processAttrConstSet((Resource) object,
                        baseURI);
                executeUpdate("Update SDFInputPatternElement set dependsOnID="
                                + attrConstSetID
                                + " where InputPatternElementID="
                                + inputPatternElementID, false);
            }

        }
    }

    public  int processInputPattern(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die InputPattern-Information: " + uri);
        int inputPatternID = getIDForResource(uri, false);
        if (inputPatternID == -1) {
            inputPatternID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFInputPattern(InputPatternID,InputPatternURI) values("
                            + inputPatternID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasAttributeAttributeBindingPair)) {
                processInputPatternElement((Resource) object, baseURI,
                        inputPatternID);
            }
        }
        return inputPatternID;
    }

    public  int processAccessPattern(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die AccessPattern-Information: " + uri);
        int accessPatternID = getIDForResource(uri, false);
        if (accessPatternID == -1) {
            accessPatternID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFAccessPattern(AccessPatternID,AccessPatternURI) values("
                            + accessPatternID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasInputPattern)) {
                int inputPatternID = processInputPattern((Resource) object,
                        baseURI);
                executeUpdate("Update SDFAccessPattern set InputPatternID="
                                + inputPatternID
                                + " where AccessPatternID="
                                + accessPatternID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasOutputPattern)) {
                int outputPatternID = processOutputPattern((Resource) object,
                        baseURI);
                executeUpdate("Update SDFAccessPattern set OutputPatternID="
                                + outputPatternID
                                + " where AccessPatternID="
                                + accessPatternID, false);
            }
        }
        return accessPatternID;
    }

    public  int processUnit(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Einheiten-Information: " + uri);
        int unitID = getIDForResource(uri, false);
        if (unitID == -1) {
            unitID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFUnit(UnitID,Unituri) values("
                            + unitID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate.getURI().equals(
                    mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.isA)) {
                int superUnitID = processUnit((Resource) object, baseURI);
                executeUpdate("Update SDFUnit set superUnitID="
                        + superUnitID + " where UnitID=" + unitID, false);
            }
        }
        return unitID;
    }

    public  int processDatatype(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Datentyp-Information: " + uri);
        int datatypeID = getIDForResource(uri, false);
        if (datatypeID == -1) {
            datatypeID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFDatatype values("
                    + datatypeID + ",'" + uri + "')", false);
        }
        return datatypeID;
    }

    public  int processAttrConst(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die AttributKonstanten-Information: " + uri);
        int attrConstID = getIDForResource(uri, false);
        if (attrConstID == -1) {
            attrConstID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFAttrConst(AttrConstID,AttrConstURI) values("
                            + attrConstID + ",'" + uri + "')", false);
        }
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);

            // Testen ob das Element ein Attribut oder eine Konstante ist
            if (predicate.getURI().equals(
                    "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                logger.debug("Test ob Konstante oder Attribut ");
                if (object
                        .toString()
                        .equals(
                                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.Constant)) {
                    executeUpdate("Update SDFAttrConst set isConstant=1 where AttrConstID="
                                    + attrConstID, false);
                    logger.debug("-->Konstante");
                } else {
                    executeUpdate("Update SDFAttrConst set isConstant=0 where AttrConstID="
                                    + attrConstID, false);
                    logger.debug("-->Attribut");
                }
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.hasBaseDatatype)) {
                logger.debug("Element hat den Datentypen " + object);
                int datatypeID = processDatatype((Resource) object, baseURI);
                executeUpdate("Update SDFAttrConst set DatatypeID="
                                + datatypeID + " where AttrConstID="
                                + attrConstID, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasValue)) {
                logger.debug("Konstante hat den Wert " + object);
                executeUpdate("Update SDFAttrConst set value='"
                        + object + "' where AttrConstID=" + attrConstID, false);
            }
            if (predicate.getURI().equals(
                    mg.dynaquest.sourcedescription.sdf.vocabulary.SDFUnits.hasUnit)) {
                logger.debug("Hat eine Einheit " + object);
                int unitID = processUnit((Resource) object, baseURI);
                String sql = "Update SDFAttrConst set UnitID=" + unitID
                        + " where AttrConstID=" + attrConstID;              
                executeUpdate(sql, false);
            }
                                    
            if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.hasMaxLength)){
            	logger.debug("Hat ein MaxLength Constraint"+ object);
            	String sql = "Insert into SDFAttrConstConstraint(AttrConstConstraintTypeURI, AttrConstID, value1) values("+
				"'"+predicate.getURI()+"',"+attrConstID+",'"+object+"')";           
            	executeUpdate(sql, true);            	
            }
            if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.hasRange)){
            	logger.debug("Hat ein Range-Constraint"+object);
           		SDFIntervall intervall = processIntervall((Resource) object, baseURI);
            	String sql = "Insert into SDFAttrConstConstraint(AttrConstConstraintTypeURI, AttrConstID, value1, value2, value3, value4) values("+
							  "'"+predicate.getURI()+"',"+attrConstID+",'"+intervall.getLeftBorder()+"','"+intervall.isLeftIsOpen()+"','"
						+intervall.getRightBorder()+"','"+intervall.isRightIsOpen()+"')";            	         
            	executeUpdate(sql, true);            	
            }
            if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.hasGranularity)){
            	logger.debug("Hat ein Granularity Constraint"+ object);
        		String sql = "Insert into SDFAttrConstConstraint(AttrConstConstraintTypeURI, AttrConstID, value1) values("+
        					 "'"+predicate.getURI()+"',"+attrConstID+",'"+object+"')";           
        		executeUpdate(sql, true);            	
            }    
        }
        return attrConstID;
    }

    public SDFIntervall processIntervall(Resource r, String baseURI){
    	SDFIntervall intervall = new SDFIntervall(r.getURI());
        StmtIterator iter = r.listProperties();
        while (iter.hasNext()) {
        	Statement stmt = iter.nextStatement(); // get next statement
        	Property predicate = stmt.getPredicate(); // get the predicate
        	RDFNode object = stmt.getObject(); // get the object
        	

          if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.hasIntervallStartOpen)){
        	  intervall.setLeftBorder(Double.parseDouble(""+object), true);
          }

          if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.hasIntervallStartClose)){
        	  intervall.setLeftBorder(Double.parseDouble(""+object), false);
          }

          if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.hasIntervallEndOpen)){
        	  intervall.setRightBorder(Double.parseDouble(""+object), true);
          }

          if (predicate.getURI().equals(mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.hasIntervallEndClose)){
        	  intervall.setRightBorder(Double.parseDouble(""+object), false);
          }
        }
        return intervall;
    }
    
    public  int processEntity(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Entity-Information: " + uri);
        StmtIterator iter = r.listProperties();
        int entityID = getIDForResource(uri, false);
        if (entityID == -1) {
            entityID = createNewIdForResource(uri);
            executeUpdate("Insert into SDFEntity values("
                    + entityID + ",'" + uri + "')", false);
        }
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            // printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasAttribute)) {
                logger.debug("Erstelle Verknüpfung zu Attribut " + object);
                int attrConstID = processAttrConst((Resource) object, baseURI);
                try {
                    executeUpdate("Insert into SDFEntityAttribute(AttrConstID,EntityID,idAttr) values("
                                    + attrConstID + "," + entityID + ",0)", false);
                } catch (Exception e) {
                    if (e.getLocalizedMessage().startsWith(
                            "ORA-00001: unique constraint")) {
                        logger.error("IGNORED --> "+e.getMessage());                            
                    } else {
                        logger.error(e);
                    }
                }
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasIdAttribute)) {
                logger.debug("Erstelle identifizierende Verknüpfung zu Attribut "
                        + object);
                int attrConstID = processAttrConst((Resource) object, baseURI);
                String sql = "Insert into SDFEntityAttribute(AttrConstID,EntityID,idAttr) values("
                        + attrConstID + "," + entityID + ",1)";
                logger.debug(sql);
                try {
                    executeUpdate(sql, false);
                } catch (Exception e) {
                    if (e.getLocalizedMessage().startsWith(
                            "ORA-00001: unique constraint")) {
                        logger.error("IGNORED --> "+e.getMessage());
                    } else {
                    	logger.error(e);
                    }
                }
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasConstant)) {
                int attrConstID = processAttrConst((Resource) object, baseURI);
                String sql = "Insert into SDFEntityAttribute(AttrConstID,EntityID,idAttr) values("
                        + attrConstID + "," + entityID + ",0)";
                logger.debug(sql);
                executeUpdate(sql, true);  
            }

        }
        return entityID;

    }

    public int processSchema(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Schemainformation: " + uri);
        StmtIterator iter = r.listProperties();
        int schemaId = getIDForResource(uri, false);
        // Wenn es das Schema noch nicht gibt muss es in die DB eingetragen
        // werden bevor die Entities verarbeitet werden, da ansonsten ein
        // Konstraint verletzt wird
        if (schemaId == -1) {
            schemaId = createNewIdForResource(uri);
            executeUpdate("Insert into SDFSchema(SchemaID,SchemaURI) values("
                            + schemaId + ",'" + uri + "')", false);
        }

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            // printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.hasEntity)) {
                int entityID = processEntity((Resource) object, baseURI);
                // Wenn der Wert schon eingetragen ist, kann das einfach
                // ignoriert
                // werden
                try {
                    executeUpdate("Insert into SDFSchemaEntity values("
                                    + schemaId + "," + entityID + ")", false);
                } catch (Exception ex) {
                    logger.error("IGNORIERT-->!" + ex.getMessage());
                    //ex.printStackTrace();
                }
            }
            // toDO..
        }
        return schemaId;
    }

    public int insertIntensionalSourceDescription(Resource r,
            String baseURI) throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die intensionale Information: " + uri);
        StmtIterator iter = r.listProperties();
        int intDescId = getIDForResource(uri, false);
        if (intDescId == -1) {
            intDescId = createNewIdForResource(uri);
            executeUpdate("Insert into SDFIntensionalDescription(IntDescId,IntDescUri) values("
                            + intDescId + ",'" + uri + "')", false);
        }
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasLocalSchema)) {
                int schemaID = processSchema((Resource) object, baseURI);
                executeUpdate("Update SDFIntensionalDescription set SchemaID="
                                + schemaID + " where IntDescId=" + intDescId, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasAccessPattern)) {
                int accPattId = processAccessPattern((Resource) object, baseURI);
                String sql = "Insert into SDFIntDescAccessPattern(IntDescId,AccessPatternId) values("
                        + intDescId + "," + accPattId + ")";
                //logger.debug(sql);
                try {
                    executeUpdate(sql, false);
                } catch (Exception e) {
                    if (e.getLocalizedMessage().startsWith(
                            "ORA-00001: unique constraint")) {
                        logger.error("IGNORED --> "
                                + e.getLocalizedMessage());
                    } else {
                        logger.error(e);
                    }
                }
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions.hasSchemaMapping)) {
            	logger.debug("SDFIntensionalDescriptions.hasSchemaMapping");
                int schemaMappingId = processSchemaMapping((Resource) object,
                        baseURI);
                String sql = "Insert into SDFIntDescSchemaMap(IntDescId,MappingId) values("
                        + intDescId + "," + schemaMappingId + ")";
                //logger.debug(sql);
                try {
                    executeUpdate(sql, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
        return intDescId;
    }

    public int processPredicateType(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die PredicateType-Information: " + uri);
        int id = getIDForResource(uri, false);
        if (id == -1) {
            id = createNewIdForResource(uri);
            
                   executeUpdate("Insert into SDFPredicateType(PredicateTypeID,PredicateTypeURI) values("
                            + id + ",'" + uri + "')", false);
        }
        return id;
    }

    public int processComplexPredicate(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Complex Prädikat Information: " + uri);
        StmtIterator iter = r.listProperties();
        int id = getIDForResource(uri, false);
        if (id == -1) {
            id = createNewIdForResource(uri);
            
                   executeUpdate("Insert into SDFComplexPredicate(ComplexPredicateID,ComplexPredicateURI) values("
                            + id + ",'" + uri + "')", false);
        }

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object

            if (predicate.getURI().equals(
                    "www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                int predicateTypeID = processPredicateType((Resource) object,
                        baseURI);
                
                       executeUpdate("Update SDFComplexPredicate set PredicateTypeID="
                                + predicateTypeID
                                + " where ComplexPredicateID=" + id, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.hasSimpleLeftPart)) {
                int leftID = processSimplePredicate((Resource) object, baseURI);
                String sql = "Update SDFComplexPredicate set leftSimplePartID="
                        + leftID + " where ComplexPredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.hasSimpleRightPart)) {
                int rightID = processSimplePredicate((Resource) object, baseURI);
                String sql = "Update SDFComplexPredicate set rightSimplePartID="
                        + rightID + " where ComplexPredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.hasComplexLeftPart)) {
                int leftID = processComplexPredicate((Resource) object, baseURI);
                String sql = "Update SDFComplexPredicate set leftComplexID="
                        + leftID + " where ComplexPredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.hasComplexRightPart)) {
                int rightID = processComplexPredicate((Resource) object,
                        baseURI);
                String sql = "Update SDFComplexPredicate set rightComplexID="
                        + rightID + " where ComplexPredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

        }
        return id;
    }

    public int processSimplePredicate(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die einfache Prädikat Information: " + uri);
        StmtIterator iter = r.listProperties();
        int id = getIDForResource(uri, false);
        if (id == -1) {
            id = createNewIdForResource(uri);
            
                   executeUpdate("Insert into SDFSimplePredicate(SimplePredicateID,SimplePredicateURI) values("
                            + id + ",'" + uri + "')", false);
        }

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object

            logger.debug("PREDICATE -->");
            // printTriple(subject, predicate, object, baseURI);

            if (predicate.getURI().equals(
                    "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                int predicateTypeID = processPredicateType((Resource) object,
                        baseURI);
                
                       executeUpdate("Update SDFSimplePredicate set PredicateTypeID="
                                + predicateTypeID
                                + " where SimplePredicateID="
                                + id, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.predAttribute)) {
                int attrID = processAttrConst((Resource) object, baseURI);
                String sql = "Update SDFSimplePredicate set predAttrID="
                        + attrID + " where SimplePredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.predStringOperator)
                    || predicate
                            .getURI()
                            .equals(
                                    mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.predNumberOperator)) {
                int opId = processCompareOperator((Resource) object, baseURI);
                String sql = "Update SDFSimplePredicate set compareOpID="
                        + opId + " where SimplePredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.predConstant)) {
                int attrID = processAttrConst((Resource) object, baseURI);
                String sql = "Update SDFSimplePredicate set constantValueID="
                        + attrID + " where SimplePredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.predValue)) {
                String sql = "Update SDFSimplePredicate set value='"
                        + object.toString() + "' where SimplePredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

            // Wenn es ein InPrädikat ist, dann gibt es eine Liste von
            // Konstanten
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates.predConstantSet)) {
                int constSet = processAttrConstSet((Resource) object, baseURI);
                String sql = "Update SDFSimplePredicate set InSetId="
                        + constSet + " where SimplePredicateID=" + id;
                logger.debug(sql);
                executeUpdate(sql, false);
            }

        }
        return id;
    }

    public int insertExtensionalSourceDescription(Resource r,
            String baseURI) throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die extensionale Information: " + uri);
        StmtIterator iter = r.listProperties();
        int extDescId = getIDForResource(uri, false);
        if (extDescId == -1) {
            extDescId = createNewIdForResource(uri);
            
                   executeUpdate("Insert into SDFExtensionalDescription(ExtDescId,ExtDescUri) values("
                            + extDescId + ",'" + uri + "')", false);
        }
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //System.out.print("EXTENSIONAL -->");
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFExtensionalDescriptions.hasSimpleDescriptionPredicate)) {
                int predID = processSimplePredicate((Resource) object, baseURI);
                String sql = "Insert into SDFHasSimpleDescriptionPredica(ExtDescId,SimplePredicateID) values("
                        + extDescId + "," + predID + ")";
                executeUpdate(sql, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFExtensionalDescriptions.hasComplexDescriptionPredicate)) {
                int predID = processComplexPredicate((Resource) object, baseURI);
                String sql = "Insert into SDFHasComplexDescriptionPredic(ExtDescId,ComplexPredicateID) values("
                        + extDescId + "," + predID + ")";
                executeUpdate(sql, false);
            }

        }
        return extDescId;
    }

    public int insertQualitySourceDescription(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die qualitative Information: " + uri);
        StmtIterator iter = r.listProperties();
        int qualDescId = getIDForResource(uri, false);
        if (qualDescId == -1) {
            qualDescId = createNewIdForResource(uri);
            
                   executeUpdate("Insert into SDFQualitativeDescription(QualDescId,QualDescUri) values("
                            + qualDescId + ",'" + uri + "')", false);
        }
        while (iter.hasNext()) {
//            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
//            Property predicate = stmt.getPredicate(); // get the predicate
//            RDFNode object = stmt.getObject(); // get the object
        }
        return qualDescId;
    }

    // Diese Methode trägt die Knoten-Daten der Quellenbeschreibung ein und
    // ruft rekursiv die Methoden zum Auffruf der anderen Teile der Beschreibung
    // auf
    public void insertSourceDescription(Resource r, String baseURI)
            throws RDFException, SQLException {
        String uri = getURIForResource(r, baseURI);
        logger.debug("Verarbeite die Quellenbeschreibung: " + uri);
        StmtIterator iter = r.listProperties();
        int sourceDescID = getIDForResource(r, baseURI, false);
        if (sourceDescID == -1) {
            sourceDescID = createNewIdForResource(uri);
            
                   executeUpdate("Insert into SDFSourceDescription(SourceDescId,SourceDescUri) values("
                            + sourceDescID + ",'" + uri + "')", false);
        }
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
//            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            //printTriple(subject, predicate, object, baseURI);
            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDescriptions.aboutSource)) {
                String aboutSource = object.toString();
                
                       executeUpdate("Update SDFSourceDescription set aboutSource='"
                                + aboutSource
                                + "' where SourceDescID="
                                + sourceDescID, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDescriptions.hasIntensionalDesc)) {
                int intDescId = insertIntensionalSourceDescription(
                        (Resource) object, baseURI);
                
                       executeUpdate("Update SDFSourceDescription set IntDescID="
                                + intDescId
                                + " where SourceDescID="
                                + sourceDescID, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDescriptions.hasExtensionalDesc)) {
                int extDescId = insertExtensionalSourceDescription(
                        (Resource) object, baseURI);
                
                       executeUpdate("Update SDFSourceDescription set ExtDescID="
                                + extDescId
                                + " where SourceDescID="
                                + sourceDescID, false);
            }

            if (predicate
                    .getURI()
                    .equals(
                            mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDescriptions.hasQualityDesc)) {
                int qualDescId = insertQualitySourceDescription(
                        (Resource) object, baseURI);
                
                       executeUpdate("Update SDFSourceDescription set QualDescID="
                                + qualDescId
                                + " where SourceDescID="
                                + sourceDescID, false);
            }
        }

    }

    public void readSourceDescription(Model model, String baseURI)
            throws RDFException, SQLException {
        // Der erste Schritt besteht darin, das Start-Element zu finden, d.h.
        // das Element, dass die gesamte Quelle zusammenhält
        String queryString = "select ?result "
                + "WHERE (?result,<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>,<"+SDFDescriptions.SourceDescription+">)";
        Query query = new Query(queryString);
        query.setSource(model);
        // Für jede Quellenbeschreibung die eventuell hier jetzt vorhanden ist
        QueryExecution qe = new QueryEngine(query);
        QueryResults results = qe.exec();
        while (results.hasNext()) {
            ResultBinding resBinding = (ResultBinding) results.next();
            Resource sdesc = (Resource) resBinding.get("result");
            // Zunächst einmal muss die in der DB existierende
            // Quellenbeschreibung
            // zu dieser URI gelöscht werden
            // removeSourceDescription(sdec); --> das geht aber gar nicht so
            // einfach
            // ich weiss nämlich gar nicht, ob eine Quellenbeschreibung ein
            // bestimmtes
            // Subobjekt exclusiv benutzt!!
            // --> beim Eintragen muss dann entweder ein Insert oder ein Update
            // gemacht werden --> später!!
            // und dann kann die neue/aktualisierte eingetragen werden
            insertSourceDescription(sdesc, baseURI);
        }

    }

    public List<Resource> getSourceDescriptionNode(Model model)
            throws RDFException {
        return RDFHelper.getNodesWithRDFType(
                model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDescriptions.SourceDescription);
    }

    public List<Resource> getDataschemaNode(Model model) throws RDFException {
        return RDFHelper.getNodesWithRDFType(model,
                mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema.Dataschema);
    }

//    public List<Resource> getNodesWithRDFType(Model model, String rdfType)
//            throws RDFException {
//        return findNodes(model,
//                "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", rdfType);
//    }
//
//    public List<Resource> findNodes(Model model, String predicate,
//            String object) throws RDFException {
//        List<Resource> listOfNodes = new ArrayList<Resource>();
//        // Der erste Schritt besteht darin, das Start-Element zu finden, d.h.
//        // das Element, dass die gesamte Quelle zusammenhält
//        String queryString = "select ?result " + "WHERE (?result,<" + predicate
//                + ">,<" + object + ">)";
//        Query query = new Query(queryString);
//        query.setSource(model);
//        // Für jeden Knoten
//        QueryExecution qe = new QueryEngine(query);
//        QueryResults results = qe.exec();
//        while (results.hasNext()) {
//            ResultBinding resBinding = (ResultBinding) results.next();
//            listOfNodes.add((Resource)resBinding.get("result"));
//        }
//        return listOfNodes;
//    }

//    public Model readRDF(BufferedReader in, String baseURI)
//            throws RDFException {
//        Model model = ModelFactory.createDefaultModel();
//        // read the RDF/XML file
//        model.read(in, baseURI);
//        return model;
//    }

    public void dumpRDF(Model model, String baseURI) throws RDFException {
        // Einfach mal alle Tripel raushausen
        // list the statements in the graph
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
            printTriple(subject, predicate, object, baseURI);
        }
    }

    public void removeInfoFromDB(String baseURI) throws SQLException,
            ClassNotFoundException {
        // u.U. müssen erst noch mal die Statements initialisiert werden
        if (prepStatements.size() == 0)
            initDeleteStatements();
        java.sql.Statement dbStmt = dbConnection.createStatement();

        // Erster Schritt
        // Lese aus SDFURIIDMapping alle IDs aus die baseURI als Präfix haben
        ResultSet rs = dbStmt
                .executeQuery("Select count(id) from SDFURIIDMapping where URI like '"
                        + baseURI + "%'");
        rs.next();
        int[] allIds = new int[rs.getInt(1) + 1];
        rs = dbStmt
                .executeQuery("Select id from SDFURIIDMapping where URI like '"
                        + baseURI + "%'");
        {
            int i = 0;
            while (rs.next()) {
                allIds[++i] = rs.getInt("ID");
                //System.out.print(rs.getInt("ID"));
            }
            rs.close();
        }

        int deletedRows = 0;
        for (int st = 0; st < prepStatements.size(); st++) {
            PreparedStatement prep = (PreparedStatement) prepStatements.get(st);
            //logger.debug("Neues Statement "+prep.toString());
            for (int i = 0; i < allIds.length; i++) {
                int noOfParams = ((Integer) numberOfParamsInStatement.get(st))
                        .intValue();
                for (int pa = 1; pa <= noOfParams; pa++) {
                    prep.setInt(pa, allIds[i]);
                    //logger.debug(pa+"-->"+allIds[i]);
                }
                try {
                    deletedRows = deletedRows + prep.executeUpdate();
                } catch (Exception e) {
                    logger.error("IGNORED -->" + e.getLocalizedMessage()
                            + " in " + prepStatementsString.get(st));
                }
                //logger.debug("Führe Statement aus. Geändert: "+);
            }
        }
        logger.debug("Anzahl gelöschter Tupel " + deletedRows);
        dbStmt.close();
    }

//    public Model readModel(String baseURI) throws MalformedURLException,
//            IOException, FileNotFoundException, RDFException {
//    	logger.debug("Lese das Modell "+baseURI);
//        BufferedReader reader = null;
//        if (baseURI.startsWith("file://")){
//            reader = new BufferedReader(new FileReader(baseURI.substring(7)));
//        }else{
//            reader = HttpAccess.get(new URL(baseURI), 1);
//        }
//        Model model = readRDF(reader, baseURI);
//        return model;
//    }

    /** Laden von RDF in die Datenbank
     *  baseUri Name der Datei, die zu laden ist (auch http:)
     *  isGlobalSchemaFile ist die Datei ein globales Schema
     *  initSDFDB soll die Datenbank neu initialisiert werden
     *  globalSchemaURI welches globale Schema gehört dazu
     */
    public void loadRDF(String baseURI, boolean isGlobalSchemaFile,
            boolean initSDFDB, String globalSchemaURI) throws RDFException, MalformedURLException,
            IOException, FileNotFoundException, SQLException,
            ClassNotFoundException {
        if (initSDFDB){
            initSDFSchema();
        }
        java.sql.Statement dbStmt = dbConnection.createStatement();
        //dbConnection.setAutoCommit(false);
        //dbStmt.execute("SET CONSTRAINTS ALL DEFERRED");
        //    System.exit(-1999);
        Model model = RDFHelper.readModel(baseURI);
        if (!isGlobalSchemaFile){
            Model global = RDFHelper.readModel(globalSchemaURI);
            model.add(global);
        }

        if (isGlobalSchemaFile) {
            
        	// TODO: Alte Einträge entfernen
        	// removeInfoFromDB(baseURI);
            // Falls ein globales Schema eingelesen werden soll:
            List<Resource> nodeList = getDataschemaNode(model);
            for (int i = 0; i < nodeList.size(); i++) {
                int schemaID = processSchema((Resource) nodeList.get(i),
                        baseURI);
                try {
                    dbStmt.executeUpdate("Insert into SDFGlobalSchema values("
                            + schemaID + ")");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            // TODO: Erst mal alles alte entfernen:
            // removeInfoFromDB(baseURI);
            // Die Knoten suchen, der die Quellenbeschreibungen enthalten
            // i.d.R. nur einer
            List<Resource> nodeList = getSourceDescriptionNode(model);
            for (int i = 0; i < nodeList.size(); i++) {
                insertSourceDescription((Resource) nodeList.get(i), baseURI);
            }
        }
        //dbConnection.commit();

        dbStmt.close();
    }
        
    //------------------------------------------------------------------------
    // Ab hier: Lesender Zugriff
//  ------------------------------------------------------------------------    
    
	private int getIDforURI(String URI) throws SQLException {
		int resultID = -1;
		ResultSet rs = null;
		synchronized (queryIDFromURI) {
			queryIDFromURI.setString(1, URI);
			rs = queryIDFromURI.executeQuery();
		}
		if (rs.next()) {
			resultID = rs.getInt("id");
			rs.close();
		}
		return resultID;
	}

	private String getURIForID(int id) throws SQLException {
		String URI = null;
		ResultSet rs = null;
		synchronized (queryURIFromID) {
			queryURIFromID.setInt(1, id);
			rs = queryURIFromID.executeQuery();
		}
		if (rs.next()) {
			URI = rs.getString("uri");
			rs.close();
		}
		return URI;
	}

	public SDFUnit getUnit(int unitID) throws SQLException {
		java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
				.executeQuery("SELECT unit.UnitURI, superunit.UnitURI "
						+ "FROM SDFUnit unit, SDFUnit superunit "
						+ "WHERE unit.superUnitID = superunit.UnitID AND "
						+ "      unit.UnitID=" + unitID);
		SDFUnit unit = null;
		if (rs.next()) {
			unit = SDFUnitFactory.getUnit(rs.getString(1), rs.getString(2));
		}
		rs.close();
		dbStmt.close();
		return unit;
	}

	public SDFDatatype getDatatype(String URI) throws SQLException {
		return getDatatype(this.getIDforURI(URI));
	}

	public SDFDatatype getDatatype(int datatypeID) throws SQLException {
		return SDFDatatypeFactory.getDatatype(this.getURIForID(datatypeID));
	}

	public SDFAttribute getAttribute(String URI) throws SQLException {
		return (SDFAttribute) getAttrConst(URI);
	}

	public SDFConstant getConstant(String URI) throws SQLException {
		return (SDFConstant) getAttrConst(URI);
	}

	public SDFSchemaElement getAttrConst(String URI) throws SQLException {
		return getAttrConst(this.getIDforURI(URI));
	}

	public SDFSchemaElement getAttrConst(int attrConstID) throws SQLException {
		SDFSchemaElement retElem = (SDFSchemaElement) attrConstCache
				.get(new Integer(attrConstID));
		if (retElem == null) {
			java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("SELECT isConstant, AttrConstURI, UnitID, DatatypeID, value "
							+ "FROM SDFAttrConst "
							+ "WHERE AttrConstID="
							+ attrConstID);
			if (rs.next()) {
				int datatypeID = rs.getInt("DatatypeID");
				SDFDatatype dt = null;
				if (!rs.wasNull()) {
					// DT auslesen und zuweisen später
					dt = getDatatype(datatypeID);
				}
				
				if (rs.getInt("isConstant") > 0) {
					String attrConstURI = rs.getString("AttrConstURI");
					String value = rs.getString("value");
					if (mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.String.equals(dt.getURI(false))){					
						retElem = new SDFStringConstant(attrConstURI, value);						
					}else if (mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes.Number.equals(dt.getURI(false))){											
						retElem = new SDFNumberConstant(attrConstURI, value);						
					} 
				} else {
					retElem = new SDFAttribute(rs.getString("AttrConstURI"));					
				}

				int unitID = rs.getInt("UnitID");
				if (!rs.wasNull()) {
					// UnitID auslesen und zuweisen
					SDFUnit unit = getUnit(unitID);
					retElem.setUnit(unit);
				}
				
				if (dt != null) {
					retElem.setDatatype(dt);
				}
			}
			rs.close();
			dbStmt.close();
			RelationalSourceDescriptionManager.attrConstCache.put(new Integer(attrConstID), retElem);
		}
		
		// Zusätzlich nun noch die Constraints an die Attribute/Constanten binden
		// Constraints finden sich in einer anderen Tabelle (SdfAttrConstConstraints)
		java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
			.executeQuery("SELECT AttrConstConstraintTypeURI, value1, value2, value3, value4 "
					+ "FROM SDFAttrConstConstraint "
					+ "WHERE AttrConstID="+ attrConstID);

		while (rs.next()){
			String constraintTypeURI = rs.getString(1);
			if (mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.hasMaxLength.equals(constraintTypeURI)){
				SDFMaxLengthConstraint constraint = new SDFMaxLengthConstraint(null,rs.getInt(2));
				retElem.addDtConstraint(constraint);
			}else if (mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.hasGranularity.equals(constraintTypeURI)){
				boolean isInt = mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.IntegerNumbers.equals(rs.getString(2));
				SDFGranularityConstraint constraint = new SDFGranularityConstraint(null,isInt);
				retElem.addDtConstraint(constraint);
			}else if (mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints.hasRange.equals(constraintTypeURI)){
				double left = rs.getDouble(2);
				boolean leftOpen = rs.getBoolean(3);
				double right = rs.getDouble(4);
				boolean rightOpen = rs.getBoolean(5);
				SDFIntervall intervall = new SDFIntervall(null, left, leftOpen, right, rightOpen);
				SDFRangeConstraint constraint = new SDFRangeConstraint(null,intervall);
				retElem.addDtConstraint(constraint);
			}
		}		
		rs.close();
		dbStmt.close();				
		return retElem;
	}

	public SDFAttributeList getAttributeSet(String URI) throws SQLException {
		return getAttributeSet(this.getIDforURI(URI));
	}

	public SDFAttributeList getAttributeSet(int setID) throws SQLException {
		return (SDFAttributeList) getSchemaElementSet(setID, true);
	}

	public SDFConstantList getConstantSet(String URI) throws SQLException {
		return getConstantSet(this.getIDforURI(URI));
	}

	public SDFConstantList getConstantSet(int setID) throws SQLException {
		return (SDFConstantList) getSchemaElementSet(setID, false);
	}

	private SDFSchemaElementSet getSchemaElementSet(int setID,
			boolean containsAttributes) throws SQLException {
	    //System.out.println("getSchemaElementSet "+setID);
		SDFSchemaElementSet elementSet = (SDFSchemaElementSet) elementSetCache
				.get(new Integer(setID));
		if (elementSet == null) {
			// Eigentlich müsste ich hier jetzt erst wissen, ob
			// das Attribute oder Konstanten sind
			if (containsAttributes) {
				elementSet = new SDFAttributeList(this.getURIForID(setID));
			} else {
				elementSet = new SDFConstantList(this.getURIForID(setID));
			}
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt.executeQuery("SELECT AttrConstId "
					+ "FROM SDFAttrConstSetElements " + "WHERE SetId = "
					+ setID);
			while (rs.next()) {
				int attrConstId = rs.getInt("AttrConstId");
				SDFSchemaElement sElem = this.getAttrConst(attrConstId);
				elementSet.add(sElem);
				//System.out.println("getSchemaElementSet Element hinzugefügt");
			}
			elementSetCache.put(new Integer(setID), elementSet);
		}
		return elementSet;
	}

	public SDFEntity getEntity(String URI) throws SQLException {
		return getEntity(this.getIDforURI(URI));
	}

	public SDFEntity getEntity(int enID) throws SQLException {
		SDFEntity entity = (SDFEntity) entityCache.get(new Integer(enID));
		if (entity == null) {
			entity = new SDFEntity(this.getURIForID(enID));
            java.sql.Statement dbStmt = dbConnection.createStatement();
			// Attribute und Konstanten auslesen
			ResultSet rs = dbStmt.executeQuery("SELECT AttrConstId, idAttr "
					+ "FROM SDFEntityAttribute " + "WHERE EntityID = " + enID);
			while (rs.next()) {
				int attrConstId = rs.getInt("AttrConstId");
				int id = rs.getInt("idAttr");
				SDFSchemaElement sElem = this.getAttrConst(attrConstId);
				// Ist es ein Attribute oder eine Konstante
				if (sElem.getClass().getName().equals(
						"mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute")) {
					if (id > 0) {
						entity.addIdAttribute((SDFAttribute) sElem);
					} else {
						entity.addAttribute((SDFAttribute) sElem);
					}
				} else {
					entity.addConstant((SDFConstant) sElem);
				}
			}
			rs.close();
			dbStmt.close();
			entityCache.put(new Integer(enID), entity);
		}
		return entity;
	}

	public SDFDataschema determineSchemaForMapping(int mappingId)
			throws SQLException {
		SDFDataschema outSchema = null;
        java.sql.Statement dbStmt = dbConnection.createStatement();
		/*System.out.println("Select distinct SchemaID "
				+ " FROM SDFSchemaMapHasOutAttribute, SDFEntityAttribute, SDFSchemaEntity "
				+ " WHERE MappingID = "
				+ mappingId
				+ " AND "
				+ "       SDFSchemaMapHasOutAttribute.AttrConstId = SDFEntityAttribute.AttrConstId AND"
				+ "       SDFEntityAttribute.EntityID = SDFSchemaEntity.EntityID");
*/
		ResultSet rs = dbStmt
				.executeQuery("Select distinct SchemaID "
						+ " FROM SDFSchemaMapHasOutAttribute, SDFEntityAttribute, SDFSchemaEntity "
						+ " WHERE MappingID = "
						+ mappingId
						+ " AND "
						+ "       SDFSchemaMapHasOutAttribute.AttrConstId = SDFEntityAttribute.AttrConstId AND"
						+ "       SDFEntityAttribute.EntityID = SDFSchemaEntity.EntityID");
		
			// Ein Mapping ist immer nur zwischen zwei Schemata definiert
		// d.h. hier kann nie mehr als ein Schema bei rauskommen
		if (rs.next()) {
			outSchema = this.getSchema(rs.getInt("SchemaID"));
		}
		rs.close();
		dbStmt.close();
		return outSchema;
	}

	public SDFFunction getFunction(String URI) throws SQLException {
		return this.getFunction(this.getIDforURI(URI), false);
	}

	public SDFFunction getFunction(int functionID, boolean processReverse)
			throws SQLException {
		//System.out.println("getFunction " + functionID+ " "+processReverse);
		SDFFunction function = null;
        java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
				.executeQuery("SELECT MappingFunctionURI, FunctionTypeURI, hasReverseFunction "
						+ "FROM SDFMappingFunction, SDFFunctionType "
						+ "WHERE SDFMappingFunction.FunctionTypeID = SDFFunctionType.FunctionTypeID "
						+ "AND SDFMappingFunction.MappingFunctionID = "
						+ functionID);
		if (rs.next()) {
			function = SDFFunctionFactory.getFunction(rs
					.getString("MappingFunctionURI"), rs
					.getString("FunctionTypeURI"));
			if (!processReverse) {
				int rFunc = rs.getInt("hasReverseFunction");
				if (rs.wasNull())
					rFunc = -1;
				if (rFunc > 0) {
					// Jetzt muss sicher gestellt werden, dass hier keine
					// Endlosschleifen
					// eingebaut werden
					SDFFunction reverseFunction = getFunction(rFunc, true);
					function.setReverseFunction(reverseFunction);
					// Hier die Umkehrfunktion setzen (Rekursion!)
					reverseFunction.setReverseFunction(function);
				}
			}
		}
		return function;
	}

	public SDFSchemaMapping getSchemaMapping(String mappingURI,
			SDFDataschema localSchema) throws SQLException {
		return this.getSchemaMapping(this.getIDforURI(mappingURI), localSchema);
	}

	public SDFSchemaMapping getSchemaMapping(int mappingId,
			SDFDataschema localSchema) throws SQLException {
		// Welches ist das globale Schema?
		SDFDataschema globalSchema = determineSchemaForMapping(mappingId);
		SDFSchemaMapping mapping = null;
        java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
				.executeQuery("SELECT MappingURI, MappingTypeURI, MappingFunctionID "
						+ "FROM SDFSchemaMapping, SDFMappingType "
						+ "WHERE SDFSchemaMapping.MappingTypeID = SDFMappingType.MappingTypeID "
						+ "AND SDFSchemaMapping.MappingID = " + mappingId);
		if (rs.next()) {
			String mappingURI = rs.getString("MappingURI");
			String mappingTypeURI = rs.getString("MappingTypeURI");
			int mappingFunctionId = rs.getInt("MappingFunctionID");
			// Falls es keine Mapping-Funktion gibt
			if (rs.wasNull())
				mappingFunctionId = -1;

			mapping = SDFSchemaMappingFactory.getSchemaMapping(mappingURI,
					mappingTypeURI, localSchema, globalSchema);

			// Jetzt noch die Attribute zuweisen
			rs.close();
			rs = dbStmt
					.executeQuery("SELECT AttrConstID FROM SDFSchemaMapHasInAttribute "
							+ "WHERE MappingID = " + mappingId + " ORDER BY pos");
			while (rs.next()) {
				mapping.addInSchemaElement(this.getAttrConst(rs
						.getInt("AttrConstID")));
			}
			rs.close();
			rs = dbStmt
					.executeQuery("SELECT AttrConstID FROM SDFSchemaMapHasOutAttribute "
							+ "WHERE MappingID = " + mappingId + " ORDER BY pos");
			while (rs.next()) {
				mapping.addOutSchemaElement(this.getAttrConst(rs
						.getInt("AttrConstID")));
			}
			rs.close();
			// Schließliche die Mapping-Function
			if (mappingFunctionId > 0) {
				mapping.setMappingFunction(this.getFunction(mappingFunctionId,
						false));
			}
		}
		//System.out.println(mappingId+" "+mapping);
		dbStmt.close();
		return mapping;
	}

	public SDFDataschema getSchema(String URI) throws SQLException {
		return this.getSchema(this.getIDforURI(URI));
	}

	public SDFDataschema getSchema(int schemaId) throws SQLException {
		SDFDataschema dataSchema = (SDFDataschema) dataschemaCache
				.get(new Integer(schemaId));
		if (dataSchema == null) {
			dataSchema = new SDFDataschema(this.getURIForID(schemaId));
			// Entitäten auslesen und zuweisen
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("Select EntityID from SDFSchemaEntity where SchemaID ="
							+ schemaId);
			while (rs.next()) {
				int enID = rs.getInt("EntityID");
				dataSchema.addEntity(getEntity(enID));
			}
			rs.close();
			dbStmt.close();
			dataschemaCache.put(new Integer(schemaId), dataSchema);
		}
		return dataSchema;
	}

	public SDFNecessityState getNecessity(int necessityID) throws SQLException {
		SDFNecessityState ness = (SDFNecessityState) necessityStateCache
				.get(new Integer(necessityID));
		if (ness == null) {
			ness = new SDFNecessityState(this.getURIForID(necessityID));
			necessityStateCache.put(new Integer(necessityID), ness);
		}
		return ness;
	}

	public SDFInputPattern getInputPattern(int inPatternID) throws SQLException {
		SDFInputPattern inputPattern = intputPatternCache.get(new Integer(inPatternID));
		String inputPatternURI = this.getURIForID(inPatternID);
		if (inputPattern == null) {
			inputPattern = new SDFInputPattern(inputPatternURI);
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("SELECT InputPatternElementURI, InputPatternElementID, AttrConstID, NecessityID, ConstantSetSelectionId, dependsOnId "
							+ "FROM SDFInputPatternElement "
							+ "WHERE InputPatternID = " + inPatternID);
			while (rs.next()) {
				String inPatternElementURI = rs
						.getString("InputPatternElementURI");
				int intPatternElementID = rs.getInt("InputPatternElementID");
				SDFAttribute attribute = (SDFAttribute) this.getAttrConst(rs
						.getInt("AttrConstID"));
				int necessity = rs.getInt("NecessityID");
				if (rs.wasNull())
					necessity = -1;
				
				// Es gibt jetzt eine Menge von CompareOps in einer anderen Relation ...
				//int compareOpID = rs.getInt("CompareOpID");
				//if (rs.wasNull())
				//	compareOpID = -1;
								
				
				int constantSetSelectionId = rs
						.getInt("ConstantSetSelectionId");
				if (rs.wasNull())
					constantSetSelectionId = -1;
				int dependsOnId = rs.getInt("dependsOnId");
				if (rs.wasNull())
					dependsOnId = -1;
				
				@SuppressWarnings("unused")
				SDFConstantList constandSetSelection = null;
				@SuppressWarnings("unused")
				SDFAttributeList dependsOn = null;

				// AttributeBinding erzeugen ... eigentlich ist das nicht
				// ganz ok (zumindest was die gesamte Modellierung angeht,
				// aber nicht entscheident!)
				SDFInputAttributeBinding attributeBinding = new SDFInputAttributeBinding(
						inPatternElementURI);
				if (necessity > 0) {
					attributeBinding.setNecessity(getNecessity(necessity));
				}
				// Auslesen der CompareOperatoren
				processCompareOperators(attributeBinding, intPatternElementID);
//				if (compareOpID > 0) {
//					attributeBinding.setCompareOp(SDFCompareOperatorFactory
//							.getCompareOperator(this.getURIForID(compareOpID)));
//				}
				if (constantSetSelectionId > 0) {
				    // Hier muss jetzt nicht die Konstantenmenge ausgelesen werden
				    // sondern für diese Konstantenmenge der Selektionstyp und die SetID
				    //int conSetSelTypeId = getConstantSetSelectionTypeId(constantSetSelectionId);
				    SDFConstantList conSet = getConstantSetSelection(constantSetSelectionId);
				    // TODO
				    //attributeBinding.setSetSelectionType();
					attributeBinding.setConstantSet(conSet);
				}
				if (dependsOnId > 0) {
					attributeBinding.setRequiredAttributes(getAttributeSet(dependsOnId));
				}
				inputPattern
						.addAttributeAttributeBindingPair(new SDFAttributeAttributeBindingPair(
								inPatternElementURI, attribute, attributeBinding));
			}
			rs.close();
			intputPatternCache.put(new Integer(inPatternID), inputPattern);
			dbStmt.close();
		}
		return inputPattern;
	};
	
	
	
	private void processCompareOperators(SDFInputAttributeBinding attributeBinding, int intPatternElementID) throws SQLException {
		java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
				.executeQuery("SELECT CompareOpId "
						+ "FROM SDFInputPatternCompareOperator "
						+ "WHERE InputPatternElementID = " + intPatternElementID);
		while (rs.next()) {
			int compareOpID = rs.getInt("CompareOpId");
			if (compareOpID > 0) {
				attributeBinding.addCompareOp(SDFCompareOperatorFactory.getCompareOperator(this.getURIForID(compareOpID)));
			}
		}
		rs.close();
		dbStmt.close();				
	}

	public SDFConstantList getConstantSetSelection(int constantSetSelectionId)
	throws SQLException{
	    SDFConstantList constSetSelection =  (SDFConstantList) constSetSelectionCache.get(new Integer(constantSetSelectionId));   
	    if (constSetSelection == null){
            java.sql.Statement dbStmt = dbConnection.createStatement();
	        ResultSet rs = dbStmt
			.executeQuery("SELECT SETID "
					+ "FROM SDFConstantSetSelection "
					+ "WHERE CONSTANTSETSELECTIONID  = " + constantSetSelectionId);
	        if (rs.next()){
	            constSetSelection = this.getConstantSet(rs.getInt(1));
	        }
	    }
	    return constSetSelection;
	}

	public SDFOutputPattern getOutputPattern(int outPatternID) throws SQLException {
		SDFOutputPattern outputPattern = outputPatternCache.get(new Integer(outPatternID));
		String outputPatternURI = this.getURIForID(outPatternID);
		if (outputPattern == null) {
			outputPattern = new SDFOutputPattern(outputPatternURI);
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("SELECT OutputPatternElementURI, AttrConstID, OutputAttributeBindingId "
							+ "FROM SDFOutputPatternElement "
							+ "WHERE OutputPatternID = " + outPatternID);
			while (rs.next()) {
				String outPatternElementURI = rs
						.getString("OutputPatternElementURI");
				SDFAttribute attribute = (SDFAttribute) this.getAttrConst(rs
						.getInt("AttrConstID"));
				int outAttributeBindingId = rs.getInt("OutputAttributeBindingId");
                // Default ist kein Out-AttributeBindingment
				SDFAttributeBindung attributeBinding = SDFOutputAttributeBindingFactory.getOutputAttributeBinding(SDFIntensionalDescriptions.None);
				if (!rs.wasNull()) {
					attributeBinding = SDFOutputAttributeBindingFactory.getOutputAttributeBinding(this
							.getURIForID(outAttributeBindingId));
				}
				outputPattern
						.addAttributeAttributeBindingPair(new SDFAttributeAttributeBindingPair(
								outPatternElementURI, attribute, attributeBinding));
			}
			outputPatternCache.put(new Integer(outPatternID),outputPattern);
			rs.close();
			dbStmt.close();
		}
		return outputPattern;
	}

	public SDFAccessPattern getAccessPattern(String URI) throws SQLException {
		return this.getAccessPattern(this.getIDforURI(URI));
	}

	public SDFAccessPattern getAccessPattern(int accessPatternId)
			throws SQLException {
		SDFAccessPattern accessPattern = accessPatternCache.get(new Integer(accessPatternId));
		if (accessPattern == null) {
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("Select AccessPatternURI, InputPatternID, OutputPatternID from SDFAccessPattern where AccessPatternID ="
							+ accessPatternId);
			if (rs.next()) {
				accessPattern = new SDFAccessPattern(rs
						.getString("AccessPatternURI"));
				int inPattId = rs.getInt("InputPatternID");
				int outPattId = rs.getInt("OutputPatternID");
				SDFInputPattern inPattern = getInputPattern(inPattId);
				SDFOutputPattern outPattern = getOutputPattern(outPattId);
				accessPattern.setInputPatt(inPattern);
				accessPattern.setOutputPatt(outPattern);
				accessPatternCache.put(new Integer(accessPatternId),accessPattern);
			}
			rs.close();
		}
		return accessPattern;
	}

	public SDFIntensionalSourceDescription getIntensionalSourceDescription(
			String URI) throws SQLException {
		SDFIntensionalSourceDescription intDesc = new SDFIntensionalSourceDescription(
				URI);
		int intDescId = this.getIDforURI(URI);
		// Lokales Schema auslesen
		int localSchemaId = -1;
        java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
				.executeQuery("Select SchemaID from SDFIntensionalDescription where IntDescID ="
						+ intDescId);
		if (rs.next()) {
			localSchemaId = rs.getInt("SchemaID");
		}
		SDFDataschema localSchema = null;
		if (localSchemaId != -1) {
			localSchema = getSchema(localSchemaId);
			intDesc.setLocalSchema(localSchema);
		}
		rs.close();
		// Mappings auslesen
		rs = dbStmt
				.executeQuery("Select MappingID from SDFIntDescSchemaMap where IntDescID ="
						+ intDescId);
		while (rs.next()) {
			int mappingID = rs.getInt("MappingID");
			SDFSchemaMapping mapping = getSchemaMapping(mappingID, localSchema);
			intDesc.addSchemaMapping(mapping);
		}
		rs.close();
		// Zugriffsmuster
		rs = dbStmt
				.executeQuery("Select AccessPatternID from SDFIntDescAccessPattern where IntDescID ="
						+ intDescId);
		while (rs.next()) {
			int accessPatternID = rs.getInt("AccessPatternID");
			SDFAccessPattern accessPattern = getAccessPattern(accessPatternID);
			intDesc.addAccessPattern(accessPattern);
		}
		return intDesc;
	};

	public SDFSimplePredicate getSimplePredicate(int predicateID)
			throws SQLException {
		SDFSimplePredicate sPred = (SDFSimplePredicate)predicateCache.get(new Integer(predicateID));
		if (sPred == null) {
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("select SimplePredicateURI, predAttrID, Value, ConstantValueID, CompareOpID, PredicateTypeID, INSETID from SDFSimplePredicate where SimplePredicateID="
							+ predicateID);
			if (rs.next()) {
				String predURI = rs.getString("SimplePredicateURI");
				SDFAttribute attribute = (SDFAttribute) getAttrConst(rs
						.getInt("predAttrID"));
				Object value = rs.getString("Value");
				if (rs.wasNull())
					value = null;
				SDFConstant constValue = null;
				int constValueId = rs.getInt("ConstantValueID");
				if (!rs.wasNull()) {
					constValue = (SDFConstant) getAttrConst(constValueId);
					//System.out.println(constValueId +" "+
					// constValue.toString());
					value = constValue.getString();
				}
				String predTypeURI = this.getURIForID(rs
						.getInt("PredicateTypeID"));

				SDFCompareOperator compOp = null;
				int compOpId = rs.getInt("CompareOpID");
				if (!rs.wasNull()) {
					compOp = SDFCompareOperatorFactory.getCompareOperator(this
							.getURIForID(compOpId));
				}
				SDFConstantList elements = null;
				int inSetID = rs.getInt("INSETID");
				if (!rs.wasNull()) {
					elements = getConstantSet(inSetID);
				}
				sPred = SDFSimplePredicateFactory.createSimplePredicate(
						predURI, predTypeURI, attribute, compOp, value,
						constValue, elements, null);

				predicateCache.put(new Integer(predicateID), sPred);
			}
			rs.close();
		}
		return sPred;
	}

	public SDFComplexPredicate getComplexPredicate(int predicateID)
			throws SQLException {
		SDFComplexPredicate cPred = (SDFComplexPredicate) predicateCache.get(new Integer(predicateID));
		if (cPred == null) {
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt
					.executeQuery("select ComplexPredicateURI, PredicateTypeID, rightComplexID, leftComplexID, leftSimplePartID, rightSimplePartID from SDFComplexPredicate where ComplexPredicateID="
							+ predicateID);
			if (rs.next()) {
				String predURI = rs.getString("ComplexPredicateURI");
				String pTypeURI = getURIForID(rs.getInt("PredicateTypeID"));
				cPred = SDFComplexPredicateFactory.createComplexPredicate(
						predURI, pTypeURI);

				int rightComplexID = rs.getInt("rightComplexID");
				if (!rs.wasNull()) {
					cPred.setRight(getComplexPredicate(rightComplexID));
				}
				int leftComplexID = rs.getInt("leftComplexID");
				if (!rs.wasNull()) {
					cPred.setLeft(getComplexPredicate(leftComplexID));
				}
				int leftSimplePartID = rs.getInt("leftSimplePartID");
				if (!rs.wasNull()) {
					cPred.setLeft(getSimplePredicate(leftSimplePartID));
				}
				int rightSimplePartID = rs.getInt("rightSimplePartID");
				if (!rs.wasNull()) {
					cPred.setRight(getSimplePredicate(rightSimplePartID));
				}

				predicateCache.put(new Integer(predicateID), cPred);
			}
			rs.close();
		}
		return cPred;
	}

	public SDFExtensionalSourceDescription getExtensionalSourceDescription(
			String URI) throws SQLException {
		SDFExtensionalSourceDescription extDesc = new SDFExtensionalSourceDescription(
				URI);
		// Die extensionale Beschreibung ist eine Menge von Prädikaten und zwar
		// einfach und komplexen
		int extDescId = this.getIDforURI(URI);
        java.sql.Statement dbStmt = dbConnection.createStatement();
		// Zunächst die einfachen Prädikate
		ResultSet rs = dbStmt
				.executeQuery("Select SimplePredicateID from SDFHasSimpleDescriptionPredica where ExtDescID ="
						+ extDescId);
		while (rs.next()) {
			SDFSimplePredicate sPred = getSimplePredicate(rs
					.getInt("SimplePredicateID"));
			extDesc.addDescriptionPredicate(sPred);
		}
		rs.close();
		// Komplexe Prädikate verarbeiten
		rs = dbStmt
				.executeQuery("Select ComplexPredicateID from SDFHasComplexDescriptionPredic where ExtDescID ="
						+ extDescId);
		while (rs.next()) {
			SDFComplexPredicate cPred = getComplexPredicate(rs
					.getInt("ComplexPredicateID"));
			extDesc.addDescriptionPredicate(cPred);
		}

		dbStmt.close();
		return extDesc;
	};

	public SDFQualitativeSourceDescription getQualitativeSourceDescription(
			String URI) {
		SDFQualitativeSourceDescription qualDesc = new SDFQualitativeSourceDescription(
				URI);

		return qualDesc;
	};

	public SDFSourceDescription getSourceDescription(int sdID) throws Exception {
		return this.getSourceDescription(getURIForID(sdID));
	}

	public SDFSourceDescription getSourceDescription(String URI)
			throws Exception {
		SDFSourceDescription sd = new SDFSourceDescription(URI);
		int intDescId = -1;
		int extDescId = -1;
		int qualDescId = -1;
		String aboutSource = null;
        java.sql.Statement dbStmt = dbConnection.createStatement();
		ResultSet rs = dbStmt
				.executeQuery("Select IntDescID, ExtDescID,QualDescID, aboutSource from "
						+ "SDFSourceDescription where SourceDescURI = '"
						+ URI
						+ "'");
		if (rs.next()) {
			intDescId = rs.getInt("IntDescID");
			extDescId = rs.getInt("ExtDescID");
			qualDescId = rs.getInt("QualDescID");
			aboutSource = rs.getString("aboutSource");
		}
		if (intDescId != -1) {
			String intDescUri = getURIForID(intDescId);
			SDFIntensionalSourceDescription intDesc = getIntensionalSourceDescription(intDescUri);
			sd.setIntDesc(intDesc);
		}
		if (extDescId != -1) {
			String extDescUri = getURIForID(extDescId);
			SDFExtensionalSourceDescription extDesc = getExtensionalSourceDescription(extDescUri);
			sd.setExtDesc(extDesc);
		}
		if (qualDescId != -1) {
			String qualDescUri = getURIForID(qualDescId);
			SDFQualitativeSourceDescription qualDesc = getQualitativeSourceDescription(qualDescUri);
			sd.setQualDesc(qualDesc);
		}
		if (aboutSource != null) {
			sd.setAboutSource(new SDFSource(aboutSource));
		}
		dbStmt.close();
		return sd;
	}

	/**
	 * Liefert eine Menge der URIs der Quellen, die jeweils mindestens eines der
	 * Attribute aus Attributes enthalten
	 */
	public Collection<String> getSourcesWithAttributeFromList(SDFAttributeList attributes)
			throws Exception {
		Collection<String> listOfSources = new ArrayList<String>();
		if (attributes.getAttributeCount() > 0) {
			String sql = "select distinct SourceDescURI, aboutSource "
					+ "from SDFSourceDescription,  SDFIntDescAccessPattern, SDFAccessPattern, SDFOutputPatternElement, SDFAttribute, SDFSchemaMapHasInAttribute, SDFSchemaMapHasOutAttribute "
					+ "where SDFSourceDescription.IntDescID = SDFIntDescAccessPattern.IntDescID AND "
					+ "SDFIntDescAccessPattern.AccessPatternID = SDFAccessPattern.AccessPatternID AND "
					+ "SDFAccessPattern.OutputPatternID = SDFOutputPatternElement.OutputPatternID AND "
					+ "SDFOutputPatternElement.AttrConstID = SDFAttribute.ATTRIBUTEID AND "
					+ "SDFAttribute.ATTRIBUTEID = SDFSchemaMapHasInAttribute.AttrConstID AND "
					+ "SDFSchemaMapHasInAttribute.MappingID = SDFSchemaMapHasOutAttribute.MappingID AND (";
			for (int i = 0; i < attributes.getAttributeCount(); i++) {
				SDFAttribute sdfAttrib = attributes.getAttribute(i);
				//System.out.println(sdfAttrib);
				String attrib = sdfAttrib.getURI(false);
				int attribID = this.getIDforURI(attrib);
				sql = sql + "SDFSchemaMapHasOutAttribute.AttrConstID = "
						+ attribID;
				if (i < attributes.getAttributeCount() - 1) {
					sql = sql + " OR ";
				}
			}
			sql = sql + ")";
			//System.out.println(sql);
            java.sql.Statement dbStmt = dbConnection.createStatement();
			ResultSet rs = dbStmt.executeQuery(sql);
			while (rs.next()) {
				listOfSources.add(rs.getString("SourceDescURI"));
			}
			rs.close();
			dbStmt.close();
		}
		//System.out.println("Fertig");
		return listOfSources;
	}

	public SDFSource getSourceForLocalAttribute(SDFAttribute attribute) throws Exception{
		String aboutSource = null;
        java.sql.Statement dbStmt = dbConnection.createStatement();
        // Achtung! Erstmal ein Hack. Eigentlich müsste die Query komplexer sein und die intensionale
        // Beschreibung und die Entitäten durchgehen. Im Moment ist aber garantiert, dass es
        // funktioniert, da die URIs der Attribute immer die URI der Quellenbeschreibung enthalten
		ResultSet rs = dbStmt
				.executeQuery("Select aboutSource from "
						+ "SDFSourceDescription where SourceDescURI like '"
						+ attribute.getURIWithoutQualName()
						+ "%'");
		if (rs.next()) {
			aboutSource = rs.getString("aboutSource");
		}
		if (aboutSource != null){
			return new SDFSource(aboutSource);
		}else{
			return null;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
	      Logger root = Logger.getRootLogger();
	        root.removeAllAppenders();
	        root.addAppender(new ConsoleAppender(
	                new PatternLayout("[%t] %l %x - %m%n")));
	        
	        root.setLevel(Level.ALL);
			
			Properties properties = new Properties();
			String propFile = System.getProperty("user.home")
					+ "/DynaQuest.properties";
			properties.load(new FileInputStream(propFile));

			String sdm_user = properties.getProperty("sdm_user");
			String sdm_password = properties.getProperty("sdm_password");
			String sdm_jdbcString = properties.getProperty("sdm_jdbcString");
			String sdm_driverClass = properties.getProperty("sdm_driverClass");
			String sdm_sqlfile = properties.getProperty("sdm_sqlfile");

			RelationalSourceDescriptionManager sdm = RelationalSourceDescriptionManager.getInstance(
					"DynaQuestLoaderSDM",sdm_user, sdm_password, sdm_jdbcString, sdm_driverClass, false, sdm_sqlfile);

			SDFAttribute attribute = sdm.getAttribute("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2004/08/Automarkt2004_Quelle_1.sdf#DYNQ_HERSTELLER.Marke");
			
			System.out.println(attribute);
			System.out.println(attribute.getURIWithoutQualName());
			
			SDFSource source = sdm.getSourceForLocalAttribute(attribute);
			System.out.println(source);
			
	}
	
	
}