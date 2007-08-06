package mg.dynaquest.gui.composer;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/22 12:22:03 $ 
 Version: $Revision: 1.22 $
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import mg.dynaquest.queryexecution.po.access.HttpAccessPO;
import mg.dynaquest.queryexecution.po.access.PhysicalAccessPO;

import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;
import mg.dynaquest.queryexecution.po.base.PhysicalCollectorPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.convert.AraneusConvertPO;
import mg.dynaquest.queryexecution.po.relational.FileDumperPO;
import mg.dynaquest.queryexecution.po.relational.SelectLoaderPO;
import mg.dynaquest.queryexecution.po.relational.SortMemPO;
import mg.dynaquest.queryexecution.po.relational.SortMergeJoinPO;
import mg.dynaquest.queryexecution.po.relational.XJoinPO;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import mg.dynaquest.queryexecution.po.xml.XMLDocumentAccessPO;
import mg.dynaquest.queryexecution.po.xml.XPathProjectionPO;
import mg.dynaquest.wrapper.access.HttpAccess;
import mg.dynaquest.wrapper.access.HttpAccessParams;

// import java.awt.BorderLayout;

public class TreeComposer extends JFrame {

	// private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6247038941515352526L;

	public TreeComposer() {
		super("TreeComposer and Viewer for DynaQuest");
		try {
			// TEst fuer das CVS
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void test10(String[] args) {

		// MyTimer timer = new MyTimer(); // initialisiere Stoppuhr
		String basedir = "";
		int[] restrToAttribs = { 0, 1 };
		int partSize = 550;
		// int time = 500;
		int time = -1;
		long mainTime = 0;
		long start, stop;

		start = System.currentTimeMillis();
		PhysicalAccessPO actorsAccess = new SelectLoaderPO(
				basedir + "testfile1.loader", '|', restrToAttribs);

		// actorsAccess.setDebug(true);
		// actorsAccess.setMaxElementsBufferSize(200);
		actorsAccess.setPOName("LOADER TESTFILE 1");
		// actorsAccess.start();

		PhysicalAccessPO biographiesAccessUns = new SelectLoaderPO(basedir
				+ "testfile2.loader", '|', restrToAttribs);

		// biographiesAccessUns.setDebug(true);
		// biographiesAccessUns.setMaxElementsBufferSize(200);
		biographiesAccessUns.setPOName("LOADER TESTFILE 2");
		// biographiesAccessUns.start();

		/*
		 * PhysicalAccessPO actorsAccess = new SelectLoaderPO(basedir +
		 * "ausleihe.loader", '|', restrToAttribs); actorsAccess.start();
		 * 
		 * PhysicalAccessPO biographiesAccessUns = new SelectLoaderPO(basedir +
		 * "bibliothek.loader", '|', restrToAttribs);
		 * biographiesAccessUns.start();
		 */
		// System.err.println("Bin hier");
		RelationalTupleCorrelator compareAttrs = new RelationalTupleCorrelator(1);
		compareAttrs.setPair(0, 0, 0);

		// BinaryPlanOperator joinActors = new HashJoinPO(compareAttrs);
		// BinaryPlanOperator joinActors = new HybridHashJoinPO(compareAttrs,
		// partSize);
		// BinaryPlanOperator joinActors = new DoublePipeJoinPO(compareAttrs,
		// time);
		// BinaryPlanOperator joinActors = new
		// RippleJoinPO(compareAttrs,partSize,time);
		BinaryPlanOperator joinActors = new XJoinPO(compareAttrs, partSize,
				time);

		joinActors.setMaxElementsBufferSize(200);
		joinActors.setLeftInput(actorsAccess);
		joinActors.setRightInput(biographiesAccessUns);
		// joinActors.setDebug(true);
		joinActors.start();
		joinActors.setPOName("JOIN");

		PlanOperator top = joinActors;
		Object obj = null;
		Object test = new Object();
		PhysicalCollectorPO dummy = new PhysicalCollectorPO();
		dummy.setPOName("WUME");
		try {
			top.open(dummy);
			int count = 0;
			do {
				try {
					obj = top.next(dummy, time);
				} catch (TimeoutException e) {
					obj = test;

				}
				if (obj != test && obj != null) {
					System.out.println(obj);
					stop = System.currentTimeMillis();
					mainTime = mainTime + (stop - start);
					System.out.println("Zeit: " + mainTime);
					if (count == 10) {
						System.out.println("Zeit erste Zehn: " + mainTime);
					}
					start = System.currentTimeMillis();
					count++;
				}
			} while (obj != null);

			top.close(dummy);
			stop = System.currentTimeMillis();
			System.out.println("Gesamtlaufzeit: " + mainTime);
			System.out.println("Gesamtergebnisse: " + count);
		} catch (Exception e) {
			e.printStackTrace();

		}
		System.exit(0);
	}

	public static void test1(String[] args) {
		UIManager.put("mg.dynaquest.composer.JPlanOperatorUI",
				"mg.dynaquest.composer.JBasicPlanOperatorUI");
		UIManager.put("mg.dynaquest.composer.JPOConnectionUI",
				"mg.dynaquest.composer.JBasicPOConnectionUI");
		JFrame f = new TreeComposer();
		f.setBounds(100, 100, 700, 480);
		f.getContentPane().setLayout(null);
		f.getContentPane().setBackground(new Color(230, 230, 230));
		JScrollPane jsp = new JScrollPane();
		f.getContentPane().add(jsp);
		// / Container cont = f.getContentPane();

		Container cont = new Container();
		cont.setBounds(0, 0, 1000, 1000);
		jsp.setViewportView(cont);

		jsp.setBounds(0, 0, 600, 380);
		jsp.setVisible(true);
		// Container cont = jsp.getViewport();
		jsp.getViewport().setSize(1000, 1000);
		jsp.getViewport().setOpaque(false);


		String basedir = "d:\\diplom\\DynaQuest\\";
		// String basedir = "d:\\Development\\database\\oraload\\";
		int[] restrToAttribs = { 0, 1 };

		PhysicalAccessPO actorsAccess = new SelectLoaderPO(basedir + "ausleihe.loader",
				'|', restrToAttribs);
		actorsAccess.start();
		JPlanOperator actorsAccessVIS = new JPlanOperator("actorsAccess",
				(TriggeredPlanOperator) actorsAccess);

		cont.add(actorsAccessVIS);

		PhysicalAccessPO biographiesAccessUns = new SelectLoaderPO(basedir
				+ "bibliothek.loader", '|', null);
		biographiesAccessUns.start();
		JPlanOperator biographiesAccessUnsVIS = new JPlanOperator(
				"biographiesAccessUns",
				(TriggeredPlanOperator) biographiesAccessUns);

		cont.add(biographiesAccessUnsVIS);

		UnaryPlanOperator biographiesAccess = new SortMemPO();
		biographiesAccess.start();
		JPlanOperator biographiesAccessVIS = new JPlanOperator(
				"biographiesAccess", (TriggeredPlanOperator) biographiesAccess);

		cont.add(biographiesAccessVIS);

		biographiesAccess.setInputPO(biographiesAccessUns);
		JPOConnection conn1 = new JPOConnection(biographiesAccessUnsVIS,
				biographiesAccessVIS, 0);
		conn1.setBounds(0, 0, 1000, 1000);
		cont.add(conn1);

		UnaryPlanOperator inputSortAccess = new SortMemPO();
		inputSortAccess.start();
		inputSortAccess.setInputPO(actorsAccess);
		JPlanOperator inputSortAccessVIS = new JPlanOperator("inputSortAccess",
				(TriggeredPlanOperator) inputSortAccess);

		cont.add(inputSortAccessVIS);

		JPOConnection conn2 = new JPOConnection(actorsAccessVIS,
				inputSortAccessVIS, 0);
		conn2.setBounds(0, 0, 1000, 1000);
		cont.add(conn2);

		RelationalTupleCorrelator compareAttrs = new RelationalTupleCorrelator(1);
		compareAttrs.setPair(0, 0, 0);

//		int outputAttributeCount = 9;
//		boolean leftOuterJoin = true;
//		boolean rightOuterJoin = true;
		BinaryPlanOperator joinActors = new SortMergeJoinPO(compareAttrs);
		joinActors.start();
		JPlanOperator joinActorsVIS = new JPlanOperator("joinActors",
				(TriggeredPlanOperator) joinActors);

		cont.add(joinActorsVIS);

		joinActors.setLeftInput(biographiesAccess);
		joinActors.setRightInput(inputSortAccess);

		JPOConnection conn3 = new JPOConnection(biographiesAccessVIS,
				joinActorsVIS, 0);
		conn3.setBounds(0, 0, 1000, 1000);
		cont.add(conn3);

		JPOConnection conn4 = new JPOConnection(inputSortAccessVIS,
				joinActorsVIS, 1);
		conn4.setBounds(0, 0, 1000, 1000);
		cont.add(conn4);

		// UnaryPlanOperator dump = new
		// FileDumperPO(basedir+"alleSchauspielerBio.loader",false);
		// dump.setInputPO(joinActors);

		// NAryPlanOperator top = joinActors;
		//
		// StringBuffer xmlRep = new StringBuffer();
		// xmlRep.append("<?xml version='1.0'?>\n");
		// xmlRep.append("<plan>\n");
		// top.getXMLRepresentation(" "," ",xmlRep);
		//		    
		// xmlRep.append("</plan>\n");
		//
		// System.out.println(xmlRep.toString());

		//RelationalTuple tupel = null;

		// Pazieren der Operatoren (wie im Baum, von oben nach unten)
		Dimension dim = null;

		dim = joinActorsVIS.getPreferredSize();
		joinActorsVIS.setBounds(300, 0, dim.width, dim.height);

		dim = biographiesAccessVIS.getPreferredSize();
		biographiesAccessVIS.setBounds(50, 150, dim.width, dim.height);

		dim = inputSortAccessVIS.getPreferredSize();
		inputSortAccessVIS.setBounds(500, 150, dim.width, dim.height);

		dim = biographiesAccessUnsVIS.getPreferredSize();
		biographiesAccessUnsVIS.setBounds(50, 300, dim.width, dim.height);

		dim = actorsAccessVIS.getPreferredSize();
		actorsAccessVIS.setBounds(500, 300, dim.width, dim.height);

		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.repaint();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

		// try{
		// joinActorsVIS.execute();
		// }catch(POException e){
		// System.out.println(e.getMessage());
		// e.printStackTrace();
		// }

	}

	public static void test2(String[] args) {
		UIManager.put("mg.dynaquest.composer.JPlanOperatorUI",
				"mg.dynaquest.composer.JBasicPlanOperatorUI");
		UIManager.put("mg.dynaquest.composer.JPOConnectionUI",
				"mg.dynaquest.composer.JBasicPOConnectionUI");
		JFrame f = new TreeComposer();
		f.setBounds(100, 100, 700, 480);
		Container cont = f.getContentPane();
		cont.setLayout(null);
		cont.setBackground(new Color(230, 230, 230));


		String basedir = "e:\\testdata\\";

		String[] files = { basedir + "dblp.xml" };

		// 1. Zugriffsoperator

		XMLDocumentAccessPO actorFile = new XMLDocumentAccessPO(files);
		actorFile.start();
		JPlanOperator actorsAccessVIS = new JPlanOperator("dbFile",
				(TriggeredPlanOperator) actorFile);

		cont.add(actorsAccessVIS);

		// 2.XPath-Operator
		XPathProjectionPO project = new XPathProjectionPO("/article");
		project.start();
		project.setInputPO(actorFile);
		JPlanOperator projectVIS = new JPlanOperator("project",
				(TriggeredPlanOperator) project);

		cont.add(projectVIS);

		JPOConnection conn2 = new JPOConnection(actorsAccessVIS, projectVIS, 0);
		cont.add(conn2);
		conn2.setBounds(0, 0, 1000, 1000);

		UnaryPlanOperator dump = new FileDumperPO(basedir + "article.xml",
				false);
		dump.setInputPO(project);
		dump.start();

		JPlanOperator dumpVIS = new JPlanOperator("dump",
				(TriggeredPlanOperator) dump);

		cont.add(dumpVIS);

		JPOConnection conn3 = new JPOConnection(projectVIS, dumpVIS, 0);
		cont.add(conn3);
		conn3.setBounds(0, 0, 1000, 1000);

		// Pazieren der Operatoren (wie im Baum, von oben nach unten)
		Dimension dim = null;

		dim = dumpVIS.getPreferredSize();
		dumpVIS.setBounds(50, 10, dim.width, dim.height);

		dim = projectVIS.getPreferredSize();
		projectVIS.setBounds(50, 150, dim.width, dim.height);

		dim = actorsAccessVIS.getPreferredSize();
		actorsAccessVIS.setBounds(50, 300, dim.width, dim.height);

		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.repaint();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public static void test3(String[] args) throws Exception {
		UIManager.put("mg.dynaquest.composer.JPlanOperatorUI",
				"mg.dynaquest.composer.JBasicPlanOperatorUI");
		UIManager.put("mg.dynaquest.composer.JPOConnectionUI",
				"mg.dynaquest.composer.JBasicPOConnectionUI");
		JFrame f = new TreeComposer();
		f.setBounds(100, 100, 700, 480);
		Container cont = f.getContentPane();
		cont.setLayout(null);
		cont.setBackground(new Color(230, 230, 230));

		// 1. Zugriffsoperator
		HttpAccessParams imdbParams = new HttpAccessParams();
		imdbParams.setURL("http://uk.imdb.com/Name?Willis,+Bruce");
		imdbParams.setGet();
		HttpAccessPO imdb = new HttpAccessPO();
		imdb.start();
		imdb.appendHttpAccessParams(imdbParams);

		JPlanOperator imdbAccessVIS = new JPlanOperator(
				"IMDB SchauspielerSuche", (TriggeredPlanOperator) imdb);
		cont.add(imdbAccessVIS);

		// 2. Converter
		AraneusConvertPO convert = new AraneusConvertPO();
		convert.start();
		convert.setConvertClassName("mg.dynaquest.wrapper.imdb.IMDB_PersonPage");
		convert.setStartMethodeName("IMDB_PersonPage");
		convert.setInputPO(imdb);
		convert.setNOOfConsumerPOs(2);

		JPlanOperator convertVIS = new JPlanOperator("SchauspielerConverter",
				(TriggeredPlanOperator) convert);
		cont.add(convertVIS);

		JPOConnection conn2 = new JPOConnection(imdbAccessVIS, convertVIS, 0);
		cont.add(conn2);
		conn2.setBounds(0, 0, 1000, 1000);

		// 3.XPath-Operator (1)
		XPathProjectionPO project = new XPathProjectionPO(
				"/person/filmographie[@type='actor']/movie/title");
		project.start();
		project.setInputPO(convert);
		JPlanOperator projectVIS = new JPlanOperator("Title-Projection",
				(TriggeredPlanOperator) project);

		cont.add(projectVIS);

		JPOConnection conn3 = new JPOConnection(convertVIS, projectVIS, 0);
		cont.add(conn3);
		conn3.setBounds(0, 0, 1000, 1000);

		// 3.XPath-Operator (2)
		XPathProjectionPO project2 = new XPathProjectionPO(
				"/person/filmographie[@type='actor']/movie/title");
		project2.start();
		project2.setInputPO(convert);
		JPlanOperator project2VIS = new JPlanOperator("Title-Projection2",
				(TriggeredPlanOperator) project2);

		cont.add(project2VIS);

		JPOConnection conn4 = new JPOConnection(convertVIS, project2VIS, 0);
		cont.add(conn4);
		conn4.setBounds(0, 0, 1000, 1000);

		// 4. Collector, der beide wieder zusammenführt
		PhysicalCollectorPO collect = new PhysicalCollectorPO();
		collect.start();
		collect.setNoOfInputs(2);
		collect.setInputPO(0, project);
		collect.setInputPO(1, project2);

		JPlanOperator collectVIS = new JPlanOperator("Collector", collect);
		cont.add(collectVIS);

		JPOConnection conn5 = new JPOConnection(projectVIS, collectVIS, 0);
		cont.add(conn5);
		conn5.setBounds(0, 0, 1000, 1000);
		JPOConnection conn6 = new JPOConnection(project2VIS, collectVIS, 1);
		cont.add(conn6);
		conn6.setBounds(0, 0, 1000, 1000);

		// Pazieren der Operatoren (wie im Baum, von oben nach unten)
		Dimension dim = null;

		int xBase = 10;
		int xpos = xBase;

		dim = collectVIS.getPreferredSize();
		collectVIS.setBounds(50, xpos, dim.width, dim.height);

		xpos += 100;
		dim = projectVIS.getPreferredSize();
		projectVIS.setBounds(50, xpos, dim.width, dim.height);

		dim = project2VIS.getPreferredSize();
		project2VIS.setBounds(250, xpos, dim.width, dim.height);

		xpos += 100;
		dim = convertVIS.getPreferredSize();
		convertVIS.setBounds(50, xpos, dim.width, dim.height);

		xpos += 100;
		dim = imdbAccessVIS.getPreferredSize();
		imdbAccessVIS.setBounds(50, xpos, dim.width, dim.height);

		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.repaint();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public static void test4(String[] args) throws Exception {
		UIManager.put("mg.dynaquest.composer.JPlanOperatorUI",
				"mg.dynaquest.composer.JBasicPlanOperatorUI");
		UIManager.put("mg.dynaquest.composer.JPOConnectionUI",
				"mg.dynaquest.composer.JBasicPOConnectionUI");
		JFrame f = new TreeComposer();
		f.setBounds(100, 100, 350, 750);
		Container cont = f.getContentPane();
		cont.setLayout(null);
		cont.setBackground(new Color(230, 230, 230));

		// 1. Zugriffsoperator
		HttpAccessParams imdbParams = new HttpAccessParams();
		imdbParams.setURL("http://uk.imdb.com/Find");
		imdbParams.setPost();
		imdbParams.appendParam("select", "People");
		imdbParams.appendParam("for", "Hanks");
		HttpAccessPO imdb = new HttpAccessPO();
		imdb.start();
		imdb.appendHttpAccessParams(imdbParams);
		imdb.setNOOfConsumerPOs(2);

		JPlanOperator imdbAccessVIS = new JPlanOperator("IMDB PersonenSuche",
				(TriggeredPlanOperator) imdb);
		cont.add(imdbAccessVIS);

		// 2. Converter
		AraneusConvertPO convert = new AraneusConvertPO();
		convert.start();
		convert.setConvertClassName("mg.dynaquest.wrapper.imdb.IMDB_PersonList");
		convert.setStartMethodeName("IMDB_PersonList");
		convert.setInputPO(imdb);
		convert.setNOOfConsumerPOs(1);

		JPlanOperator convertVIS = new JPlanOperator("ResultsetConverter",
				(TriggeredPlanOperator) convert);
		cont.add(convertVIS);

		JPOConnection conn2 = new JPOConnection(imdbAccessVIS, convertVIS, 0);
		cont.add(conn2);
		conn2.setBounds(0, 0, 1000, 1000);

		// 3.XPath-Operator (1)
		XPathProjectionPO project = new XPathProjectionPO(
				"/result/group[@role='Actor' or @role='Actress']/person/url");
		project.start();
		project.setInputPO(convert);
		JPlanOperator projectVIS = new JPlanOperator("URL-Projection",
				(TriggeredPlanOperator) project);

		cont.add(projectVIS);

		JPOConnection conn3 = new JPOConnection(convertVIS, projectVIS, 0);
		cont.add(conn3);
		conn3.setBounds(0, 0, 1000, 1000);

		// Zugriffsoperator, der die Seiten anhand der übergebenen Parameter
		// ausliest
		HttpAccessPO imdb_link = new HttpAccessPO();
		imdb_link.start();
		imdb_link.setNoOfInputs(1);
		imdb_link.setInputPO(0, project);
		imdb_link.setBaseURL("http://uk.imdb.com");

		JPlanOperator imdb_linkAccessVIS = new JPlanOperator(
				"IMDB PersonenZugriff", (TriggeredPlanOperator) imdb_link);
		cont.add(imdb_linkAccessVIS);

		JPOConnection conn4 = new JPOConnection(projectVIS, imdb_linkAccessVIS,
				0);
		cont.add(conn4);
		conn4.setBounds(0, 0, 1000, 1000);

		// Hier zwischen muss erst mal kombiniert werden

		PhysicalCollectorPO collect = new PhysicalCollectorPO();
		collect.start();
		collect.setNoOfInputs(2);
		collect.setInputPO(0, imdb);
		collect.setInputPO(1, imdb_link);

		JPlanOperator collectVIS = new JPlanOperator("Collector", collect);
		cont.add(collectVIS);

		JPOConnection conn5 = new JPOConnection(imdbAccessVIS, collectVIS, 0);
		cont.add(conn5);
		conn5.setBounds(0, 0, 1000, 1000);
		JPOConnection conn6 = new JPOConnection(imdb_linkAccessVIS, collectVIS,
				1);
		cont.add(conn6);
		conn6.setBounds(0, 0, 1000, 1000);

		// Converter
		AraneusConvertPO convert2 = new AraneusConvertPO();
		convert2.start();
		convert2.setConvertClassName("mg.dynaquest.wrapper.imdb.IMDB_PersonPage");
		convert2.setStartMethodeName("IMDB_PersonPage");
		convert2.setInputPO(collect);

		JPlanOperator convert2VIS = new JPlanOperator("SchauspielerConverter",
				(TriggeredPlanOperator) convert2);
		cont.add(convert2VIS);

		JPOConnection conn15 = new JPOConnection(collectVIS, convert2VIS, 0);
		cont.add(conn15);
		conn15.setBounds(0, 0, 1000, 1000);

		// Pazieren der Operatoren (wie im Baum, von oben nach unten)
		Dimension dim = null;

		int xBase = 10;
		int xpos = xBase;

		dim = convert2VIS.getPreferredSize();
		convert2VIS.setBounds(100, xpos, dim.width, dim.height);

		xpos += 100;
		dim = collectVIS.getPreferredSize();
		collectVIS.setBounds(100, xpos, dim.width, dim.height);

		xpos += 100;
		dim = imdb_linkAccessVIS.getPreferredSize();
		imdb_linkAccessVIS.setBounds(150, xpos, dim.width, dim.height);

		xpos += 100;
		dim = projectVIS.getPreferredSize();
		projectVIS.setBounds(150, xpos, dim.width, dim.height);

		xpos += 100;
		dim = convertVIS.getPreferredSize();
		convertVIS.setBounds(150, xpos, dim.width, dim.height);

		xpos += 100;
		dim = imdbAccessVIS.getPreferredSize();
		imdbAccessVIS.setBounds(50, xpos, dim.width, dim.height);

		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.repaint();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public static void test5(String[] args) throws Exception {
		UIManager.put("mg.dynaquest.composer.JPlanOperatorUI",
				"mg.dynaquest.composer.JBasicPlanOperatorUI");
		UIManager.put("mg.dynaquest.composer.JPOConnectionUI",
				"mg.dynaquest.composer.JBasicPOConnectionUI");
		JFrame f = new TreeComposer();
		f.setBounds(100, 100, 700, 480);
		Container cont = f.getContentPane();
		cont.setLayout(null);
		cont.setBackground(new Color(230, 230, 230));


		String loginHttp = "http://www.adac.de/login/_FrameLogin.asp?id=5&AuthPage=logout/default.asp";
		HttpAccessParams loginParams = new HttpAccessParams();
		loginParams.setURL(loginHttp);
		loginParams.appendParam("name", "Marco Grawunder");
		loginParams.appendParam("membernumber", "204264643");
		BufferedReader in = HttpAccess.post(new URL(loginHttp), loginParams
				.toString(), 1);
		System.out.println(in);

		// String baseurl =
		// "http://www.adac.de/Auto_Motorrad/Autokatalog/default.asp";
//		String params = "autokatmode=2&Page=9&selOrder=autokatalog.hersteller,Hersteller";
		String basedir = "./";

		// 1. Zugriffsoperator

		HttpAccessPO adacAccessPO = new HttpAccessPO();
		// adacAccessPO.setBaseURL(baseurl);

		HttpAccessParams accessParams = new HttpAccessParams();
		accessParams.setGet();
		accessParams
				.setURL("http://www.adac.de/Auto_Motorrad/Autokatalog/default.asp");
		accessParams.appendParam("autokatmode", "2");
		accessParams.appendParam("Page", "9");
		accessParams.appendParam("selOrder",
				"autokatalog.hersteller,Hersteller");

		adacAccessPO.appendHttpAccessParams(accessParams);
		adacAccessPO.start();
		JPlanOperator adacAccessPOVIS = new JPlanOperator("adacAccessPO",
				(TriggeredPlanOperator) adacAccessPO);

		cont.add(adacAccessPOVIS);

		UnaryPlanOperator dump = new FileDumperPO(basedir + "auto.html", false);
		dump.setInputPO(adacAccessPO);
		dump.start();

		JPlanOperator dumpVIS = new JPlanOperator("dump",
				(TriggeredPlanOperator) dump);

		cont.add(dumpVIS);

		JPOConnection conn3 = new JPOConnection(adacAccessPOVIS, dumpVIS, 0);
		cont.add(conn3);
		conn3.setBounds(0, 0, 1000, 1000);

		// Pazieren der Operatoren (wie im Baum, von oben nach unten)
		Dimension dim = null;

		dim = dumpVIS.getPreferredSize();
		dumpVIS.setBounds(50, 10, dim.width, dim.height);

		dim = adacAccessPOVIS.getPreferredSize();
		adacAccessPOVIS.setBounds(50, 150, dim.width, dim.height);

		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.repaint();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

	}



	public static void main(String[] args) {
		try {

			// ACHTUNG!! TESTS FUNKTIONIEREN IM MOMENT NOCH NICHT
			// WIEDER!

			 test10(args);
			// test2(args);
			// test3(args);
			// test4(args);
			// test5(args);

			// test6(args);
			// test7(args);
			// testBeanInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {

	}

}