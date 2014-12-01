package com.softtechdesign.ga;


import com.mdvrp.Cost;
import com.mdvrp.Customer;
import com.mdvrp.Instance;
import com.mdvrp.Route;
import com.mdvrp.Vehicle;

public class ProtoChromosome {
	//support class in the creation of the initial population. it contains the chromosome divided in a matrix: [vehicle][customer]
	
	
	Route routes[][]; //it'a matrix for compatibility with TS format
	int NVehic;
	int NCust;
	

	public ProtoChromosome(Instance instance) {
		NVehic = instance.getVehiclesNr();
		NCust = instance.getCustomersNr();
		Route routes[][] = new Route[NVehic][1];
		for(int i = 0; i<NVehic; i++){
			//only one column is actually used
			routes[i][0] = new Route();
			// add the depot as the first node to the route
			routes[i][0].setDepot(instance.getDepot(0));
			// set the cost of the route
			Cost cost = new Cost();
			routes[i][0].setCost(cost);
			// assign vehicle
			Vehicle vehicle = new Vehicle();
			vehicle.setCapacity(instance.getCapacity(0, 0));
			vehicle.setDuration(instance.getDuration(0, 0));
			routes[i][0].setAssignedVehicle(vehicle);
		}
		
//
//  from mySolution.initializeRoutes			
//		// Creation of the routes; each route starts at the depot
//		for (int i = 0; i < instance.getDepotsNr(); ++i)
//			for (int j = 0; j < instance.getVehiclesNr(); ++j){
//					// initialization of routes
//					routes[i][j] = new Route();
//					routes[i][j].setIndex(i*(instance.getVehiclesNr()) + j);
//					
//					// add the depot as the first node to the route
//					routes[i][j].setDepot(instance.getDepot(i));
//					
//					// set the cost of the route
//					Cost cost = new Cost();
//					routes[i][j].setCost(cost);
//					
//					// assign vehicle
//					Vehicle vehicle = new Vehicle();
//					vehicle.setCapacity(instance.getCapacity(i, 0));
//					vehicle.setDuration(instance.getDuration(i, 0));
//					routes[i][j].setAssignedVehicle(vehicle);
//					
//				}
	}
	
	


	public void addGene(int v, Customer cust) {
		routes[v][0].addCustomer(cust);
	}

	
	public Chromosome toChromosome() {
		//TODO chiamata a oggetto myconverter
		return null;
	}
	
	public boolean checkDuration(int vehic, Customer candidate){
		if (candidate.getServiceDuration() + routes[vehic][0].getDuration()  
				<= routes[vehic][0].getDurationAdmited()){
			return true;
		}
		else
			return false;
	}
	
	public boolean checkCapacity(int vehic, Customer candidate){
		if (candidate.getCapacity() + routes[vehic][0].getCost().load 
				<= routes[vehic][0].getLoadAdmited())
			return true;
		else
			return false;
	}
}
