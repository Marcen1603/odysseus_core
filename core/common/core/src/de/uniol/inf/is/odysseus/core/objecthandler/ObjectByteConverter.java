package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

public final class ObjectByteConverter {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectByteConverter.class);

	private ObjectByteConverter() {
	}

	public static Object bytesToObject(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}

		final ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;
		try {
			in = new OsgiObjectInputStream(bis);
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("Could not read object", e);
		} finally {
			try {
				bis.close();
				if (in != null) {
					in.close();
				}
			} catch (final IOException ex) {
			}
		}

		return null;
	}

	public static byte[] objectToBytes(Object obj) {
		if (obj == null) {
			return new byte[0];
		}

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			return bos.toByteArray();
		} catch (final IOException e) {
			LOG.error("Could not convert object {} to byte array", obj, e);
			return new byte[0];
		} finally {
			tryClose(out);
			tryClose(bos);
		}
	}

	private static void tryClose(ObjectOutput out) {
		try {
			out.close();
		} catch (final IOException ex) {
		}
	}

	private static void tryClose(OutputStream out) {
		try {
			out.close();
		} catch (final IOException ex) {
		}
	}
}
