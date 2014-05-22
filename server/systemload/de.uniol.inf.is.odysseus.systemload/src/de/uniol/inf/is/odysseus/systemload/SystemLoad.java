package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class SystemLoad implements ISystemLoad, Cloneable, Serializable  {

	private transient static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[]{ 
		ISystemLoad.class
	};
	private transient static final DecimalFormat TO_STRING_FORMATTER = new DecimalFormat("###.##");
	
	private final Map<String, SystemLoadEntry> systemLoads = Maps.newHashMap();
	
	public SystemLoad() {
		
	}
	
	public SystemLoad( SystemLoad copy ) {
		systemLoads.putAll(copy.systemLoads);
	}
	
	@Override
	public ISystemLoad clone() {
		return new SystemLoad(this);
	}
	
	@Override
	public void addSystemLoad( String name ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for system load must not be null or empty!");
		
		SystemLoadEntry entry = systemLoads.get(name);
		if( entry == null ) {
			entry = new SystemLoadEntry(name);
			systemLoads.put(name, entry);
		}
		
		entry.addSystemLoad();
	}
	
	void insert( SystemLoad other ) {
		for( String name : other.systemLoads.keySet() ) {
			if( systemLoads.containsKey(name)) {
				systemLoads.get(name).addExistingSystemLoadSnapshots(other.systemLoads.get(name).getSystemLoadSnapshots());
			} else {
				SystemLoadEntry e = new SystemLoadEntry(name);
				e.addExistingSystemLoadSnapshots(other.systemLoads.get(name).getSystemLoadSnapshots());
				systemLoads.put(name, e);
			}
		}
	}
	
	@Override
	public Collection<String> getSystemLoadNames() {
		return systemLoads.keySet();
	}
	
	@Override
	public List<SystemLoadSnapshot> getSystemLoadSnapshots( String name ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for system load must not be null or empty!");
		
		if( systemLoads.containsKey(name)) {
			return systemLoads.get(name).getSystemLoadSnapshots();
		}
		
		return Lists.newArrayList();
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public String getName() {
		return "SystemLoad";
	}

	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		StringBuilder sb = new StringBuilder();
		for( String name : systemLoads.keySet() ) {
			if( sb.length() > 0 ) {
				sb.append(delimiter);
			}
			
			List<SystemLoadSnapshot> loads = systemLoads.get(name).getSystemLoadSnapshots();
			double cpu = 0;
			double cpuMax = 0;
			double mem = 0;
			double memMax = 0;
			double netin = 0;
			double netout = 0;
			double netmax = 0;
			
			for( SystemLoadSnapshot load : loads ) {
				cpu += load.getCpuLoad();
				cpuMax += load.getCpuMax();
				mem += load.getMemLoad();
				memMax += load.getMemMax();
				netin += load.getNetInLoad();
				netout += load.getNetOutLoad();
				netmax += load.getNetMax();
			}
			
			int count = loads.size();
			
			if( numberFormatter != null ) {
				sb.append(numberFormatter.format(cpu / count)).append(delimiter);
				sb.append(numberFormatter.format(cpuMax / count)).append(delimiter);
				sb.append(numberFormatter.format(mem / count)).append(delimiter);
				sb.append(numberFormatter.format(memMax / count)).append(delimiter);
				sb.append(numberFormatter.format(netin / count)).append(delimiter);
				sb.append(numberFormatter.format(netout / count)).append(delimiter);
				sb.append(numberFormatter.format(netmax / count));

			} else {
				sb.append(TO_STRING_FORMATTER.format(cpu / count)).append(delimiter);
				sb.append(TO_STRING_FORMATTER.format(cpuMax / count)).append(delimiter);
				sb.append(TO_STRING_FORMATTER.format(mem / count)).append(delimiter);
				sb.append(TO_STRING_FORMATTER.format(memMax / count)).append(delimiter);
				sb.append(TO_STRING_FORMATTER.format(netin / count)).append(delimiter);
				sb.append(TO_STRING_FORMATTER.format(netout / count)).append(delimiter);
				sb.append(TO_STRING_FORMATTER.format(netmax / count));
			}
		}
		
		return sb.toString();
	}

	@Override
	public String getCSVHeader(char delimiter) {
		StringBuilder sb = new StringBuilder();
		
		for( String name : systemLoads.keySet() ) {
			if( sb.length() > 0 ) {
				sb.append(delimiter);
			}

			sb.append("cpu_").append(name).append(delimiter);
			sb.append("cpumax_").append(name).append(delimiter);

			sb.append("mem_").append(name).append(delimiter);
			sb.append("memmax_").append(name).append(delimiter);

			sb.append("netin_").append(name).append(delimiter);
			sb.append("netout_").append(name).append(delimiter);
			sb.append("netmax_").append(name);
		}
		
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{sysload:");
		for( String name : systemLoads.keySet() ) {
			
			List<SystemLoadSnapshot> loads = systemLoads.get(name).getSystemLoadSnapshots();
			sb.append(name).append("-[").append(loads.size()).append("]->{");
			double cpu = 0;
			double cpuMax = 0;
			double mem = 0;
			double memMax = 0;
			double netin = 0;
			double netout = 0;
			double netmax = 0;
			
			for( SystemLoadSnapshot load : loads ) {
				cpu += load.getCpuLoad();
				cpuMax += load.getCpuMax();
				mem += load.getMemLoad();
				memMax += load.getMemMax();
				netin += load.getNetInLoad();
				netout += load.getNetOutLoad();
				netmax += load.getNetMax();
			}
			
			int count = loads.size();
			String delimiter = ",";
			
			sb.append("cpu=").append(TO_STRING_FORMATTER.format(cpu / count)).append(delimiter);
			sb.append("cpuMax=").append(TO_STRING_FORMATTER.format(cpuMax / count)).append(delimiter);
			sb.append("mem=").append(TO_STRING_FORMATTER.format(mem / count)).append(delimiter);
			sb.append("memMax=").append(TO_STRING_FORMATTER.format(memMax / count)).append(delimiter);
			sb.append("netin=").append(TO_STRING_FORMATTER.format(netin / count)).append(delimiter);
			sb.append("netout=").append(TO_STRING_FORMATTER.format(netout / count)).append(delimiter);
			sb.append("netMax=").append(TO_STRING_FORMATTER.format(netmax / count));
			
			sb.append("}");
		}	
		sb.append("}");
		
		return sb.toString();
	}
}
