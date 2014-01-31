package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class GroupSplittFileWriter<R extends IStreamObject<?>> extends AbstractSink<R> {

	Map<Long,File> filemap = new HashMap<>();
	final String path;
	final IGroupProcessor<R, R> groupProcessor;
		
	
	public GroupSplittFileWriter(String path,
			IGroupProcessor<R, R> groupProcessor) {
		this.path = path;
		this.groupProcessor = groupProcessor;
	}

	@Override
	protected void process_next(R object, int port) {
		long id = groupProcessor.getGroupID(object);
		File file = filemap.get(id);
		if (file == null){
			file = new File(path+groupProcessor.toGroupString(object));
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractSink<R> clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
