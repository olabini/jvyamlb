/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.jruby.util.ByteList;

import org.joda.time.DateTime;

import org.jvyamlb.exceptions.ScannerException;

import junit.framework.AssertionFailedError;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SimpleLoadTest extends YAMLTestCase {
    public SimpleLoadTest(final String name) {
        super(name);
    }

    public void testStrings() throws Exception {
        assertLoad(s("str"),"!!str str");
        assertLoad(s("str"),"--- str");
        assertLoad(s("str"),"---\nstr");
        assertLoad(s("str"),"--- \nstr");
        assertLoad(s("str"),"--- \n str");
        assertLoad(s("str"),"str");
        assertLoad(s("str")," str");
        assertLoad(s("str"),"\nstr");
        assertLoad(s("str"),"\n str");
        assertLoad(s("str"),"\"str\"");
        assertLoad(s("str"),"'str'");
        assertLoad(s("str")," --- 'str'");
        assertLoad(s("1.0"),"!!str 1.0");

        assertLoad10(s("str"),"!str str");
        assertLoad10(s("1.0"),"!str 1.0");
    }

    public void testArrays() throws Exception {
        List expected = new ArrayList();
        expected.add(s("a"));
        expected.add(s("b"));
        expected.add(s("c"));

        assertLoad(expected, "--- \n- a\n- b\n- c\n");
        assertLoad(expected, "--- [a, b, c]");
        assertLoad(expected, "[a, b, c]");
    }

    public void testEmpty() throws Exception {
        assertLoad(null, "---\n!!str");      // this doesn't match Ruby's way of doing things
        assertLoad10(null, "---\n!str");
    }

    public void testStrange() throws Exception {
        assertLoad(s("---"), "--- ---\n");
        assertLoad(s("---"), "---");
    }
    
    public void testShared() throws Exception {
        ByteList inp = s("abcde");
        inp.begin = 2;
        inp.realSize = 3;
        assertLoad(s("cde"),inp);
    }

    public void testDate() throws Exception {
        assertEquals(ByteList.class, ((Map)load("{a: 2007-01-01 01:12:34}")).get(s("a")).getClass());
        assertEquals(ByteList.class, load("2007-01-01 01:12:34").getClass());
        assertEquals(ByteList.class, load("2007-01-01 01:12:34.0").getClass());
        assertEquals(DateTime.class, load("2007-01-01 01:12:34 +00:00").getClass());
        assertEquals(DateTime.class, load("2007-01-01 01:12:34.0 +00:00").getClass());
        assertEquals(ByteList.class, ((Map)load("{a: 2007-01-01 01:12:34}")).get(s("a")).getClass());
    }

    public void testObjectIdentity() throws Exception {
        List val = (List)load("---\n- foo\n- foo\n- [foo]\n- [foo]\n- {foo: foo}\n- {foo: foo}\n");
        assertNotSame(val.get(0), val.get(1));
        assertNotSame(val.get(2), val.get(3));
        assertNotSame(val.get(4), val.get(5));
    }

    public void testStrangeNesting() throws Exception {
        Map expected = new HashMap();
        Map internal = new HashMap();
        internal.put(s("bar"), null);
        expected.put(s("foo"), internal);
        assertLoad(expected, "---\nfoo: { bar }\n");

        expected = new HashMap();
        List internal2 = new ArrayList();
        internal2.add(s("a"));
        expected.put(s("default"), internal2);

        expected = new HashMap();
        internal = new HashMap();
        internal.put(s("bar"),null);
        internal.put(s("qux"),null);
        expected.put(s("foo"), internal);
        assertLoad(expected, "---\nfoo: {bar, qux}");

        // This looks like bad YAML to me... But MRI supports it
        //        assertLoad(expected, "---\ndefault: -\n- a\n");
    }

    public void testStrangeCharacters() throws Exception {
        assertLoad(s(",a"), "--- \n,a"); 

        Map expected = new HashMap();
        expected.put(s("foobar"), s(">= 123"));
        assertLoad(expected, "foobar: >= 123");

        expected = new HashMap();
        expected.put(s("foo"), s("bar"));
        assertLoad(expected, "---\nfoo: \tbar");

        expected = new HashMap();
        expected.put(s("foobar"), s("|= 567"));
        assertLoad(expected, "foobar: |= 567");
    }

    public void testAtSign() throws Exception {
        Map expected = new HashMap();
        expected.put(s("foo"), s("@bar"));
        assertLoad(expected, "foo: @bar");
    }

    public void testSymbols() throws Exception {
        assertLoad(s(":a"), "--- \n:a"); 
        List expected = new ArrayList();
        expected.add(s(":a"));
        assertLoad(expected, "--- \n[:a]"); 

        Map expected2 = new HashMap();
        expected2.put(s(":year"), s(":last"));
        expected2.put(s(":month"), s(":jan"));
        assertLoad(expected2, "--- \n{:year: :last, :month: :jan}");
        
        expected2 = new HashMap();
        List l = new ArrayList();
        l.add(s(":year"));
        expected2.put(s("order"), l);

        assertLoad(expected2, "--- \norder: [:year]");
    }

    public void testLoadOfAsterisk() throws Exception {
        assertLoad(s("*.rb"), "--- \n*.rb"); 
        assertLoad(s("*.rb"), "--- \n'*.rb'");

        assertLoad(s("&.rb"), "--- \n&.rb"); 
        assertLoad(s("&.rb"), "--- \n'&.rb'");
        
        try {
            assertLoad(null, "--- \n*r.b");
            assertTrue(false);
        } catch(ScannerException e) {
            assertTrue(true);
        }

        try {
            assertLoad(null, "--- \n&r.b");
            assertTrue(false);
        } catch(ScannerException e) {
            assertTrue(true);
        }
    }

    public void testSimpleFailure() throws Exception {
        try {
            assertLoad(null, "--- -\nh");
        } catch(AssertionFailedError e) {
            assertTrue("should fail on faulty YAML: " + e, false);
        } catch(ScannerException e) {
            assertTrue(true);
        }
    }

    public void testEmptyDocument() throws Exception {
        assertLoad(null, "---\n");
        assertLoad(null, "--- \n");
        assertLoad(s("---"), "---");
    }

    public void testBase64UnevenBinary() throws Exception {
        assertLoad10(s("<131>Mar 26 01:42:54 ControlTowerServer[68251\u0000\u0000").bytes(), "--- !binary 'PDEzMT5NYXIgMjYgMDE6NDI6NTQgQ29udHJvbFRvd2VyU2VydmVyWzY4MjUxAAA='\n");
        assertLoad10(s("<131>Mar 26 01:42:54 ControlTowerServer[68251\u0000\u0000").bytes(), "--- !binary 'PDEzMT5NYXIgMjYgMDE6NDI6NTQgQ29udHJvbFRvd2VyU2VydmVyWzY4MjUx\n  AAA=\n  '\n");
    }
}// SimpleLoadTest
