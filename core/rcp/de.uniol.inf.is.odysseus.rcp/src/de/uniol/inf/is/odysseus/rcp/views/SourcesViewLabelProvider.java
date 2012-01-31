/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.usermanagement.IUser;


public class SourcesViewLabelProvider implements ILabelProvider {

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
		if (element instanceof Entry) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>) element;
			if (OdysseusRCPPlugIn.getExecutor().getDataDictionary().isView(entry.getKey())){
				return ImageManager.getInstance().get("view");
			}else{
				return ImageManager.getInstance().get("source");
			}
		}
		if (element instanceof SDFAttribute) {
			return ImageManager.getInstance().get("attribute");
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Entry) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>) element;
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey()).append(" [")
					.append(entry.getValue().getClass().getSimpleName())
					.append("]");
			IUser user = OdysseusRCPPlugIn.getExecutor().getDataDictionary().getCreator(
					entry.getKey());
			if (user != null) {
				sb.append(" " + OdysseusNLS.CreatedBy + " ").append(user.getName());
			} else {
				sb.append(" " + OdysseusNLS.CreatedBy + " no user ??");
			}
			return sb.toString();
		}
		if (element instanceof SDFAttribute) {
			SDFAttribute a = (SDFAttribute) element;
			StringBuffer name = new StringBuffer(a.getAttributeName());
			name.append(":").append(a.getDatatype().getURI());
			return name.toString();
		}
		return null;
	}

}
