/*
    jaiml - java AIML library
    Copyright (C) 2004, 2009  Kim Sullivan

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package aiml.classifier;

import aiml.classifier.node.PatternNode;

import com.ibm.icu.text.Transliterator;

/**
 * Utility methods for analyzing patterns.
 * <p>
 * <i>Note to self:</i> I don't know why, but this somehow feels wrong,
 * shouldn't I be passing an instance of a secial pattern class (that
 * encapsulates the depth and the pattern string, and provides lexical
 * analysis), instead of a string and depth? How does this class relate to the
 * {@link aiml.classifier.PatternSequence.Pattern PatternSequence.Pattern} inner
 * class?
 * </p>
 * 
 * @author Kim Sullivan
 * @version 1.0
 */

public class Pattern {

  /**
   * A transliterator that removes diacritics and turns the result into
   * uppercase. Used for pattern normalization.
   */
  private static Transliterator normalizer = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; Upper; NFC;");

  /**
   * Returns the type of the pattern at the current depth. This can be either
   * PatternNode.STAR PatternNode.STRING or PatternNode.UNDERSCORE.
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return the type of pattern
   */
  public static int getType(int depth, String pattern) {
    if (depth == pattern.length()) {
      return PatternNode.STRING;
    }
    switch (pattern.charAt(depth)) {
    case '*':
      return PatternNode.STAR;
    case '_':
      return PatternNode.UNDERSCORE;
    default:
      return PatternNode.STRING;
    }
  }

  /**
   * Determines if the current character in the pattern is a wildcard
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return <code>true</code> if the current character in the pattern is a
   *         wildcard <code>false</code> otherwise
   */
  public static boolean isWildcard(int depth, String pattern) {
    if (depth == pattern.length()) {
      return false;
    }
    char c = pattern.charAt(depth);
    return (c == '*' || c == '_');
  }

  /**
   * Determines if the current character in the pattern is a * wildcard
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return <code>true</code> if the current character in the pattern is a *
   *         wildcard <code>false</code> otherwise
   */
  public static boolean isStar(int depth, String pattern) {
    if (depth == pattern.length()) {
      return false;
    }
    char c = pattern.charAt(depth);
    return (c == '*');
  }

  /**
   * Determines if the current character in the pattern is a _ wildcard
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return <code>true</code> if the current character in the pattern is a _
   *         wildcard <code>false</code> otherwise
   */

  public static boolean isUnderscore(int depth, String pattern) {
    if (depth == pattern.length()) {
      return false;
    }
    char c = pattern.charAt(depth);
    return (c == '_');
  }

  /**
   * Retrieves the position of the next * wildcard
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return the position of the next * wildcard; -1 if it is not present
   */
  public static int nextStar(int depth, String pattern) {
    return pattern.indexOf('*', depth);
  }

  /**
   * Retrieves the position of the next _ wildcard
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return the position of the next _ wildcard; -1 if it is not present
   */
  public static int nextUnderscore(int depth, String pattern) {
    return pattern.indexOf('_', depth);
  }

  /**
   * Retrieves the position of the next wildcard
   * 
   * @param depth
   *          the depth
   * @param pattern
   *          the pattern
   * @return the position of the next wildcard; -1 if it is not present
   */

  public static int nextWildcard(int depth, String pattern) {
    int ns = nextStar(depth, pattern);
    int nu = nextUnderscore(depth, pattern);

    if (ns >= 0 && nu > 0) {
      return (ns < nu ? ns : nu);
    }
    if (ns < 0) {
      return nu;
    }
    return ns;
  }

  /**
   * Returns the length of the common prefix of the two strings a and b.
   * 
   * @param a
   *          a string
   * @param b
   *          a string
   * @return the length of the longest common prefix of strings a and b
   */
  public static int prefixLength(String a, String b) {
    if (a.length() == 0 || b.length() == 0) {
      return 0;
    }
    int length = 0;

    while (a.charAt(length) == b.charAt(length)) {
      length++;
      if (a.length() == length || b.length() == length) {
        return length;
      }
    }
    return length;
  }

  /**
   * Normalize a string according to pattern matching rules. This means that:
   * <ol>
   * <li>all diacritical marks are stripped and</li>
   * <li>characters are converted to uppercase.</li>
   * </ol>
   * This method is called both when adding a pattern to the classifier, as well
   * as on input that is matched.
   * 
   * @param s
   * @return
   */
  public static String normalize(String s) {
    return normalizer.transliterate(s);
  }

  /**
   * Normalize a string according to pattern matching rules. This means that:
   * <ol>
   * <li>all diacritical marks are stripped and</li>
   * <li>characters are converted to uppercase.</li>
   * </ol>
   * This method is called both when adding a pattern to the classifier, as well
   * as on input that is matched.
   * 
   * @param ch
   * @return
   */
  public static char normalize(char ch) {
    return normalizer.transliterate(String.valueOf(ch)).charAt(0);
  }

}
