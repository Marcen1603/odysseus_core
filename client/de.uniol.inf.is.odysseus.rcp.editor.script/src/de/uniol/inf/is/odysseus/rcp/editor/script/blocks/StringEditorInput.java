package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

// from: http://blog.eclipse-tips.com/2008/06/opening-editor-without-ifile.html
public final class StringEditorInput implements IStorageEditorInput {

	private static final Logger LOG = LoggerFactory.getLogger(StringEditorInput.class);
	
	private final IFile baseFile;
	private final String name;
	private final String extension;
	
	private final List<IStringEditorInputChangeListener> listeners = Lists.newArrayList();
	
	private String inputString;

	public StringEditorInput(String inputString, String name, String extension, IFile baseFile) {
		Preconditions.checkNotNull(baseFile, "baseFile must not be null!");

		if( inputString == null ) {
			inputString = "";
		}
		if( Strings.isNullOrEmpty(name)) {
			name = "UnnamedQuery";
		}
		
		this.inputString = inputString;
		this.baseFile = baseFile;
		this.name = name;
		this.extension = extension;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof StringEditorInput)) {
			return false;
		}
		
		StringEditorInput other = (StringEditorInput)obj;
		return name.equals(other.name)&& extension.equals(other.extension); 
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + ( extension.hashCode() * 31);
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if( adapter.equals(IFile.class)) {
			return (T) baseFile;
		}
		
		return null;
	}

	@Override
	public String getName() {
		return name + "." + extension;
	}

	@Override
	public String getToolTipText() {
		return "tool tip";
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void addListener(IStringEditorInputChangeListener listener ) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(IStringEditorInputChangeListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}

	protected void fireListeners( String from, String to ) {
		synchronized( listeners ) {
			for( IStringEditorInputChangeListener listener : listeners ) {
				try {
					listener.stringChanged(this, from, to);
				} catch( Throwable t ) {
					LOG.error("Exception in string editor input change listener", t);
				}
			}
		}
	}
	
	@Override
	public IStorage getStorage() throws CoreException {
		return new IStorage() {

			@Override
			public InputStream getContents() throws CoreException {
				return new ByteArrayInputStream(inputString.getBytes());
			}

			@Override
			public IPath getFullPath() {
				return null;
			}
			
			@Override
			public String getName() {
				return StringEditorInput.this.getName();
			}

			@Override
			public boolean isReadOnly() {
				return false;
			}

			@Override
			public <T> T getAdapter(Class<T> adapter) {
				return null;
			}
		};
	}

	public void set(String string) {
		if( !string.equals(inputString)) {
			String copy = inputString;
			inputString = string;
			
			fireListeners(copy, string);
		}
	}
	
	public String get() {
		return inputString;
	}

	public void dispose() {
		synchronized( listeners ) {
			listeners.clear();
		}
	}

}
