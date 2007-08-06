package mg.dynaquest.queryexecution.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

/**
 * @author  Marco Grawunder
 */
public class FifoObjectBuffer {

	/**
	 * @uml.property  name="rafile"
	 */
	private RandomAccessFile rafile = null;

	/**
	 * @uml.property  name="currWritePos"
	 */
	private long currWritePos = 10;

	/**
	 * @uml.property  name="currReadPos"
	 */
	private long currReadPos = 10;

	/**
	 * @uml.property  name="noOfObjects"
	 */
	private int noOfObjects = 0;

	public synchronized void put(Object object) throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream(1000);
		ObjectOutputStream oos = new ObjectOutputStream(bao);
		oos.writeObject(object);
		oos.flush();
		rafile.seek(currWritePos);
		byte[] array = bao.toByteArray();
		rafile.writeInt(array.length);
		rafile.write(array);
		currWritePos = currWritePos + 8 + array.length;
		notify();
		noOfObjects++;
	}

	public synchronized Object get() throws IOException, ClassNotFoundException {
		while (currReadPos > rafile.length()) {
			try {
				System.out.println("FileObjectBuffer --> warte");
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		rafile.seek(currReadPos);
		int size = rafile.readInt();
		byte[] array = new byte[size];
		rafile.read(array);
		ByteArrayInputStream bai = new ByteArrayInputStream(array);
		ObjectInputStream ois = new ObjectInputStream(bai);
		Object obj = ois.readObject();
		currReadPos = currReadPos + 8 + size;
		noOfObjects--;
		return obj;
	}

	public FifoObjectBuffer(String filename) {
		try {
			rafile = new RandomAccessFile(filename, "rw");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			FifoObjectBuffer fifo = new FifoObjectBuffer("egal__1.buf");
			fifo.put("Marco Grawunder");
			System.out.println(fifo.get());
			fifo.put("Marco Grawunder");
			fifo.put("Tjorven Grawunder");
			fifo.put("Silke Grawunder");
			System.out.println(fifo.get());
			System.out.println(fifo.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return  the noOfObjects
	 * @uml.property  name="noOfObjects"
	 */
	public int getNoOfObjects() {
		return noOfObjects;
	}

}