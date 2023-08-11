import java.util.Random;

public class Border {
    private final int length;
    private final int width;
    private final double probability;
    private final Sensor[][] sensors;

    public Border(int length, int width, double p) {
        this.length = length;
        this.width = width;
        this.probability = p;
        this.sensors = new Sensor[length][width];
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < width; y++) {
                this.sensors[x][y] = new Sensor(probability);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public boolean getSensorState(int x, int y) {
        return this.sensors[x][y].isState();
    }

    public void setSensorState(int x, int y, boolean state) {
        this.sensors[x][y].setState(state);
    }

    public void assignSensorStates() {
        Random random = new Random();
        for (int x = 0; x < this.length; x++) {
            for (int y = 0; y < this.width; y++) {
                this.sensors[x][y].setState(false);
                if (random.nextDouble() < this.probability) {
                    // will turn true if coin flip outcome is Head
                    this.sensors[x][y].setState(true);
                }
            }
        }
    }
}
