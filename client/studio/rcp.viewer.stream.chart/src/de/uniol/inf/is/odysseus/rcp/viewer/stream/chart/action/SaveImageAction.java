/********************************************************************************** 
 * Copyright 2014b The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SaveImageAction extends Action {
    private final AbstractChart<?, ?> chart;
    private final Shell parentShell;

    public SaveImageAction(final Shell shell, final AbstractChart<?, ?> chart) {
        super();
        this.chart = chart;
        this.parentShell = shell;

    }

    @Override
    public void run() {
        try {
            final FileDialog dialog = new FileDialog(this.parentShell, SWT.SAVE);

            dialog.setFilterNames(new String[] {"Image Files (*.png)"});
            dialog.setFilterExtensions(new String[] {"*.png"});
            dialog.setText("Select file..");
            final String file = dialog.open();
            if (file != null) {
                this.chart.saveImage(new File(file));
            }

        }
        catch (final Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
