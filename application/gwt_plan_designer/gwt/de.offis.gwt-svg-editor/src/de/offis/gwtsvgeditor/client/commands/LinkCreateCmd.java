package de.offis.gwtsvgeditor.client.commands;

import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgGhostLink;
import de.offis.gwtsvgeditor.client.svg.SvgLink;
import de.offis.gwtsvgeditor.client.IncompatiblePortsEx;
import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Command to link two Modules.
 * 
 * @author Alexander Funk
 *
 */
public class LinkCreateCmd implements ISvgEditorCommand {

    private boolean previewed = false;
    private SvgEditor editor;
    private SvgLink link;
    private SvgGhostLink preview;

    public LinkCreateCmd(SvgLink link) {
        this.editor = link.getEditor();
        this.link = link;
    }

    private void removePreview() {
        if (preview != null && preview.isAttached()) {
            preview.removeFromParent();
            preview = null;
        }
    }

    public void commit() throws IncompatiblePortsEx {
        if (previewed) {
            removePreview();
        }

        editor.createVisualLink(link);
    }

    public void cancel() {
        removePreview();
    }

    public void preview() {
        SvgPort start = link.getSource();
        SvgPort end = link.getDestination();

        if(editor.getPortLogic().match(start, end) != null){
            return;
        }

        if (start == null || end == null) {
            return;
        }

        preview = new SvgGhostLink(start.getX() + 10, start.getY() + 10);
        preview.update(end.getX() + 10, end.getY() + 10);

        editor.add(preview);

        previewed = true;
    }
}
