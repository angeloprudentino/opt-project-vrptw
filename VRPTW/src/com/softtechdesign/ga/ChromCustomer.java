/**
 *  package: com.softtechdesign.ga
 *  class: ChromCustomer.java
 */
package com.softtechdesign.ga;

import java.util.Arrays;

import com.mdvrp.Cost;
import com.mdvrp.Customer;
import com.mdvrp.Depot;
import com.mdvrp.Instance;

/**
 * 
 * Chromosome class where genes are stored as an array of Customers (pointers)
 * 
 * @author Angelo Prudentino
 * @date 20/nov/2014
 *
 */
public class ChromCustomer extends Chromosome {

    private static final int DEPOT = 0;

    /** array of genes which comprise this Chromosome */
    private Customer[] genes;
    private Depot[] depots; 		     //some customers are actually depots
    
    private Instance instance;           // Reference to the instance of the problem 
    private Cost cost;			         // Cost of the entire chromosome
    
    
    /**
     * Creates new Customer array of genes 
     * @param iGenesDim the size of the array of genes
     */
    protected ChromCustomer(int iGenesDim, Instance instance)
    {
        this.setGenes(new Customer[iGenesDim]);
        this.instance = instance;
        this.cost = new Cost();
        buildDepots();
    }
    
    /**
     * this method is required to build an array of all the depots
     * considered in the instance of problem
     */
    private void buildDepots() {
	int depot_num = instance.getDepotsNr();
	depots = new Depot[depot_num];
	for(int i=0; i<depot_num; i++)
	    depots[i] = instance.getDepot(i);
    }

    /**
     * @return the cost
     */
    protected Cost getCost() {
        return cost;
    }

    /**
     * @return the instance
     */
    protected Instance getInstance() {
        return instance;
    }

    /**
     * sets the gene value
     * @param gene value to set
     * @param geneIndex index of gene
     */
    
    protected void setGene(Customer gene, int geneIndex)
    {
        getGenes()[geneIndex] = gene;
    }

    /**
     * Calculates how many genes are the same between this chromosome and the given chromosome
     * @param chromosome chromosome to compare
     * @return int
     */
    protected int getNumGenesInCommon(Chromosome chromosome)
    {
        int numGenesInCommon = 0;
        ChromCustomer chromR = (ChromCustomer)chromosome;

        for (int i = 0; i < getGenes().length; i++)
        {
            if (this.getGenes()[i].equals(chromR.getGenes()[i]))
                numGenesInCommon++;
        }
        return (numGenesInCommon);
    }

    /**
     * Get a reference to the genes array, edited to be public instead of protected
     * @return String[]
     */
    public Customer[] getGenes()
    {
        return genes;
    }
    
