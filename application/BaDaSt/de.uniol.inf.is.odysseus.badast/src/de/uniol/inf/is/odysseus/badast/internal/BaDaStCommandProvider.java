package de.uniol.inf.is.odysseus.badast.internal;

import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.badast.ABaDaStReader;
import de.uniol.inf.is.odysseus.badast.IBaDaStReader;
import de.uniol.inf.is.odysseus.badast.readers.BaDaStFileReader;
import de.uniol.inf.is.odysseus.core.collection.IPair;

// TODO javaDoc
public class BaDaStCommandProvider implements CommandProvider {

	private static final Map<String, IBaDaStReader<?>> cReaderTypes = Maps.newHashMap();

	static {
		IBaDaStReader<String> fileReader = new BaDaStFileReader();
		cReaderTypes.put(BaDaStFileReader.class.getAnnotation(ABaDaStReader.class).type(), fileReader);
	}

	public static void bindReader(IBaDaStReader<?> reader) {
		cReaderTypes.put(reader.getClass().getAnnotation(ABaDaStReader.class).type(), reader);
	}

	public static void unbindReader(IBaDaStReader<?> reader) {
		cReaderTypes.remove(reader.getClass().getAnnotation(ABaDaStReader.class).type());
	}

	private static final Map<String, IPair<String, IBaDaStReader<?>>> cReaders = Maps.newHashMap();

	public void _lsReaderTypes(CommandInterpreter ci) {
		ci.println("Available types of BaDaSt readers:\n");
		for (String type : cReaderTypes.keySet()) {
			ci.print(type + ": ");
			String[] params = cReaderTypes.get(type).getClass().getAnnotation(ABaDaStReader.class).parameters();
			for (int i = 0; i < params.length; i++) {
				ci.print(params[i]);
				if (i < params.length - 1) {
					ci.print(", ");
				}
			}
			ci.println();
		}
	}

	public void _lsReaders(CommandInterpreter ci) {
		ci.println("Available BaDaSt readers:\n");
		if (cReaders.isEmpty()) {
			ci.println("None");
			return;
		}
		for (String name : cReaders.keySet()) {
			ci.println(name + " (" + cReaders.get(name).getE1() + ")");
		}
	}

	// TODO _createReader
	// TODO _startReader
	// TODO _closeReader

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}