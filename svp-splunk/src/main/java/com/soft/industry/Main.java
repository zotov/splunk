package com.soft.industry;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

	public static void main(String[] args) throws Throwable {

		/*
		 * @SuppressWarnings("resource") ApplicationContext context = new
		 * ClassPathXmlApplicationContext(new String[]{"applicationContext.xml",
		 * "hibernate-config.xml"}); StandaloneServer server =
		 * context.getBean(StandaloneServer.class); server.executeActions();
		 */

		// value = FileUtils.readFileToString(new
		// File("/home/denis/Projects/value1.txt"));
		// String regex_= "[a-zA-Z0-9-_',\\.]+";
		// String regex="(?s).*[a-zA-Z].*";
		// String regex="(?m)^[a-zA-Z]+$";
		// String regex="[\\x00-\\x7F]+";

		// String value_=value.replaceAll(regex_, "");
		// boolean result = value.matches(regex);

		// Pattern pattern = Pattern.compile(regex);
		// boolean result = pattern.matcher(value).matches();

		/*
		 * String result =
		 * "entrance_1_2_3_teststreet_street_1.png".replaceAll("entrance_\\d+_"
		 * ,""); result = result.substring(0, result.indexOf("_"));
		 */

		// System.out.println("value=" + value);
		// System.out.println("value1=" + value_);
		/* System.out.println("matches=" + result); */

		/*
		 * splitString("123abc345def"); splitString("123AbC345Def");
		 * splitString("9її123abc345ююж"); splitString("ъъї123abc345ююж");
		 * splitString("її123abc345ююж"); splitString("123ППіі345юВж");
		 */

		// System.out.println(String.format("hello-%s hello-%s", "?", "5" ));

		// compareAlphanum();

		// processResource();

		//isMobile();
		
		//serializable();	
		//checkException();
		
		calcFib(13);
	}

	static String value = "llllllllllllBe preparedi";

	public static void splitString(final String data) {
		Pattern p = Pattern.compile("[a-zA-Z\u0400-\u04FF]+|\\d+");
		Matcher m = p.matcher(data);
		ArrayList<String> allMatches = new ArrayList<String>();
		while (m.find()) {
			allMatches.add(m.group());
		}

		System.out.println("result:");
		for (String str : allMatches) {
			System.out.println(str);
		}
		System.out.println("");
	}

	public static void compareAlphanum() {
		AlphanumComparator alp = new AlphanumComparator();
		List<String> data1 = new ArrayList<String>(Arrays.asList(
				"Callisto Morphamax", "Callisto Morphamax 500",
				"Callisto Morphamax 5000", "Callisto Morphamax 600",
				"Callisto Morphamax 6000 SE2", "Callisto Morphamax 6000 SE",
				"Callisto Morphamax 700", "Callisto Morphamax 7000"));
		List<String> data = new ArrayList<String>(Arrays.asList(
				"Пввівтаі Мьббюьовш 600", "Пввівтаі Мьббюьовш 500",
				"Пввівтаі Мьббюьовш 5000", "Пввівтаі Мьббюьовш 6000 ВА2",
				"Пввівтаі Мьббюьовш 6000 ВА", "Пввівтаі Мьббюьовш",
				"Пввівтаі Мьббюьовш 700", "Пввівтаі Мьббюьовш 7000"));

		List<String> data2 = new ArrayList<String>(Arrays.asList(
				"вул. Вишнёвая", "вул. Жуковського", "вул. Чайковського",
				"вул. Шкільна", "вул. Шмідта", "вул. Ярова", "вул. Яцівська",
				"вул. Єлецька", "вул. І. Шрага"));

		Collections.sort(data2, alp);

		for (String str : data2) {
			System.out.println(str);
		}
	}

	public static void processResource() {
		String refer = "var hrefCss='<link href=\"${resourceUrl}/css/common/styleSPcenter.css\" rel=\"stylesheet\" type=\"text/css\" />';";
		String pattern = "^var[a-zA-Z_0-9 ]+=[ '\"]+.+[ '\"]+;$";
		if (refer.matches(pattern)) {
			refer = refer.substring(0, refer.indexOf("=") + 1) + "'" + refer
					+ "';";
			System.out.println(refer);
		} else {
			System.out.println("not mutch pattern=" + pattern);
		}
	}

	public static void isMobile() {
		// String
		// mobileDevPattern="(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge
		// |maemo|midp|mmp|netfront|opera m(ob|in)i|palm(
		// os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows
		// (ce|phone)|xda|xiino/i.test(navigator.userAgent)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a
		// wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r
		// |s
		// )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1
		// u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp(
		// i|ip)|hs\-c|ht(c(\-|
		// |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac(
		// |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt(
		// |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg(
		// g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-|
		// |o|v)|zz)|mt(50|p1|v
		// )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v
		// )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-|
		// )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-";
		String mobileDevPattern1 = "1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-";
		String mobileDevPattern2 = "(android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino";
		Pattern check1IsDeviceMobile = Pattern.compile(mobileDevPattern1,
				Pattern.CASE_INSENSITIVE);
		Pattern check2IsDeviceMobile = Pattern.compile(mobileDevPattern2,
				Pattern.CASE_INSENSITIVE);

		String header = "Mozilla/5.0 (Linux; Android 4.4.4; Samsung Galaxy S4 - 4.4.4 - API 19 - 1080x1920 Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36";
		header = header.toLowerCase();
		Matcher m = check1IsDeviceMobile.matcher(header);
		System.out.println("isMobile1=" + m.matches());
		m = check2IsDeviceMobile.matcher(header);
		System.out.println("isMobile2=" + m.matches());
	}
	
	 public static void serializable() throws InterruptedException {
	        String filename = "A.ser";
	        
	        A a = new A();

	        // save the object to file
	        FileOutputStream fos = null;
	        ObjectOutputStream out = null;
	        try {
	            fos = new FileOutputStream(filename);
	            out = new ObjectOutputStream(fos);
	            out.writeObject(a);

	            out.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            Thread.sleep(500);
	        }
	        
	        System.out.println("serializable object=" + a);
	        Thread.sleep(500);
	        
	        // read the object from file
	        // save the object to file
	        FileInputStream fis = null;
	        ObjectInputStream in = null;
	        try {
	            fis = new FileInputStream(filename);
	            in = new ObjectInputStream(fis);
	            a = (A) in.readObject();
	            in.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            Thread.sleep(500);
	        }
	        System.out.println("deserializable object=" + a);
	        
	        
	    }
	 
	 public  static void checkException() throws Throwable {
		 C c = new C();
		 D d = new D();
		 c.print_();
		 d.print_();
	 }
	 
	 
	 
	static  int[] fib_result;
	
	public static void calcFib(int n) {
		fib_result = new int[n+1];		
		Arrays.fill(fib_result,0);
		fib_result[1]=1;
		memo_fib(n);
		System.out.println(Arrays.toString(fib_result));
		
		Arrays.fill(fib_result,0);
		fib_result[1]=1;
		dp_fib(n);
		System.out.println(Arrays.toString(fib_result));
	}

	public static int  memo_fib( int n) {
	 int ret=0;
	   if ( n < 2 ) {		
	      return n;
	  }
	  if ( fib_result[n]  > 0) {
	      return fib_result[n];
	}
	  ret = memo_fib(n - 1) + memo_fib(n - 2);
	  fib_result[n] = ret;
	  return ret;
	}
	 
	public static void dp_fib(int n) {
	     int partial_answers[]={0,1};
	     int index=2;
	     while (index <= n) {
	         fib_result[index]=partial_answers[0] + partial_answers[1];
	         partial_answers[0]=partial_answers[1];
	         partial_answers[1]=fib_result[index];
	         index++;
	   }
	}
	 
	 

}

