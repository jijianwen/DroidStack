/**
 * CODE by: Kostya Vasilyev (http://goo.gl/8XLGsj)
 */

package org.stackdroid.utils;

import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.NumberKeyListener;

public class IPv6AddressKeyListener extends NumberKeyListener {
 
    private char[] mAccepted;
    private static IPv6AddressKeyListener sInstance;
 
    @Override
    protected char[] getAcceptedChars() {
        return mAccepted;
    }
 
    /**
     * The characters that are used.
     * 
     * @see KeyEvent#getMatch
     * @see #getAcceptedChars
     */
    private static final char[] CHARACTERS =
 
    new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':' };
 
    private IPv6AddressKeyListener() {
        mAccepted = CHARACTERS;
    }
 
    /**
     * Returns a IPAddressKeyListener that accepts the digits 0 through 9, plus the dot
     * character, subject to IP address rules: the first character has to be a digit, and
     * no more than 3 dots are allowed.
     */
    public static IPv6AddressKeyListener getInstance() {
        if (sInstance != null) return sInstance;
 
        sInstance = new IPv6AddressKeyListener();
        return sInstance;
    }
 
    /**
     * Display a number-only soft keyboard.
     */
    public int getInputType() {
        return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }
 
    /**
     * Filter out unacceptable dot characters.
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
            int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
 
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }
 
        int decimal = -1;
        int dlen = dest.length();
 
        // Prevent two dot characters in a row
        if (dstart > 0 && dest.charAt(dstart - 1) == ':') {
            decimal = dstart - 1;
        }
        if (dend < dlen && dest.charAt(dend) == ':') {
            decimal = dend;
        }
 
        // Up to three dot charcters, and no more
        if (decimal == -1) {
            int decimalCount = 0;
            for (int i = 0; i < dstart; i++) {
                char c = dest.charAt(i);
 
                if (c == ':') {
                    decimalCount++;
                    decimal = i;
                }
            }
            for (int i = dend; i < dlen; i++) {
                char c = dest.charAt(i);
 
                if (c == ':') {
                    decimalCount++;
                    decimal = i;
                }
            }
 
            if (decimalCount < 3) {
                decimal = -1;
            }
        }
 
        SpannableStringBuilder stripped = null;
 
        for (int i = end - 1; i >= start; i--) {
            char c = source.charAt(i);
            boolean strip = false;
 
            if (c == ':') {
                if (i == start && dstart == 0) {
                    strip = true;
                } else if (decimal >= 0) {
                    strip = true;
                } else {
                    decimal = i;
                }
            }
 
            if (strip) {
                if (end == start + 1) {
                    return ""; // Only one character, and it was stripped.
                }
 
                if (stripped == null) {
                    stripped = new SpannableStringBuilder(source, start, end);
                }
 
                stripped.delete(i - start, i + 1 - start);
            }
        }
 
        if (stripped != null) {
            return stripped;
        } else if (out != null) {
            return out;
        } else {
            return null;
        }
    }
}
