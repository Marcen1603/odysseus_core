package de.uniol.inf.is.odysseus.wrapper.ice;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.ice.impl.ICESourceAdapter;

public class Test {
    SourceSpec source;

    @Before
    public void setUp() throws Exception {
        source = createMock(SourceSpec.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void testConnection() {
        SourceAdapter adapter = new ICESourceAdapter();
        
        expect(source.getName()).andReturn("Test").anyTimes();
        
        SourceConfiguration configuration = new SourceConfigurationImpl();
        configuration.put("service", "ClientConnector");
        configuration.put("host", "192.168.1.99");
        configuration.put("port", "10000");
        configuration.put("ownPort", "10200");
        configuration.put("ownService", "SaLsA.Simulation.Gui");
        configuration.put("protocol", "default");
        
        expect(source.getConfiguration()).andReturn(configuration).anyTimes();
        replay(source);
    //    Assert.assertEquals("Test", source.getName());
        adapter.registerSource(source);
        verify(source);
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

}
