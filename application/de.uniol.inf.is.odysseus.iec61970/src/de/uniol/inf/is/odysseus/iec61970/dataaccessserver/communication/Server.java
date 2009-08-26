package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser.Facade;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.ResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.DescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.daf_services.descriptions.ResourceDescription;
import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.services.PathnameService;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IServer;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;
/**
 * Die Server Klasse lässt sich aus der Main Klasse heraus starten.
 *  Sie ist die HAuptklasse des Projektes
 *  Initialisierungen der Dienste wie RMI,ResourceIDService, Provider gestartet und ein zu übergebenes Datenmodell, CIM, und Instanzen werden übergeben.
 * @author Mart Köhler
 * 
 */
public class Server extends UnicastRemoteObject implements IServer {
	// RMI Registry globaler Zugriff
	public static Registry registry = null;
	// Globaler Zugriff auf die Registry über JNDI
	public static InitialContext ictx = null;
	public static String host;
	public static String owlPropPath;
	public static OntModel model;
	public static OntModel helper;
	public static String protocolNS;
	public static String instanceNS;
	public static boolean hMode;
	private ArrayList<ISession> sessions;
	private Provider provider;
	public static String cimNS;
	private String serverStartTime;
	
	public Server(String ontConfigPath) throws RemoteException {
		super();
		this.serverStartTime = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.sessions = new ArrayList<ISession>();
		Server.registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		// JNDI Kontext setzen
		Hashtable<String, String> hashtable = new Hashtable<String, String>(2);
		hashtable.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.rmi.registry.RegistryContextFactory");
		hashtable.put(Context.PROVIDER_URL, "rmi://" + host + ":1099");
		try {
			Server.ictx = new InitialContext(hashtable);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		Server.model = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM, null );
		Server.helper = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM, null );
		setOwlPropPath(ontConfigPath);
        Properties owlProp = new Properties();
        try {
			owlProp.loadFromXML(new FileInputStream(getOwlPropPath()));
		} catch (FileNotFoundException e) {
			System.err.println("Property Datei fehlt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Konfiguration wird eingelesen
		for(Object key : owlProp.keySet()) {
			if(!((String)key).equals("instance") && !((String)key).equals("protocolNamespace") && !((String)key).equals("instanceNamespace") && !((String)key).equals("cimNamespace")){
				model.getDocumentManager().addAltEntry((String)key,owlProp.getProperty((String)key));
				model.read((String)key);
			}
		}
		//Namespace Global zur Verfügung stellen
		Server.cimNS = owlProp.getProperty("cimNamespace");
		Server.protocolNS = owlProp.getProperty("protocolNamespace");
		Server.instanceNS = owlProp.getProperty("instanceNamespace");
		InputStream in = FileManager.get().open(owlProp.getProperty("instance"));
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: "+owlProp.getProperty("instance")+" from config: "+getOwlPropPath()+" not found");
		}
		// Instanzen aus File einlesen
		Server.model.read(in, "");
		//Initialisieren der ganzen Descriptions
		ResourceDescription.getInstance();
		// Pathnames in das Modell integrieren
		PathnameService.getInstance().appendPathnames(model);
		// Kommunikations Service muss noch für die Übetragung gestartet werden, damit wir den Transfer regeln können
		System.out.println("TransferService wird gestartet...");
		try {
			this.provider = new Provider(this,InetAddress.getByName(host),1200);
			Thread t = new Thread(provider);
			t.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//für jedes ITemAttribute suchen wir das item
//		for(IResourceID id :ResourceIDService.getInstance().getItemAttributeGroup()) {
//			IItemAttribute attr = new ItemAttribute();
//			System.out.println("zu AttributItem"+id.getContainer()+" "+id.getFragment()+" haben wir attributeitem: "+attr.getReferencingItem(id).getContainer()+" "+attr.getReferencingItem(id).getFragment());
//		}
//		for(IResourceID item : ResourceIDService.getInstance().getItemGroup()) {
//			IItemAttribute attr = new ItemAttribute();
//			for(IResourceID id : attr.findByItem(item)) {
//			}
//		}
		
	}
	

	public static void print(Statement stmt) {
		System.out.println(stmt.getSubject().toString()+" ; "+stmt.getPredicate().toString()+" ; "+stmt.getObject().toString());
	}

	protected void showStructure(StmtIterator iterator) {
		Statement stmt = null;
		Resource subject = null;
		Property predicate = null;
		RDFNode object = null;
		long counter = 0L;
		while(iterator.hasNext()) {
			stmt = iterator.nextStatement();
			subject = stmt.getSubject();
			predicate = stmt.getPredicate();
			object = stmt.getObject();
			System.out.println(counter+++" Subject:"+subject.toString()+" ; "+predicate.toString()+" ; "+object.toString());
		}
	}
	protected void showPredicate(StmtIterator iterator) {
		Statement stmt = null;
		Property predicate = null;
		while(iterator.hasNext()) {
			stmt = iterator.nextStatement();
			predicate = stmt.getPredicate();
			System.out.println("Prädikat: "+predicate.toString());
		}
	}
	/**
	 * 
	 * @param args
	 *            args[0] Parameter wird die Codebase übergeben und mit
	 *            args[1] die aktuelle IP des Hosts
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		if (args.length > 0) {
			if(args[0].equals("hsda")) {
				Server.hMode = true;
			}
			else if(args[0].equals("tsda")){
				Server.hMode = false;
			}
			else {
				System.out.println("1. Argument ist keine gültige Spezifikation gewählt (hsda|tsda)");
			}
		}
		if (args.length > 1) {
			System.setProperty("java.security.policy", args[1]);
		} else {
			System.out.println("2. Argument fehlt (Pfad zur Policy)");
			System.exit(0);
		}
		if (args.length > 2) {
			Server.host = args[2];
		} else {
			System.out.println("3. Argument fehlt (Hostname)");
			System.exit(0);
		}
		if (args.length <4) {
			System.out.println("4. Argument Pfad zur Ontologie Konfiguration fehlt");
			System.exit(0);
		}
		System.setSecurityManager(new RMISecurityManager());
		IServer serverImpl = new Server(args[3]);
//		RemoteServer.setLog(System.out);
		try {
			System.out.println("bind: " + "rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/");
			Server.ictx.rebind("rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/", serverImpl);
			System.out.println("bind: " + "rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/ResourceIDService/");
			Server.ictx.rebind("rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/ResourceIDService/", ResourceIDService.getInstance());
			System.out.println("bind: " + "rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/ResourceDescriptionService/");
			Server.ictx.rebind("rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/ResourceDescriptionService/", DescriptionStorage.getInstance());
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		//integrierter Server zur Generierung von Daten
//		SourceServer reader = null;
//		try {
//			reader = new SourceServer(InetAddress.getByName("localhost"),1200);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		new Thread(reader).start();
	}


	@Override
	/**
	 * Eine neue Session wird erzeugt und in der rmiregistry registriert.
	 * Eine zu der derzeitigen Spezifikation passende Facade wird erzeugt
	 * 
	 */
	public void createDataAccessSession() throws RemoteException {
		ISession s = new Session();
		this.sessions.add(s);
		try {
			System.out.println("bind:" + "rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/session/");
			Server.ictx.rebind("rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/session/", s);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		createFacade();
	}

	@Override
	public ISession createDataAccessSessionForView() throws RemoteException {
		return null;
	}

	@Override
	public String findViews() throws RemoteException {
		return null;
	}


	@Override
	public String getServerUpTime() throws RemoteException {
		return null;
	}
	public static String getOwlPropPath() {
		return owlPropPath;
	}

	public static void setOwlPropPath(String owlPropPath) {
		Server.owlPropPath = owlPropPath;
	}
	
	/**
	 * Hier werden alle Schnittstellenmethoden mal getestet
	 * @throws RemoteException
	 */
//	public void testing() throws RemoteException{
//		IResourceIDService service = ResourceIDService.getInstance();
//		ArrayList<IResourceID> types = service.getTypeGroup();
//		for(IResourceID type : types) {
//			System.out.println("Type ID: C: " +type.getContainer()+" F:"+type.getFragment());
//		}
//		ArrayList<IResourceID> nodes = service.getNodeGroup();
//		for(IResourceID node : nodes) {
//			System.out.println("Node ID: C: " +node.getContainer()+" F:"+node.getFragment());
//		}
//		ArrayList<IResourceID> items = service.getItemGroup();
//		for(IResourceID item : items) {
//			System.out.println("Item ID: C: " +item.getContainer()+" F:"+item.getFragment());
//		}
////		for(IResourceID id : service.getNodeGroup()) {
////			System.out.println("container: "+id.getContainer()+" fragment: "+id.getFragment());
////		}
//		INode node = new Node();
//		IResourceDescription desc = node.find(new ResourceID(1L,1L));
//		if(desc==null) {
//			System.out.println("das ist schlecht");
//		}
//		else {
//			System.out.println("das ist gut :-) ich hab nämlich was gefunden");
//		}
//		System.out.println("reingegeben: "+ResourceIDService.getInstance().getURI(new ResourceID(1L,1L)));
//		IResourceDescriptionIterator<IResourceDescription> resIter = node.findByParent(new ResourceID(1L,1L));
//		System.out.println("erhalten: ");
//		while(resIter.hasNext()) {
//			IResourceDescription description = resIter.next();
//			System.out.println("zu erhalten:"+ResourceIDService.getInstance().getURI(description.getResource()).toString());
//		}
//		List<String> list = new ArrayList<String>();
//		list.add("IECTC57PhysicalView.Company.HydroPowerPlant.StringMeasurement.Messwert.value");
//		List<IResourceID> ids = node.getIds(list);
//		if(ids.isEmpty()) {
//			System.out.println(":-((((hmm");
//		}
//		else {
//			for(IResourceID id : ids) {
//				System.out.println("Ergebnis ist: "+id.getContainer()+" "+id.getFragment());
//			}
//			System.out.println(":-))))");
//		}
//		List<IResourceID> liste = new ArrayList<IResourceID>();
//		liste.add(new ResourceID(1L,20L));
//		List<String> pNames = node.getPathnames(liste);
//		if(pNames.isEmpty()) {
//			System.out.println(":-((((");
//		}
//		else {
//			System.out.println(":-))))");
//		}
//		System.out.println("reingegeben: "+ResourceIDService.getInstance().getURI(new ResourceID(2L,18L)));
//		IResourceDescriptionIterator<IResourceDescription> findByType = node.findByType(new ResourceID(2L,18L));
//		System.out.println("erhalten: ");
//		while(findByType.hasNext()) {
//			IResourceDescription description = findByType.next();
//			System.out.println("zu erhalten:"+ResourceIDService.getInstance().getURI(description.getResource()).toString());
//		}
////		Appender.getInstance().appendPathnames(Server.getModel());
////		Resource res = Appender.getInstance().findRoot(Server.getModel());
////		System.out.println("ROOOOOOT: "+res.toString());
//	}


	public void createFacade() throws RemoteException {
		//Services und Facades für Client werden erzeugt und registriert
		try {
			IFacade facade = new Facade();
//				IHSDAFacade facade = new HSDAFacade();
				System.out.println("Facade wird bereitgestellt...");
				System.out.println("bind: " + "rmi://" + Server.host+ ":" + "1099"
				+ "/hsda_tsda_server/Facade/");
					Server.ictx.rebind("rmi://" + Server.host + ":" + "1099"
					+ "/hsda_tsda_server/Facade/", facade);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}



	public ArrayList<ISession> getSessions() {
		return sessions;
	}


	@Override
	public String getServerStartTime() throws RemoteException {
		return this.serverStartTime;
	}
	public static OntModel getModel() {
		return model;
	}


	@Override
	public boolean getTransferMode() throws RemoteException {
		return hMode;
	}

}
