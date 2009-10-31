package org.jvyamlb.debug;

import java.io.InputStream;
import java.io.FileInputStream;

import java.util.Iterator;

import org.jruby.util.ByteList;

import org.jvyamlb.*;

public class TimeImplicit {
    public static void main(String[] args) throws Exception {
        ResolverScanner rs = new ResolverScanner();
        ByteList b1 = new ByteList("one little thing".getBytes());
        ByteList b2 = new ByteList("13455".getBytes());
        int times = 10000000;
        long before = System.currentTimeMillis();
        for(int i=0; i<times; i++) {
            rs.recognize(b1);
            rs.recognize(b2);
        }
        long after = System.currentTimeMillis();
        System.err.println("implicit a string and a number " + times + " times took " + (after-before) + "ms");
    }
}

