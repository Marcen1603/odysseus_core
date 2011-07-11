package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;

public interface IStreamHandler<T>{

	void transfer(T object) throws IOException;
	void done()throws IOException;;

}
