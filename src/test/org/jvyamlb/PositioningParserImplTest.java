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
import org.jvyamlb.events.PositionedSequenceStartEvent;
import org.jvyamlb.events.PositionedSequenceEndEvent;
import org.jvyamlb.events.PositionedAliasEvent;
import org.jvyamlb.exceptions.ParserException;
import org.jvyamlb.exceptions.PositionedParserException;

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

    public void testThatAOneItemSequenceWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,2,2), new Position(0,3,3))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(0,3,3))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,3,3))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,3,3))));

        List events = getParse("- a");
        assertEquals(expected, events);
    }

    public void testThatMoreThanOneItemSequenceWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,2,2), new Position(0,3,3))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(1,4,8), new Position(1,5,9))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("c"), (char)0, new Position.Range(new Position(2,2,12), new Position(2,3,13))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(2,3,13))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(2,3,13))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(2,3,13))));

        List events = getParse("- a\n"+
                              "-   b\n"+
                              "- c");
        assertEquals(expected, events);
    }

    public void testThatListedSequenesWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,2,2), new Position(0,3,3))));
        expected.add(new PositionedSequenceStartEvent(null, null, true, false, new Position.Range(new Position(1,2,6))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("foo"), (char)0, new Position.Range(new Position(1,4,8), new Position(1,7,11))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("bar"), (char)0, new Position.Range(new Position(2,4,16), new Position(2,7,19))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(2,7,19))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(3,2,22), new Position(3,3,23))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(3,3,23))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(3,3,23))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(3,3,23))));

        List events = getParse("- a\n"+
                               "- - foo\n"+
                               "  - bar\n"+
                               "- b");
        assertEquals(expected, events);
    }


    public void testThatSimpleFlowMappingWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent(null, null, true, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,1,1), new Position(0,2,2))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,4,4), new Position(0,5,5))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(0,6,6))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,7,7))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,7,7))));

        List events = getParse("{a: b }");
        assertEquals(expected, events);
    }

    public void testThatSimpleFlowSequenceWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent(null, null, true, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,1,1), new Position(0,2,2))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(0,2,2))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,3,3))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,3,3))));

        List events = getParse("[a]");
        assertEquals(expected, events);
    }

    public void testThatFlowSequenceWithMoreThanOneElementWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent(null, null, true, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,1,1), new Position(0,2,2))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,4,4), new Position(0,5,5))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(0,6,6))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,7,7))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,7,7))));

        List events = getParse("[a, b ]");
        assertEquals(expected, events);
    }

    public void testScalarWithTag() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, "!str", new boolean[]{false,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,6,6))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,6,6))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,6,6))));

        List events = getParse("!str a");
        assertEquals(expected, events);
    }

    public void testScalarWithAnchor() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent("blad", null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,7,7))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,7,7))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,7,7))));

        List events = getParse("&blad a");
        assertEquals(expected, events);
    }

    public void testScalarWithAnchorAndTag() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent("blad", "!str", new boolean[]{false,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,12,12))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,12,12))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,12,12))));

        List events = getParse("&blad !str a");
        assertEquals(expected, events);

        events = getParse("!str &blad a");
        assertEquals(expected, events);
    }

    public void testAlias() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedAliasEvent("blad", new Position.Range(new Position(0,0,0), new Position(0,5,5))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,5,5))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,5,5))));

        List events = getParse("*blad");
        assertEquals(expected, events);
    }

    public void testSequenceWithTag() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent(null, "!seq", false, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,6,6), new Position(0,7,7))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(0,7,7))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,8,8))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,8,8))));

        List events = getParse("!seq [a]");
        assertEquals(expected, events);
    }

    public void testSequenceWithAnchor() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent("blad", null, true, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,7,7), new Position(0,8,8))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(0,8,8))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,9,9))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,9,9))));

        List events = getParse("&blad [a]");
        assertEquals(expected, events);
    }

    public void testSequenceWithAnchorAndTag() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedSequenceStartEvent("blad", "!seq", false, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,12,12), new Position(0,13,13))));
        expected.add(new PositionedSequenceEndEvent(new Position.Range(new Position(0,13,13))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,14,14))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,14,14))));

        List events = getParse("&blad !seq [a]");
        assertEquals(expected, events);

        events = getParse("!seq &blad [a]");
        assertEquals(expected, events);
    }


    public void testMappingWithTag() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent(null, "!map", false, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,6,6), new Position(0,7,7))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,9,9), new Position(0,10,10))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(0,10,10))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,11,11))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,11,11))));

        List events = getParse("!map {a: b}");
        assertEquals(expected, events);
    }

    public void testMappingWithAnchor() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent("blad", null, true, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,7,7), new Position(0,8,8))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,10,10), new Position(0,11,11))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(0,11,11))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,12,12))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,12,12))));

        List events = getParse("&blad {a: b}");
        assertEquals(expected, events);
    }

    public void testMappingWithAnchorAndTag() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent("blad", "!map", false, true, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,12,12), new Position(0,13,13))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("b"), (char)0, new Position.Range(new Position(0,15,15), new Position(0,16,16))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(0,16,16))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,17,17))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,17,17))));

        List events = getParse("&blad !map {a: b}");
        assertEquals(expected, events);

        events = getParse("!map &blad {a: b}");
        assertEquals(expected, events);
    }

    public void testLiteralScalar() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{false,true}, s("abc\nfoobar"), '|', new Position.Range(new Position(0,0,0), new Position(2,7,14))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(2,7,14))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(2,7,14))));

        List events = getParse("|\n"+
                               " abc\n"+
                               " foobar");
        assertEquals(expected, events);
    }

    public void testFoldedScalar() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{false,true}, s("abc foobar"), '>', new Position.Range(new Position(0,0,0), new Position(2,7,14))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(2,7,14))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(2,7,14))));

        List events = getParse(">\n"+
                               " abc\n"+
                               " foobar");
        assertEquals(expected, events);
    }

    public void testSingleQuotedScalar() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{false,true}, s("abc"), '\'', new Position.Range(new Position(0,0,0), new Position(0,5,5))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(0,5,5))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(0,5,5))));

        List events = getParse("'abc'");
        assertEquals(expected, events);
    }

    public void testParserExceptionIncludesPositioningInformation() throws Exception {
        try {
            getParse("*foobar bla");
            assertTrue("parsing should throw an exception", false);
        } catch(ParserException e) {
            assertTrue("Exception should be instance of PositionedParserException", e instanceof PositionedParserException);
            assertEquals("Position should be correct for exception", new Position.Range(new Position(0,8,8),new Position(0,11,11)), ((PositionedParserException)e).getRange());
        }
    }

    public void testCommentBeforeSecondKeyWithBlankFirst() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("a"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s(""), (char)0, new Position.Range(new Position(0,2,2), new Position(0,2,2))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("c"), (char)0, new Position.Range(new Position(2,0,9), new Position(2,1,10))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("d"), (char)0, new Position.Range(new Position(2,3,12), new Position(2,4,13))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(2,4,13))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(2,4,13))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(2,4,13))));

        List events = getParse("a: \n#foo\nc: d");
        assertEquals(expected, events);
    }

    public void testCommentBeforeSecondKeyWithBlankFirstAndSomeMore() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartEvent(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartEvent(false, new int[]{1,1}, null, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedMappingStartEvent(null, null, true, false, new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("foo"), (char)0, new Position.Range(new Position(0,0,0), new Position(0,3,3))));
        expected.add(new PositionedMappingStartEvent(null, null, true, false, new Position.Range(new Position(1,2,7))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("inner"), (char)0, new Position.Range(new Position(1,2,7), new Position(1,7,12))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("val"), (char)0, new Position.Range(new Position(1,9,14), new Position(1,12,17))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(1,12,17))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("bar"), (char)0, new Position.Range(new Position(4,0,27), new Position(4,3,30))));
        expected.add(new PositionedScalarEvent(null, null, new boolean[]{true,false}, s("baz"), (char)0, new Position.Range(new Position(4,5,32), new Position(4,8,35))));
        expected.add(new PositionedMappingEndEvent(new Position.Range(new Position(4,8,35))));
        expected.add(new PositionedDocumentEndEvent(false, new Position.Range(new Position(4,8,35))));
        expected.add(new PositionedStreamEndEvent(new Position.Range(new Position(4,8,35))));

        List events = getParse("foo:\n" +
                               "  inner: val\n" +
                               "\n" +
                               "# Hello\n" +
                               "bar: baz");
        assertEquals(expected, events);
    }
}
