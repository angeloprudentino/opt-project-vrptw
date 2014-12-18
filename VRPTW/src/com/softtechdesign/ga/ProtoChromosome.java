package com.softtechdesign.ga;


import com.TabuSearch.MyTSsolution;
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
	Instance instance;
	

	public ProtoChromosome(Instance instance) {
		this.instance = instance; 
		NVehic = instance.getVehiclesNr();
		NCust = instance.getCustomersNr();
		this.routes = new Route[1][NVehic];
		for(int i = 0; i<NVehic; i++){
			//only one column is actually used
			routes[0][i] = new Route();
			// add the depot as the first node to the route
			routes[0][i].setDepot(instance.getDepot(0));
			// set the cost of the route
			Cost cost = new Cost();
			routes[0][i].setCost(cost);
			// assign vehicle
			Vehicle vehicle = new Vehicle();
			vehicle.setCapacity(instance.getCapacity(0, 0));
			vehicle.setDuration(instance.getDuration(0, 0));
			routes[0][i].setAssignedVehicle(vehicle);
		}
	}
		

	public void addGene(int v, Customer cust) {
		routes[0][v].addCustomer(cust);
	}

	
	public Chromosome toChromosome() {
		//just a sort of wrapping. TSsol is created with an empty builder
		MyTSsolution TSsol = new MyTSsolution(instance, false);
		TSsol.setRoutes(routes);
		
		//MyTSsolution -> MyGaSolution
		MyGAsolution gaSol = TSsol.ConvertTSGA();
		
		return gaSol.getSolution();
	}
	
	public boolean checkDuration(int vehic, Customer candidate){
		if (candidate.getServiceDuration() + routes[0][vehic].getDuration()  
				<= routes[0][vehic].getDurationAdmited()){
			return true;
		}
		else
			return false;
	}
	
	public boolean checkCapacity(int vehic, Customer candidate){
		if (candidate.getCapacity() + routes[0][vehic].getCost().load 
				<= routes[0][vehic].getLoadAdmited())
			return true;
		else
			return false;
	}
}
