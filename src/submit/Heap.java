package submit;

import java.util.*;

/** An instance is a min-heap or a max-heap of distinct values of type E <br>
 * with priorities of type double. */
public class Heap<E> {

	/** Class Invariant: <br>
	 * 0. size = b.size(); ---the size of ArrayList b. <br>
	 * 1. b[0..b.size-1] represents a complete binary tree.<br>
	 * .. b[0] is the root; <br>
	 * .. For k > 0, (k-1)/2 (using int division) is the index in b of the parent of b[k]<br>
	 * .. For k >= 0, 2k+1 and 2k+2 are the indexes in b of left and right children of b[k].<br>
	 *
	 * 2. For k in 0..size-1, b[k] contains the value and its priority.
	 *
	 * 3. The values in b[0..size-1] are all different.
	 *
	 * 4. For k in 1..size-1, <br>
	 * .. if isMaxHeap, (b[k]'s priority) <= (b[k]'s parent's priority),<br>
	 * .. if !isMaxHeap, (b[k]'s priority) >= (b[k]'s parent's priority).
	 *
	 * map and the tree are in sync, meaning:
	 *
	 * 5. The keys of map are the values in b[0..size-1]. This implies that size = map.size().
	 *
	 * 6. if value v is in b[k], then map.get(v) = k. */
	private final boolean isMaxHeap;
	private ArrayList<Element> b= new ArrayList<>();
	private int size;
	private HashMap<E, Integer> map= new HashMap<>();

	/** Constructor: an empty heap with capacity 10. <br>
	 * It is a max-heap if isMax is true and a min-heap if isMax is false. */
	public Heap(boolean isMax) {
		isMaxHeap= isMax;
	}

	/** An object of class Element houses a value and a priority; <br>
	 * it is an element of the heap. */
	class Element {
		protected E val;             // The value
		protected double priority;   // The priority

		/** An instance with value v and priority p. */
		protected Element(E v, double p) {
			val= v;
			priority= p;
		}

		/** Return a representation of this object. */
		@Override
		public String toString() {
			return "(" + val + ", " + priority + ")";
		}

		/** = "this and ob are of the same class and have equal val and priority fields." */
		@Override
		public boolean equals(Object ob) {
			if (ob == null || getClass() != ob.getClass()) return false;
			@SuppressWarnings("unchecked")
			Element obc= (Element) ob;
			return val == obc.val && priority == obc.priority;
		}
	}

	/** Add v with priority p to the heap. <br>
	 * Throw an illegalArgumentException if v is already in the heap. <br>
	 * The expected time is logarithmic and <br>
	 * the worst-case time is linear in the size of the heap. */
	public void add(E v, double p) throws IllegalArgumentException {
		if (map.containsKey(v)) throw new IllegalArgumentException("v is already in the heap");
		map.put(v, size);
		b.add(new Element(v, p));
		size= size + 1;
		bubbleUp(size - 1);
	}

	/** Return the size of this heap. <br>
	 * This operation takes constant time. */
	public int size() {
		return size;
	}

	/** Swap b[h] and b[k]. <br>
	 * Precondition: 0 <= h < heap-size, 0 <= k < heap-size. */
	void swap(int h, int k) {
		assert 0 <= h && h < size && 0 <= k && k < size;
		Element temph= b.get(h);
		Element tempk= b.get(k);
		b.set(h, tempk);
		b.set(k, temph);
		map.put(b.get(h).val, h);
		map.put(b.get(k).val, k);
	}

	/** If a value with priority p1 belongs above <br>
	 * a value with priority p2 in the heap, return 1.<br>
	 * If priority p1 and priority p2 are the same, return 0. <br>
	 * If a value with priority p1 should be below <br>
	 * a value with priority p2 in the heap, return -1.<br>
	 * This is based on what kind of a heap this is, <br>
	 * ... E.g. a max-heap, the value with the largest priority is in the root.<br>
	 * ... E.g. a min-heap, the value with the smallest priority is in the root. */
	int compareTo(double p1, double p2) {
		if (p1 == p2) return 0;
		if (isMaxHeap) { return p1 > p2 ? 1 : -1; }
		return p1 < p2 ? 1 : -1;
	}

