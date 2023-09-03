package main;

import configuration.Configuration;
import generic.Misc;
import generic.Simulator2;
import generic.Statistics;
import generic.Simulator;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2)
        {
            Misc.printErrorAndExit("usage: java -jar <path-to-jar-file> <path-to-assembly-program> <path-to-object-file>\n");
        }
//        Simulator2.setupSimulation(args[0]);
//        Simulator2.assemble(args[1]);
//        System.out.println("dada is over now");
        Simulator.setupSimulation(args[0]);
        Simulator.assemble(args[1]);
    }
}