    /**
     * return the array of genes as a string; 
     * Example: " [0] = 0 - 1 - 2 - 3
     *            [1] = 4 - 5 -6 "
     * @return String
     */
    protected String getGenesAsStr()
    {
        int genesLength = getGenes().length;
        
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < genesLength; i++)
        {
        	if(getGenes()[i].getNumber() == 0)
        		sb.append("\n  [" + i + "] = ");
            sb.append(getGenes()[i].toString());
        }
        sb.append("\n");
        sb.append(cost.toString());
        return (sb.toString());
    }

    
    /**
     * return the gene indexed by iGene as a char
     * @param iGene
     * @return String
     */
    protected Customer getGene(int iGene)
    {
        return (this.getGenes()[iGene]);
    }
    
    /**
     * Copy the genes from the given chromosome over the existing genes
     * @param chromosome
     */
    protected void copyChromGenes(Chromosome chromosome)
    {
        int iGene;
        ChromCustomer chromC = (ChromCustomer)chromosome;

        for (iGene = 0; iGene < getGenes().length; iGene++)
            this.getGenes()[iGene] = chromC.getGenes()[iGene];
        
        updateChromCost(chromC);
    }
    
    private void updateChromCost(ChromCustomer chromosome){
	
//	double loadV = 0;
//	double durationV = 0;
	double twViol = 0;
	double waitingT = 0;
	double totalTime = 0;
	
	Customer customerK, customerK_1;
	int iter = chromosome.length();
	
	// sum distances between each node in the route
	for (int k=1; k < iter; ++k) {
	    // get the actual customer
	    customerK = chromosome.getGene(k);
	    // get the preceding customer
	    customerK_1 = chromosome.getGene(k-1);
	    
	    // add travel time to the chromosome cost
	    if (k == 1) { 
	    	//the starting point is always the depot
	    	getCost().travelTime += getInstance().getTravelTime(DEPOT, customerK.getNumber());
	    	totalTime += getInstance().getTravelTime(DEPOT, customerK.getNumber());
	    } 
	    
    	if (k > 1 && customerK.getNumber() != 0) { // customerK.getNumber() == 0 --> Depot 
    	    getCost().travelTime += getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber());
    	    totalTime += getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber());
        } 
        else { // k > 1 && customerK.getNumber() == 0 
        	//this is the end of a single route for a vehicle 
    	    getCost().travelTime += getInstance().getTravelTime(customerK_1.getNumber(), DEPOT);
    	    totalTime += getInstance().getTravelTime(customerK_1.getNumber(), DEPOT);
        } // end if else
	    
	    customerK.setArriveTime(totalTime);
	    // add waiting time if any
	    waitingT = Math.max(0, customerK.getStartTw() - totalTime);
	    getCost().waitingTime += waitingT;
	    // update customer timings information
	    customerK.setWaitingTime(waitingT);

	    totalTime = Math.max(customerK.getStartTw(), totalTime);

	    // add time window violation if any
	    twViol = Math.max(0, totalTime - customerK.getEndTw());
	    getCost().addTWViol(twViol);
	    customerK.setTwViol(twViol);
	    // add the service time to the total
	    totalTime += customerK.getServiceDuration();
	    // add service time to the chromosome
	    getCost().serviceTime += customerK.getServiceDuration();
	    // add capacity to the chromosome
	    
	    // TODO the load cost should be in some way related the serving vehicle
	    //getCost().load += customerK.getCapacity();
	
	    if (k == iter-1){
	    	// the last vehicle goes back to the depot
	    	getCost().travelTime += getInstance().getTravelTime(customerK.getNumber(), DEPOT);
	    	totalTime += getInstance().getTravelTime(customerK.getNumber(), DEPOT);
			// add the depot time window violation if any
			twViol = Math.max(0, totalTime - getDepot().getEndTw());
			getCost().addTWViol(twViol);
			// update cost with timings of the depot
			getCost().setDepotTwViol(twViol);
			getCost().setReturnToDepotTime(totalTime);
			//TODO implement the counting of load violation in cost
			//getCost().setLoadViol(Math.max(0, getCost().load - route.getLoadAdmited()));
			//getCost().setDurationViol(Math.max(0, route.getDuration() - route.getDurationAdmited()));
	
			// update total violation
			// Angelo -> to me this is useless
			//getCost().calculateTotalCostViol();
	    }
	 }
    }
 
    /**
     * @return
     */
	private Depot getDepot() {
		return depots[DEPOT]; // TODO at the moment i manage only the case of one depot
	}

	protected int length() {
		return getGenes().length;
	}

	protected int getPosition(Customer gene) {
		for (int i = 0; i < getGenes().length; i++) {
			if (getGenes()[i].equals(gene) == true)
				return i;
		}
		return -1; // error code because this method have to find the value into the array
	}
	
	public int getPositionNumber(Customer gene) {
		for(int i = 0; i < genes.length; i++){
			if(genes[i].getNumber() == gene.getNumber()) return i;
		}
		return -1; //error code because this method have to find the value into the array
	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(Customer[] genes) {
		this.genes = genes;
	}

	@Override
	public String toString() {
		return "ChromCustomer [genes=" + Arrays.toString(genes) + ", cost=" + cost + "]";
	}

	
	
}
