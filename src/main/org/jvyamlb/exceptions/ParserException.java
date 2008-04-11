/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb.exceptions;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ParserException extends YAMLException {
    private String when;
    private String what;
    private String note;

    public ParserException(final String when, final String what, final String note) {
        super("ParserException " + when + " we had this " + what);
        this.when = when;
        this.what = what;
        this.note = note;
    }

    public ParserException(final Throwable thr) {
        super(thr);
    }
    
    public String toString() {
        final StringBuffer lines = new StringBuffer();
        if(this.when != null) {
            lines.append(this.when).append("\n");
        }
        if(this.what != null) {
            lines.append(this.what).append("\n");
        }
        if(this.note != null) {
            lines.append(this.note).append("\n");
        }
        lines.append(super.toString());
        return lines.toString();
    }
}// ParserException
