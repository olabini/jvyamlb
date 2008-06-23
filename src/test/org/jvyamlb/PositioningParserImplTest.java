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
import org.jvyamlb.events.PositionedMappingStartEvent;
import org.jvyamlb.events.PositionedMappingEndEvent;

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

    public void testThatComplicatedeScalarGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(true, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{false,true}, s("abcdefafgsdfgsdfg sfgdfsg fdsgfgsdf"), '"', new Position.Range(new Position(0,4,4), new Position(2,10,41))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(2,10,41))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(2,10,41))));

        List events = getParse("--- \"abcdefafgsdfgsdfg\n"+
                               "sfgdfsg\n"+
                               "fdsgfgsdf\"");
        assertEquals(expected, events);
    }

    public void testThatAKeyValuePairGetsCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,3,3), new Position(0,4,4))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(0,4,4))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,4,4))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,4,4))));

        List events = getParse("a: b");
        assertEquals(expected, events);
    }

    public void testThatTwoKeyValuePairsGetsCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,3,3), new Position(0,4,4))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("c"), (char)0, new Position.Range(new Position(1,0,5), new Position(1,1,6))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("d"), (char)0, new Position.Range(new Position(1,4,9), new Position(1,5,10))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(1,5,10))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(1,5,10))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(1,5,10))));

        List events = getParse("a: b\n"+
                               "c:  d");
        assertEquals(expected, events);
    }

}
