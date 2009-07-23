package org.jvyamlb.debug;

import java.io.InputStream;
import java.io.FileInputStream;

import org.jruby.util.ByteList;

import org.jvyamlb.*;

public class TimeParsing {
    public static byte[] realloc(byte[] input, int size) {
        byte[] newArray = new byte[size];
        System.arraycopy(input, 0, newArray, 0, input.length);
        return newArray;
    }

    public static void main(String[] args) throws Exception {
        String filename = args[0];
        int len = 8000;
        int read = 0;
        int currRead = 0;
        byte[] buffer = new byte[1024];
        byte[] input = new byte[len];
        InputStream is = new FileInputStream(filename);
        while((currRead = is.read(buffer, 0, 1024)) != -1) {
            if(read + currRead >= len) {
                len *= 2;
                input = realloc(input, len);
            }
            System.arraycopy(buffer, 0, input, read, currRead);
            read += currRead;
        }
        int times = 10000;
        long before = System.currentTimeMillis();
        for(int i=0; i<times; i++) {
            YAML.load(new ByteList(input, 0, read));
        }
        long after = System.currentTimeMillis();
        System.err.println("parsing " + filename + " " + times + " times took " + (after-before) + "ms");
    }
}

