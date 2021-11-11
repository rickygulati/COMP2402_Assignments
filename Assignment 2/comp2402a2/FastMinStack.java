package comp2402a2;

import java.util.Comparator;
import java.util.List;

import javax.lang.model.util.ElementScanner14;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayDeque;

public class FastMinStack<T> implements MinStack<T> {

  protected Comparator<? super T> comp;
  ArrayDeque<T> myStack;
  ArrayDeque<T> minStack;

  public FastMinStack() {
    this(new DefaultComparator<T>());
  }

  public FastMinStack(Comparator<? super T> comp) {
    this.comp = comp;
    myStack = new ArrayDeque<>();
    minStack = new ArrayDeque<>();
  }

  public void push(T x) {
    if(myStack.isEmpty()){
      minStack.add(x);
      myStack.add(x);
    }
    else if(comp.compare(x, minStack.getLast()) < 0){
      minStack.add(x);
      myStack.add(x);
    }
    else{
      minStack.add(minStack.getLast());
      myStack.add(x);
    }
  }

  public T pop() {
    minStack.pollLast();
    return myStack.pollLast();
  }

  public T min() {
    return minStack.getLast();
  }

  public int size() {
    return myStack.size();
  }

  public Iterator<T> iterator() {
    if(!myStack.isEmpty())  
      return myStack.iterator();
    return null;
  }

  public Comparator<? super T> comparator() {
    return comp;
  }
}
