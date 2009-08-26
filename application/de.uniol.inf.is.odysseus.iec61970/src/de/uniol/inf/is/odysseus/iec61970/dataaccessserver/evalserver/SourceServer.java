package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.evalserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.HSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.ResourceID;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.TSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IServer;

/**
 * Implementierung eines Datenstroms, der Daten an den Server reicht
 * Diese Klasse dient zur Evaluation des DataAccessServers und DataAccessClients
 * 
 * @author Mart Köhler
 *
 */
public class SourceServer implements Runnable{
	private BufferedReader readFile = null;
	private SocketChannel socketChannel = null;
	private ArrayList<Long> ResourceIDContainer;
	private ArrayList<Long> ResourceIDFragment;
	private ArrayList<String> quality;
	private ArrayList<String> value;
	private InitialContext ictx = null;
	private IResourceIDService service = null;
	private IServer server = null;
	private Registry registry = null;
	public long sent = 0;
	private boolean hMode;
	
	public SourceServer(InetAddress host, int port) {
		try {
			 System.setProperty("java.security.policy", "/home/mart/Development/security.policy");
			 this.registry = LocateRegistry.getRegistry(host.getHostAddress(), 1099);
			 Hashtable hashtable = new Hashtable(2);
			 hashtable.put (Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.rmi.registry.RegistryContextFactory");
			 hashtable.put (Context.PROVIDER_URL,"rmi://"+host.getHostAddress()+":1099");
			ictx = new InitialContext(hashtable);
			this.service = (IResourceIDService) ictx.lookup("rmi://" + host.getHostAddress() + ":" + "1099" + "/hsda_tsda_server/ResourceIDService/");
			this.server = (IServer) ictx.lookup("rmi://" + host.getHostAddress() + ":" + "1099"
					+ "/hsda_tsda_server/");
			this.hMode = this.server.getTransferMode();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(host, port));
			socketChannel.finishConnect();
			
			this.ResourceIDContainer = new ArrayList<Long>();
			this.ResourceIDFragment = new ArrayList<Long>();
			this.quality = new ArrayList<String>();
			this.value = new ArrayList<String>();
			
			
			this.ResourceIDContainer.add(1L);
//			this.ResourceIDContainer.add(2L);
//			this.ResourceIDContainer.add(3L);
//			this.ResourceIDContainer.add(4L);
			for(Long i=14L; i<16L; i++) {
				this.ResourceIDFragment.add(i);
				this.value.add(i.toString());
			}
			for(Long i=1L; i<100L; i++) {
				this.value.add(i.toString());
			}
			
			this.quality.add("Good");
			this.quality.add("Bad");
			this.hMode = hMode;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	 public static byte[] getBytes(Object obj) throws java.io.IOException{
	      ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	      ObjectOutputStream oos = new ObjectOutputStream(bos); 
	      oos.writeObject(obj);
	      oos.flush(); 
	      oos.close(); 
	      bos.close();
	      byte [] data = bos.toByteArray();
	      return data;
	 }
	 
	 /**
	  * Generiert TSDA Daten
	  * 
	  * @return TSDAObject
	  */
	 public TSDAObject generateTSDAData() {
			Collections.shuffle(this.ResourceIDContainer);
			Collections.shuffle(this.ResourceIDFragment);
			Collections.shuffle(this.value);
			Collections.shuffle(this.quality);
			IResourceID resID = null;
			try {
				resID = new ResourceID(this.ResourceIDContainer.get(0),this.ResourceIDFragment.get(0));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			try {
				while(true) {
					if(service.getURI(resID)==null) {
						Collections.shuffle(this.ResourceIDContainer);
						Collections.shuffle(this.ResourceIDFragment);
						resID = new ResourceID(this.ResourceIDContainer.get(0),this.ResourceIDFragment.get(0));
					}
					else {
						break;
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			ArrayList<Object> valueList = new ArrayList<Object>();
			ArrayList<String> qualityList = new ArrayList<String>();
			ArrayList<String> timestampList = new ArrayList<String>();
			ArrayList<ArrayList> list = new ArrayList<ArrayList>();
			for(int i=0; i<4; i++) {
				Collections.shuffle(this.value);
				Collections.shuffle(this.quality);
				valueList.add(this.value.get(0));
				qualityList.add(this.quality.get(0));
				timestampList.add((new Timestamp(Calendar.getInstance().getTimeInMillis()).toString()));
			}
			list.add(valueList);
			list.add(timestampList);
			list.add(qualityList);
			
			return new TSDAObject (resID,list);
		}
	 
	 
	 /**
	  * Generiert HSDA Daten
	  * @return HSDAObject
	  */
	public HSDAObject generateHSDAData() {
		Collections.shuffle(this.ResourceIDContainer);
		Collections.shuffle(this.ResourceIDFragment);
		Collections.shuffle(this.value);
		Collections.shuffle(this.quality);
		IResourceID resID = null;
		try {
			resID = new ResourceID(this.ResourceIDContainer.get(0),this.ResourceIDFragment.get(0));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		try {
			while(true) {
				if(service.getURI(resID)==null) {
					Collections.shuffle(this.ResourceIDContainer);
					Collections.shuffle(this.ResourceIDFragment);
					resID = new ResourceID(this.ResourceIDContainer.get(0),this.ResourceIDFragment.get(0));
				}
				else {
					break;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return new HSDAObject (resID,this.value.get(0),this.quality.get(0), (new Timestamp(Calendar.getInstance().getTimeInMillis()).toString()));
	}
	
	@Override
	public void run() {
		ByteBuffer buffer =  ByteBuffer.allocate(8192);
		ByteBuffer sizeBuffer =  ByteBuffer.allocate(4);
		while(true) {
			try {
				synchronized (this) {
					this.wait(1000);
				}
			} catch (InterruptedException e) {
				System.out.println("SourceServer gestoppt");
				break;
			}
			try {
				byte[] dataBytes = null;
				if(hMode) {
					HSDAObject data = generateHSDAData();
					System.out.println("Sende HSDA Objekt: "+data.getId().getContainer()+" "+data.getId().getFragment());
					dataBytes = getBytes(data);
				}
				else {
					TSDAObject data = generateTSDAData();
					System.out.println("Sende TSDA Objekt: "+data.getId().getContainer()+" "+data.getId().getFragment());
					dataBytes = getBytes(data);
				}
					sizeBuffer.clear();
					sizeBuffer.putInt(dataBytes.length);
					buffer.put(dataBytes);
					buffer.flip();
					sizeBuffer.flip();
					socketChannel.write(sizeBuffer);
					int nw = socketChannel.write(buffer); // Bytes senden					
					sent = sent + nw;
					buffer.clear();
			} catch (IOException e1) {
				System.out.println("SourceServer gestoppt");
				break;
			}
		}
			
	}

	public static void main(String[] args) {
		try {
			SourceServer reader = new SourceServer(InetAddress.getByName("localhost"),1200);
			new Thread(reader).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
