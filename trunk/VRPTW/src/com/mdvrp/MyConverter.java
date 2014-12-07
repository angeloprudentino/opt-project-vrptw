package com.mdvrp;

import java.util.ArrayList;
import java.util.List;

import com.TabuSearch.MyTSsolution;
import com.softtechdesign.ga.ChromCustomer;
import com.softtechdesign.ga.MyGAsolution;

public class MyConverter {

	/**
	 * Tabu Search format (routes matrix) to Genetic Algorithm format (linear
	 * vector)
	 */
	@SuppressWarnings("static-access")
	public static MyGAsolution ConvertTSGA(MyTSsolution sol) {
		// Cycles variables
		int i = 0, j = 0, k = 0, tot = 0;
		// Instance data
		Instance inst = sol.getInstance();
		// Routes matrix from the TS
		Route[][] routes = sol.getRoutes();
		// Customers list for a specific route
		List<Customer> currV;
		// Customers array
		Customer[] GACustArray;
		// Solution in the GA format
		MyGAsolution GAsol = new MyGAsolution(inst);
		// Solution in the chromosome format
		ChromCustomer ChrSol = GAsol.getSolution();

		// Create Depot type customer (id<>0, id=number of customers)
		Customer DepotCust = new Customer();
		DepotCust.setNumber(inst.getCustomersNr());

		// Customer array lenght calculation
		tot = inst.getCustomersNr() + inst.getVehiclesNr();

		// Creation of the customer array
		GACustArray = new Customer[tot];
		// First customer is a depot
		GACustArray[k] = DepotCust;
		k++;

		// Populate the customer array
		for (i = 0; i < inst.getVehiclesNr(); i++) {
			// for every vehicle, get the customer list
			currV = routes[0][i].getCustomers();
			// cycle through the customer list
			for (j = 0; j < routes[0][i].getCustomersLength(); j++) {
				// write every customer in the customer array
				GACustArray[k] = currV.get(j);
				k++;
			}
			if (k == tot)
				// if this is the last position of the customer array, terminate
				// the cycle
				break;
			// after every customer list, there is the depot
			GACustArray[k] = DepotCust;
			k++;
		}

		// Write the customer array in the chromosome
		ChrSol.setGenes(GACustArray);
		// Write the chromosome in the GAsolution
		GAsol.UpdateSolution(ChrSol);

		// Return the GASolution
		return GAsol;
	}

	/**
	 * Genetic Algorithm format (linear vector) to Tabu Search format (routes
	 * matrix)
	 */
	public static MyTSsolution ConvertGATS(MyGAsolution sol) {
		// Instance infomation
		Instance inst = sol.getInstance();
		// Temporary customers array
		ArrayList<Customer> tempCust = new ArrayList<Customer>();
		// TS solution with instance informations and without the generation of
		// the routes
		MyTSsolution TSsol = new MyTSsolution(inst, false);
		// Cycle variable
		int k = 0;

		// Routes matrix for the TS
		Route[][] routes = TSsol.getRoutes();
		// Customer array from the GA (edited method)
		Customer[] GACustArray = sol.getSolution().getGenes();

		// Write in the routes matrix
		for (int i = 1; i < GACustArray.length; i++) {
			// If the current customer is a depot,
			if (GACustArray[i].getNumber() == inst.getCustomersNr()) {
				// write the customer list in the routes matrix (edited method)
				routes[0][k].setCustomers(tempCust);
				// clear the temporary customers ArrayList
				tempCust.clear();
				// go to the following route
				k++;
				continue;
			}
			// Otherwise add the current costumer in the temporary customer
			// ArrayList
			tempCust.add(GACustArray[i]);
		}
		// Write the routes in the TSsolution
		TSsol.setRoutes(routes);
		// Return the TSsolution
		return TSsol;
	}
}
