/**
 *  package: com.softtechdesign.ga
 *  class: ChromCustomer.java
 */
package com.softtechdesign.ga;

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

    private static final int DEPOT_NUM = 0;

    /** array of genes which comprise this Chromosome */
    private Customer[] genes;
    private Depot[] depots; 		 //some customers are actually depots
    
    private Instance instance;           // Reference to the instance of the problem 
    private Cost cost;			 // Cost of the entire chromosome
    
    
    /**
     * Creates new Customer array of genes 
     * @param iGenesDim the size of the array of genes
     */
    public ChromCustomer(int iGenesDim)
    {
        genes = new Customer[iGenesDim];
        cost = new Cost();
    }
    
    /**
     * @param instance of the problem
     */
    public void setInstance(Instance instance) {
        this.instance = instance;
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
     * Returns the array of genes as a string
     * @return String
     */
    public String toString()
    {
        return (getGenesAsStr());
    }

    /**
     * @return the cost
     */
    public Cost getCost() {
        return cost;
    }

    /**
     * @return the instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * sets the gene value
     * @param gene value to set
     * @param geneIndex index of gene
     */
    
    public void setGene(Customer gene, int geneIndex)
    {
        genes[geneIndex] = gene;
    }

    /**
     * Calculates how many genes are the same between this chromosome and the given chromosome
     * @param chromosome chromosome to compare
     * @return int
     */
    public int getNumGenesInCommon(Chromosome chromosome)
    {
        int numGenesInCommon = 0;
        ChromCustomer chromR = (ChromCustomer)chromosome;

        for (int i = 0; i < genes.length; i++)
        {
            if (this.genes[i].equals(chromR.genes[i]))
                numGenesInCommon++;
        }
        return (numGenesInCommon);
    }

    /**
     * Get a reference to the genes array
     * @return String[]
     */
    public Customer[] getGenes()
    {
        return(genes);
    }
    
    /**
     * return the array of genes as a string; 
     * Example: "0 - 1 - 2 - 3|0 - 4 - 5|"
     * 
     * @return String
     */
    public String getGenesAsStr()
    {
        int genesLength = genes.length;
        
        StringBuffer sb = new StringBuffer(genesLength);
        for (int i = 0; i < genesLength; i++)
        {
            sb.append(genes[i].toString());
            if (i < genesLength - 1)
                sb.append("|");
        }
        return (sb.toString());
    }

    /**
     * return the gene indexed by iGene as a char
     * @param iGene
     * @return String
     */
    public Customer getGene(int iGene)
    {
        return (this.genes[iGene]);
    }
    
    /**
     * Copy the genes from the given chromosome over the existing genes
     * @param chromosome
     */
    public void copyChromGenes(Chromosome chromosome)
    {
        int iGene;
        ChromCustomer chromC = (ChromCustomer)chromosome;

        for (iGene = 0; iGene < genes.length; iGene++)
            this.genes[iGene] = chromC.genes[iGene];
        
        updateChromCost(chromC);
    }
    
    private void updateChromCost(ChromCustomer chromosome){
	
//	double loadV = 0;
//	double durationV = 0;
	double twViol = 0;
	double waitingT = 0;
	double totalTime = 0;
	
	Customer customerK, customerK_1;
	
	// sum distances between each node in the route
	for (int k = 0; k < chromosome.length(); ++k) {
	    // get the actual customer
	    customerK = chromosome.getGene(k);
	    customerK_1 = chromosome.getGene(k-1);
	    
	    // add travel time to the chromosome
	    if (k == 0 && customerK.getNumber() == 0) { // customerK.getNumber() = 0 --> Depot
		getCost().travelTime += getInstance().getTravelTime(DEPOT_NUM, customerK.getNumber());
		totalTime += getInstance().getTravelTime(DEPOT_NUM, customerK.getNumber());
	    } else {
		getCost().travelTime += getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber());
		totalTime += getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber());
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
	    getCost().load += customerK.getCapacity();
	
	    if (k != 0 && customerK.getNumber() == 0) {// k!=0 means the end of // one route
		// add the distance to return to depot: from last node to depot
		totalTime += getInstance().getTravelTime(customerK_1.getNumber(), DEPOT_NUM);
		getCost().travelTime += getInstance().getTravelTime(customerK_1.getNumber(), DEPOT_NUM);
		// add the depot time window violation if any
		twViol = Math.max(0, totalTime - getDepot().getEndTw());
		getCost().addTWViol(twViol);
		// update route with timings of the depot
		getCost().setDepotTwViol(twViol);
		getCost().setReturnToDepotTime(totalTime);
		//TODO implement the counting of violation in cost
		//getCost().setLoadViol(Math.max(0, getCost().load - route.getLoadAdmited()));
		//getCost().setDurationViol(Math.max(0, route.getDuration() - route.getDurationAdmited()));

		// update total violation
		getCost().calculateTotalCostViol();
	    }
	}
    }
 
    /**
     * @return
     */
    private Depot getDepot() {
	return depots[DEPOT_NUM-1]; //TODO at the moment i manage only the case of one depot
    }

    public int length() {
	return genes.length;
    }

    public int getPosition(Customer gene) {
	for (int i = 0; i < genes.length; i++) {
	    if (genes[i].equals(gene) == true)
		return i;
	}
	return -1; // error code because this method have to find the value into
		   // the array
    }

}
