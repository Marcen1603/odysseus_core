package de.uniol.inf.is.odysseus.iec61970.dataaccessclient.handler;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import de.uniol.inf.is.odysseus.iec61970.DataAccessHandler;
import de.uniol.inf.is.odysseus.iec61970.HSDASink;
import de.uniol.inf.is.odysseus.iec61970.IDataAccessHandler;
import de.uniol.inf.is.odysseus.iec61970.IEC61970DataAccessPO;
import de.uniol.inf.is.odysseus.iec61970.TSDASink;
import de.uniol.inf.is.odysseus.iec61970.dataaccessclient.Client;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.HSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.TSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.base.OpenFailedException;

/**
 * Führt hier bei einer Subscription in der GUI die nötigen Schritte aus, um den entsprechenden Operator zu starten
 * @author Mart Köhler
 *
 */
public class ActionHandler {
	private IDataAccessHandler handler;
	private String host;
	private int port;
	private ISession session;
	private IFacade facade;
	private IEC61970DataAccessPO po = null;
	private ISink sink;
	private TableModel tableModel;
	
	public IDataAccessHandler createHandler() {
		if(Client.hMode) {
			handler = new DataAccessHandler<HSDAObject>();
		}
		else{
			handler = new DataAccessHandler<TSDAObject>();
		}
		return handler;
	}
	
	public ActionHandler(String host, int port, ISession session, IFacade facade) {
		this.host = host;
		this.port = port;
		this.session = session;
		this.facade = facade;
	}

	public void subscribe(ArrayList<String> pathnames) {
		if(po!=null) {
			unsubscribe();
		}
		try {
			System.err.println("Bauen einen neuen operator");
			po = new IEC61970DataAccessPO<DataAccessHandler>(createHandler(),host, port, session, facade, pathnames);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(!po.isOpen()) {
			try {
				po.open();
			} catch (OpenFailedException e) {
				e.printStackTrace();
			}
		}
		if(Client.hMode) {
			System.out.println("HSDASink");
			HSDASink<HSDAObject> tempSink = new HSDASink<HSDAObject>();
			tempSink.setTableModel(this.tableModel);
			tempSink.setFacade(facade);
			sink = tempSink;
		}
		else {
			System.out.println("TSDASink");
			TSDASink<TSDAObject> tempSink = new TSDASink<TSDAObject>();
			tempSink.setTableModel(this.tableModel);
			tempSink.setFacade(facade);
			sink = tempSink;
		}
		po.subscribe(sink, port);
		
	}
	public synchronized void unsubscribe() {
		System.out.println("Unsubscribe");
		if(po!=null) {
			po.close();
			po.done(port);
			po = null;
			System.gc();
		}
//		try {
//			//Macht Probleme, deswegen entfernen wir den direkt vom Provider
////			server.getProvider().removeCallBack(host, port);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	public void setTableModel(TableModel queryresultTableModel) {
		this.tableModel = queryresultTableModel;
	}

}
