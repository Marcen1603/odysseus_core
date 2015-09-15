package de.uniol.inf.is.odysseus.iql.basic.ui.wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.osgi.framework.Bundle;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;


public class IQLProjectCreator extends org.eclipse.xtext.ui.wizard.AbstractPluginProjectCreator {

	protected static final String SRC_ROOT = "src";
	protected static final String SRC_GEN_ROOT = "src-gen";
	protected final List<String> SRC_FOLDER_LIST = ImmutableList.of(SRC_ROOT, SRC_GEN_ROOT);

	@Inject
	private IIQLTypeDictionary typeDictionary;
	
	@Override
	protected IQLProjectInfo getProjectInfo() {
		return (IQLProjectInfo) super.getProjectInfo();
	}
	
	protected String getModelFolderName() {
		return SRC_ROOT;
	}
	
	@Override
	protected List<String> getAllFolders() {
        return SRC_FOLDER_LIST;
    }
	
    protected List<String> getImportedPackages() {
		Set<String> result = new HashSet<>();
		for (String p : typeDictionary.getImportedPackages()) {
			result.add(p);
		}		
		for (IIQLDependenciesProvider provider : IQLDependenciesProviderServiceBinding.getProviders()) {
			result.addAll(provider.getImportedPackages());
		}
		return new ArrayList<String>(result);
    }

    @Override
	protected List<String> getRequiredBundles() {
		Set<String> result = new HashSet<>();
		
		for (Bundle dep : typeDictionary.getRequiredBundles()) {
			result.add(dep.getSymbolicName());
		}		
		for (IIQLDependenciesProvider provider : IQLDependenciesProviderServiceBinding.getProviders()) {
			result.addAll(provider.getRequiredBundles());
		}
		return new ArrayList<String>(result);
	}
    

}