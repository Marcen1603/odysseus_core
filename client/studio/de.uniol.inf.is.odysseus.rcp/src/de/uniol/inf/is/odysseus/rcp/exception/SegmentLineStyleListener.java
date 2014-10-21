package de.uniol.inf.is.odysseus.rcp.exception;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;

public class SegmentLineStyleListener implements LineStyleListener {

    public SegmentLineStyleListener() {
        super();
    }

    @Override
    public void lineGetStyle(LineStyleEvent event) {
        List<StyleRange> styles = new ArrayList<>();
        @SuppressWarnings("unused")
		int start = 0;
        int length = event.lineText.length();
        if (event.lineText.startsWith("* ")) {
            StyleRange style = new StyleRange();
            style.start = event.lineOffset;
            style.length = length;
            style.fontStyle = SWT.BOLD;
            styles.add(style);
        }

        event.styles = styles.toArray(new StyleRange[0]);
    }


}
