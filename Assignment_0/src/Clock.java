public class Clock {
    private int time;

    public int getTime() {
        return time;
    }

    public Clock() {
        this.time = 0;
    }

    public void increment() {
        this.time += 1;
    }

    public void increment(int sec) {
        this.time += sec;
    }

}
