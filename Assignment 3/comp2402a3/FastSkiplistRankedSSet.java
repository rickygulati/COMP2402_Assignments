package comp2402a3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/**
 * An implementation of skiplists for searching
 *
 * @author morin
 *
 * @param <T>
 */
public class FastSkiplistRankedSSet<T> implements RankedSSet<T> {
	protected Comparator<T> c;

	@SuppressWarnings("unchecked")
	protected static class Node<T> {
		T x;
		Node<T>[] next;
		int[] length;
		public Node(T ix, int h) {
			x = ix;
			next = (Node<T>[])Array.newInstance(Node.class, h+1);
			length = new int[h + 1];
		}
		public int height() {
			return next.length - 1;
		}
	}

	/**
	 * This node<T> sits on the left side of the skiplist
	 */
	protected Node<T> sentinel;

	/**
	 * The maximum height of any element
	 */
	int h;

	/**
	 * The number of elements stored in the skiplist
	 */
	int n;

	/**
	 * A source of random numbers
	 */
	Random rand;

	/**
	 * Used by add(x) method
	 */
	protected Node<T>[] stack;

	@SuppressWarnings("unchecked")
	public FastSkiplistRankedSSet(Comparator<T> c) {
		this.c = c;
		n = 0;
		sentinel = new Node<T>(null, 32);
		stack = (Node<T>[])Array.newInstance(Node.class, sentinel.next.length);
		h = 0;
		rand = new Random();
	}

	public FastSkiplistRankedSSet() {
		this(new DefaultComparator<T>());
	}

	/**
	 * Find the node<T> u that precedes the value x in the skiplist.
	 *
	 * @param x - the value to search for
	 * @return a node<T> u that maximizes u.x subject to
	 * the constraint that u.x < x --- or sentinel if u.x >= x for
	 * all node<T>s x
	 */
	protected Node<T> findPredNode(T x) {
		Node<T> u = sentinel;
		int r = h;
		while (r >= 0) {
			while (u.next[r] != null && c.compare(u.next[r].x,x) < 0)
				u = u.next[r];   // go right in list r
			r--;               // go down into list r-1
		}
		return u;
	}

	public T find(T x) {
		Node<T> u = findPredNode(x);
		return u.next[0] == null ? null : u.next[0].x;
	}

	public T findGE(T x) {
		if (x == null) {   // return first node<T>
			return sentinel.next[0] == null ? null : sentinel.next[0].x;
		}
		return find(x);
	}

	public T findLT(T x) {
		if (x == null) {  // return last node<T>
			Node<T> u = sentinel;
			int r = h;
			while (r >= 0) {
				while (u.next[r] != null)
					u = u.next[r];
				r--;
			}
			return u.x;
		}
		return findPredNode(x).x;
	}

	Node findPred(int i) { 
		Node u = sentinel; 
		int r = h; 
		int j = -1; // index of the current node in list 0 
		while (r >= 0) { 
			while (u.next[r] != null && j + u.length[r] < i) { 
				j += u.length[r]; 
			  	u = u.next[r]; 
			} 
			r--; 
		} 
		return u; 
	}

	public T get(int i) {
		// This is too slow and making it faster will take changes to this
		// structure
		/*Iterator<T> it = this.iterator();
		for (int j = 0; j < i; j++) {
			it.next();
		}
		return it.next();
		*/
		if((i > size() - 1) || ( i < 0)){
			return null;
		}
		else{
			return (T)findPred(i).next[0].x;
		}
	}

	public int rank(T x) {
		// This is too slow and making it faster will take changes to this
		// structure
		/*Iterator<T> it = this.iterator();
		int r = 0;
		while (it.hasNext() && c.compare(it.next(), x) < 0) {
			r++;
		}
		return r;*/
		Node u = sentinel;
		int r = h;
		int j = 0; // starting at zero coz starting from -1 will give value one less than the actual  
		while(r >= 0){
			while(u.next[r] != null && c.compare((T)u.next[r].x, x) < 0){
				j += u.length[r];
				u = u.next[r];
			}
			r--;
		}
		return j;
	}

