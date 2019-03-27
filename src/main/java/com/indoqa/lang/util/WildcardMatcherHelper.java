/*
 * Licensed to the Indoqa Software Design und Beratung GmbH (Indoqa) under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Indoqa licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indoqa.lang.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class is a utility class that performs wildcard-patterns matching and isolation.
 *
 * @see "http://svn.apache.org/repos/asf/cocoon/cocoon3/trunk/cocoon-util/src/main/java/org/apache/cocoon/util/wildcard/
 * WildcardMatcherHelper.java"
 */
public final class WildcardMatcherHelper {
    // ~ Static fields/initializers -----------------------------------------------------------------

    /**
     * Default path separator: "/"
     */
    public static final char ESC = '\\';

    /**
     * Default path separator: "/"
     */
    public static final char PATHSEP = '/';

    /**
     * Default path separator: "/"
     */
    public static final char STAR = '*';

    // ~ Methods ------------------------------------------------------------------------------------

    /**
     * Cache for compiled pattern matchers
     */
    private static final Map<String, Matcher> CACHE = new HashMap<>();

    private WildcardMatcherHelper() {
        // hide utility class constructor
    }

    /**
     * Match a pattern against a string and isolate wildcard replacements into a <code>Map</code>. <br>
     * Here is how the matching algorithm works:
     *
     * <ul>
     * <li>The '*' character, meaning that zero or more characters (excluding the path separator '/') are to be matched.</li>
     * <li>The '**' sequence, meaning that zero or more characters (including the path separator '/') are to be matched.</li>
     * <li>The '\*' sequence is honored as a literal '*' character, not a wildcard</li>
     * </ul>
     * <br>
     * When more than two '*' characters, not separated by another character, are found their value is considered as '**' and immediate
     * succeeding '*' are skipped. <br>
     * The '**' wildcard is greedy and thus the following sample matches as {"foo/bar","baz","bug"}:
     * <dl>
     * <dt>pattern</dt>
     * <dd>&#42;&#42;/&#42;/&#42;&#42;</dd>
     * <dt>string</dt>
     * <dd>foo/bar/baz/bug</dd>
     * </dl>
     * The first '**' in the pattern will suck up as much as possible without making the match fail.
     *
     * @param pat The pattern string.
     * @param str The string to match against the pattern
     * @return a <code>Map</code> containing the representation of the extracted pattern. The extracted patterns are keys in the
     * <code>Map</code> from left to right beginning with "1" for the left most, "2" for the next, a.s.o. The key "0" is the
     * string itself. If the return value is null, string does not match to the pattern.
     */
    public static Map<String, String> match(final String pat, final String str) {
        Matcher matcher;
        synchronized (CACHE) {
            matcher = CACHE.get(pat);
            if (matcher == null) {
                matcher = new Matcher(pat);
                CACHE.put(pat, matcher);
            }
        }

        String[] list = matcher.getMatches(str);
        if (list == null) {
            return null;
        }

        int n = list.length;
        Map<String, String> map = new HashMap<String, String>(n * 2 + 1);
        for (int i = 0; i < n; i++) {
            map.put(String.valueOf(i), list[i]);
        }

        return map;
    }

    // ~ Inner Classes ------------------------------------------------------------------------------

    /**
     * Compile wildcard pattern into regexp pattern.
     *
     * @param pat The wildcard pattern
     * @return compiled regexp program.
     */
    private static Pattern compileRegexp(String pat) {
        StringBuilder repat = new StringBuilder(pat.length() * 6);
        repat.append('^');

        // Add an extra character to allow unchecked wcpat[i+1] accesses.
        // Unterminated ESC sequences are silently handled as '\\'.
        char[] wcpat = (pat + ESC).toCharArray();
        for (int i = 0, n = pat.length(); i < n; i++) {
            char ch = wcpat[i];

            if (ch == STAR) {
                if (wcpat[i + 1] != STAR) {
                    repat.append("([^/]*)");
                    continue;
                }

                // Handle two and more '*' as single '**'.
                while (wcpat[i + 1] == STAR) {
                    i++;
                }
                repat.append("(.*)");
                continue;
            }

            // Match ESC+ESC and ESC+STAR as literal ESC and STAR which needs to be escaped
            // in regexp. Match ESC+other as two characters ESC+other where other may also
            // need to be escaped in regexp.
            if (ch == ESC) {
                ch = wcpat[++i];
                if (ch != ESC && ch != STAR) {
                    repat.append("\\\\");
                }
            }

            if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9' || ch == '/') {
                repat.append(ch);
                continue;
            }

            repat.append('\\');
            repat.append(ch);
        }
        repat.append('$');

