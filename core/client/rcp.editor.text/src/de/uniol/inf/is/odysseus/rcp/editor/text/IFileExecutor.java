package de.uniol.inf.is.odysseus.rcp.editor.text;

import de.uniol.inf.is.odysseus.core.collection.Context;

public interface IFileExecutor {

	public String getFileExtension();

	public void run(String text, Context context);

}
