package de.uniol.inf.is.odysseus.net.data.console;

import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class DistributedDataConsole implements CommandProvider {

	private static IDistributedDataManager distributedDataManager;

	// called by OSGi-DS
	public static void bindDistributedDataManager(IDistributedDataManager serv) {
		distributedDataManager = serv;
	}

	// called by OSGi-DS
	public static void unbindDistributedDataManager(IDistributedDataManager serv) {
		if (distributedDataManager == serv) {
			distributedDataManager = null;
		}
	}
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Distributed Data Commands ---\n");
		sb.append("  listDistributedData/ls... <filter>                      - Lists stored data in distributed data container\n");
		return sb.toString();
	}

	public void _listDistributedData( CommandInterpreter ci ) {
		String filter = ci.nextArgument();
		
		if( distributedDataManager == null ) {
			ci.println("Distributed data manager not available");
			return;
		}
		
		Collection<String> names = distributedDataManager.getNames();
		if( names.isEmpty() ) {
			ci.println("Distributed data container is empty");
			return;
		}
		
		for( String name : names ) {
			
			if( Strings.isNullOrEmpty(filter) || name.contains(filter)) {
				Collection<IDistributedData> datas = distributedDataManager.get(name);
				if( !datas.isEmpty() ) {
					ci.println(name);
					for( IDistributedData data : datas ) {
						ci.print("\t");
						ci.print(data.getUUID());
						ci.print(":");
						ci.println(data.getData().toString());
					}
					ci.println();
				}
			}
		}
	}
	
	public void _lsDistributedData( CommandInterpreter ci ) {
		_listDistributedData(ci);
	}
}
