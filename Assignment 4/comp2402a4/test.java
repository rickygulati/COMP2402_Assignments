package comp2402a4;

import comp2402a4.RyabkoTree;
import comp2402a4.SlowPrefixStack;
import java.util.*;

public class test {

  public static void main(String args[]) {
    RyabkoTree r = new RyabkoTree();
    SlowPrefixStack s = new SlowPrefixStack();
    //Random rand = new Random(10);
    int arr[] = {2, 1, 7, 3, 2, 3, 4, 5, 6, 7, 8, 9};
    for(int i = 0; i < 12; i++) {
      //int random = rand.nextInt(i);
      int random = arr[i];
      r.push(random);
      s.push(random);
      System.out.println("SlowPrefixStack: added: " + s.get(i) + " prefixSum: " + s.prefixSum(i));
      System.out.println("RyabkoTree: added: " + r.get(i) + " prefixSum: " + r.prefixSum(i));
    }

    for(int i = 11; i >= 0; i--) {
      //int random = rand.nextInt(i);
      //int random = i;
      System.out.println();
      System.out.println("SlowPrefixStack: to pop: " + s.get(i) + " prefixSum: " + s.prefixSum(i));
      System.out.println("RyabkoTree: to pop: " + r.get(i) + " prefixSum: " + r.prefixSum(i));

      System.out.println("SlowPrefixStack: popped: " + s.pop());
      System.out.println("RyabkoTree: popped: " + r.pop());


      // System.out.println("SlowPrefixStack: after pop: " + s.get(i) + " prefixSum: " + s.prefixSum(i));
      // System.out.println("RyabkoTree: after pop: " + r.get(i) + " prefixSum: " + r.prefixSum(i));
    }
  }
}