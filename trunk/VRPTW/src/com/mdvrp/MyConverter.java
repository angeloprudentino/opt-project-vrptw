package com.mdvrp;

import java.util.ArrayList;
import java.util.List;

import com.TabuSearch.MyTSsolution;



public class MyConverter {

	/**
	 * Tabu Search format (routes matrix) to Genetic Algorithm format (linear vector)
	 */	
	@SuppressWarnings("static-access")
	public static Customer[] ConvertTSGA(MyTSsolution sol){
		int i=0, j=0, k=0, tot=0;
		Route[][] routes = sol.getRoutes();
		List<Customer> currV;
		Customer[] GACustArray; 
				
		
		Customer DepotCust = new Customer();
		DepotCust.setNumber(0);
		
		for (i=0; i<sol.getInstance().getVehiclesNr(); i++){
			for (j=0; j<routes[0][i].getCustomersLength() ; j++) {
					tot++;
			}
			tot++;
		}
		GACustArray = new Customer[tot];
		GACustArray[k]=DepotCust;
		k++;
		for (i=0; i<sol.getInstance().getVehiclesNr(); i++){
			currV = routes[0][i].getCustomers();
			for (j=0; j<routes[0][i].getCustomersLength() ; j++) {
					GACustArray[k] = currV.get(j);
					k++;
			}
			if (k==tot)
				break;
			GACustArray[k]=DepotCust;
			k++;
		}
		
		sol.setGACustArray(GACustArray);
		
		return GACustArray;
	}
	
	/**
	 * Genetic Algorithm format (linear vector) to Tabu Search format (routes matrix)
	 */	
	public static Route[][] ConvertGATS (MyTSsolution sol){
		ArrayList<Customer> tempCust = new ArrayList<Customer>();
		//Route[][] froutes = new Route[instance.getDepotsNr()][instance.getVehiclesNr()];
		int k=0;
		Route[][] routes  = sol.getRoutes();
		Customer[] GACustArray = sol.getGACustArray();
		
		for (int i=1; i<sol.getGACustArray().length; i++){
			 if (GACustArray[i].getNumber()==0){
				 k++;
				 routes[0][k].setCustomers(tempCust);
				 tempCust.clear();
				 continue;
			 }
			 tempCust.add(GACustArray[i]);
		}
		sol.setRoutes(routes);
		return routes;	
	}
}
