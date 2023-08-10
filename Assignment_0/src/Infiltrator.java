import java.util.*;

public class Infiltrator {
    private int x;
    private int y;


    private boolean validMove(int x, int y, Border border){
        if (x >= border.getLength() || x < 0){
            return false;
        } else if (y >= border.getWidth() || y < 0) {
            return false;
        }
        return true;
    }

    private PriorityQueue<Integer[]> makeMoveOnlyFwd(Border border){
        int[] priorArr = {1,2,2,2,1};
        int[] cols = {-1,-1,0,1,1};
        int[] rows = {0,1,1,1,0};
        PriorityQueue<Integer[]> moveList = new PriorityQueue<Integer[]>(new PqCompares());
        for(int i=0;i< cols.length;i++){
            if(validMove(this.x+cols[i],this.y+rows[i],border) && !border.getSensorState(this.x+cols[i],this.y+rows[i])){
                moveList.add(new Integer[]{priorArr[i],this.x+cols[i],this.y+rows[i]});
            }
        }
        return moveList;
    }

    private LinkedList<int[]> makeMoveAllDirections(Border border){
        int[] cols = {1,1,1,0,0,0,-1,-1,-1};
        int[] rows = {-1,0,1,-1,0,1,-1,0,1};
        LinkedList<int[]> moveList = new LinkedList<int[]>();
        for(int i=0;i< cols.length;i++){
            if(validMove(this.x+cols[i],this.y+rows[i],border) && !border.getSensorState(this.x+cols[i],this.y+rows[i])){
                moveList.add(new int[]{this.x+cols[i],this.y+rows[i]});
            }
        }
        return moveList;
    }

    public void randomMovePolicy(Border border){
        PriorityQueue<Integer[]> moveList = this.makeMoveOnlyFwd(border);
        int len = moveList.size();
        Random rand = new Random();
        Integer[] randomEle = moveList.peek();
        assert randomEle != null;
        this.x = randomEle[1];
        this.y = randomEle[2];
    }
}
