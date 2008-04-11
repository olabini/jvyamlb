/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.util;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class IntStack {
    private int[] values;
    private int capacity;
    private int index;

    public IntStack() {
        this(27);
    }
    public IntStack(int size) {
        this.values = new int[size];
        this.capacity = size-1;
        this.index = -1;
    }

    public boolean isEmpty() {
        return index == -1;
    }

    public int size() {
        return index + 1;
    }
    
    public int get(int ix) {
        if(ix < 0 || ix > index) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + ix);
        }
        return values[ix];
    }

    public int pop() {
        if(index == -1) {
            throw new IllegalStateException("Can't pop from an empty stack");
        }
        return values[index--];
    }

    public void push(int value) {
        values[++index] = value;
        if(index == capacity) {
            int[] newValues = new int[this.values.length*2];
            System.arraycopy(values,0,newValues,0,this.values.length);
            capacity = newValues.length - 1;
            values = newValues;
        }
    }
}// IntStack
