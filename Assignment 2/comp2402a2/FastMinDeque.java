package comp2402a2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Deque;

import javax.lang.model.util.ElementScanner14;

import java.util.LinkedList;
import java.util.Iterator;

public class FastMinDeque<T> implements MinDeque<T> {

  protected Comparator<? super T> comp;
  ArrayDeque<T> front;
  ArrayDeque<T> back;
  ArrayDeque<T> minBack;
  ArrayDeque<T> minFront;

  public FastMinDeque() {
    this(new DefaultComparator<T>());
  }

  public FastMinDeque(Comparator<? super T> comp) {
    this.comp = comp;
    front = new ArrayDeque<>();
    back = new ArrayDeque<>();
    minBack = new ArrayDeque<>();
    minFront = new ArrayDeque<>();
  }

  public void addFirst(T x) {
    if(front.isEmpty()){
      front.addFirst(x);
      minFront.addFirst(x);
    }
    else if(comp.compare(x, minFront.getFirst()) < 0){
      front.addFirst(x);
      minFront.addFirst(x);
    }
    else{
      front.addFirst(x);
      minFront.addFirst(minFront.getFirst());
    }
    balance();
  }

  public void addLast(T x) {
    if(back.isEmpty()){
      back.addLast(x);
      minBack.addLast(x);
    }
    else if(comp.compare(x, minBack.getLast()) < 0){
      back.addLast(x);
      minBack.addLast(x);
    }
    else{
      back.addLast(x);
      minBack.addLast(minBack.getLast());
    }
    balance();
  }

  public T removeFirst() {
     if(!front.isEmpty()){ 
      minFront.pollFirst();
      balance();
      return front.pollFirst();
     }
     else{
       minBack.pollFirst();
       balance();
       return back.pollFirst();
     }
  }

  public T removeLast() {
    if(!back.isEmpty()){  
      minBack.pollLast();
      balance();
      return back.pollLast();
    }
    else{
      minFront.pollLast();
      balance();
      return front.pollLast();
    }
  }

  public T min() {
    if(!front.isEmpty() && !back.isEmpty())
      return (comp.compare(minFront.getFirst(), minBack.getLast()) < 0)? minFront.getFirst(): minBack.getLast();
    else if(front.isEmpty())
      return minBack.getLast();
    else
      return minFront.getFirst();
  }

  public int size() {
    return (front.size() + back.size());
  }

  public Iterator<T> iterator() {
     ArrayList<T> iterator = new ArrayList<>();
     iterator.addAll(front);
     iterator.addAll(back);
     if(!iterator.isEmpty()){
       return iterator.iterator();
     }
     return null;
  }

  public Comparator<? super T> comparator() {
    return comp;
  }

  private void balance()
  {
    int n = size();
    Deque<T> dFront = new ArrayDeque<>();
    Deque<T> dBack = new ArrayDeque<>();

    //front<back
    if (3*front.size() < back.size()) 
    {
        int s = n/2 - front.size();
      
        List<T> tempBack = new LinkedList<T>();

        minFront.clear();
        minBack.clear();

       
        tempBack.addAll(back);
        
        dFront.addAll(front);
        dFront.addAll(tempBack.subList(0,s));

        dBack.addAll(tempBack.subList(s,tempBack.size()));

        front.clear();
        front.addAll(dFront);

        back.clear();
        back.addAll(dBack);


//Minimum deque front
        while(!dFront.isEmpty())
        {
          T temp=dFront.getFirst();
          if(minFront.isEmpty())
          {
            minFront.addFirst(temp);
            dFront.pollFirst();

          }
          else if(comp.compare(temp,minFront.getFirst())<0)
          {
            minFront.addFirst(temp);
            dFront.pollFirst();

          }
          else
          {
            minFront.addFirst(minFront.getFirst());
            dFront.pollFirst();

          }

        }

       //minimum deque back 
        while(!dBack.isEmpty())
        {
          T temp=dBack.getLast();
          if(minBack.isEmpty())
          {
            minBack.addLast(temp);
            dBack.pollLast();

          }
          else if(comp.compare(temp,minBack.getLast())<0)
          {
            minBack.addLast(temp);
            dBack.pollLast();

          }
          else
          {
            minBack.addLast(minBack.getLast());
            dBack.pollLast();

          }

        }

    }

//front>back
        else if (3* back.size()<front.size()) 
        {
          int t = front.size()-n/2;
        
          List<T> tempFront = new LinkedList<T>();

          minFront.clear();
          minBack.clear();
  
         
          tempFront.addAll(front);
          
          dBack.addAll(tempFront.subList(t,front.size()));
          dBack.addAll(back);
          
          dFront.addAll(tempFront.subList(0,t));
  
          front.clear();
          front.addAll(dFront);
  
          back.clear();
          back.addAll(dBack);
  
  
  //minimum deque front
          while(!dFront.isEmpty())
          {
            T temp=dFront.getFirst();
            if(minFront.isEmpty())
            {
              minFront.addFirst(temp);
              dFront.pollFirst();
  
            }
            else if(comp.compare(temp,minFront.getFirst())<0)
            {
              minFront.addFirst(temp);
              dFront.pollFirst();
  
            }
            else
            {
              minFront.addFirst(minFront.getFirst());
              dFront.pollFirst();
  
            }
  
          }
  
          //minimum deque back
          while(!dBack.isEmpty())
          {
            T temp=dBack.getLast();
            if(minBack.isEmpty())
            {
              minBack.addLast(temp);
              dBack.pollLast();
  
            }
            else if(comp.compare(temp,minBack.getLast())<0)
            {
              minBack.addLast(temp);
              dBack.pollLast();
  
            }
            else
            {
              minBack.addLast(minBack.getLast());
              dBack.pollLast();
  
            }
  
        
          }
        
      }
  
  }

}
