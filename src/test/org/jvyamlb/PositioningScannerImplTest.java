/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jvyamlb.tokens.PositionedDocumentEndToken;
import org.jvyamlb.tokens.PositionedDocumentStartToken;
import org.jvyamlb.tokens.PositionedScalarToken;
import org.jvyamlb.tokens.PositionedStreamStartToken;
import org.jvyamlb.tokens.PositionedStreamEndToken;
import org.jvyamlb.tokens.PositionedBlockMappingStartToken;
import org.jvyamlb.tokens.PositionedBlockEndToken;
import org.jvyamlb.tokens.PositionedKeyToken;
import org.jvyamlb.tokens.PositionedValueToken;
import org.jvyamlb.tokens.PositionedBlockSequenceStartToken;
import org.jvyamlb.tokens.PositionedBlockEntryToken;
import org.jvyamlb.tokens.PositionedFlowMappingStartToken;
import org.jvyamlb.tokens.PositionedFlowMappingEndToken;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningScannerImplTest extends YAMLTestCase {
    public PositioningScannerImplTest(String name) {
        super(name);
    }

    protected List getScan(String input) {
        Scanner s = new PositioningScannerImpl(input);
        List output = new ArrayList();
        for(Iterator iter = s.iterator(); iter.hasNext();) {
            output.add(iter.next());
        }
        return output;
    }

    public void testThatEmptyScannerGeneratesPositionedEnvelope() {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedStreamEndToken(new Position.Range(new Position(0,0,0))));

        List tokens = getScan("");
        assertEquals(expected, tokens);
    }

    public void testThatSimpleScalarGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0, new Position.Range(new Position(0,0,0), new Position(0, 1, 1))));
        expected.add(new PositionedStreamEndToken(new Position.Range(new Position(0,1,1))));

        List tokens = getScan("a");
        assertEquals(expected, tokens);
    }

    public void testThatSimpleScalarWithSpaceGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedStreamEndToken(new Position.Range(new Position(0,2,2))));

        List tokens = getScan("a ");
        assertEquals(expected, tokens);
    }

    public void testThatSimpleScalarWithNewlineGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0, new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedStreamEndToken(new Position.Range(new Position(1,0,2))));

        List tokens = getScan("a\n");
        assertEquals(expected, tokens);
    }

    public void testThatComplicatedeScalarGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartToken(new Position.Range(new Position(0,0,0),new Position(0,3,3))));
        expected.add(new PositionedScalarToken(s("abcdefafgsdfgsdfg sfgdfsg fdsgfgsdf"), false, '"', new Position.Range(new Position(0,4,4), new Position(2,10,41))));
        expected.add(new PositionedStreamEndToken(new Position.Range(new Position(2,10,41))));

        List tokens = getScan("--- \"abcdefafgsdfgsdfg\n"+
                              "sfgdfsg\n"+
                              "fdsgfgsdf\"");
        assertEquals(expected, tokens);
    }

    public void testThatAKeyValuePairGetsCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(                   new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockMappingStartToken(             new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedKeyToken(                           new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0,  new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedValueToken(                         new Position.Range(new Position(0,2,2))));
        expected.add(new PositionedScalarToken(s("b"), true, (char)0,  new Position.Range(new Position(0,3,3), new Position(0,4,4))));
        expected.add(new PositionedBlockEndToken(                      new Position.Range(new Position(0,4,4))));
        expected.add(new PositionedStreamEndToken(                     new Position.Range(new Position(0,4,4))));

        List tokens = getScan("a: b");
        assertEquals(expected, tokens);
    }

    public void testThatTwoKeyValuePairsGetsCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(                   new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockMappingStartToken(             new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedKeyToken(                           new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0,  new Position.Range(new Position(0,0,0), new Position(0,1,1))));
        expected.add(new PositionedValueToken(                         new Position.Range(new Position(0,2,2))));
        expected.add(new PositionedScalarToken(s("b"), true, (char)0,  new Position.Range(new Position(0,3,3), new Position(0,4,4))));
        expected.add(new PositionedKeyToken(                           new Position.Range(new Position(1,0,5))));
        expected.add(new PositionedScalarToken(s("c"), true, (char)0,  new Position.Range(new Position(1,0,5), new Position(1,1,6))));
        expected.add(new PositionedValueToken(                         new Position.Range(new Position(1,2,7))));
        expected.add(new PositionedScalarToken(s("d"), true, (char)0,  new Position.Range(new Position(1,4,9), new Position(1,5,10))));
        expected.add(new PositionedBlockEndToken(                      new Position.Range(new Position(1,5,10))));
        expected.add(new PositionedStreamEndToken(                     new Position.Range(new Position(1,5,10))));

        List tokens = getScan("a: b\n"+
                              "c:  d");
        assertEquals(expected, tokens);
    }
    
    // TODO: add testing of nested block mappings

    public void testThatAOneItemSequenceWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(                   new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockSequenceStartToken(            new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0,  new Position.Range(new Position(0,2,2), new Position(0,3,3))));
        expected.add(new PositionedBlockEndToken(                      new Position.Range(new Position(0,3,3))));
        expected.add(new PositionedStreamEndToken(                     new Position.Range(new Position(0,3,3))));

        List tokens = getScan("- a");
        assertEquals(expected, tokens);
    }

    public void testThatMoreThanOneItemSequenceWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(                   new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockSequenceStartToken(            new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0,  new Position.Range(new Position(0,2,2), new Position(0,3,3))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(1,0,4))));
        expected.add(new PositionedScalarToken(s("b"), true, (char)0,  new Position.Range(new Position(1,4,8), new Position(1,5,9))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(2,0,10))));
        expected.add(new PositionedScalarToken(s("c"), true, (char)0,  new Position.Range(new Position(2,2,12), new Position(2,3,13))));
        expected.add(new PositionedBlockEndToken(                      new Position.Range(new Position(2,3,13))));
        expected.add(new PositionedStreamEndToken(                     new Position.Range(new Position(2,3,13))));

        List tokens = getScan("- a\n"+
                              "-   b\n"+
                              "- c");
        assertEquals(expected, tokens);
    }

    public void testThatListedSequenesWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(                   new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockSequenceStartToken(            new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0,  new Position.Range(new Position(0,2,2), new Position(0,3,3))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(1,0,4))));

        expected.add(new PositionedBlockSequenceStartToken(            new Position.Range(new Position(1,2,6))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(1,2,6))));
        expected.add(new PositionedScalarToken(s("foo"), true, (char)0,new Position.Range(new Position(1,4,8), new Position(1,7,11))));
        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(2,2,14))));
        expected.add(new PositionedScalarToken(s("bar"), true, (char)0,new Position.Range(new Position(2,4,16), new Position(2,7,19))));

        // This is a bit unintuitive - a block ending is on the new line afterwards.
        expected.add(new PositionedBlockEndToken(                      new Position.Range(new Position(3,0,20))));

        expected.add(new PositionedBlockEntryToken(                    new Position.Range(new Position(3,0,20))));
        expected.add(new PositionedScalarToken(s("b"), true, (char)0,  new Position.Range(new Position(3,2,22), new Position(3,3,23))));
        expected.add(new PositionedBlockEndToken(                      new Position.Range(new Position(3,3,23))));
        expected.add(new PositionedStreamEndToken(                     new Position.Range(new Position(3,3,23))));

        List tokens = getScan("- a\n"+
                              "- - foo\n"+
                              "  - bar\n"+
                              "- b");
        assertEquals(expected, tokens);
    }

    public void testThatSimpleFlowMappingWorks() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(                   new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedFlowMappingStartToken(              new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedKeyToken(                           new Position.Range(new Position(0,1,1))));
        expected.add(new PositionedScalarToken(s("a"), true, (char)0,  new Position.Range(new Position(0,1,1), new Position(0,2,2))));
        expected.add(new PositionedValueToken(                         new Position.Range(new Position(0,3,3))));
        expected.add(new PositionedScalarToken(s("b"), true, (char)0,  new Position.Range(new Position(0,4,4), new Position(0,5,5))));
        expected.add(new PositionedFlowMappingEndToken(                new Position.Range(new Position(0,5,5))));
        expected.add(new PositionedStreamEndToken(                     new Position.Range(new Position(0,6,6))));

        List tokens = getScan("{a: b}");
        assertEquals(expected, tokens);
    }
}// PositioningScannerImplTest
