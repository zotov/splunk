package com.soft.industry;

public class Algorithm {
		
	public static void main(String []args) {
		System.out.println("Hello");
		TestBinarySearch tb=new TestBinarySearch();
		tb.testSearch();
	}

}

class TestBinarySearch {
	
	 public int binarySearchRecursive(int[] array, int startPos, int maxPosition, int target) {
	 
	     if(startPos <= maxPosition) {
	 
	    	int midPos=startPos +(maxPosition-startPos)/2;
	    	 
	    	 
	        if(array[midPos] == target) {
	            return midPos;
	        }
	        
	        int result;
	        if(array[midPos] < target) {	           
	            result = binarySearchRecursive(array, midPos+1, maxPosition, target);
	        } else {	          
	            result = binarySearchRecursive(array, startPos, midPos-1, target);
	        }	        	       
	        return result;       
	     }
	     
	     return -1;
	 }
	 
	 public int bynarySearchIterative(int[]array, int minPos, int maxPos, int target){


		    while( minPos <= maxPos) {
		    
		        int midPos= minPos + (maxPos - minPos)/2;
		            
		        if(array[midPos] == target) {
		           return midPos;
		        }
		        
		        if(array[midPos] > target) {        
		          maxPos=midPos-1;
		        } else {    
		          minPos=midPos+1;
		        } 		    
		    };
		    
		    return -1;

		}
	 
	 
	 public  void testSearch() {

		   int []sortedArray= new int[]{1,3,4,5,6,7,8,9,11,34};
		   int targetValue=2;
		   int length=sortedArray.length;
		   int i = targetValue;
		   for( i=0; i<35; i++) {
			  targetValue=i;
		      System.out.println(i+") targe=" + targetValue + ", position=" + binarySearchRecursive(sortedArray, 0, length-1, targetValue));
		      System.out.println(i+") targe=" + targetValue + ", position=" + bynarySearchIterative(sortedArray, 0, length-1, targetValue));
		   }

	 }
}