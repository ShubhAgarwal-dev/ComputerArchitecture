public class Sensor {
    private boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Sensor(boolean state) {
        this.state = state;
    }

    public Sensor() {
        this.state = false;
    }

    public Sensor(double probability) {
        double randProb = Math.random();
        this.state = randProb > probability;
    }
}
