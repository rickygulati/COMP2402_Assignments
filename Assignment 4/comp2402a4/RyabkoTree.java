

package comp2402a4;
import java.util.Random;
import java.util.Iterator;
import java.util.Arrays; 
import java.lang.*;
public class RyabkoTree implements PrefixStack {
  int intArray[];   
  long sumArray[];   
  int intTemp[];   
  long sumTemp[]; 
  int size;
  int length;

  public RyabkoTree() {
    length = 2;
    intArray = new int[length+1];  
    sumArray = new long[length+1];  
    size = 0;

    //javac *.java && java RyabkoTee
   // ArrayList<Integer> fool= new ArrayList<Integer>(n); 
  }

  public void push(int x) {
    set ( ++size,x);
      }

  public int pop() {
    return set((--size) ,0);
  }

  public int set(int i, int x) {
   i++;
   int temp = i;
   
  if (size  >= length){
    length *= 2;
    intTemp = new int[length+1];
    sumTemp = new long[length+1];
  System.arraycopy(sumArray, 1,sumTemp,1,size);
  System.arraycopy(intArray, 1,intTemp,1,size);
    sumTemp[length] = sumArray[size];
    intArray= intTemp;
    sumArray= sumTemp;
  }
    while(i <=length+1  ) { 
      sumArray[i] += x-intArray[temp];
       i += i & -i;
    } 
  int k = intArray[temp];
  intArray[temp] = x;
  return k;
  }


  
  public int get(int i) {
    return intArray[i+1];
  }
//javac *.java && ryabkotree.java
  public long prefixSum(int i ) {
    long sum = 0;
    i++;
    while (i > 0) {
    // System.out.println("the jumps"+sumArray[i]);
      sum += sumArray[i];
        i -= i & (-i);
    } 
    return sum;
  }


    public int size() {
        return size;
    }

    public Iterator<Integer> iterator() {
    return Arrays.stream(intArray).iterator();
  }
 
 
}
