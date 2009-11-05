package de.uniol.inf.is.odysseus.iec61970.dataaccessclient;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.client.service.IShutdownCallBack;
public class CallBack extends UnicastRemoteObject implements ICallBack{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1656176217561007838L;
	public static int bufferSize = 1024;
	private IShutdownCallBack shutdownCallback = null;
	private long benchMilliSek;
	private long benchCounter = 0L;
	private ArrayList<Float> benchValues;
	private String connection;
	private int port;
	public CallBack(String connection, int port) throws RemoteException {
		super();
		this.port = port;
		this.benchValues = new ArrayList<Float>();
		this.connection = connection;
	}


	/**
	 * Interne Benchmarkroutine, um die Performance verschiedener Stream Techniken zu messen im Callback.
	 * Dabei sind die Werte, nicht exakt, da Nebenläufigkeiten nicht betrachtet werden.
	 */
	@Deprecated
	protected void benchmark() {
		if((getBenchMilliSek()+1000l)<=System.currentTimeMillis()) {
			setBenchMilliSek(System.currentTimeMillis());
			getBenchValues().add(new Float(bufferSize*getBenchCounter()/1000000));
			setBenchCounter(0);					
		}
	}
	
	public void print(String message) throws RemoteException {
		System.out.println(message);
	}
	

		
	

	public ArrayList<Float> getBenchValues() {
		return benchValues;
	}
	@Deprecated
	public void setBenchValues(ArrayList<Float> benchValues) {
		this.benchValues = benchValues;
	}
	@Deprecated
	public long getBenchMilliSek() {
		return benchMilliSek;
	}

	public void setBenchMilliSek(long benchMilliSek) {
		this.benchMilliSek = benchMilliSek;
	}

	public long getBenchCounter() {
		return benchCounter;
	}

	public void setBenchCounter(long benchCounter) {
		this.benchCounter = benchCounter;
	}


	public IShutdownCallBack getShutdownCallback() {
		return shutdownCallback;
	}

	public void setShutdownCallback(IShutdownCallBack shutdownCallback) {
		this.shutdownCallback = shutdownCallback;
	}






	@Override
	public String getConnectionAddress() throws RemoteException {
		return connection;
	}


	@Override
	public int getConnectionPort() throws RemoteException {
		return port;
	}


}
