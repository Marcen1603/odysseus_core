/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.gui.client.gwtgraphics.shape.path;

/**
 * This class is an abstract class for Path steps.
 *
 * @author Henri Kerola / IT Mill Ltd
 *
 */
public abstract class PathStep {

        public abstract String getSVGString();

        @Override
        public String toString() {
                return getSVGString();
        }
}

