/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jruby.util.ByteList;

import org.jvyamlb.events.PositionedStreamStartEvent;
import org.jvyamlb.events.PositionedStreamEndEvent;
import org.jvyamlb.events.PositionedDocumentStartEvent;
import org.jvyamlb.events.PositionedDocumentEndEvent;
import org.jvyamlb.events.PositionedScalarEvent;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningParserImplTest extends YAMLTestCase {
    public PositioningParserImplTest(String name) {
        super(name);
    }

    protected List getParse(String input) {
        Parser parser = new PositioningParserImpl(new PositioningScannerImpl(input));
        List output = new ArrayList();
        for(Iterator iter = parser.iterator(); iter.hasNext();) {
            output.add(iter.next());
        }
        return output;
    }
    
    public void testThatEmptyParserGeneratesPositionedEnvelope() {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,0,0))));

        List events = getParse("");
        assertEquals(expected, events);
    }

    public void testThatSimpleScalarGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,1,1))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,1,1))));

        List events = getParse("a");
        assertEquals(expected, events);        
    }
}
