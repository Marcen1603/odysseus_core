package de.uniol.inf.is.odysseus.incubation.graph.datatype;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.IClone;

public class Graph implements IClone, Cloneable {

	private String name;
	
	public String getName() {return name;}
	
	public Graph(Graph other){
		this(other.name);
	}
	
	public Graph(String name) {
		this.name = name;
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
	}

	public static Graph fromBuffer(ByteBuffer buffer) {
		Graph result = new Graph(new String(buffer.array()));
		
		return result;
	}

	@Override
	public String toString() {
		return "Graph [name=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Graph other = (Graph) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
