/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
