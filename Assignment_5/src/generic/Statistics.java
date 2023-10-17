package generic;

import java.io.PrintWriter;

public class Statistics {
    static int staticInstCount = 0;
    static int dynamicInstCount = 0;
    static int numCycles = 0;
    static float IPC;
    static float frequency;

    static int stalls;
    static int numBranchHazards;

    public static int getStalls() {
        return stalls;
    }

    public static void setStalls(int stalls) {
        Statistics.stalls = stalls;
    }

    public static int getNumBranchHazards() {
        return numBranchHazards;
    }

    public static void setNumBranchHazards(int numBranchHazards) {
        Statistics.numBranchHazards = numBranchHazards;
    }

    public static void setDynamicInstCount(int dynamicInstCount) {
        Statistics.dynamicInstCount = dynamicInstCount;
    }

    public static int getDynamicInstCount() {
        return dynamicInstCount;
    }

    public static void setStaticInstCount(int staticInstCount) {
        Statistics.staticInstCount = staticInstCount;
    }

    public static int getStaticInstCount() {
        return staticInstCount;
    }

    public static void setNumCycles(int numCycles) {
        Statistics.numCycles = numCycles;
    }

    public static int getNumCycles() {
        return numCycles;
    }

    public static void setIPC(float IPC) {
        Statistics.IPC = IPC;
    }

    public static float getIPC() {
        return IPC;
    }

    public static void setFrequency(float frequency) {
        Statistics.frequency = frequency;
    }

    public static float getFrequency() {
        return frequency;
    }

    public static void printStatistics(String statFile) {
        try {
            PrintWriter writer = new PrintWriter(statFile);

            writer.println("Static Instructions= " + staticInstCount);
            writer.println("Dynamic Instructions= " + dynamicInstCount);
            writer.println("Number of Cycles= " + numCycles);
            writer.println("IPC = " + IPC);
            writer.println("Frequency = " + frequency + " GHz");
            writer.println("Stalls = " + stalls);
            writer.println("Wrong Branch Taken = " + numBranchHazards);

            writer.close();
        } catch (Exception e) {
            Misc.printErrorAndExit(e.getMessage());
        }
    }
}
