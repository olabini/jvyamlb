/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Random;

import org.jruby.util.ByteList;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class RoundtripTest extends YAMLTestCase {
    public RoundtripTest(final String name) {
        super(name);
    }

    public void testSeveralStrings() throws Exception {
        assertRoundtrip("C VW\u0085\u000B\u00D1XU\u00E6");
        assertRoundtrip("\n8 xwKmjHG");
        assertRoundtrip("1jq[\205qIB\ns");
        assertRoundtrip("\rj\230fso\304\nEE");
        assertRoundtrip("ks]qkYM\2073Un\317\nL\346Yp\204 CKMfFcRDFZ\u000BMNk\302fQDR<R\u000B \314QUa\234P\237s aLJnAu \345\262Wqm_W\241\277J\256ILKpPNsMPuok");
    }

    public void testChineseStringDump() throws Exception {
        assertRoundtrip("中文");
    }

    public void testFuzzyStringRoundtrip() throws Exception {
        int[] values = new int[255];
        for(int i=0;i<values.length;i++) {
            values[i] = i+1;
        }

        char[] more = new char[26*2];
        for(int i=0;i<26;i++) {
            more[i] = (char)('a' + i);
        }
        for(int i=26;i<52;i++) {
            more[i] = (char)('A' + (i-26));
        }

        char[] blanks = new char[]{' ', '\t', '\n'};

        char[][] types = new char[3][];
        
        types[0] = new char[more.length*10 + blanks.length*2];
        int index = 0;
        for(int j=0;j<10;j++) {
            for(int i=0;i<more.length;i++) {
                types[0][index++] = more[i];
            }
        }
        for(int j=0;j<2;j++) {
            for(int i=0;i<blanks.length;i++) {
                types[0][index++] = blanks[i];
            }
        }

        types[1] = new char[values.length + more.length*10 + blanks.length*2];
        index = 0;
        for(int i=0;i<values.length;i++) {
            types[1][index++] = (char)values[i];
        }
        for(int j=0;j<10;j++) {
            for(int i=0;i<more.length;i++) {
                types[1][index++] = more[i];
            }
        }
        for(int j=0;j<2;j++) {
            for(int i=0;i<blanks.length;i++) {
                types[1][index++] = blanks[i];
            }
        }

        types[2] = new char[values.length + more.length*10 + blanks.length*20];
        index = 0;
        for(int i=0;i<values.length;i++) {
            types[2][index++] = (char)values[i];
        }
        for(int j=0;j<10;j++) {
            for(int i=0;i<more.length;i++) {
                types[2][index++] = more[i];
            }
        }
        for(int j=0;j<20;j++) {
            for(int i=0;i<blanks.length;i++) {
                types[2][index++] = blanks[i];
            }
        }

        int[] sizes = new int[]{10,81,214};

        Random rand = new Random();

        for(int i=0;i<types.length;i++) {
            for(int j=0;j<sizes.length;j++) {
                for(int k=0;k<1000;k++) {
                    byte[] bs = new byte[sizes[j]];
                    for(int s=0;s<sizes[j];s++) {
                        bs[s] = (byte)(types[i][rand.nextInt(types[i].length)]);
                    }
                    ByteList b = new ByteList(bs, false);
                    assertRoundtrip(b);
                }
            }
        }
    }
}// RoundtripTest
