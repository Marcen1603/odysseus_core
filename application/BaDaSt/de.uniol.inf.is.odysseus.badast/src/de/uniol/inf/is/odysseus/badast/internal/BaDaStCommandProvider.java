package de.uniol.inf.is.odysseus.badast.internal;

import java.util.Map;

import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.badast.ABaDaStReader;
import de.uniol.inf.is.odysseus.badast.IBaDaStReader;
import de.uniol.inf.is.odysseus.badast.readers.BaDaStFileReader;

// TODO javaDoc
public class BaDaStCommandProvider implements CommandProvider {

	private static final Map<String, IBaDaStReader<?>> cReaders = Maps.newHashMap();

	static {
		IBaDaStReader<String> fileReader = new BaDaStFileReader();
		cReaders.put(BaDaStFileReader.class.getAnnotation(ABaDaStReader.class).type(), fileReader);
	}

	// TODO bindReader
	// TODO unbindReader
	// TODO _lsReaderTypes
	// TODO _lsReaders
	// TODO _createReader
	// TODO _readerUsage
	// TODO _createReader
	// TODO _startReader
	// TODO _closeReader

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}