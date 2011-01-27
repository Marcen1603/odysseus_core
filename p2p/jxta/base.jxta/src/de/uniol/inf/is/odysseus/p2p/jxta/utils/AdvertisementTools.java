package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64InputStream;

public class AdvertisementTools {
	
	public static PipeAdvertisement getServerPipeAdvertisement(PeerGroup netPeerGroup) {
		ID pipeId = IDFactory.newPipeID(netPeerGroup.getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(pipeId);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("serverPipe");
		return advertisement;
	}
	
	public static PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;

		socketID = (PipeID) IDFactory.newPipeID(PeerGroupTool.getPeerGroup().getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("P2PPipe Verbindung");

		return advertisement;
	}
	
	public static String toBase64String(Object object){
		try {
			return new String(toBase64(object).toByteArray(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ByteArrayOutputStream toBase64(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		Base64OutputStream b64os = null;
		try {

			b64os = new Base64OutputStream(bos);
			oos = new ObjectOutputStream(b64os);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			if (oos != null) {
				oos.writeObject(object);
				b64os.flush();
				oos.flush();
				b64os.close();
				oos.close();
				bos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos;
	}
	
	public static Object fromBase64String(String text) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					text.getBytes("utf-8"));
			Base64InputStream b64in = new Base64InputStream(bis);
			ObjectInputStream ois = new ObjectInputStream(b64in);
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return obj;
	}


	
}
