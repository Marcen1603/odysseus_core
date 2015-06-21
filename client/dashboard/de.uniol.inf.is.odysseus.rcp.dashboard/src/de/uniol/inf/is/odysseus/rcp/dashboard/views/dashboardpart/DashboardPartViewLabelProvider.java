/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.views.dashboardpart;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DashboardPartViewLabelProvider implements ILabelProvider {

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof IConfigurationElement) {
            IConfigurationElement sp = (IConfigurationElement) element;
            if (sp.getAttribute("icon") != null) {
                ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(sp.getDeclaringExtension().getNamespaceIdentifier(), sp.getAttribute("icon"));
                return image.createImage();
            }
        }
        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IConfigurationElement) {
            IConfigurationElement sp = (IConfigurationElement) element;
            return sp.getAttribute("name");
        }
        if (element instanceof String) {
            return element.toString();
        }
        return null;
    }

}
