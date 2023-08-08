public class Border {
    private int L;
    private int W;
    private Sensor sensors[][];

    public Border(int l, int w) {
        this.L = l;
        this.W = w;
        this.sensors = new Sensor[w][l];
        for (int x=0; x<w; x++){
            for (int y=0; y<l; y++){
                this.sensors[x][y] = new Sensor();
            }
        }
    }
}
