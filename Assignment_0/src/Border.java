public class Border {
    public int getLength() {
        return length;
    }

    private int length;

    public int getWidth() {
        return width;
    }

    private int width;
    private double probability;
    private Sensor sensors[][];

    public Border(int length, int width, double p) {
        this.length = length;
        this.width = width;
        this.probability = p;
        this.sensors = new Sensor[length][width];
        for (int x = 0; x< length; x++){
            for (int y = 0; y< width; y++){
                this.sensors[x][y] = new Sensor();
            }
        }
    }

    public Boolean get_sensor_state(int x, int y){
        return this.sensors[x][y].isState();
    }

    public void set_sensor_state(int x, int y, boolean state){
        this.sensors[x][y].setState(state);
    }
}
