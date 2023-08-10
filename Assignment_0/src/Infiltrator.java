import java.util.LinkedList;
import java.util.List;

public class Infiltrator {
    private int x;
    private int y;

    public void random_move_policy(Border border){
        List<int[]> off_sensors = new LinkedList<int[]>();
    }

    private boolean valid_move(int x, int y, Border border){
        if (x >= border.getLength() || x < 0){
            return false;
        } else if (y >= border.getWidth() || y < 0) {
            return false;
        }
        return true;
    }

}
