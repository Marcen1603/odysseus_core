package mg.dynaquest.monitor;

/**
 * POMonitorTimerTask: periodisches PO-Logging
 * 
 * schreibt periodisch Log-Daten der POs in eine Datenbank
 * Wird so im moment nicht verwendet! 
 * ACHTUNG NICHT VERWENDEN !!
 */

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.ExceptionEvent;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.access.HttpAccessPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.po.convert.AraneusConvertPO;
import mg.dynaquest.queryexecution.po.xml.XPathProjectionPO;
import mg.dynaquest.wrapper.access.HttpAccessParams;

//import mg.dynaquest.queryexecution.po.xml.XMLFileDumperPO;

/**
 * @author  Marco Grawunder
 */
@SuppressWarnings("unused")
public class POMonitorTimerTask extends TimerTask implements SimplePlanOperator,
		POMonitorEventListener {
	private static final boolean debug = true;

	private static final boolean runOnlyOnce = false;

	/* System-Konfiguration */
	private static String proxyHost = "proxy";

	private static String proxyPort = "3128";

	private static String dbUser = "hobelmann";

	private static String dbPass = "farzbarz";

	/**
	 * @uml.property  name="t"
	 */
	private Thread t;

	/**
	 * @uml.property  name="ht"
	 * @uml.associationEnd  readOnly="true"
	 */
	private HttpAccessPO ht;

	/**
	 * @uml.property  name="conv"
	 * @uml.associationEnd  readOnly="true"
	 */
	private AraneusConvertPO conv;

	/**
	 * @uml.property  name="xp"
	 * @uml.associationEnd  readOnly="true"
	 */
	private XPathProjectionPO xp;

	/**
	 * @uml.property  name="top"
	 * @uml.associationEnd  readOnly="true"
	 */
	private PlanOperator top;

	/* TimerTask.run() */
	public void run() {
//		t = Thread.currentThread();
//		if (debug) {
//			Date heute = new Date();
//			System.out.println("Starte eine neue Verarbeitung "
//					+ heute.toString());
//		}
//		ht = new HttpAccessPO();
//		conv = new AraneusConvertPO();
//		xp = new XPathProjectionPO("/result/car");
//		top = null;
//		try {
//			HttpAccessParams params = new HttpAccessParams();
//			params.setURL("http://www.autoscout24.de/home/index/list.asp");
//			params.setGet();
//			params.appendParam("make", "13");
//			params.appendParam("carsperpage", "5");
//			params.appendParam("country", "D");
//
//			ht.start();
//			ht.appendHttpAccessParams(params);
//			ht.setNOOfConsumerPOs(1);
//
//			conv.start();
//			conv.setConvertClassName("mg.dynaquest.wrapper.autoscout24.CarPage");
//			conv.setStartMethodeName("CarPage");
//			conv.setInputPO(ht);
//
//			xp.start();
//			xp.setInputPO(conv);
//			POMonitor htMon = new POMonitor("htMon", ht);
//			POMonitor convMon = new POMonitor("convMon", conv);
//			POMonitor xpMon = new POMonitor("xpMon", xp);
//		
//			
//			PODatabaseLogger htDB = new PODatabaseLogger(htMon,  dbUser,
//					dbPass);
//			PODatabaseLogger convDB = new PODatabaseLogger(convMon, htDB,
//					dbUser, dbPass);
//			@SuppressWarnings("unused")			
//			PODatabaseLogger xpDB = new PODatabaseLogger(xpMon, convDB, dbUser,
//					dbPass);
//			htMon.addPOMonitorEventListener(this, ExceptionEvent.ID);
//			convMon.addPOMonitorEventListener(this, ExceptionEvent.ID);
//			xpMon.addPOMonitorEventListener(this, ExceptionEvent.ID);
//			/* Zum Testen */
//			top = xp;
//			//boolean openResult =
//			top.open(this);
//			//if (debug) System.out.println("top.open returns " + openResult);
//			for (Object o = top.next(this, -1); t != null && o != null; o = top
//					.next(this, -1)) {
//				//if (debug) System.out.println("next returns: " + o);
//			}
//			// Eigentlich muss hier gewartet werden, bis auch das
//			// next_done-Event durch ist
//
//			/*
//			 * XMLFileDumper, falls die noch gebraucht werden... XMLFileDumperPO
//			 * fd1 = new XMLFileDumperPO("xmlDump.txt",false); fd1.start();
//			 * fd1.setInputPO(conv); XMLFileDumperPO fd2 = new
//			 * XMLFileDumperPO("testDump.txt", false); fd2.start();
//			 * fd2.setInputPO(xp); POMonitor fd1Mon = new POMonitor("fd1Mon",
//			 * fd1); POMonitor fd2Mon = new POMonitor("fd2Mon", fd2);
//			 * PODatabaseLogger fd1DB = new PODatabaseLogger(fd1Mon, dbUser,
//			 * dbPass); PODatabaseLogger fd2DB = new PODatabaseLogger(fd2Mon,
//			 * dbUser, dbPass); fd1.stop(); fd2.stop();
//			 */
//		} catch (MalformedURLException e) {
//			System.err.println("kaputtes URL: " + e);
//		} catch (POException e) {
//			System.err.println("POMonitorTimerTask: POException: " + e);
//	    } catch (TimeoutException e) {
//	        System.err.println("POMonitorTimerTask: POException: " + e);
//    }
//cleanup();
	}

	private void cleanup() {
		/*
		 * Verarbeitung abschließend, sonst terminiert der Prozess nie
		 */
		if (debug)
			System.out.println("Beende die Verarbeitung");
		try {
			top.close(this);
		} catch (POException ex) {
			ex.printStackTrace();
		}

		if (debug)
			System.out.println("Stoppe alle POs");
		if (debug) {
			Date heute = new Date();
			System.out.println("Beende die Verarbeitung " + heute.toString());
		}

		ht.stop();
		conv.stop();
		xp.stop();
	}

	/* POMonitorEventListener.poMonitorEventOccured() */
	public void poMonitorEventOccured(POMonitorEvent ev) {
		//    if (!(ev.getType().equals(POMonitorEvent.ERROR))) {
		//      System.err.println("Fehler in
		// POMonitorTimerTask.poMonitorEventOccured:");
		//      System.err.println("Event ist keine ExceptionEvent!");
		//    }
		//    /* Exception; alles stoppen */
		//    t = null;
		//    cleanup();
	}

	/** startet periodisch einen PO-Lauf */
	public static void main(String[] args) {
		Properties prop = System.getProperties();
		prop.put("http.proxyHost", proxyHost);
		prop.put("http.proxyPort", proxyPort);
		if (runOnlyOnce)
			new POMonitorTimerTask().run();
		else {
			Timer timer = new Timer();
			TimerTask task = new POMonitorTimerTask();
			/* 900000 msec ^= 15 min */
			timer.scheduleAtFixedRate(task, 0, 900000);
		}
	}

	/*
	 * PlanOperator Interface Funktionen das Interface ist nur ein Dummy; die
	 * Return-Values sollten eigentlich nie benötigt werden...
	 */

	/* PlanOperator.open() */
	public void open(SimplePlanOperator caller) throws POException {
		//return true;
	}

	/* PlanOperator.next() */
	public Object next(SimplePlanOperator caller, long timeout) throws POException {
		return null;
	}

	/* PlanOperator.close() */
	public boolean close(SimplePlanOperator caller) throws POException {
		return true;
	}

    public String getPOName() {
        return "POMonitorTimerTask";
    }



}