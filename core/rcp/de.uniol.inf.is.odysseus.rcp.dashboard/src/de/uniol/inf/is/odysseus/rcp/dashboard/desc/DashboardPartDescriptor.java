package de.uniol.inf.is.odysseus.rcp.dashboard.desc;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;

public final class DashboardPartDescriptor {
	
	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartDescriptor.class);

	private static final String DEFAULT_DESCRIPTION = "DashboardPart description for";
	
	private final Map<String, SettingDescriptor<?>> settings;
	private final String name;
	private final String description;
	private final Image image;
	
	public DashboardPartDescriptor( String name, String description ) {
		this(name, description, Lists.<SettingDescriptor<?>>newArrayList());
	}
	
	public DashboardPartDescriptor( String name, String description, List<SettingDescriptor<?>> settingDescriptors ) {
		this(name, description, settingDescriptors, null);
	}
	
	public DashboardPartDescriptor( String name, String description, Image image) {
		this(name, description, Lists.<SettingDescriptor<?>>newArrayList(), image);
	}
	
	public DashboardPartDescriptor( String name, String description, List<SettingDescriptor<?>> settingDescriptors, Image image ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name of DashboardPartDescriptor is null or empty!");
		this.name = name;
		
		if( Strings.isNullOrEmpty(description)) {
			LOG.warn("Description for DashboardPartDescriptor {} is null or empty.", name);
			description = DEFAULT_DESCRIPTION + " " + name;
		}
		this.description = description;
		
		if( settingDescriptors == null ) {
			LOG.warn("List of setting descriptors for DashboardPartDescriptor {} is empty.", name);
			settingDescriptors = Lists.newArrayList();
		}
		
		settings = createSettingsMap( settingDescriptors );
		
		this.image = image;
	}
	
	public ImmutableList<String> getSettingDescriptorNames() {
		return ImmutableList.copyOf(settings.keySet());
	}
	
	public Optional<SettingDescriptor<?>> getSettingDescriptor( String descName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(descName), "Name of SettingDescriptor is null or empty!");
		return Optional.<SettingDescriptor<?>>fromNullable(settings.get(descName));
	}
	
	public boolean hasSettingDescriptor( String descName ) {
		return settings.containsKey(descName);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean hasImage() {
		return image != null;
	}
	
	public Configuration createDefaultConfiguration() {
		return new Configuration();
	}

	private static Map<String, SettingDescriptor<?>> createSettingsMap(List<SettingDescriptor<?>> settingDescriptors) {
		Map<String, SettingDescriptor<?>> map = Maps.newHashMap();
		for( SettingDescriptor<?> settingDescriptor : settingDescriptors ) {
			map.put(settingDescriptor.getName(), settingDescriptor);
		}
		return map;
	}
}
