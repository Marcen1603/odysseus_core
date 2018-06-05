/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.text.pql.actions;

import java.awt.Desktop;
import java.net.URI;

import org.eclipse.jface.action.Action;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class OpenWikiAction extends Action {
    private String url;

    /**
     * Class constructor.
     *
     */
    public OpenWikiAction(LogicalOperatorInformation operator) {
        this.url = operator.getUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(this.url));
                }
            }
            else {
                Runtime.getRuntime().exec(new String[] { "open ", this.url });
            }
        }
        catch (final Exception e) {
            System.err.println("Unable to open documentation");
        }
    }
}
