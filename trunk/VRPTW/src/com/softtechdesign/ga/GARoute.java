/**
 *  package: com.softtechdesign.ga
 *  class: GARoute.java
 */
package com.softtechdesign.ga;


import java.util.Random;
import com.mdvrp.Instance;



/**
 * @author Angelo Prudentino
 * @date 22/nov/2014
 */
public class GARoute extends GA {

    private static final double alpha = 0.54;
    private static final double beta = 0.30;
    private static final double gamma = 0.19;
    private Instance instance;

    /**
     * @param chromosomeDim
     * @param populationDim
     * @param crossoverProb
     * @param randomSelectionChance
     * @param maxGenerations
     * @param numPrelimRuns
     * @param maxPrelimGenerations
     * @param mutationProb
     * @param crossoverType
     * @param computeStatistics
     */
    public GARoute(int chromosomeDim, 
	           int populationDim, 
	           double crossoverProb, 
	           int randomSelectionChance,
	           int maxGenerations, 
	           int numPrelimRuns, 
	           int maxPrelimGenerations, 
	           double mutationProb, 
	           int crossoverType,
	           boolean computeStatistics,
	           Instance instance) {
	
	super(chromosomeDim, populationDim, crossoverProb, randomSelectionChance, maxGenerations, numPrelimRuns,
		maxPrelimGenerations, mutationProb, crossoverType, computeStatistics);
	this.instance = instance;
	
    }

    /** 
     * @see com.softtechdesign.ga.GA#initPopulation()
     */
    @Override
    protected void initPopulation() {
    	// TODO gestire il caso in cui non riesca a trovare soluzioni feasible almeno in tempo utile
    	int v, c;
    	// TODO implement properly:
    	
    	int NUM_VEHIC = instance.getVehiclesNr();
    	int NUM_CUST = instance.getCustomersNr();
    	Random random = instance.getRandom();
    	
    	
    	boolean[] assignedCust = new boolean[NUM_VEHIC]; 
    	ProtoChromosome temp = new ProtoChromosome(instance.getVehiclesNr(), instance.getCustomersNr());
    	//number of not assigned customers:
    	int notAssigned;
    	
    	//for each chromosome to be generated (one comes from TS):
        for (int chrom = 0; chrom < populationDim-1; chrom++){
        	notAssigned = NUM_CUST;
        	
        	//until all customers are assigned:
        	while(notAssigned > 0){
        		//find a cust which is still not served
        		do {
	            	c = random.nextInt(NUM_CUST);
        		} while (assignedCust[c] == false);
        		// TODO: optimize the research for nearly empty vector
            		
        		//chose a random vehicle to start
            	v = random.nextInt(NUM_VEHIC);
        		
        		//cycle through each vehicle
        		for (int i = 0; i<NUM_VEHIC; i++) {
        			//try to assign c to v:
        			//verify the Capacity & Duration.
        			if(){
	        			//if it's ok to be added, make it active (add to temp) 
	        			temp.addGene(v, instance.getCustomerByNumID(c));
        				//disable customer (already served) setting the assignedCust
        				assignedCust[c] = true;
        				notAssigned--;
        				break;
	        		}
        			//if it's not ok to be added
        			//cycle through the others vehicle v
        			v=(v+1)%NUM_VEHIC;
        		}
        		//if customer is not assignable, then the protosolution must be rebuilt from scratch.
        		if (assignedCust[c] == false){
        			//repeat last iteration of chrom for.
        			chrom--;
        			break;
        		}
        		//if customer was assigned we can go on with the next customer
        	}
        	
        	//if it's a feasible solution then build the chromosome from ProtoChromosome
        	if (notAssigned == 0){
        		this.chromosomes[chrom] = temp.toChromosome();
            	this.chromosomes[chrom].fitness = getFitness(chrom);
        	}
    }
}

    /** 
     * @see com.softtechdesign.ga.GA#doRandomMutation(int)
     */
    @Override
    protected void doRandomMutation(int iChromIndex) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doOnePtCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doOnePtCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doTwoPtCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doTwoPtCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doUniformCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doUniformCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#getFitness(int)
     */
    @Override
    protected double getFitness(int iChromIndex) {
	
	ChromCustomer chrom = (ChromCustomer) chromosomes[iChromIndex];
	double total = chrom.getTravelTime() + alpha * chrom.getLoadViol() + beta * chrom.getDurationViol() + gamma * chrom.getTwViol();
	
	return 1/total;
    }

}
