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
package cc.kuka.odysseus.ontology;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.ontology.function.quality.TimelinessFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class OntologyFunctionProvider implements IFunctionProvider {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(OntologyFunctionProvider.class);

    /**
     * Default constructor.
     */
    public OntologyFunctionProvider() {

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final List<IMepFunction<?>> getFunctions() {
        final List<IMepFunction<?>> functions = new ArrayList<>();
        try {

            functions.add(new TimelinessFunction());
        }
        catch (final Exception e) {
            OntologyFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }
}
