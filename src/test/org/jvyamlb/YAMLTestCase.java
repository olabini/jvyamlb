/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import junit.framework.TestCase;

import org.jruby.util.ByteList;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class YAMLTestCase extends TestCase {
    public YAMLTestCase(final String name) {
        super(name);
    }

    protected ByteList s(String st) throws Exception {
        return new ByteList(st.getBytes("UTF-8"));
    }

    public void assertLoad(Object expected, String str) throws Exception {
        assertEquals(expected,YAML.load(s(str)));
    }

    public Object load(String str) throws Exception {
        return YAML.load(s(str));
    }

    public void assertLoad(Object expected, ByteList str) throws Exception {
        assertEquals(expected,YAML.load(str));
    }

    public void assertLoad10(Object expected, String str) throws Exception {
        assertEquals(expected,YAML.load(s(str),YAML.config().version("1.0")));
    }

    public void assertLoad10(byte[] expected, String str) throws Exception {
        assertArrayEquals(expected,(byte[])YAML.load(s(str),YAML.config().version("1.0")));
    }

    private void assertArrayEquals(byte[] expected, byte[] given) {
        assertTrue(java.util.Arrays.equals(expected, given));
    }

    public void assertRoundtrip(String value) throws Exception {
        assertEquals(s(value),YAML.load(YAML.dump(s(value))));
    }

    public void assertRoundtrip(ByteList value) throws Exception {
        try {
            assertEquals(value,YAML.load(YAML.dump(value)));
        } catch(Exception e) {
            System.err.println("bytelist[" + value.begin + ", realSize: " + value.realSize + "]");
            for(int i = 0; i < value.realSize; i++) {
                System.err.println("[" + i + "]= " + value.bytes[value.begin+i]);
            }
            System.err.println("for: \"" + value + "\" -- of len: " + value.realSize);
            System.err.println("out: \"" + YAML.dump(value) + "\"");
            throw e;
        }
    }
}// YAMLTestCase