	/** If b[h] should be above b[h] in the heap, return 1. <br>
	 * If b[h]'s priority and b[k]'s priority are the same, return 0. <br>
	 * If b[h] should be below b[k] in the heap, return -1. <br>
	 * This is based on what kind of a heap this is, <br>
	 * ... E.g. a min-heap, the value with the smallest priority is in the root. <br>
	 * ... E.g. a max-heap, the value with the largest priority is in the root. */
	int compareTo(int h, int k) {
		return compareTo(b.get(h).priority, b.get(k).priority);
	}

	/** If h >= size, return.<br>
	 * Otherwise, bubble b[h] up the heap to its right place. <br>
	 * Precondition: 0 <= h and, if h < size, <br>
	 * ... the class invariant is true, except perhaps that <br>
	 * ... b[h] belongs above its parent (if h > 0) in the heap. */
	void bubbleUp(int h) {
		if (h >= size) return;
		// Invariant: 0 <= h < size and<br>
		// .......... The class invariant is true, except perhaps<br>
		// .......... that b[h] belongs above its parent (if h > 0) in the heap, not below it.
		while (h > 0) {
			int p= (h - 1) / 2; // p is h's parent
			if (compareTo(h, p) <= 0) return;
			swap(h, p);
			h= p;
		}
	}

	/** If this is a min-heap, return the heap value with lowest priority. <br>
	 * If this is a max-heap, return the heap value with highest priority.<br>
	 * Do not change the heap. <br>
	 * This operation takes constant time. <br>
	 * Throw a NoSuchElementException if the heap is empty. */
	public E peek() {
		if (size <= 0) throw new NoSuchElementException("heap is empty");
		return b.get(0).val;
	}

	/** If h < 0 or size <= h, return.<br>
	 * Otherwise, Bubble b[h] down in heap until the class invariant is true. <br>
	 * If there is a choice to bubble down to both the left and right children <br>
	 * (because their priorities are equal), choose the left child. <br>
	 *
	 * Precondition: If 0 <= h < size, the class invariant is true except that <br>
	 * perhaps b[h] belongs below one or both of its children. */
	void bubbleDown(int h) {
		if (h < 0 || size <= h) return;
		int k= 2 * h + 1;
		// Invariant: Class invariant is true except perhaps that
		// .......... b[h] belongs below one or both of its children and
		// .......... k is h's left child.
		while (k < size) { // while b[h] has a child
			int uc= k + 1 == size || compareTo(k, k + 1) >= 0 ? k : k + 1; // uc is the bigger child
			if (compareTo(h, uc) >= 0) return;
			swap(h, uc);
			h= uc;
			k= 2 * h + 1;
		}
	}

	/** If this is a min-heap, remove and return heap value with lowest priority. <br>
	 * If this is a max-heap, remove and return heap value with highest priority. <br>
	 * Expected time: logarithmic. Worst-case time: linear in the size of the heap.<br>
	 * Throw a NoSuchElementException if the heap is empty. */
	public E poll() {
		if (size <= 0) throw new NoSuchElementException("heap is empty");

		E v= b.get(0).val;
		swap(0, size - 1);
		b.remove(size - 1);
		map.remove(v);
		size= size - 1;
		bubbleDown(0);
		return v;
	}

	/** Change the priority of value v to p. <br>
	 * Expected time: logarithmic. Worst-case time: linear in the size of the heap.<br>
	 * Throw an IllegalArgumentException if v is not in the heap. */
	public void updatePriority(E v, double p) {
		Integer index= map.get(v);
		if (index == null) throw new IllegalArgumentException("v is not in the heap");
		double oldP= b.get(index).priority;
		b.get(index).priority= p;
		int t= compareTo(p, oldP);
		if (t == 0) return;
		if (t < 0) bubbleDown(index);
		else bubbleUp(index);
	}

	/** Return the heap values (only, not the priorities) in form [5, 3, 2]. */
	public String toStringValues() {
		StringBuilder resb= new StringBuilder("[");
		for (int h= 0; h < size; h= h + 1) {
			if (h > 0) resb.append(", ");
			resb.append(b.get(h).val);
		}
		return resb.append(']').toString();
	}

	/** Return the heap priorities in form [5.0, 3.0, 2.0]. */
	public String toStringPriorities() {
		StringBuilder resb= new StringBuilder("[");
		for (int h= 0; h < size; h= h + 1) {
			if (h > 0) resb.append(", ");
			resb.append(b.get(h).priority);
		}
		return resb.append(']').toString();
	}
}