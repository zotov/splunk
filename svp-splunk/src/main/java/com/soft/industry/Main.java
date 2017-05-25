package com.soft.industry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Home
 *
 */
public class Main {
	
		
	public static void main(String[] args) throws Exception {
				      
		/*@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml",
				 																	 "hibernate-config.xml"});
        StandaloneServer server = context.getBean(StandaloneServer.class);
        server.executeActions();*/
		
		
		//value = FileUtils.readFileToString(new File("/home/denis/Projects/value1.txt"));
		//String regex_= "[a-zA-Z0-9-_',\\.]+";
		//String regex="(?s).*[a-zA-Z].*";
		//String regex="(?m)^[a-zA-Z]+$";
		//String regex="[\\x00-\\x7F]+";
		
		//String value_=value.replaceAll(regex_, "");
		//boolean result = value.matches(regex);
		
		//Pattern pattern = Pattern.compile(regex);
		//boolean result = pattern.matcher(value).matches();
		
		
		/*String result = "entrance_1_2_3_teststreet_street_1.png".replaceAll("entrance_\\d+_","");
		result = result.substring(0, result.indexOf("_"));*/
				
	
		//System.out.println("value=" + value);	
		//System.out.println("value1=" + value_);
		/*System.out.println("matches=" + result);*/	
		
		/*splitString("123abc345def");
		splitString("123AbC345Def");
		splitString("9її123abc345ююж");
		splitString("ъъї123abc345ююж");
		splitString("її123abc345ююж");
		splitString("123ППіі345юВж");*/
		
		//System.out.println(String.format("hello-%s hello-%s", "?", "5" ));
		
		//compareAlphanum();
		
		processResource();
		
     }
		
	static String value="llllllllllllBe preparedi";	 
	
	
	public static void splitString(final String data) {
		Pattern p = Pattern.compile("[a-zA-Z\u0400-\u04FF]+|\\d+");
		Matcher m = p.matcher(data);
		ArrayList<String> allMatches = new ArrayList<>();
		while (m.find()) {
		    allMatches.add(m.group());
		}
		
		System.out.println("result:");
		for(String str: allMatches) {
			System.out.println(str);
		}
		System.out.println("");
	}



public static void compareAlphanum() {
	AlphanumComparator alp = new AlphanumComparator();	
       List<String>	data1 = new ArrayList<String>(Arrays.asList("Callisto Morphamax",
															   "Callisto Morphamax 500",
															   "Callisto Morphamax 5000",
															   "Callisto Morphamax 600",
															   "Callisto Morphamax 6000 SE2",
															   "Callisto Morphamax 6000 SE",															 
															   "Callisto Morphamax 700",
															   "Callisto Morphamax 7000"));
       List<String>	data = new ArrayList<String>(Arrays.asList("Пввівтаі Мьббюьовш 600",
															   "Пввівтаі Мьббюьовш 500",
															   "Пввівтаі Мьббюьовш 5000",
															   "Пввівтаі Мьббюьовш 6000 ВА2",
															   "Пввівтаі Мьббюьовш 6000 ВА",
															   "Пввівтаі Мьббюьовш",
															   "Пввівтаі Мьббюьовш 700",
															   "Пввівтаі Мьббюьовш 7000"));
       
       List<String>	data2 = new ArrayList<String>(Arrays.asList(
    		   													"вул. Вишнёвая",
    		   													"вул. Жуковського",	
																"вул. Чайковського",
																"вул. Шкільна",
																"вул. Шмідта",
																"вул. Ярова",
																"вул. Яцівська",
																"вул. Єлецька",		
																"вул. І. Шрага"));
       
       Collections.sort(data2, alp);
       
       for(String str : data2) {
    	   System.out.println(str);
       }       
  }

public static void  processResource() {
	String refer = "var hrefCss='<link href=\"${resourceUrl}/css/common/styleSPcenter.css\" rel=\"stylesheet\" type=\"text/css\" />';";
	String pattern = "^var[a-zA-Z_0-9 ]+=[ '\"]+.+[ '\"]+;$";
	if(refer.matches(pattern)) {
		refer = refer.substring(0, refer.indexOf("=")+1)+ "'" + refer + "';"; 
		 System.out.println(refer);
	} else {
		 System.out.println("not mutch pattern=" + pattern);
	}
}

}

class AlphanumComparator implements Comparator<String> {
	
    private final boolean isDigit(char ch)
    {
        return ch >= 48 && ch <= 57;
    }

    /** Length of string is passed in for improved efficiency (only need to calculate it once) **/
    private final String getChunk(String s, int slength, int marker)
    {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        marker++;
        if (isDigit(c))
        {
            while (marker < slength)
            {
                c = s.charAt(marker);
                if (!isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        } else
        {
            while (marker < slength)
            {
                c = s.charAt(marker);
                if (isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        }
        return chunk.toString();
    }

    public int compare(final String s1, final String s2)  {        

        int thisMarker = 0;
        int thatMarker = 0;
        int s1Length = s1.length();
        int s2Length = s2.length();

        while (thisMarker < s1Length && thatMarker < s2Length)
        {
            String thisChunk = getChunk(s1, s1Length, thisMarker);
            thisMarker += thisChunk.length();

            String thatChunk = getChunk(s2, s2Length, thatMarker);
            thatMarker += thatChunk.length();

            // If both chunks contain numeric characters, sort them numerically
            int result = 0;
            if (isDigit(thisChunk.charAt(0)) && isDigit(thatChunk.charAt(0)))
            {
                // Simple chunk comparison by length.
                int thisChunkLength = thisChunk.length();
                result = thisChunkLength - thatChunk.length();
                // If equal, the first different number counts
                if (result == 0)
                {
                    for (int i = 0; i < thisChunkLength; i++)
                    {
                        result = thisChunk.charAt(i) - thatChunk.charAt(i);
                        if (result != 0)
                        {
                            return result;
                        }
                    }
                }
            } else
            {
                result = thisChunk.compareTo(thatChunk);
            }

            if (result != 0)
                return result;
        }

        return s1Length - s2Length;
    }
}

