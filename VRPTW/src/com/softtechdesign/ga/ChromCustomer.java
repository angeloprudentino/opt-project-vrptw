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
    private Depot[] depots; 		     //some customers are actually depots
    
    private Instance instance;           // Reference to the instance of the problem 
    private Cost cost;			         // Cost of the entire chromosome
    private double[] vehicle_load_viol;  // Load violation of each vehicle
    
    
    /**
     * Creates new Customer array of genes 
     * @param iGenesDim the size of the array of genes
     */
    protected ChromCustomer(int iGenesDim, Instance instance)
    {
        this.genes = new Customer[iGenesDim];
        for(int i=0; i<iGenesDim; i++)
        	genes[i] = new Customer();
        this.instance = instance;
        this.cost = new Cost();
        int nv = instance.getVehiclesNr();
        this.vehicle_load_viol = new double[nv];
        for(int i=0; i<nv; i++)
        	vehicle_load_viol[i] = -instance.getVehicleCapacity();
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
    
  //TODO at the moment I only manage the case of 1 single depot
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

        for (iGene = 0; iGene < getGenes().length; iGene++)
            this.getGenes()[iGene] = chromC.getGenes()[iGene];
        
        updateChromCost(chromC);
    }
    
    private void updateChromCost(ChromCustomer chromosome){
	
	double loadV = 0;
	double twViol = 0;
	double totTwViol = 0;
	double waitingT = 0;
	double totWaitingT = 0;
	double travelT = 0;
	double totalTime = 0;
	double serviceT = 0;
	
	Customer customerK, customerK_1;
	int iter = chromosome.length();
	int vehicle_id = 0;

	for (int k=1; k < iter; ++k) {
	    // get the actual customer
	    customerK = chromosome.getGene(k);
	    // get the preceding customer
	    customerK_1 = chromosome.getGene(k-1);
	    
	    if (k == 1) { 
	    	//the starting point is always the depot
	    	
	    	//getCost().addTravel(getInstance().getTravelTime(getDepotNum(), customerK.getNumber()));
	    	travelT += getInstance().getTravelTime(getDepotNum(), customerK.getNumber());
	    	totalTime += travelT;
    		UpdateLoadViol(vehicle_id, customerK.getCapacity());
	    } 
	    
    	if (k > 1 && customerK.getNumber() != 0) { 
    		// customerK.getNumber() == 0 --> Depot 
    	    
    		//getCost().addTravel(getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber()));
    		travelT += getInstance().getTravelTime(customerK_1.getNumber(), customerK.getNumber());
    		totalTime += travelT;
    		UpdateLoadViol(vehicle_id, customerK.getCapacity());
        } 
    	
        if((k > 1 && customerK.getNumber() == 0) || (k == iter-1)){ 
        	// (k > 1 && customerK.getNumber() == 0) -> this is the end of a single route for a vehicle
        	// (k == iter-1) -> the last vehicle goes back to the depot
        	
    	    //getCost().addTravel(getInstance().getTravelTime(customerK_1.getNumber(), getDepotNum()));
        	if(k == iter-1){
        		travelT += getInstance().getTravelTime(customerK.getNumber(), getDepotNum());
        		totalTime += travelT;
        	}
        	else{
        		travelT += getInstance().getTravelTime(customerK_1.getNumber(), getDepotNum());
        		totalTime += travelT;
        	}
        	
			// add the depot time window violation if any
			twViol += Math.max(0, totalTime - getDepot().getEndTw());
			//getCost().setTWViol(twViol);
			// update cost with timings of the depot
			//getCost().setDepotTwViol(twViol);
			//getCost().setReturnToDepotTime(totalTime);
			//getCost().addLoadViol(Math.max(0, vehicle_load_viol[vehicle_id]));
			loadV += Math.max(0, vehicle_load_viol[vehicle_id]);
			//not considered in this problem
			//getCost().addDurationViol(Math.max(0, route.getDuration() - route.getDurationAdmited()));
	
			// update total violation
			//getCost().calculateTotalCostViol();
			vehicle_id++;

        } 
	    
	    customerK.setArriveTime(totalTime);
	    // add waiting time if any
	    waitingT = Math.max(0, customerK.getStartTw() - totalTime);
	    totWaitingT += waitingT;
	    //getCost().addWaitingTime(waitingT);
	    // update customer timings information
	    customerK.setWaitingTime(waitingT);

	    totalTime += waitingT;

	    // add time window violation if any
	    twViol = Math.max(0, totalTime - customerK.getEndTw());
	    //getCost().addTWViol(twViol);
	    customerK.setTwViol(twViol);
	    totTwViol += twViol;
	    // add service time to the chromosome
	    serviceT += customerK.getServiceDuration();
	    // add the service time to the total
	    totalTime += serviceT;
	    // add service time to the chromosome
	    //getCost().addServiceTime(customerK.getServiceDuration());
	    // add capacity to the chromosome
	    	
//	    if (k == iter-1){
//	    	// the last vehicle goes back to the depot
//	    	getCost().travelTime += getInstance().getTravelTime(customerK.getNumber(), getDepotNum());
//	    	totalTime += getCost().getTravelTime();
//			// add the depot time window violation if any
//			twViol = Math.max(0, totalTime - getDepot().getEndTw());
//			getCost().addTWViol(twViol);
//			// update cost with timings of the depot
//			getCost().setDepotTwViol(twViol);
//			getCost().setReturnToDepotTime(totalTime);
//			getCost().setLoadViol(Math.max(0, getCost().load - route.getLoadAdmited()));
//			getCost().setDurationViol(Math.max(0, route.getDuration() - route.getDurationAdmited()));
//	
//			// update total violation
//			// Angelo -> to me this is useless
//			//getCost().calculateTotalCostViol();
//	    }
	 }

	cost.setTravelTime(travelT);
	cost.setServiceTime(serviceT);
	cost.setWaitingTime(totWaitingT);
	//cost.setTotal(totalTime);
	cost.setTwViol(totTwViol);
	cost.setLoadViol(loadV);
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
		int len = genes.length;
		for(int i = 0; i < len; i++){
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
