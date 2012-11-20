package de.offis.gwtsvgeditor.client.commands;

import de.offis.gwtsvgeditor.client.IncompatiblePortsEx;

/**
 * Interface the Commands have to implement.
 * 
 * @author Alexander Funk
 *
 */
public interface ISvgEditorCommand {

    void commit() throws IncompatiblePortsEx ;
    void cancel();
    void preview();
}