class AlphanumComparator implements Comparator<String> {

	private final boolean isDigit(char ch) {
		return ch >= 48 && ch <= 57;
	}

	/**
	 * Length of string is passed in for improved efficiency (only need to
	 * calculate it once)
	 **/
	private final String getChunk(String s, int slength, int marker) {
		StringBuilder chunk = new StringBuilder();
		char c = s.charAt(marker);
		chunk.append(c);
		marker++;
		if (isDigit(c)) {
			while (marker < slength) {
				c = s.charAt(marker);
				if (!isDigit(c))
					break;
				chunk.append(c);
				marker++;
			}
		} else {
			while (marker < slength) {
				c = s.charAt(marker);
				if (isDigit(c))
					break;
				chunk.append(c);
				marker++;
			}
		}
		return chunk.toString();
	}

	public int compare(final String s1, final String s2) {

		int thisMarker = 0;
		int thatMarker = 0;
		int s1Length = s1.length();
		int s2Length = s2.length();

		while (thisMarker < s1Length && thatMarker < s2Length) {
			String thisChunk = getChunk(s1, s1Length, thisMarker);
			thisMarker += thisChunk.length();

			String thatChunk = getChunk(s2, s2Length, thatMarker);
			thatMarker += thatChunk.length();

			// If both chunks contain numeric characters, sort them numerically
			int result = 0;
			if (isDigit(thisChunk.charAt(0)) && isDigit(thatChunk.charAt(0))) {
				// Simple chunk comparison by length.
				int thisChunkLength = thisChunk.length();
				result = thisChunkLength - thatChunk.length();
				// If equal, the first different number counts
				if (result == 0) {
					for (int i = 0; i < thisChunkLength; i++) {
						result = thisChunk.charAt(i) - thatChunk.charAt(i);
						if (result != 0) {
							return result;
						}
					}
				}
			} else {
				result = thisChunk.compareTo(thatChunk);
			}

			if (result != 0)
				return result;
		}

		return s1Length - s2Length;
	}
}



