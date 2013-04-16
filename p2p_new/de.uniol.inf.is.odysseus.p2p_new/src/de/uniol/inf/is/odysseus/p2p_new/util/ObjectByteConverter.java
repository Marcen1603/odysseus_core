package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ObjectByteConverter {
	
	private static final Logger LOG = LoggerFactory.getLogger(ObjectByteConverter.class);

	private ObjectByteConverter() {
	}
	
	public static byte[] objectToBytes(Object obj) {
		if( obj == null ) {
			return new byte[0];
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException e) {
			LOG.error("Could not convert object {} to byte array", obj, e);
			return new byte[0];
		} finally {
			try {
				out.close();
				bos.close();
			} catch (IOException e) {
			}
		}
	}
		
	public static Object bytesToObject( byte[] data ) {
		if( data == null || data.length == 0 ) {
			return null;
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  return in.readObject(); 
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("Could not read object", e);
		} finally {
			try {
				if( bis != null  ) {
					bis.close();
				}
				if( in != null ) {
					in.close();
				}
			} catch( IOException ex ) {
			}
		}
		
		return null;
	}
}
