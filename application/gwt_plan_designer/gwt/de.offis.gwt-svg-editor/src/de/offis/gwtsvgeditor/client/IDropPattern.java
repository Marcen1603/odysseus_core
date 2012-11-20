package de.offis.gwtsvgeditor.client;

import de.offis.gwtsvgeditor.client.commands.ISvgEditorCommand;
import java.util.List;

public interface IDropPattern {

    public List<ISvgEditorCommand> getCommands();
}