class A implements Serializable {

	static private String name;
	private String secondName;
	transient private Thread t;
	private AB ab;
		
	public A() {
		t = new Thread();
		//ab= new AB();
		name="first-name";
		this.secondName="second-name ";
		System.out.println("create A class:" + System.currentTimeMillis());
	}
	
	public String toString() {
		return "class A:" + name  + " " + secondName;
	}
	
	public static String getName( ) {
		return name;
	}
	
	 static class AB {
		 
		
		@SafeVarargs
		static final void m(List<String>... stringLists) {
			   Object[] array = stringLists;
			   List<Integer> tmpList = Arrays.asList(42);
			   array[0] = tmpList; // Semantically invalid, but compiles without warnings
			   String s = stringLists[0].get(0); // Oh no, ClassCastException at runtime!
		}
		
	    public AB() {
		   System.out.println("create AB class:" + System.currentTimeMillis());
		}
	}
}


@SuppressWarnings("rawtypes")
class C implements Comparator{
	public static void print(){}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//public void print_()  {
	public void print_() throws NullPointerException {
	//public void print_() throws IOException {
	//public void print_() throws Exception {
		Exception e = new Exception();
		IOException ioe=new IOException();
		//ioe=(IOException) e;
		e=ioe;		
		//public void print_() throws Throwable {
		//public void print_()  throws EOFException{
		System.out.println("print_ C:" + System.currentTimeMillis());
		System.out.println("print_ C: ioe=" + ioe + ", " + System.currentTimeMillis());
		System.out.println("print_ C: e=" + e + ", " + System.currentTimeMillis());
	}
}


class D extends C {
	
	public static void print(){};
	
	public void print1() throws Exception{
		print_();
	};
	
	@Override
	public void print_() {	
    //public void print_() throws RuntimeException {
	//public void print_() throws Error {
	//public void print_() throws NullPointerException {
	//public void print_()  throws EOFException{
	//public void print_() throws IOException {
	//public void print_() throws Exception {
		 System.out.println("print_ D:" + System.currentTimeMillis());
	}
	
	
}





