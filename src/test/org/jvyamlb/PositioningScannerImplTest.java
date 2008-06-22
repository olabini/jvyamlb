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

    public void testThatComplicatedeScalarGeneratesCorrectPositioning() throws Exception {
        List expected = new ArrayList();
        expected.add(new PositionedStreamStartToken(new Position.Range(new Position(0,0,0))));
        expected.add(new PositionedDocumentStartToken(new Position.Range(new Position(0,0,0),new Position(0,3,3))));
        expected.add(new PositionedScalarToken(s("abcdefafgsdfgsdfg sfgdfsg fdsgfgsdf"), false, '"', new Position.Range(new Position(0,4,4), new Position(2,10,41))));
        expected.add(new PositionedStreamEndToken(new Position.Range(new Position(2,10,41))));

        List tokens = getScan("--- \"abcdefafgsdfgsdfg\nsfgdfsg\nfdsgfgsdf\"");
        assertEquals(expected, tokens);
    }

    
    
}// PositioningScannerImplTest