        return Pattern.compile(repat.toString());
    }

    /**
     * The private matcher class
     */
    private static class Matcher {

        /**
         * Regexp to split constant parts from front and back leaving wildcards in the middle.
         */
        private static final Pattern splitter;
        /**
         * Wildcard types to short-cut simple '*' and "**' matches.
         */
        private static final int WC_CONST = 0;
        private static final int WC_STAR = 1;
        private static final int WC_STARSTAR = 2;
        private static final int WC_REGEXP = 3;

        static {
            final String fixedRE = "([^*\\\\]*)";
            final String wcardRE = "(.*[*\\\\])";
            final String splitRE = "^" + fixedRE + wcardRE + fixedRE + "$";
            splitter = Pattern.compile(splitRE);
        }

        // ~ Instance fields ------------------------------------------------------------------------

        // All fields declared final to emphasize requirement to be thread-safe.

        /**
         * Fixed text at start of pattern.
         */
        private final String prefix;

        /**
         * Fixed text at end of pattern.
         */
        private final String suffix;

        /**
         * Length of prefix and suffix.
         */
        private final int fixlen;

        /**
         * Wildcard type of pattern.
         */
        private final int wctype;

        /**
         * Compiled regexp equivalent to wildcard pattern between prefix and suffix.
         */
        private final Pattern regexp;

        // ~ Constructors ---------------------------------------------------------------------------

        /**
         * Creates a new Matcher object.
         *
         * @param pat The pattern
         * @param str The string
         */
        Matcher(final String pat) {
            java.util.regex.Matcher re = splitter.matcher(pat);

            if (re.matches()) {

                // Split pattern into (foo/)(*)(/bar).

                this.prefix = re.group(1);
                String wildcard = re.group(2);
                String tail = re.group(3);

                // If wildcard ends with \ then add the first char of postfix to wildcard.
                if (tail.length() != 0 && wildcard.charAt(wildcard.length() - 1) == ESC) {
                    wildcard = wildcard + tail.substring(0, 1);
                    this.suffix = tail.substring(1);
                }
                else {
                    this.suffix = tail;
                }

                // Use short-cuts for single * or ** wildcards

                if (wildcard.equals("*")) {
                    this.wctype = WC_STAR;
                    this.regexp = null;
                }
                else if (wildcard.equals("**")) {
                    this.wctype = WC_STARSTAR;
                    this.regexp = null;
                }
                else {
                    this.wctype = WC_REGEXP;
                    this.regexp = compileRegexp(wildcard);
                }
            }
            else {
                // Pattern is a constant without '*' or '\'.
                this.prefix = pat;
                this.suffix = "";
                this.wctype = WC_CONST;
                this.regexp = null;
            }

            this.fixlen = this.prefix.length() + this.suffix.length();
        }

        // ~ Methods --------------------------------------------------------------------------------

        /**
         * Match string against pattern.
         *
         * @param str The string
         * @return list of wildcard matches, null if match failed
         */
        String[] getMatches(final String str) {

            // Protect against 'foo' matching 'foo*foo'.
            if (str.length() < this.fixlen) {
                return null;
            }

            if (!str.startsWith(this.prefix)) {
                return null;
            }

            if (!str.endsWith(this.suffix)) {
                return null;
            }

            String infix = str.substring(this.prefix.length(), str.length() - this.suffix.length());

            if (this.wctype == WC_REGEXP) {
                java.util.regex.Matcher re = this.regexp.matcher(infix);
                if (!re.matches()) {
                    return null;
                }

                int n = re.groupCount();
                String[] list = new String[n + 1];
                list[0] = str;
                for (int i = 1; i <= n; i++) {
                    list[i] = re.group(i);
                }
                return list;
            }

            if (this.wctype == WC_CONST) {
                if (infix.length() != 0) {
                    return null;
                }
                return new String[] {str};
            }

            if (this.wctype == WC_STAR) {
                if (infix.indexOf(PATHSEP) != -1) {
                    return null;
                }
            }

            return new String[] {str, infix};
        }
    }
}
