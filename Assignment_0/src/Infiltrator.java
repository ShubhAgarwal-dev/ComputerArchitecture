import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Infiltrator {
    private int x;
    private int y;

    public Infiltrator(int borderLength) {
        this.x = ThreadLocalRandom.current().nextInt(0, borderLength);
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private boolean validMove(int x, int y, Border border) {
        if (x >= border.getLength() || x < 0) {
            return false;
        } else return y <= border.getWidth() && y >= 0;
    }

    private PriorityQueue<Integer[]> makeMoveOnlyFwd(Border border) {
        int[] priorArr = {1, 2, 2, 2, 1};
        int[] cols = {-1, -1, 0, 1, 1};
        int[] rows = {0, 1, 1, 1, 0};
        PriorityQueue<Integer[]> moveList = new PriorityQueue<Integer[]>(new PqCompares());
        for (int i = 0; i < cols.length; i++) {
            if (validMove(this.x + cols[i], this.y + rows[i], border) &&
                    border.getSensorState(this.x + cols[i], this.y + rows[i])) {
                moveList.add(new Integer[]{priorArr[i], this.x + cols[i], this.y + rows[i]});
            }
        }
        return moveList;
    }

    public Integer[] priorityMovePolicy(Border border) {
        PriorityQueue<Integer[]> moveList = this.makeMoveOnlyFwd(border);
        Integer[] randomEle = moveList.peek(); // We have Priority, X-coordinate and Y-coordinate
        assert randomEle != null;
        int oldX = this.x;
        int oldY = this.y;
        try {
            this.x = randomEle[1];
            this.y = randomEle[2];
        } catch (NullPointerException e){
            return new Integer[]{oldX, oldY};
        }
        return new Integer[]{oldX, oldY};
    }
}
