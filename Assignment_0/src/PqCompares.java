import java.util.Comparator;

public class PqCompares implements Comparator<Integer[]> {
    @Override
    public int compare(Integer[] a1,Integer[] a2){
        if(a1[0]<a2[0]){
            return 1;
        }else if(a1[0]>a2[0]){
            return -1;
        }
        return 0;
    }
}
