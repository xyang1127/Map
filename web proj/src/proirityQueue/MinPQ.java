package proirityQueue;

import java.util.*;

public class MinPQ<T> implements ExtrinsicMinPQ<T> {

    private List<Node> list;
    private Map<T, Integer> map; // map a key to its index in the list. This is also used for checkign whether an item is contained in the list

    public MinPQ() {
        list = new ArrayList<>();
        list.add(null); // add a dummy node to fill in the first position which will never be used
        map = new HashMap<>();
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null. */
    @Override
    public void add(T item, double priority) {
        if(map.containsKey(item))
            throw new IllegalArgumentException("this item already exists in the heap");

        list.add(new Node(item, priority));
        map.put(item, list.size()-1);
        swim(list.size()-1);
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if(this.size() == 0)
            throw new NoSuchElementException("the heap is empty");
        else
            return list.get(1).item;
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if(this.size() == 0)
            throw new NoSuchElementException("the heap is empty");
        else {
            int n = list.size();
            T ans = list.get(1).item;
            swap(1, n-1);
            list.remove(n-1);
            sink(1);
            map.remove(ans);
            return ans;
        }
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return list.size() - 1; // the item at index 0 is dummy
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        if(!map.containsKey(item))
            throw new NoSuchElementException();

        int idx = map.get(item);
        double oldPriority = list.get(idx).priority;
        list.get(idx).priority = priority;

        if(priority > oldPriority)
            sink(idx);
        else if(priority < oldPriority)
            swim(idx);
    }

    private void swim(int idx) {
        while(idx > 1 && list.get(idx).priority < list.get(idx/2).priority) {
            swap(idx, idx/2);
            idx = idx/2;
        }
    }

    private void sink(int idx) {
        assert idx != 0;
        int n = list.size();
        while(idx*2 < n) {
            int first = idx*2;
            int second = first+1;
            int smallerChild = first;
            if(second < n && list.get(first).priority > list.get(second).priority)
                smallerChild = second;
            if(list.get(idx).priority > list.get(smallerChild).priority) {
                swap(idx, smallerChild);
                idx = smallerChild;
            } else
                break;
        }
    }

    private void swap(int idx1, int idx2) {
        Node tmp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, tmp);

        // modify the map accordingly
        map.put(list.get(idx1).item, idx1);
        map.put(list.get(idx2).item, idx2);
    }

    class Node {
        T item;
        double priority;

        public Node(T i, double p) {
            item = i;
            priority = p;
        }
    }

}
