/*
 * Created on 24.05.2006
 *
 */
package mg.dynaquest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.monitor.PODatabaseLogger;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.base.PhysicalCollectorPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.pom.POManager;
import mg.dynaquest.queryoptimization.trafo.FullProcessPlanTransform;
import mg.dynaquest.queryoptimization.trafo.ProcessPlanTransform;
import mg.dynaquest.queryoptimization.trafo.RelationalFullProcessPlanTransform;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.SDFQueryFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdm.RelationalSourceDescriptionManager;
import mg.dynaquest.sourcedescription.sdm.SourceDescriptionManager;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.SourceSelector;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Hauptklasse zur Steuerung
 */

public class DynaQuest {

    /**
	 * @uml.property  name="sdm"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SourceDescriptionManager sdm = null;
    /**
	 * @uml.property  name="transform"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    ProcessPlanTransform transform;
   // private Thread t;
	/**
	 * @uml.property  name="poManager"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private POManager poManager;
	/**
	 * @uml.property  name="dbLogger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private PODatabaseLogger dbLogger;
    static Logger logger = Logger.getLogger(DynaQuest.class);
    /**
	 * @uml.property  name="properties"
	 * @uml.associationEnd  qualifier="constant:java.lang.String java.lang.String"
	 */
    Properties properties = null;
    
    
    /**
	 * @return  Returns the sdm.
	 * @uml.property  name="sdm"
	 */
    public synchronized SourceDescriptionManager getSdm() {
        return sdm;
    }

    private synchronized void initComponents(boolean writeToDB) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
    	
    	String propFile = System.getProperty("user.home")+"/DynaQuest.properties";
    	properties = new Properties();
    	// Properties-Datei
    	try {
			properties.load(new FileInputStream(propFile));
		} catch (FileNotFoundException e) {
			// Dann neue Properties-Datei mit Default-Werten anlegen		
	        properties.put("sdm_user","sdm_mg");
			properties.put("sdm_password","jkgwer6t97456");
			properties.put("sdm_jdbcString","jdbc:oracle:thin:@power1.offis.uni-oldenburg.de:1521:power1");
			properties.put("sdm_driverClass", "oracle.jdbc.driver.OracleDriver");
			properties.put("sdm_sqlfile", "e:/Development/DynaQuest/DynaQuest/bin/sql/createSDMSchemaOracle.sql");
			properties.put("log_user", "dynalogging");
			properties.put("log_password", "354skjfiw4");
			properties.put("log_jdbcString", "jdbc:oracle:thin:@power1.offis.uni-oldenburg.de:1521:power1");
			properties.put("log_driverClass", "oracle.jdbc.driver.OracleDriver");
			properties.store(new FileOutputStream(propFile), "Von DynaQuest erzeugtes Property-File. Werte dürfen geändert werden.");
			logger.debug("Default Property File erzeugt");
		} 
    	
        String sdm_user = properties.getProperty("sdm_user");
        String sdm_password = properties.getProperty("sdm_password");
        String sdm_jdbcString = properties.getProperty("sdm_jdbcString");
        String sdm_driverClass = properties.getProperty("sdm_driverClass");
        String log_user = properties.getProperty("log_user");
        String log_password = properties.getProperty("log_password");
        String log_jdbcString = properties.getProperty("log_jdbcString");
        String log_driverClass = properties.getProperty("log_driverClass"); 
      
               
        sdm = RelationalSourceDescriptionManager.getInstance("DynaQuestSDM",sdm_user, sdm_password, sdm_jdbcString, sdm_driverClass, false, null);            
        
        // TODO: Andere Komponenten initialisieren
    
        // Optimierung: Restrukturierung und Transformation
        
        transform = new RelationalFullProcessPlanTransform(); 
        
        // PlanManager
        
        poManager = POManager.getInstance();
        poManager.start();
                
