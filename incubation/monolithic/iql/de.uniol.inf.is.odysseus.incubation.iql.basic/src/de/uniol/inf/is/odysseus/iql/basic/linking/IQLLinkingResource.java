package de.uniol.inf.is.odysseus.iql.basic.linking;



import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;



public class IQLLinkingResource extends LazyLinkingResource {

	@Inject
	private IIQLTypeDictionary typeDictionary;
	
	private IQLModel systemFile;
	
	
	public void setSystemFile(IQLModel systemFile) {
		this.systemFile = systemFile;
	}
	
	
	
	@Override
	protected void doLinking() {
		ensureSystemFilesArePresent();
		super.doLinking();
	}


	private void ensureSystemFilesArePresent() {
		ResourceSet resourceSet = getResourceSet();
		for (IQLModel systemFile : typeDictionary.getSystemFiles()) {
			resourceSet.getResource(createURI(systemFile.getName(), typeDictionary.getFileExtension()), true);
		}
	}
	
	private URI createURI(String name, String fileExtension) {
		return URI.createURI("http:///"+name+"."+fileExtension);
	}
	
	@Override
	public void doSave(OutputStream outputStream, Map<?, ?> options)
			throws IOException {
		if (systemFile != null) {
			return;
		}		
		super.doSave(outputStream, options);
	}	
}