	public boolean remove(T x) {
		boolean removed = false;
		Node<T> u = sentinel;
		int r = h;
		
		int comp = 0;
		while (r >= 0) {
			while (u.next[r] != null
			       && (comp = c.compare(u.next[r].x, x)) < 0) {
				
				u = u.next[r];
			}
			u.length[r]--; //for the node we are removing
			if (u.next[r] != null && comp == 0) {
				x = u.next[r].x;
				u.length[r] += u.next[r].length[r];
				removed = true;
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;  // height has gone down
			}
			r--;
		}
		if (removed) n--;
		return removed;

	}


	/**
	 * Simulate repeatedly tossing a coin until it comes up tails.
	 * Note, this code will never generate a height greater than 32
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}

	public void clear() {
		n = 0;
		h = 0;
		Arrays.fill(sentinel.next, null);
	}

	public int size() {
		return n;
	}

	public Comparator<T> comparator() {
		return c;
	}

	/**
	 * Create a new iterator in which the next value in the iteration is u.next.x
	 * TODO: Constant time removal requires the use of a skiplist finger (a stack)
	 * @param u
	 * @return
	 */
	protected Iterator<T> iterator(Node<T> u) {
		class SkiplistIterator implements Iterator<T> {
			Node<T> u, prev;
			public SkiplistIterator(Node<T> u) {
				this.u = u;
				prev = null;
			}
			public boolean hasNext() {
				return u.next[0] != null;
			}
			public T next() {
				prev = u;
				u = u.next[0];
				return u.x;
			}
			public void remove() {
				// Not constant time
				FastSkiplistRankedSSet.this.remove(prev.x);
			}
		}
		return new SkiplistIterator(u);
	}

	public Iterator<T> iterator() {
		return iterator(sentinel);
	}

	public Iterator<T> iterator(T x) {
		return iterator(findPredNode(x));
	}

	public boolean add(T x) {
		Node<T> u = sentinel;
		int r = h;
		int comp = 0;
		int addingIndex = 0;
		while (r >= 0) {
			while (u.next[r] != null
			       && (comp = c.compare(u.next[r].x,x)) < 0){
				addingIndex += u.length[r];
				u = u.next[r];
				
				   }
			if (u.next[r] != null && comp == 0) return false;
				r--;          
		}
		Node<T> w = new Node<T>(x, pickHeight());

		/*while (h < w.height())
			stack[++h] = sentinel;   // height increased
		for (int i = 0; i < w.next.length; i++) {
			w.next[i] = stack[i].next[i];
			stack[i].next[i] = w;
		}*/
		
		return add(addingIndex, w);
	}

	public boolean add(int i, Node<T> w){
		Node u = sentinel;
		int k = w.height();
		if(w.height() > h)
			h = w.height();
		int r = h;
		int j = -1;
		while(r >= 0){
			while(u.next[r] != null && j + u.length[r] < i){
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]++;
			if(r <= k) {
				w.next[r] = u.next[r];
				u.next[r] = w;
				w.length[r] = u.length[r] - (i - j);
				u.length[r] = i - j;
			}
			r--;
		}
		n++;
		return true;
	}

	public static void main(String[] args) {
		int n = 20;
		
		Random rand = new java.util.Random();
		RankedSSet<Integer> rss = new FastSkiplistRankedSSet<>();
		for (int i = 0; i < n; i++) {
			rss.add(rand.nextInt(3*n));
		}
		System.out.print("Contents: ");
		for (Integer x : rss) {
			System.out.print(x + ",");
		}
		System.out.println();

		System.out.println("size()=" + rss.size());

		for (int i = 0; i < rss.size(); i++) {
			Integer x = rss.get(i);
			System.out.println("get(" + i + ")=" + x);
		}

		for (Integer x = 0; x < 3*n+1; x++) {
			int i = rss.rank(x);
			System.out.println("rank(" + x + ")=" + i);
		}
	}
}
