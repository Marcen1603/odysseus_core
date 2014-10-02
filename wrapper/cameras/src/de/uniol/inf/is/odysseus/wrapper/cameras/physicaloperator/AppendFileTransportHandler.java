/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.DirectoryWatcherTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class AppendFileTransportHandler extends DirectoryWatcherTransportHandler 
{
	public static final String NAME = "AppendFile";

	public String fileName;
	
	FileInputStream currentStream = null;
	
	public AppendFileTransportHandler() {
		super();
	}

	static public String getFilenameExtension(String fileName)
	{
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) 
			return fileName.substring(i+1);
		else 
			return "";
	}
	
	static private OptionMap updateOptionMap(OptionMap options)
	{
		options.checkRequired("filename");
		
		String fileName = options.get("filename");
		options.setOption(DIRECTORY, new File(fileName).getParent());
		options.setOption(FILTER,  	 getFilenameExtension(fileName) + "$");
		
		return options;
	}
	
	public AppendFileTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, updateOptionMap(options));
		
		fileName = options.get("filename");
	}

	@Override protected void onChangeDetected(File file) throws IOException
	{
		String s = file.getAbsolutePath();
		
		if (!s.equals(fileName)) return;
		
		if (currentStream == null)
			currentStream = new FileInputStream(file);
		else
		{
			if (currentStream.read() == '\r')
				currentStream.read();
		}
		
		fireProcess(currentStream);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new AppendFileTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() { return NAME; }

    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof AppendFileTransportHandler)) 
    		return false;
    	
    	AppendFileTransportHandler other = (AppendFileTransportHandler)o;
    	if(!this.fileName.equals(other.fileName)) 
    		return false;
    	    	
    	return true;
    }
}
