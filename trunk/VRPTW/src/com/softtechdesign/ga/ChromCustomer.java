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

    /** array of genes which comprise this Chromosome */
    private Customer[] genes;
    private int genes_dim;
    private Depot[] depots; 		     //some customers are actually depots
    private int depot_id;
    
    private Instance instance;           // Reference to the instance of the problem 
    private Cost cost;			         // Cost of the entire chromosome
    private int vehicleNum;
    private double[] vehicle_load_viol;  // Load violation of each vehicle
    
    
    /**
     * Creates new Customer array of genes 
     * @param iGenesDim the size of the array of genes
     */
    protected ChromCustomer(int iGenesDim, Instance instance)
    {
        this.genes = new Customer[iGenesDim];
        this.genes_dim = iGenesDim;
        for(int i=0; i<iGenesDim; i++)
        	genes[i] = new Customer();
        this.instance = instance;
        this.cost = new Cost();
        vehicleNum = instance.getVehiclesNr();
        this.vehicle_load_viol = new double[vehicleNum];
        initLoadViol();
        this.depot_id = instance.getCustomersNr();
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
    
    private void initLoadViol(){
        for(int i=0; i<vehicleNum; i++)
        	vehicle_load_viol[i] = -instance.getVehicleCapacity();
    }
    
    private int getDepotNum(){
    	return depots[0].getNumber();
    }
    
    private Depot getDepot(){
    	return depots[0];
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
     * update the current load violation for the vehicle
     * @param VehicleNum
     * @param load
     */
    private void UpdateLoadViol(int VehicleNum, double load){
    	vehicle_load_viol[VehicleNum] += load;
    }

    /**
     * return the gene indexed by iGene as a char
     * @param iGene
     * @return String
     */
    protected Customer getGene(int iGene)
    {
        return genes[iGene];
    }
    
    /**
     * sets the gene value
     * @param gene value to set
     * @param geneIndex index of gene
     */
    
    protected void setGene(Customer gene, int geneIndex)
    {
        genes[geneIndex] = gene;
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

        for (int i = 0; i < genes_dim; i++)
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
	 * @param genes the genes to set
	 */
	public void setGenes(Customer[] genes) {
		this.genes = genes;
	}

    /**
     * return the array of genes as a string; 
     * Example: " [0] = 0  1  2  3
     *            [1] = 4  5  6 "
     * @return String
     */
    protected String getGenesAsStr()
    {
        int genesLength = getGenes().length;
        
        StringBuffer sb = new StringBuffer("");
        for (int i = 0, j = 0; i < genesLength; i++)
        {
        	int c1 = getGenes()[i].getNumber();
        	if(c1 == getDepotNum())
        		sb.append("\n  [" + (j++) + "] = ");
            sb.append(getGenes()[i].toString()).append(" ");
        }
        sb.append("\n\n");
        sb.append(cost.toString());
        return (sb.toString());
    }

    /**
     * Copy the genes from the given chromosome over the existing genes
     * @param chromosome
     */
    protected void copyChromGenes(Chromosome chromosome)
    {
        int iGene;
        ChromCustomer chromC = (ChromCustomer)chromosome;

        for (iGene = 0; iGene < genes_dim; iGene++)
            this.getGenes()[iGene] = chromC.getGenes()[iGene];
        
        updateChromCost(this);
    }
    
    private void updateChromCost(ChromCustomer chromosome){
	
   	double customerWaitingT = 0;

    double vehicleTravelT = 0;
	double vehicleTwViol = 0;
	double vehicleWaitingT = 0;
	double vehicleServiceT = 0;
	double vehicleTotalT = 0;

	double totalTravelT = 0;
	double totalLoadViol = 0;
	double totalTwViol = 0;
	double totalWaitingT = 0;
	double totalServiceT = 0;
	double totalT = 0;

	
	Customer customerK, customerK_1;
	int iter = chromosome.length();
	int vehicle_id = 0;

	for (int k=1; k < iter; ++k) {
	    // get the actual customer
	    customerK = chromosome.getGene(k);
	    // get the preceding customer
	    customerK_1 = chromosome.getGene(k-1);
	    	    
    	if (customerK.getNumber() != depot_id) { 
    	    
    		vehicleTravelT += getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber());
    		vehicleTotalT += vehicleTravelT;
    		UpdateLoadViol(vehicle_id, customerK.getCapacity());
        } 
    	
        if((customerK.getNumber() == depot_id) || (k == iter-1)){ 
        	// (customerK.getNumber() == depot_id) -> this is the end of a single route for a vehicle
        	// (k == iter-1) -> the last vehicle goes back to the depot
        	
        	if(k == iter-1){
        		vehicleTravelT += getInstance().getTravelTime(customerK.getNumber(), getDepotNum());
        		vehicleTotalT += vehicleTravelT;
        	}
        	else{
        		vehicleTravelT += getInstance().getTravelTime(customerK_1.getNumber(), getDepotNum());
        		vehicleTotalT += vehicleTravelT;
        	}
        	
			// add the depot time window violation if any
			vehicleTwViol += Math.max(0, vehicleTotalT - getDepot().getEndTw());
			
			
			totalTravelT += vehicleTravelT;  
			totalLoadViol += Math.max(0, vehicle_load_viol[vehicle_id]); 
			totalTwViol +=  vehicleTwViol;
			totalWaitingT += vehicleWaitingT;
			totalServiceT += vehicleServiceT;
			totalT += vehicleTotalT;

			vehicle_id++;
			// new vehicle so i put everything to zero			
			vehicleTravelT = 0;
			vehicleTwViol = 0;
			vehicleWaitingT = 0;
			vehicleTotalT = 0;
			vehicleServiceT = 0;
			initLoadViol();

        } 
	    
	    customerK.setArriveTime(vehicleTotalT);
	    // add waiting time if any
	    customerWaitingT = Math.max(0, customerK.getStartTw() - vehicleTotalT);
	    vehicleWaitingT += customerWaitingT;

	    // update customer timings information
	    customerK.setWaitingTime(customerWaitingT);

	    vehicleTotalT += customerWaitingT;

	    // add time window violation if any
	    vehicleTwViol = Math.max(0, vehicleTotalT - customerK.getEndTw());

	    customerK.setTwViol(vehicleTwViol);
	    // add service time to the chromosome
	    vehicleServiceT += customerK.getServiceDuration();
	    // add the service time to the total
	    vehicleTotalT += vehicleServiceT;
	    // add service time to the chromosome
	 }
	
	cost.setTravelTime(totalTravelT);
	cost.setServiceTime(totalServiceT);
	cost.setWaitingTime(totalWaitingT);
	cost.setTotal(totalT);
	cost.setTwViol(totalTwViol);
	cost.setLoadViol(totalLoadViol);

    }
 
	protected int length() {
		return getGenes().length;
	}

	protected int getPosition(Customer gene) {
		for (int i = 0; i < genes_dim; i++) {
			if (getGenes()[i].equals(gene) == true)
				return i;
		}
		return -1; // error code because this method have to find the value into the array
	}
	
	public int getPositionNumber(Customer gene) {
		
		for(int i = 0; i < genes_dim; i++){
			if(genes[i].getNumber() == gene.getNumber()) 
				return i;
		}
		return -1; //error code because this method have to find the value into the array
	}

	@Override
	public String toString() {
		return "ChromCustomer [genes=" + Arrays.toString(genes) + ", cost=" + cost + "]";
	}

	
	
}
