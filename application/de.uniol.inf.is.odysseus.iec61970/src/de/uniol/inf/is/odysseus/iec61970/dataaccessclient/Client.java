package de.uniol.inf.is.odysseus.iec61970.dataaccessclient;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.SwingUtilities;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.uniol.inf.is.odysseus.iec61970.dataaccessclient.gui.ApplicationGui;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.IClient;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.IShutdownCallBack;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IDescriptionStorage;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceDescriptionIterator;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IServer;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;

/**
 * Hauptklasse des Clients/Adapters für ODYSSEUS 
 * 
 * @author Mart Köhler
 * 
 */
public class Client implements IClient {
	public static Registry registry = null;
	public static InitialContext ictx = null;
	public static String serverHost;
	private ArrayList<IShutdownCallBack> shutdown;
	private ArrayList<ICallBack> callBack;
	private ArrayList<ISession> sessions;
	private IServer server;
	private IFacade facade = null;
	private static IResourceIDService idService;
	private static IDescriptionStorage descService;
	public static boolean hMode;

	public Client() {
		this.callBack = new ArrayList<ICallBack>();
		this.shutdown = new ArrayList<IShutdownCallBack>();
		this.sessions = new ArrayList<ISession>();
		// Kontext setzen
		 Hashtable hashtable = new Hashtable(2);
		 hashtable.put (Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.rmi.registry.RegistryContextFactory");
		 hashtable.put (Context.PROVIDER_URL,"rmi://"+serverHost+":1099");
		 try {
			Client.ictx = new InitialContext(hashtable);
			setServer((IServer) Client.ictx.lookup("rmi://"+serverHost+":1099"+"/hsda_tsda_server/"));
			//Transfer Modus ermitteln
			hMode = getServer().getTransferMode();
			idService = (IResourceIDService) ictx.lookup("rmi://" + serverHost + ":" + "1099" + "/hsda_tsda_server/ResourceIDService/");
			descService = (IDescriptionStorage) ictx.lookup("rmi://" + serverHost + ":" + "1099" + "/hsda_tsda_server/ResourceDescriptionService/");
			//wir wollen eine konstante Verbindung aufbauen und später Subscriptions machen
			//Durch das erzeugen einer Session können wir auf die Facade zugreifen
			// Wir missbrauchen aber bei der ersten Erzeugung des Session die Schnittstellen, um ein Mapping auf SDF zu realisieren. Danach brauchen wir die Facade quasi nciht mehr, weil
			// wir alle Ressourcen kennen und einen Zugriff direkt über Subscription realisieren können.
			getServer().createDataAccessSession();
			//Erst nach erfolgreicher Erstellung einer Session kann das Facade Objekt abgeholt werden.
			ISession s = (ISession) Client.ictx.lookup("rmi://"+serverHost+":1099"+"/hsda_tsda_server/session/");
			facade = (IFacade) Client.ictx.lookup("rmi://"+serverHost+":1099"+"/hsda_tsda_server/Facade/");
			getSessions().add(s);
			System.out.println("ODYSSEUS connect");
			initGui();
		} catch (RemoteException e) {
			System.out.println("Host Server nicht erreichbar...");
			System.exit(0);
		} catch (NamingException e) {
			System.out.println("Host Server nicht erreichbar...");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("I/O Fehler...");
			System.exit(0);
		}
	}
	

	private void initGui() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ApplicationGui inst = new ApplicationGui(facade, idService, serverHost, 1201, getSessions().get(0));
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}


	public static void main(String[] args) {
		if (args.length > 0) {
			System.setProperty("java.security.policy", args[0]);
		}
		else {
			System.out.println("1. Argument: Pfad zur Policy fehlt");
			System.exit(0);	
		}
		try {
			if(args.length >1) {
				serverHost = args[1];
				Client.registry = LocateRegistry.getRegistry(serverHost, 1099);
			} else {
				System.out.println("2. Argument: Hostadresse zur Server Registrierung fehlt");
				System.exit(0);
			}
		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.setSecurityManager(new RMISecurityManager());
		Client client = new Client();
	}
	public static void showDescription(List<IResourceID> list,IResourceDescriptionIterator<StmtIterator> desc) throws RemoteException {
		for(IResourceID id : list) {
			System.out.println("Node Container: "+id.getContainer()+" Fragment: "+id.getFragment());
		}
		while(desc.hasNext()) {
			StmtIterator rdesc = desc.next();
			while(rdesc.hasNext()) {
				Statement stmt = rdesc.nextStatement();
				System.out.println("Statement: "+stmt);
			}
		}
	}

	public ArrayList<IShutdownCallBack> getShutdown() {
		return shutdown;
	}

	public void setShutdown(ArrayList<IShutdownCallBack> shutdown) {
		this.shutdown = shutdown;
	}

	public ArrayList<ICallBack> getCallBack() {
		return callBack;
	}

	public void setCallBack(ArrayList<ICallBack> callBack) {
		this.callBack = callBack;
	}

	public ArrayList<ISession> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<ISession> sessions) {
		this.sessions = sessions;
	}

	public IServer getServer() {
		return server;
	}

	public void setServer(IServer server) {
		this.server = server;
	}
}
