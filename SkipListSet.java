// Cooper Urich
// COP 3503C Summer C
// co293451
// UCFID: 4518739
// Submitted on July 31st at 930 pm

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.lang.reflect.Array;

	public class SkipListSet<T extends Comparable<T>> implements SortedSet<T> {
		SkipListItem<T> head;
		SkipListItem<T> lowestHead;
		SkipListItem<T> tail;
		SkipListItem<T> lowestTail;
		SkipListItem<T> firstNode;
		int count;
		int hashValue = 0;
		int size;
		int height = 0;
		int maxNodeLevel = 0;


		// Creates skip list
		public SkipListSet() {
			head = new SkipListItem<>(null);
			head.level = 0;

			tail = new SkipListItem<>(null);
			tail.level = 0;

			head.next = tail;
			head.previous = null;
			head.up = null;
			head.down = null;

			tail.previous = head;
			tail.up = null;
			tail.down = null;
			tail.next = null;

			// zero elements have been inserted
			size = 0;
			// initial height of 0, update when needed
			height = 0;
			// creating the 3 levels, updating the height for each list growth
			for (int i = 0; i < 3; i++) {
				growListHeight();
			}
		}

		// Creates skip list with collection
		public SkipListSet(Collection c){

			head = new SkipListItem<>(null);
			head.level = 0;
			tail = new SkipListItem<>(null);
			tail.level = 0;
			head.level = 0;
			tail.level = 0;
			head.next = tail;
			head.previous = null;
			head.up = null;
			head.down = null;

			tail.previous = head;
			tail.up = null;
			tail.down = null;
			tail.next = null;

			// zero elements have been inserted
			size = 0;
			// initial height of 0, update when needed
			height = 0;

			// creating the 3 levels, updating the height for each list growth
			for (int i = 0; i < 3; i++) {
				growListHeight();
			}

			this.addAll(c);
		}

		public static class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T> {
			SkipListItem<T> temp;
			SkipListSet<T> s;

			public SkipListSetIterator(SkipListSet<T> s) {
				this.s = s;
				temp = this.s.getFirstNode();
			}

			// true if there is a next item in iterator
			public boolean hasNext() {
				if (temp != null)
					return true;
				
				return false;
			}

			// returns next
			public T next() {
				if (temp != null){
					T elem = temp.entry;
					temp = temp.next;
					return elem;
				}
				else{
					return null;
				}
			}

			// removes next object
			public void remove() {
				s.remove((Object)temp.entry);
			}

		}

		public static class SkipListItem<T extends Comparable<T>> {

			SkipListItem<T> next = null;
			SkipListItem<T> previous = null;
			SkipListItem<T> up = null;
			SkipListItem<T> down = null;
			int level = 0;
			int hashValue;
			T entry;

			// creates skip list item
			public SkipListItem(T entry) {
				this.entry = entry;
			}

			public T entry() {
				return this.entry;
			}

		}

		// returns true is set is empty
		public boolean isEmpty() {
			if (this.size == 0) {
				return true;
			}

			else
				return false;
		}

		// returns last item in set
		public T last() {
			T elem;
			SkipListItem<T> temp = this.tail;

			while (tail.down != null) {
				tail = tail.down;
			}

			tail = tail.previous;
			elem = (T) (tail.entry);
			this.tail = temp;

			return elem;
		}

		// returns first item in set
		public T first() {
			SkipListItem<T> temp = this.head;
			SkipListItem<T> temp1 = this.head;
			T ret;

			while (temp.down != null) {
				temp = temp.down;
			}

			if (temp.next != tail) {
				temp = temp.next;
			} else {
				return null;
			}
			ret = (T) (temp.entry);

			this.head = temp1;

			return ret;

		}

		// remove all elements in set
		public void clear() {

			SkipListItem<T> temp = null;
			SkipListItem<T> temp1 = this.head;
			SkipListItem<T> temp2 = this.head;
			SkipListItem<T> foundNode;
			int i = 0;

			System.out.println(this.head.level);

			while (temp1.down != null) {
				print(".");
				temp1 = temp1.down;
			}

			if (temp1.next != tail) {
				foundNode = temp1.next;
			} 
			else {
				foundNode = null;
				return;
			}

			if (foundNode.next != tail) {
				temp = foundNode.next;
			}

			while (foundNode.next != null) {
				temp2 = foundNode.next;

				while (foundNode != null) {
					foundNode.previous.next = foundNode.next;
					foundNode.next.previous = foundNode.previous;

					if (foundNode.up == null) {
						temp = foundNode;
						foundNode = null;
						i++;
					}

					else {
						temp = foundNode;
						foundNode = null;
						i++;
						foundNode = temp.up;
					}

				}
				if (temp2 != null) {
					foundNode = temp2;
				} else {
					break;
				}
				size--;
			}

			this.head = temp2;
			height = 3;
		}

		public Comparator<? super T> comparator() {
			return null;
		}

		// finds the node before the entry to add to it
		SkipListItem<T> findPlaceToAdd(T elem) {
			int flag = 1;
			SkipListItem<T> temp = this.head;
			SkipListItem<T> temp1 = this.head;
			int i = 0;

			while (flag == 1) {
				// if the next node is the tail or the next node is not bigger, keep going
				if (temp.next.entry != null) {
					while (temp.next.entry != null && (temp.next.entry).compareTo(elem) <= 0) {
						if (temp.next.entry == null) {
							break;
						}

						if (temp.next.entry != (null)) {
							temp = temp.next;
						}

					}
				}

				// check if you can go down
				if (temp.down == null) {
					this.head = temp1;
					return temp;
				}
				if (temp.down != null) {
					temp = temp.down;
				}
				i++;
			}

			this.head = temp1;
			return null;
		}

		public int size() {
			return size;
		}

		public double random() {
			double ran;

			ran = Math.random();
			return ran;
		}

		// adding an element to a set
		@Override
		public boolean add(T elem) {
			SkipListItem<T> foundNode;
			SkipListItem<T> newNode;
			SkipListItem<T> temp;
			SkipListItem<T> newHead;
			SkipListItem<T> newTail;
			int i = 0;
			double num = random();

			foundNode = findPlaceToAdd(elem);

			if (foundNode == null){
				if (foundNode != this.head)
					return false;
			}

			if (foundNode.entry != null){
				if ((foundNode.entry).equals(elem)) {
					return false;
				}
			}

			newNode = new SkipListItem<>((T) elem);

			newNode.previous = foundNode;
			newNode.next = foundNode.next;

			foundNode.next.previous = newNode;
			foundNode.next = newNode;

			if (this.count == 0) {
				firstNode = newNode;
			}
			// update size

			newNode.level = i;

			this.size++;
			this.count++;

			while (num <= 0.5) {
				i++;
				if (i >= this.height - 1) {
					this.height = this.height + 1;

					newHead = new SkipListItem<>(null);
					newTail = new SkipListItem<>(null);

					newHead.next = newTail;
					newHead.down = head;
					head.up = newHead;

					newTail.previous = newHead;
					newTail.down = tail;
					tail.up = newTail;

					this.head = newHead;
					this.tail = newTail;

					this.head.level = this.head.level + 1;
					tail.level++;

				}

				if (foundNode.up == null) {
					while (foundNode.up == null) {
						foundNode = foundNode.previous;
					}
				}
				foundNode = foundNode.up;

				temp = new SkipListItem<>(elem);

				// linking the new node in;
				temp.previous = foundNode;
				temp.down = newNode;
				temp.next = foundNode.next;

				newNode.up = temp;
				foundNode.next.previous = temp;
				foundNode.next = temp;

				newNode.entry = temp.entry;
				newNode = temp;
				temp.level = i;
				num = random();

				if (i > this.maxNodeLevel) {
					this.maxNodeLevel = i;
				}
			}
			return true;
		}

		// grows the list height by one
		void growListHeight() {
			SkipListItem<T> newHead;
			SkipListItem<T> newTail;

			height = height + 1;

			newHead = new SkipListItem<>(null);
			newTail = new SkipListItem<>(null);

			// assign the head pointers
			newHead.next = newTail;
			newHead.down = head;
			head.up = newHead;

			// assign the tail pointers
			newTail.previous = newHead;
			newTail.down = tail;
			tail.up = newTail;

			head = newHead;
			tail = newTail;

			head.level = height;
			tail.level = height;

		}

		// true if two sets are equal, false if not
		public boolean equals(SkipListSet s) {
			int hash1 = this.hashCode();
			int hash2 = s.hashCode();
			System.out.println(hash1 + ", "  + hash2);

			if (this.size() != s.size()){
				return false;
			}
			if (hash1 == hash2 && this.size == s.size()){
				return true;
			}


			return false;
		}

		// generic toArray
		public <T> T[] toArray(T[] a) {
			if (a.length < size) { 
				a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
			  } else if (a.length > size) {
				a[size] = null;
			  }
			return a;
		}

		// returns array of elements in the set
		public Object[] toArray() {
			Object[] arr = new Object[this.size()];
			SkipListItem<T> temp = this.head;
			int i = 0;

			// System.out.println("Passed List Size: " + this.size());
			while (this.head.down != null) {
				this.head = head.down;
			}

			head = head.next;

			while (this.head.next != null) {
				// print("Array at index: " + i + ", has value of " + this.head.entry);
				arr[i] = this.head.entry;
				i++;
				this.head = this.head.next;
			}

			this.head = temp;

			return arr;
		}

		// re-adds each element to the set with different heights
		public void reBalance() {
            
			Object[] arr = new Object[this.size() + 1];

			arr = this.toArray();
			for (Object elem : arr){
				this.remove(elem);	
			}
			this.height = 0;

			for (Object elem : arr){
				this.add((T)elem);	
			}
		}

		void print(String s) {
			System.out.println(s);
		}

		@Override
		public SortedSet<T> headSet(T toElement) {
			throw new java.lang.UnsupportedOperationException();
		}

		@Override
		public SortedSet<T> subSet(T fromElement, T toElement) {
			throw new java.lang.UnsupportedOperationException();
		}

		@Override
		public SortedSet<T> tailSet(T fromElement) {
			throw new java.lang.UnsupportedOperationException();
		}

		// adds every item in the collection
		@Override
		public boolean addAll(Collection<? extends T> c) {
			boolean result = false;

			for (T elem : c) {
				result |= add(elem);
			}
			return result;
		}

		// returns true if skiplistset contains a certain object
		@Override
		public boolean contains(Object o) {
			SkipListItem<T> foundNode = findPlaceToAdd((T) o);


			if (foundNode == null) {
				return false;
			}

			if (foundNode == this.head){
				return false;
			}

			if (foundNode.entry == null){
				return false;
			}

			if ((foundNode.entry).equals(o)) {
				return true;
			}

			return false;
		}

		// removes the element in the array
		@Override
		public boolean remove(Object o) {
			SkipListItem<T> temp;
			SkipListItem<T> temp1;
			SkipListItem<T> foundNode;
			int i = 0;

			foundNode = findPlaceToAdd((T) o);
			if ((foundNode.entry).equals((T)o) == false || foundNode == null) {
				// System.out.println("Element " + o + " is not in set");
				return false;
			}

			while (foundNode != null) {
				foundNode.previous.next = foundNode.next;
				foundNode.next.previous = foundNode.previous;

				if (foundNode.up == null) {
					temp = foundNode;
					foundNode = null;
					i++;
				} else {
					temp = foundNode;
					foundNode = null;
					i++;
					foundNode = temp.up;
				}
			}
			this.size--;

			return true;
		}
		// returns the first node after the head
		SkipListItem<T> getFirstNode() {
			SkipListItem<T> temp = this.head;

			while (temp.down != null) {
				temp = temp.down;
			}

			if (temp.next != null) {
				temp = temp.next;
				return temp;
			}

			return null;
		}

		// removes all elements in the collection
		@Override
		public boolean removeAll(Collection<?> c) {
			boolean result = false;

			for (Object elem : c) {
				result |= remove(elem);
			}

			return result;
		}



		// keeps elements in the collection from the set, deletes everything else
		@Override
		public boolean retainAll(Collection<?> c) {
			boolean result = false;
			SkipListItem<T> temp;
			SkipListItem<T> head1 = this.head;
			int size = this.size();

			while (this.head.down != null) {
				this.head = this.head.down;
			}
			this.head = this.head.next;
			temp = this.head;

			for (int i = size - 1; i >= 0; i--) {
				if (c.contains(temp.entry) != true) {
					remove(temp.entry);
					result = true;
					temp = temp.next;
				}
			}

			this.head = head1;
			return false;
		}

		@Override
		public SkipListSetIterator<T> iterator() {
			SkipListSetIterator<T> iter = new SkipListSetIterator<>(this);
			return iter;
		}

		// returns true if the set contains every element in the set
		@Override
		public boolean containsAll(Collection<?> c) {
			boolean result = false;

			for (Object elem : c) {
				result |= contains((T) elem);
			}

			return result;
		}

		// returns the hash code
		@Override
		public int hashCode() {
			SkipListItem<T> temp = this.head;
			int total = 0;

			while(temp.down != null){
				temp = temp.down;
			}
			if ((temp.next.entry) == null){
				return 0;
			}
			temp = temp.next;

			while(temp.next.entry != null){

				total += (temp.entry).hashCode();
				temp = temp.next;
			}

			return total;
		}
}