package com.mdvrp;

import java.io.FileWriter;
import java.io.PrintStream;

import org.coinor.opents.TabuList;

import com.TabuSearch.MyMoveManager;
import com.TabuSearch.MyObjectiveFunction;
import com.TabuSearch.MySearchProgram;
import com.TabuSearch.MyTSsolution;
import com.TabuSearch.MyTabuList;
import com.softtechdesign.ga.GARoute;
import com.softtechdesign.ga.MyGAsolution;

public class MDVRPTW {
	
	private static String class_name = MDVRPTW.class.getName();
	private static MyLogger MyLog = new MyLogger(class_name);


	public static void main(String[] args) {
		
		Parameters          parameters 		= new Parameters(); 	// holds all the parameters passed from the input line
		Instance            instance; 					            // holds all the problem data extracted from the input file
		Duration            duration 		= new Duration(); 	    // used to calculate the elapsed time
		PrintStream         outPrintSream   = null;           		// used to redirect the output

		// Tabu search variables
		MySearchProgram     TSsearch;
		MyTSsolution        initial_TS_sol;
		MyTSsolution	    bets_TS_sol;
		MyObjectiveFunction objFunc;
		MyMoveManager       moveManager;
		TabuList            tabuList;
		
		//GA algorithm variables
		GARoute	    	    GAsearch;
		MyGAsolution        initial_GA_sol;
		MyGAsolution        best_GA_sol;                            
		
		try {
        
        	    startLog();
        	    // check to see if an input file was specified
        	    parameters.updateParameters(args);
        	    MyLog.info(class_name, "main", "parameters.updateParameters(args) => parameters set");
        	    MyLog.info(class_name, "main", parameters.toString());
        
        	    if (parameters.getInputFileName() == null) {
        		MyLog.err(class_name, "main",
        			"parameters.getInputFileName() = null => You must specify an input file name");
        		return;
        	    }
        
        	    duration.start();
        	    MyLog.info(class_name, "main", "time counting started");
        
        	    // get the instance from the file
        	    instance = new Instance(parameters);
        	    MyLog.info(class_name, "main", "new Instance(parameters) => instance created successfully");
        
        	    instance.populateFromHombergFile(parameters.getInputFileName());
        	    MyLog.info(class_name, "main",
        		    "instance.populateFromHombergFile(parameters.getInputFileName()) => instnce populated from file "
        			    + parameters.getInputFileName());
        
        	    // Init memory for Tabu Search
        	    //TODO this will be removed because initial TS solution comes from GA
        	    MyLog.info(class_name, "main", "creating required TS data structure");
        	    initial_TS_sol = new MyTSsolution(instance);
        	    MyLog.info(class_name, "main", "new MySolution(instance) => initial solution instance created");
        
        	    objFunc = new MyObjectiveFunction(instance);
        	    MyLog.info(class_name, "main", "new MyObjectiveFunction(instance) => objective function instance created and initialized with MyInitilaSolution");
        
        	    moveManager = new MyMoveManager(instance);
        	    MyLog.info(class_name, "main", "new MyMoveManager(instance) => move manager instance created");
        
        	    moveManager.setMovesType(parameters.getMovesType());
        	    MyLog.info(class_name, "main", "moveManager.setMovesType(parameters.getMovesType()) => move type set to "
        		    + parameters.getMovesType());
        
        	    // Tabu list
        	    int dimension[] = { instance.getDepotsNr(), instance.getVehiclesNr(), instance.getCustomersNr(), 1, 1 };
        	    MyLog.info(class_name, "main", "number of Depots: " + instance.getDepotsNr());
        	    MyLog.info(class_name, "main", "number of Vehicles: " + instance.getVehiclesNr());
        	    MyLog.info(class_name, "main", "number of Customers: " + instance.getCustomersNr());
        
        	    tabuList = new MyTabuList(parameters.getTabuTenure(), dimension);
        	    MyLog.info(class_name, "main", "new MyTabuList(parameters.getTabuTenure(), dimension) =>  Tabu List created");
        
        	    // Create Tabu Search object
        	    TSsearch = new MySearchProgram(instance, initial_TS_sol, moveManager, objFunc, tabuList, false, outPrintSream);
        	    MyLog.info(class_name, "main", "new MySearchProgram(instance, initialSol, moveManager, objFunc, tabuList, false, outPrintSream) => TS search program created");
        
        	    // Init memory for Genetic Algorithm
/*        	    MyLog.info(class_name, "main", "creating required GA data structure");
        	    //TODO enable the GA constructor
        	    //GAsearch = new GARoute(chromosomeDim, populationDim, crossoverProb, randomSelectionChance, maxGenerations, numPrelimRuns, maxPrelimGenerations, mutationProb, crossoverType, computeStatistics, instance);
        	    MyLog.info(class_name, "main", "new GARoute(chromosomeDim, populationDim, crossoverProb, randomSelectionChance, maxGenerations, numPrelimRuns, maxPrelimGenerations, mutationProb, crossoverType, computeStatistics, instance) => GA search program created");
        	   
        	    initial_TS_Sol.ConvertTSGA(); //TODO fix this call
        	    Thread GAThread = new Thread(GAsearch);
        	    GAThread.start();
        	    MyLog.info(class_name, "main", "GAThread.start(); => START");
        	    //wait for the search thread to finish
        	    try {
       		     // in order to apply wait on an object synchronization must be done
        		     synchronized (instance) {
       		     instance.wait();
        		}
        	    } catch (InterruptedException e1) {
        		    e1.printStackTrace();
        		    MyLog.err(class_name, "main", e1.getMessage());
        	    }
        	    
        	    MyLog.info(class_name, "main", "GAThread; => STOP");
        	    best_GA_sol = GAsearch.getBestSol();
        	    MyLog.info(class_name, "main", "best solution from the GA algorithm:");
        	    MyLog.info(class_name, "main", best_GA_sol.toString());
*/ 
        	    // Start solving
        	    TSsearch.getTabuSearch().setIterationsToGo(parameters.getIterations());
        	    MyLog.info(class_name, "main", "search.tabuSearch.setIterationsToGo(parameters.getIterations()) => number of iterations = " + parameters.getIterations());
        
        	    TSsearch.getTabuSearch().startSolving();
        	    MyLog.info(class_name, "main", "search.tabuSearch.startSolving(); => START");
        
        	    // wait for the search thread to finish
        	    try {
        		// in order to apply wait on an object synchronization must be done
        		synchronized (instance) {
        		    instance.wait();
        		}
        	    } catch (InterruptedException e1) {
        		e1.printStackTrace();
        		MyLog.err(class_name, "main", e1.getMessage());
        	    }
        
        	    duration.stop();
        	    MyLog.info(class_name, "main", "time counting stopped");
        	    MyLog.info(class_name, "main", "total execution time = " + duration.toString());
        
        	    // Count routes
        	    int routesNr = 0;
        	    for (int i = 0; i < TSsearch.feasibleRoutes.length; ++i)
        		for (int j = 0; j < TSsearch.feasibleRoutes[i].length; ++j)
        		    if (TSsearch.feasibleRoutes[i][j].getCustomersLength() > 0)
        			routesNr++;
        	    // Print results
        	    String outSol = String.format("param:%s;  cost:%5.2f;  time:%d;  veicles:%4d\r\n", instance.getParameters().getInputFileName(),
        		    TSsearch.feasibleCost.total, duration.getSeconds(), routesNr);
        	    System.out.println(outSol);
        	    FileWriter fw = new FileWriter(parameters.getOutputFileName(), true);
        	    fw.write(outSol);
        	    fw.close();
        
        	} catch (Exception e) {
        	    e.printStackTrace();
        	    MyLog.err(class_name, "main", e.getMessage());
        	}
        
        	stopLog();
	}
	
	private static void startLog(){
		
		String s = "\n"
				 + "*********************************************************\n"
				 + "*********************************************************\n"
				 + "**                                                     **\n"
				 + "**                VRPTW Execution Start                **\n"
				 + "**                                                     **\n"
				 + "*********************************************************\n"
				 + "*********************************************************\n";
		MyLog.info(class_name, "main", s);
		
	}
	
	private static void stopLog(){
		
		String s = "\n"
				 + "*********************************************************\n"
				 + "*********************************************************\n"
				 + "**                                                     **\n"
				 + "**                 VRPTW Execution End                 **\n"
				 + "**                                                     **\n"
				 + "*********************************************************\n"
				 + "*********************************************************\n";
		MyLog.info(class_name, "main", s);
	}
}
