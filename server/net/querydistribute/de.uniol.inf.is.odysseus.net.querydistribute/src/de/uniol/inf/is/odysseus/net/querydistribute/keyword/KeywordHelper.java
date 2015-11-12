package de.uniol.inf.is.odysseus.net.querydistribute.keyword;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public final class KeywordHelper {

	private KeywordHelper() {
	}

	public static List<String> generateParameterList(String[] splitted) {
		Preconditions.checkNotNull(splitted, "Splitted list of parameters must not be null!");

		if (splitted.length == 1) {
			return Lists.newArrayList();
		}

		List<String> parameters = Lists.newArrayList();
		for (int i = 1; i < splitted.length; i++) {
			parameters.add(splitted[i]);
		}

		return parameters;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IQueryBuildSetting<?>> Optional<T> getQueryBuildSettingOfType(List<IQueryBuildSetting<?>> settings, Class<T> parameterClass) {
		Preconditions.checkNotNull(settings, "List of settings must not be null!");
		Preconditions.checkNotNull(parameterClass, "Class of parameter to find must not be null!");
		
		for( IQueryBuildSetting<?> setting : settings ) {
			if( setting.getClass().equals(parameterClass)) {
				return (Optional<T>) Optional.of(setting);
			}
		}
		return Optional.absent();
	}

}
