/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.ontology.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FeatureOfInterestContentProvider implements ITreeContentProvider {

    @Override
    public void dispose() {
        // Empty block
    }

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        // Empty block
    }

    @Override
    public Object[] getElements(final Object inputElement) {
        return this.getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(final Object parentElement) {
        if (parentElement instanceof Collection) {
            return ((Collection<?>) parentElement).toArray();
        }
        else if (parentElement instanceof FeatureOfInterest) {
            final FeatureOfInterest featureOfInterest = (FeatureOfInterest) parentElement;
            return featureOfInterest.hasProperties().toArray();
        }
        else if (parentElement instanceof Property) {
            final Property property = (Property) parentElement;
            final List<Object> children = new ArrayList<>();
            children.add(property.uri());
            return children.toArray();
        }
        return null;
    }

    @Override
    public Object getParent(final Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(final Object element) {
        if (element instanceof FeatureOfInterest) {
            return true;
        }
        else if (element instanceof Property) {
            return true;
        }
        return false;
    }
}