        dbLogger = new PODatabaseLogger(log_user, log_password, log_jdbcString, log_driverClass, writeToDB);
        
        
        // Recovery Handler 
    }
    
    // Kleiner Hack um die Auswahl eines Plans zu beschleunigen
    private void processQuery(SDFQuery q, int planToExecuteNo, int outputBufferSize) throws POException, TimeoutException {
        logger.info("Starte Quellenwahl");
        
    	List<AlgebraPO> accessPlans = makeSourceSelection(q);
                      
        
        
        // Jetzt die gefundenen Pläne restrukturieren
        // TODO
        
        // Jetzt die restrukturierten Pläne übersetzen
        List<PlanOperator> physicalPlans = new ArrayList<PlanOperator>();
        for (AlgebraPO n:accessPlans){
        	logger.info("Verarbeite Plan");
            physicalPlans.addAll(transform.transform(n));
            logger.info("Durch");
        }
        
        for (PlanOperator p: physicalPlans){
        	StringBuffer dumpedPlan = new StringBuffer();
        	
        		try {
					ProcessPlanTransform.dumpPlan(p, "",dumpedPlan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
            logger.info("Übersetzter Plan: "+dumpedPlan);
        }
        
        // Dann müsste ich jetzt einen der Pläne ausführen können ...
        NAryPlanOperator p = (NAryPlanOperator) physicalPlans.get(planToExecuteNo);
        SimplePlanOperator dummy = new PhysicalCollectorPO();
        
        
        poManager.installNewPlan(p);
        POManager.setSubtreeBufferSize(p, outputBufferSize);
        dbLogger.planToExecute(p);
        
       
        dbLogger.registerMonitors(poManager.getPOMonitors());

        
        
        
        logger.info("Starte die Verarbeitung des Plans Nr. "+planToExecuteNo);        
        poManager.startSubtree(p);

	        p.open(dummy);
	        Object o = null;
	        int r=0;
	        while ((o = p.next(dummy, -1))!=null){
	        	if (r==0) {
	        		logger.info("Time to First Tuple: " + Calendar.getInstance().getTimeInMillis());
	        	}
//	        	System.out.println(o); DISABLED FOR MEASURING CACHE TIME
	        	r++;
	        }
	        logger.info(r+ " Objekte gelesen");
	        logger.info("UNIX Timestamp: " + Calendar.getInstance().getTimeInMillis());
	        p.close(dummy);
        poManager.stopSubtree(p);
        poManager.stop();
        
             
    }

	private List<AlgebraPO> makeSourceSelection(SDFQuery q) throws POException, TimeoutException {
		List<AnnotatedPlan> plans = SourceSelector.makeSourceSection(getSdm(), q);
        List<AlgebraPO> accessPlans = new ArrayList<AlgebraPO>();
        logger.info("Gefundene Pläne "+plans.size());
        int i=0;
        for (AnnotatedPlan p:plans){
            //System.out.println(p.toString());     
            accessPlans.add(p.getAccessPlan());
            StringBuffer dumpedPlan = new StringBuffer();
            ProcessPlanTransform.dumpPlan(p.getAccessPlan(), "",dumpedPlan);
            logger.info(i+++""+dumpedPlan.toString());
        }
        
		return accessPlans;
	}
    
    
    
//
//	public synchronized void start() {
//        if (t == null) {
//            t = new Thread(this);
//            t.start();
//        }
//    }    
//    
//    public synchronized void stop() {
//        t = null;
//    }
//    
//     public synchronized void run() {
//        // Hier erstmal nur den Thread am Leben halten
//        while ( t != null){
//            try {
//                wait(1000);
//            } catch (InterruptedException e) {
//                t = null;
//                e.printStackTrace();
//            }
//        }
//    }
//    
    
	private void processQuery(int planToExecute, int noOfRuns, int waitSeconds, Collection<SDFQuery> testQuery, int bufferSize) throws POException, TimeoutException {
		for (SDFQuery q: testQuery){
            logger.info("Verarbeite Query: "+q.toString());
            for (int i=0;i<noOfRuns || noOfRuns==-1;i++){
            	processQuery(q,planToExecute, bufferSize);
                try {
		        	logger.info("Time to Finish: " + Calendar.getInstance().getTimeInMillis());
                	logger.info("Warte "+waitSeconds+" Sekunden ....");
		        	Thread.sleep(waitSeconds * 1000);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                }
            }               
        }
		logger.info("Verarbeitung terminiert");
	}
	
    public DynaQuest(boolean writeToDB) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        initComponents(writeToDB);
    }
    
    public static void main(String[] args) throws Exception {
        // Logging konfigurieren

        Logger root = Logger.getRootLogger();
        root.removeAllAppenders();
        root.addAppender(new ConsoleAppender(
                new PatternLayout("[%t] %l %x - %m%n")));
        root.addAppender(new FileAppender(new PatternLayout("[%t] %l %x - %m%n"), "dq_logging"));
        
        root.setLevel(Level.OFF);
        Logger.getLogger("mg.dynaquest.DynaQuest").setLevel(Level.ALL);
        
        Logger.getLogger("mg.dynaquest.monitor").setLevel(Level.OFF);
        
//        Logger.getLogger("mg.dynaquest.queryexecution").setLevel(Level.ALL);
//        Logger.getLogger("mg.dynaquest.sourceselection").setLevel(Level.INFO);
        
//        Logger.getLogger("mg.dynaquest.queryexecution.po.relational").setLevel(Level.ALL);
//        Logger.getLogger("mg.dynaquest.queryexecution.po.base").setLevel(Level.ALL);
//        Logger.getLogger("mg.dynaquest.sourceselection.TerminatorPO").setLevel(Level.OFF);
//        Logger.getLogger("mg.dynaquest.sourceselection.CompensatePO").setLevel(Level.OFF);
//        Logger.getLogger("mg.dynaquest.sourceselection.SDMAccessPO").setLevel(Level.OFF);
//        Logger.getLogger("mg.dynaquest.sourceselection.SDFtoAnnotatedPlanPO").setLevel(Level.OFF);
//        Logger.getLogger("mg.dynaquest.queryexecution.po.access.RMIDataAccessPO").setLevel(Level.ERROR);
//        Logger.getLogger("mg.dynaquest.queryoptimization.trafo.rules.AccessPOTransformationRule").setLevel(Level.ALL);
//        Logger.getLogger("mg.dynaquest.queryexecution.po.algebra.AccessPO").setLevel(Level.ALL);
//        Logger.getLogger("mg.dynaquest.sourcedescription.sdm.RelationalSourceDescriptionManager").setLevel(Level.ALL);
//        Logger.getLogger("mg.dynaquest.queryexecution.po.base.FilterPO").setLevel(Level.ALL);  
        Logger.getLogger("mg.dynaquest.queryexecution.po.caching.CachingPO").setLevel(Level.INFO);      
//        String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/Query1sort.sdf";
//        String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/Query1.sdf";
//        String queryURI = "http://134.106.144.4/~codey/Query1.sdf"; /* Peter Barreto */
//        String queryURI = "http://134.106.144.4/~codey/Query5.sdf"; /* Paul Barreto */
        String queryURI = "http://134.106.144.4/~codey/Query10.sdf"; /* Barreto ^ PLZ > 55000 ~ 500 */
//        String queryURI = "http://134.106.144.4/~codey/Query11.sdf"; /* Barreto ^ PLZ > 80000 ~ 200 */
//        String queryURI = "http://134.106.144.4/~codey/Query12.sdf"; /* Barreto ^ PLZ > 90000 ~ 100 */
//        String queryURI = "http://134.106.144.4/~codey/Query13.sdf"; /* Barreto ^ PLZ > 35000 ~  */
//        String queryURI = "http://134.106.144.4/~codey/Query3.sdf"; /* Peter */
//        String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/QueryBenj1.sdf";
//        String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/Query1Full.sdf"; /* Barreto */
        //String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/Query1mitJoin.sdf";

//        String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/QueryOnSource4.sdf";
//        String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/QueryOnSource4Full.sdf";
//        String queryURI = "http://134.106.144.4/~codey/QueryOnSource4Cached.sdf";
        // String queryURI = "http://www-is.offis.uni-oldenburg.de/~grawund/rdf/2004/08/QueryOnSource4Sorted.sdf";
        
        
        int planToExecute = 1;
        int noOfRuns = 1;
        boolean writeToDB = false;
        int waitSeconds = 1;
        int bufferSize = 100;
        
        /* SessionID. Sollte später durch Client gesetzt werden */
        String sessionId = "DEFAULT_SESSION_ID";
        
        if (args.length>0){
        	queryURI = args[0];
        	planToExecute = Integer.parseInt(args[1]);         
        	noOfRuns = Integer.parseInt(args[2]);
        	writeToDB = Boolean.parseBoolean(args[3]);
        	waitSeconds = Integer.parseInt(args[4]);
        	bufferSize = Integer.parseInt(args[5]);
        }
        
        logger.info("Starte DynaQuest");
        logger.info("UNIX Timestamp: " + Calendar.getInstance().getTimeInMillis());
        DynaQuest dq = new DynaQuest(writeToDB);
        //dq.start();    
        SDFQueryFactory factory = new SDFQueryFactory(dq.getSdm());
    
        Collection<SDFQuery> testQuery = factory.readQuerys(queryURI, sessionId);
        
        dq.processQuery(planToExecute, noOfRuns, waitSeconds, testQuery, bufferSize);
        
        
    }



 

}

