package de.uniol.inf.is.odysseus.iec61970;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.iec61970.dataaccessclient.CallBack;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;



/**
 * Planoperator für IEC61970-404(HSDA) und IEC61970-407(TSDA) in ODYSSEUS
 * @author Mart Köhler
 *
 * @param <W>
 */
public class IEC61970DataAccessPO<W extends IClone> extends AbstractPipe<ByteBuffer, W> {
	
	private IDataAccessHandler<W> handler;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4); 
	private int currentSize = 0;
	private IEC61970Router router;
	private String host;
	private int port;
	boolean opened;
	private boolean hMode;
	private ISession session;
	private IFacade facade;
	private List<String> pathnames;
	
	public IEC61970DataAccessPO(IDataAccessHandler<W> handler, String host, int port, ISession session, IFacade facade, List<String> pathnames) throws IOException{
		super();
		this.handler = (IDataAccessHandler<W>)handler.clone();
		router = IEC61970Router.getInstance();
		this.host = host;
		this.port = port;
		this.opened = false;
		this.session = session;
		this.facade = facade;
		this.pathnames = pathnames;
	}
	
	public IEC61970DataAccessPO(IEC61970DataAccessPO<W> daPO) {
		super();
		handler = (IDataAccessHandler<W>)daPO.handler.clone();
		size = daPO.size;
		currentSize = daPO.currentSize;
		router = daPO.router;
		host = daPO.host;
		port = daPO.port;
		opened = daPO.opened;
		session = daPO.session;
		facade = daPO.facade;
		pathnames = daPO.pathnames;
		
	}
	
	@Override
	protected ByteBuffer cloneIfNessessary(ByteBuffer object, boolean exclusive, int port) {
		return object;
	}
	
	@Override
	public boolean modifiesInput() {
		return false;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		if (!opened){
			try {
				router.connectToServer(this, host, port);
				opened = true;
				subscribe(pathnames);
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}
	
	@Override
	protected void process_done(int port) {
		opened = false;
		super.process_done(port);
	}
	
	@Override
	protected synchronized void process_next(ByteBuffer buffer, int port) {
		try {
			while (buffer.remaining() > 0){
				//Gesamtgröße des Objekts rausfinden
				if (size == -1){
					this.sizeBuffer.clear();
					while(sizeBuffer.position() < 4 && buffer.remaining() != 0){
						sizeBuffer.put(buffer.get());
					}
					if (sizeBuffer.position() == 4){
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						handler.setObjSize(size);
						System.out.println("Objekt Größe: "+size);
					}					
				}				
				if (size != -1 && buffer.remaining()>0){
					//Kleiner oder passt
					if (currentSize+buffer.remaining() <= size){
						currentSize = currentSize + buffer.remaining();
						//statt buildObject auf dem Server
							handler.put(buffer);
						//da wir alles komplett genommen haben, können wir alles zurücksetzen
						if(currentSize==size) {
	//							HSDAData data = getHSDAData(this.receiveObj);
							transfer();
	//							System.out.println("erzeuge Objekt: "+data.getId().getContainer()+" "+data.getId().getFragment());
						}
						//currentsize ist fest, aber remaining kann diesen Wert übertreffen durch in der zwischenzeit auftretende datenmengen und wir haben immernoch kein objekt
					}else{ 
						// Split
							handler.put(buffer, size-currentSize);
							transfer();
	//							HSDAData data = getHSDAData(this.receiveObj);
	//							System.out.println("erzeuge Objekt: "+data.getId().getContainer()+" "+data.getId().getFragment());
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(pathnames!=null) {
				for(String name: pathnames) {
					System.out.println("Unsubscribe: "+name);
				}
				close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private synchronized void transfer() throws IOException, ClassNotFoundException {
		W toTrans = handler.create();
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		currentSize = 0;
	}

	@Override
	public IEC61970DataAccessPO<W> clone() {
		return new IEC61970DataAccessPO<W>(this);
	}
	
	@Override
	public String toString() {
		return super.toString()+" "+host+" "+port;
	}
	
	public boolean subscribe(String pathname) {
		try {
			if(session.getGroups().isEmpty()) {
				session.createGroup("IECGroup", new CallBack("127.0.0.1",1201));
			}

			//getNode deswegen, weil es egal ist, ob die Methode von Node oder Item abgefragt wird
			//Außerdem haben wir nur eine Gruppe immer aktiv, deswegen get(0)
//			System.out.println("Subscription zu Objekt: "+getFacade().getNode().getPathnameId(pathname).getContainer()+" "+getFacade().getNode().getPathnameId(pathname).getFragment());
			session.getGroups().get(0).getGroupManager().createEntry(getFacade().getNode().getPathnameId(pathname));
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Subscription von Item Elementen. Wenn eine Subscription auf ein Node gemacht wird, dann wwerden automatisch alle Item Elemente in die Subscription aufgenommen
	 * @param pathnames
	 * @return
	 */
	public boolean subscribe(List<String> pathnames) {
		for(String path : pathnames) {
			if(!subscribe(path)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Unsubscribe wird auf allen Subscriptions getätigt
	 * @return
	 */
	public boolean unsubscribe(/*String pathname*/) {
		try {
			if(!session.getGroups().isEmpty()) {
				session.getGroups().get(0).getGroupManager().removeEntry();
				return true;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	public IFacade getFacade() {
		return facade;
	}
	
	/**
	 * Durch stoppen des Routers werden alle Aktivitäten beendet
	 */
	public void close() {
		//			if(!session.getGroups().isEmpty()) {
//				session.getGroups().get(0).getGroupManager().removeEntry();
//			}
		unsubscribe();
		router.stopRouting();
	}

}
