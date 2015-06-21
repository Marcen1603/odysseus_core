/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.views.dashboardpart.DashboardPartView;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DashboardPerspective implements IPerspectiveFactory {
    @Override
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        // Top left: Project Explorer view and Bookmarks view placeholder
        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
        topLeft.addView(ProjectExplorer.VIEW_ID);

        IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea);
        right.addView(DashboardPartView.VIEW_ID);

        @SuppressWarnings("unused")
        IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.60f, editorArea);

        layout.addNewWizardShortcut(DashboardPlugIn.WIZARD_DASHBOARD_ID);
        layout.addNewWizardShortcut(DashboardPlugIn.WIZARD_DASHBOARDPART_ID);

    }
}
