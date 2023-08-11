import java.io.FileWriter;
import java.io.IOException;

class Main {
    public static void main(String[] args) {
        int borderLength = 1000;
        int borderWidth = 5;
        double probability = 0.5;
        if (args.length == 3) {
            borderLength = Integer.parseInt(args[0]);
            borderWidth = Integer.parseInt(args[1]);
            probability = Double.parseDouble(args[2]);
        } else if (args.length == 2) {
            borderLength = Integer.parseInt(args[0]);
            borderWidth = Integer.parseInt(args[1]);
        }

        String filename = borderWidth + "_" + probability + ".txt";
        try {
            FileWriter outputFile = new FileWriter(filename);

            Clock globalClock = new Clock();
            Border myBorder = new Border(borderLength, borderWidth, probability);
            Infiltrator primeInfiltrator = new Infiltrator(borderLength);
            boolean infiltratorCaught = false;
            boolean borderReached = false;

            while (!infiltratorCaught && !borderReached) {
                if (globalClock.getTime() % 10 == 0) {
                    myBorder.assignSensorStates();
                }

                globalClock.increment(10);
                if (!myBorder.getSensorState(primeInfiltrator.getX(), primeInfiltrator.getY())) {
                    Integer[] oldPosition = primeInfiltrator.priorityMovePolicy(myBorder);
                    System.out.println(oldPosition[0] + "\t" + oldPosition[1]);
                }

                if (primeInfiltrator.getY() >= borderWidth) {
                    borderReached = true;
                }
            }

            outputFile.write(globalClock.getTime() + '\n');
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}