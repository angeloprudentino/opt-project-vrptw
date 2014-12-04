/**
 *  package: com.softtechdesign.ga
 *  class: GARoute.java
 */
package com.softtechdesign.ga;


import java.util.Random;

import com.mdvrp.Customer;
import com.mdvrp.Instance;



/**
 * @author Angelo Prudentino
 * @date 22/nov/2014
 */
public class GARoute extends GA {

    private static final double alpha = 0.54;
    private static final double beta = 0.30;
    private static final double gamma = 0.19;
	private static final double LOAD_RATIO = 0.10; //used in initPopualtion
    
	private Instance instance;
    private MyGAsolution best_sol; //TODO we need to store the best feasible solution of the GA at each iteration

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
		this.best_sol = new MyGAsolution(instance, chromosomeDim);
		
    }

    /** 
     * @see com.softtechdesign.ga.GA#initPopulation()
     */
    @Override
    protected void initPopulation() {
    	// TODO if i can't find any feasible solution??
    	int v;
    	int c = 0;
    	int NUM_VEHIC = instance.getVehiclesNr();
    	int NUM_CUST = instance.getCustomersNr();
    	Random random = instance.getRandom();
    	
    	
    	boolean[] assignedCust = new boolean[NUM_VEHIC]; 
    	//ProtoChromosome temp = new ProtoChromosome(NUM_VEHIC, NUM_CUST);
    	ProtoChromosome temp = new ProtoChromosome(instance);
    	//number of not assigned customers:
    	int notAssigned;
    	
    	//for each chromosome to be generated (one comes from TS greedy method):
        for (int chrom = 0; chrom < populationDim-1; chrom++){
        	notAssigned = NUM_CUST;
        	for(int i=0; i<NUM_VEHIC; i++)
        		assignedCust[i] = false;
        	
        	//until all customers are assigned:
        	while(notAssigned > 0){
        		//find a cust which is still not served
        		if (notAssigned/NUM_CUST < LOAD_RATIO) {
        			//choose randomly
	        		do {
		            	c = random.nextInt(NUM_CUST);
	        		} while (assignedCust[c] == false);
        		}
        		else {
        			//scan the vector
        			do {
        				c = (++c) % NUM_CUST;
        			} while (assignedCust[c] == false);
        		}
            		
        		//chose a random vehicle to start
            	v = random.nextInt(NUM_VEHIC);
        		
        		//cycle through each vehicle
        		for (int i = 0; i<NUM_VEHIC; i++) {
        			Customer chosenCust = instance.getCustomerByNumID(c);
        			
        			//verify the Capacity & Duration.
        			if(temp.checkCapacity(v, chosenCust) && temp.checkDuration(v, chosenCust)){
	        			//if it's ok to be added, assign it (add to temp) 
	        			temp.addGene(v, chosenCust);
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
    	//not used
    }

    /** 
     * @see com.softtechdesign.ga.GA#doTwoPtCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doTwoPtCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    	ChromCustomer parent1 = (ChromCustomer)Chrom1;
    	ChromCustomer parent2 = (ChromCustomer)Chrom2;
    	ChromCustomer child1 = null, child2=null;
//    	int dimAlphabet = 200;				//TODO IMPOSTARE UNA COSTANTE CON IL NUMERO DI CUSTUMERS
    	pmX(parent1, parent2, child1, child2, populationDim);	//è giusto il valore contenuto in populationDim??
    	Chrom1=child1;
    	Chrom2=child2;
    	
    }
    
    /** 
     * @see partially mapped crossover pmX
     * @author: Luca Boni
     * @date: 28/11/2014
     */
	private void pmX(ChromCustomer parent1,ChromCustomer parent2,
			ChromCustomer child1,ChromCustomer child2,
			/*int cutPoint1,int cutPoint2,*/ int dimAlphabet){
	    
		boolean[] usedValuesChild1 = new boolean[dimAlphabet];
		boolean[] usedValuesChild2 = new boolean[dimAlphabet];
		int arrayDimension = parent1.length();
		int cutPoint1 = (int)Math.random()%arrayDimension;
		int cutPoint2;
		while((cutPoint2 = (int)Math.random()%arrayDimension) == cutPoint1); // to avoid cutPoint1 == cutPoint2

    	    try{
    	    	
    			//initialization of boolean arrays
    			for(int i=0; i<dimAlphabet; i++){
    				usedValuesChild1[i] = false;
    				usedValuesChild2[i] = false;
    				}
    			
    			//part between cut points
    			for(int i = cutPoint1; i < cutPoint2; i++){
    				child1.setGene(parent2.getGene(i), i);
    				usedValuesChild1[child1.getGene(i).getNumber()] = true;
    				child2.setGene(parent2.getGene(i), i);
    				usedValuesChild2[child2.getGene(i).getNumber()] = true;
    			}
    			
    			//part after second cut point
    			for(int i = cutPoint2; i < arrayDimension; i++){
    				
    				//about child 1
    				int pos = applyPmxRule(i, usedValuesChild1, parent1, parent2);
    				child1.setGene(parent1.getGene(pos), i);
    				usedValuesChild1[child1.getGene(pos).getNumber()] = true;
    				
    				//about child 2
    				pos = applyPmxRule(i, usedValuesChild2, parent2, parent1);
    				child2.setGene(parent2.getGene(pos), i);
    				usedValuesChild2[child2.getGene(pos).getNumber()] = true;
    			}
    			
    			//part before first cut point
    			for(int i=0; i<cutPoint1; i++){
    				
    				//about child 1
    				int pos = applyPmxRule(i, usedValuesChild1, parent1, parent2);
    				child1.setGene(parent1.getGene(pos), i);
    				usedValuesChild1[child1.getGene(pos).getNumber()] = true;
    				
    				//about child 2
    				pos = applyPmxRule(i, usedValuesChild2, parent2, parent1);
    				child2.setGene(parent2.getGene(pos), i);
    				usedValuesChild2[child2.getGene(pos).getNumber()] = true;
    			}
    		}catch(GAException e){
    			System.out.println(e);
    		}
	}

	private int applyPmxRule (int i, boolean[] booleanArray, ChromCustomer parentA, ChromCustomer parentB) throws GAException{
		int pos = i;
		while(alreadyUsed(booleanArray,parentA.getGene(pos)) == true){
			pos = parentB.getPosition(parentA.getGene(pos));
			if(pos < 0) throw new GAException("Crossover error"); 
		}
		return pos;
	}

	private boolean alreadyUsed(boolean[] array, Customer c) {
		int value = c.getNumber();
		return array[value];
	}
    
        /** 
         * @see com.softtechdesign.ga.GA#doUniformCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
         */
        @Override
        protected void doUniformCrossover(Chromosome Chrom1, Chromosome Chrom2) {
        	//not used
        }
    
        /** 
         * @see com.softtechdesign.ga.GA#getFitness(int)
         */
        @Override
        protected double getFitness(int iChromIndex) {
    	
    	ChromCustomer chrom = (ChromCustomer) chromosomes[iChromIndex];
    	double total = chrom.getCost().getTravelTime() + alpha * chrom.getCost().getLoadViol() + beta * chrom.getCost().getDurationViol() + gamma * chrom.getCost().getTwViol();
    	
    	return 1/total;
        }

		/**
		 * @return the best_sol
		 */
		public MyGAsolution getBestSol() {
			return best_sol;
		}

		/**
		 * @param best_sol -> the best fesible sol from GA
		 */
		public void setBestSol(MyGAsolution best_sol) {
			this.best_sol = best_sol;
		}

}
