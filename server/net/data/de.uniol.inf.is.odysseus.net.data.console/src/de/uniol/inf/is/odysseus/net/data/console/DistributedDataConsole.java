package de.uniol.inf.is.odysseus.net.data.console;

import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.container.IDistributedDataContainer;

public class DistributedDataConsole implements CommandProvider {

	private static IDistributedDataManager distributedDataManager;
	private static IDistributedDataContainer ddContainer;

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
	
	// called by OSGi-DS
	public static void bindDistributedDataContainer(IDistributedDataContainer serv) {
		ddContainer = serv;
	}

	// called by OSGi-DS
	public static void unbindDistributedDataContainer(IDistributedDataContainer serv) {
		if (ddContainer == serv) {
			ddContainer = null;
		}
	}
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Distributed Data Commands ---\n");
		return sb.toString();
	}

	public void _listDistributedData( CommandInterpreter ci ) {
		String filter = ci.nextArgument();
		
		Collection<String> names = ddContainer.getNames();
		if( names.isEmpty() ) {
			ci.println("Distributed DataContainer is empty");
			return;
		}
		
		for( String name : names ) {
			
			if( Strings.isNullOrEmpty(filter) || name.contains(filter)) {
				Collection<IDistributedData> datas = ddContainer.get(name);
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
