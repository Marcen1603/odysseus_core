package de.uniol.inf.is.odysseus.incubation.graph.datatype;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.IClone;

public class Graph implements IClone, Cloneable {

	private String name;
	private String node1 = null;
	private String node2 = null;
	
	public String getName() {return name;}
	public String getNode1() {return node1;}
	public String getNode2() {return node2;}
	
	public Graph(Graph other){
		this(other.name);
	}
	
	public Graph(String name) {
		this.name = name;
	}
	
	public Graph(String name, String node1, String node2) {
		this.name = name;
		this.node1 = node1;
		this.node2 = node2;
	}
	
	@Override
	public Graph clone() {
		return this;
	}

	// Reads a graph from a stream.
	public static Graph fromStream(InputStream inputStream) throws IOException {
		DataInputStream stream = new DataInputStream(inputStream);
		Graph result = new Graph(stream.readUTF());
		return result;
	}

	public void appendToByteBuffer(ByteBuffer buffer) {
		buffer.put(name.getBytes());
		buffer.put(node1.getBytes());
		buffer.put(node2.getBytes());
	}

	public static Graph fromBuffer(ByteBuffer buffer) {
		Graph result = new Graph(new String(buffer.array()));
		
		return result;
	}
}
