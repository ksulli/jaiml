package aiml.script;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import aiml.parser.AimlParserException;

public class ElementParserFactory {
  
  private static HashMap<String,Class<? extends Script>> elements = new HashMap<String, Class<? extends Script>>();
  private static Class<? extends Script> text;
  
  static {
    text=TextElement.class;
    elements.put("bot",BotElement.class);
    elements.put("set",SetElement.class);
    elements.put("get",GetElement.class);
    elements.put("random",RandomElement.class);
    elements.put("condition",ConditionElement.class);
    elements.put("star",StarElement.class);
    elements.put("thatstar",StarElement.class);
    elements.put("topicstar",StarElement.class);
    elements.put("srai",SraiElement.class);
    elements.put("think",ThinkElement.class);
    elements.put("formal",FormalElement.class);
    elements.put("uppercase",UppercaseElement.class);
    elements.put("lowercase",LowercaseElement.class);
    elements.put("sentence",SentenceElement.class);
    elements.put("that",ThatElement.class);
    elements.put("input",InputElement.class);
    elements.put("version",VersionElement.class);
    elements.put("size",SizeElement.class);
    elements.put("id",IDElement.class);
    elements.put("date",DateElement.class);
    elements.put("sr",SrElement.class);

  }
  
  private ElementParserFactory() {
    super();
  }
  
  public static void addElementParser(String name, Class<? extends Script> c) {
    elements.put(name, c);    
  }
  
  public static void addTextParser(Class<? extends Script> c) {
    text=c;
  }
  
  public static Script getElementParser(XmlPullParser parser) throws XmlPullParserException, AimlParserException, IOException {
    try {
      switch (parser.getEventType()) {
        case XmlPullParser.TEXT:
          if (text!=null) {                        
            return text.newInstance().parse(parser);
          } else
            throw new NullPointerException("Cannot handle text events "+parser.getPositionDescription());
        case XmlPullParser.START_TAG:
          if (elements.containsKey(parser.getName())) {
            return elements.get(parser.getName()).newInstance().parse(parser);
          }  
        default:
          throw new AimlParserException("Unexpected " + XmlPullParser.TYPES[parser.getEventType()] + " " + parser.getName() + " " + parser.getPositionDescription());
      }          
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
}
