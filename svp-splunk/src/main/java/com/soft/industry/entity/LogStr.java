package com.soft.industry.entity;

public class LogStr {
	
	private StringBuffer strBuff = new StringBuffer();
	
	public  LogStr append (String param, String ... values) {
		    if(strBuff.length() > 0){
		     if(strBuff.charAt(strBuff.length()-1)=='#'){
		    	 strBuff.append(" ");
		     } else {
		      strBuff.append(", ");
		     }
		    } 
		    strBuff.append(param).append(":[");
		    int index = 0;
		    for(String value:values) {
		      if(index>0) {
		    	  strBuff.append(", ");
		      }
		      strBuff.append(value);
		      index++;
		    }
		    strBuff.append("]");
		return this;
	}
	
	public static LogStr getInstance(){
		return new LogStr();
	}
	
	public String toString() {
		return strBuff.append("#").toString();
	}
}
