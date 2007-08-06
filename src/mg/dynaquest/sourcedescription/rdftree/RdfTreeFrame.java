package mg.dynaquest.sourcedescription.rdftree;


import com.hp.hpl.jena.rdf.model.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import mg.dynaquest.support.RDFHelper;
import mg.dynaquest.wrapper.access.*;

public class RdfTreeFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7494945101498968424L;

	/**
	 * @uml.property  name="imageHelp"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ImageIcon imageHelp = new ImageIcon(
			mg.dynaquest.sourcedescription.rdftree.RdfTreeFrame.class
					.getResource("help.gif"));

	/**
	 * @uml.property  name="imageClose"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ImageIcon imageClose = new ImageIcon(
			mg.dynaquest.sourcedescription.rdftree.RdfTreeFrame.class
					.getResource("closefile.gif"));

	/**
	 * @uml.property  name="imageOpen"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ImageIcon imageOpen = new ImageIcon(
			mg.dynaquest.sourcedescription.rdftree.RdfTreeFrame.class
					.getResource("openfile.gif"));

	/**
	 * @uml.property  name="buttonHelp"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton buttonHelp = new JButton();

	/**
	 * @uml.property  name="buttonClose"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton buttonClose = new JButton();

	/**
	 * @uml.property  name="buttonOpen"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton buttonOpen = new JButton();

	/**
	 * @uml.property  name="toolBar"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JToolBar toolBar = new JToolBar();

	/**
	 * @uml.property  name="statusBar"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JLabel statusBar = new JLabel();

	/**
	 * @uml.property  name="menuHelpAbout"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JMenuItem menuHelpAbout = new JMenuItem();

	/**
	 * @uml.property  name="menuHelp"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JMenu menuHelp = new JMenu();

	/**
	 * @uml.property  name="menuFileExit"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JMenuItem menuFileExit = new JMenuItem();

	/**
	 * @uml.property  name="menuFile"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JMenu menuFile = new JMenu();

	/**
	 * @uml.property  name="menuBar"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JMenuBar menuBar = new JMenuBar();

	/**
	 * @uml.property  name="panelCenter"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel panelCenter = new JPanel();

	/**
	 * @uml.property  name="layoutMain"
	 */
	private BorderLayout layoutMain = new BorderLayout();

	private HashSet<Resource> listOfUris = null;

	/**
	 * @uml.property  name="jScrollPane1"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JScrollPane jScrollPane1 = new JScrollPane();

	/**
	 * @uml.property  name="jTree1"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTree jTree1 = new JTree();

	/**
	 * @uml.property  name="defaultNameSpaceNames"
	 * @uml.associationEnd  qualifier="constant:java.lang.String java.lang.String"
	 */
	private HashMap<String, String> defaultNameSpaceNames = new HashMap<String, String>();

	/**
	 * @uml.property  name="lastURI"
	 */
	private String lastURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Source1.sdf";

	public RdfTreeFrame() {
		try {
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/test/Global.sdf#",
							"globalSchema");
			defaultNameSpaceNames.put(
					"http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
			defaultNameSpaceNames.put("http://www.w3.org/2000/01/rdf-schema#",
					"rdfs");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatypes.sdf#",
							"sdfDatatypes");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_descriptions.sdf#",
							"sdfDescriptions");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_extensional_descriptions.sdf#",
							"sdfExtensionalDescriptions");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#",
							"sdfFunctions");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_intensional_descriptions.sdf#",
							"sdfIntensionalDescriptions");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_mappings.sdf#",
							"sdfMappings");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_predicates.sdf#",
							"sdfPredicates");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_quality.sdf#",
							"sdfQuality");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_quality_descriptions.sdf#",
							"sdfQualityDescriptions");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_schema.sdf#",
							"sdfSchema");
			defaultNameSpaceNames
					.put(
							"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_units.sdf#",
							"sdfUnits");
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setJMenuBar(menuBar);
		this.getContentPane().setLayout(layoutMain);
		panelCenter.setLayout(null);
		this.setSize(new Dimension(554, 617));
		this.setTitle("RDF Tree");
		menuFile.setText("File");
		menuFileExit.setText("Exit");
		menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fileExit_ActionPerformed(ae);
			}
		});
		menuHelp.setText("Help");
		menuHelpAbout.setText("About");
		menuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				helpAbout_ActionPerformed(ae);
			}
		});
		statusBar
				.setText("Zum Einlesen eines Files auf Open klicken und URL angeben.");
		buttonOpen.setToolTipText("Open File");
		buttonOpen.setIcon(imageOpen);
		buttonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ea) {
				open_ActionPerformed(ea);
			}
		});
		buttonClose.setToolTipText("Close File");
		buttonClose.setIcon(imageClose);
		buttonHelp.setToolTipText("About");
		buttonHelp.setIcon(imageHelp);
		menuFile.add(menuFileExit);
		menuBar.add(menuFile);
		menuHelp.add(menuHelpAbout);
		menuBar.add(menuHelp);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		toolBar.add(buttonOpen);
		toolBar.add(buttonClose);
		toolBar.add(buttonHelp);
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(panelCenter, BorderLayout.WEST);
		jScrollPane1.getViewport().add(jTree1, null);
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void initTree(String baseURI) throws Exception {
		listOfUris = new HashSet<Resource>();
		TreeModel treeModel = createTreeModel(baseURI);

		jTree1.setModel(treeModel);

	}

	public String prettyPrint(Resource r) {
		return prettyPrint(r.toString());
	}

	public String prettyPrint(String URI) {
		int pos = URI.lastIndexOf("#");
		String elem = URI.substring(pos + 1);
		String pref = URI.substring(0, pos + 1);
		String xmlns = (String) defaultNameSpaceNames.get(pref);
		if (xmlns != null) {
			return xmlns + ":" + elem;
		}
		return URI;
	}

	TreeModel createTreeModel(String baseURI) throws RDFException,
			MalformedURLException, IOException, FileNotFoundException {
		this.defaultNameSpaceNames.put(baseURI + "#", "local");
		BufferedReader reader = HttpAccess.get(new URL(baseURI), 1);
        Model model = RDFHelper.readRDF(reader, baseURI);
		//Resource res =
		// SimpleTestLoader.getSourceDescriptionNode(model,baseURI);
		List<Resource> allRoots = RDFHelper.getRootNodes(model);
		DefaultMutableTreeNode rdfNode = new DefaultMutableTreeNode("rdf");
		for (int i = 0; i < allRoots.size(); i++) {
			System.out.println("Root-Knoten: " + allRoots.get(i));
			// Jetzt habe ich den obersten Knoten.
			Resource res = (Resource) allRoots.get(i);
			rdfNode.add(createTreeModel(res));
		}
		return new DefaultTreeModel(rdfNode);
	}

	DefaultMutableTreeNode createTreeModel(Resource r) throws RDFException {
		System.out.println(r.getURI() + " " + r.getId() + " " + r.toString());
		// Testen ob es die URI bereits gibt
		if (listOfUris.contains(r)) {

			return new DefaultMutableTreeNode("-->" + prettyPrint(r));
		}

		listOfUris.add(r);
		DefaultMutableTreeNode subjectNode = new DefaultMutableTreeNode(
				prettyPrint(r));
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object
			DefaultMutableTreeNode predNode = new DefaultMutableTreeNode(
					prettyPrint(predicate));
			subjectNode.add(predNode);
			if (object instanceof Resource) {
				predNode.add(createTreeModel((Resource) object));
			} else {
				predNode.add(new DefaultMutableTreeNode(prettyPrint(object
						.toString())));
			}
		}
		return subjectNode;
	}

	void fileExit_ActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	void helpAbout_ActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(this, new RdfTreeFrame_AboutBoxPanel1(),
				"About", JOptionPane.PLAIN_MESSAGE);
	}

	void open_ActionPerformed(ActionEvent e) {
		try {
			String inputValue = JOptionPane.showInputDialog(
					"Bitte URI angeben", lastURI);
			lastURI = inputValue;
			initTree(inputValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			RdfTreeFrame frame = new RdfTreeFrame();
			//    SwingUtilities.updateComponentTreeUI(this);
			//    this.pack();

			frame.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}

	}

}