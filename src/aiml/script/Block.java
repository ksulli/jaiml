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

package aiml.script;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import aiml.classifier.Classifier;
import aiml.classifier.MatchState;
import aiml.parser.AimlParserException;

public class Block implements Script {
  String blockName;
  ArrayList<Script> items = new ArrayList<Script>();

  public Script parse(XmlPullParser parser, Classifier classifier)
      throws XmlPullParserException, IOException, AimlParserException {
    blockName = parser.getName();
    Script lastScript;
    parser.next();
    while (!((parser.getEventType() == XmlPullParser.END_TAG) && parser.getName().equals(
        blockName))) {
      lastScript = ElementParserFactory.getElementParser(parser, classifier);
      if (!(lastScript instanceof EmptyScript))
        items.add(lastScript);
    }
    switch (items.size()) {
    case 1:
      return items.get(0);
    case 0:
      return new EmptyScript();
    default:
      return this;
    }
  }

  public void addScript(Script s) {
    items.add(s);
  }

  public String evaluate(MatchState m) {
    StringBuffer result = new StringBuffer();
    for (Script s : items) {
      result.append(s.evaluate(m));
    }
    return result.toString();
  }

  public String toString() {
    return items.toString();
  }
}
