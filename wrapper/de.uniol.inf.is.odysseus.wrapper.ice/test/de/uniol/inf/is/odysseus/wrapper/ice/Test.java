package de.uniol.inf.is.odysseus.wrapper.ice;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.Source;
import de.uniol.inf.is.odysseus.wrapper.ice.impl.ICESourceAdapter;

public class Test {
    Source source;

    @Before
    public void setUp() throws Exception {
        source = createStrictMock(Source.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void testConnection() {
        SourceAdapter adapter = new ICESourceAdapter();
        expect(source.getName()).andReturn("Test").anyTimes();
        replay(source);
        Assert.assertEquals("Test", source.getName());
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
