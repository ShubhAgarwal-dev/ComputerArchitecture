import java.util.*;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer[]> pq = new PriorityQueue<Integer[]>(new arrComp());
        pq.add(new Integer[]{1,2,3});
        pq.add(new Integer[]{3,2,3});
        pq.add(new Integer[]{2,2,3});
        assert pq.peek() != null;
        System.out.println(pq.peek()[0]);
    }

    static class arrComp implements Comparator<Integer[]>{
        public int compare(Integer[] a1,Integer[] a2){
            if(a1[0]<a2[0]){
                return 1;
            }else if(a1[0]>a2[0]){
                return -1;
            }
            return 0;
        }
    }
}