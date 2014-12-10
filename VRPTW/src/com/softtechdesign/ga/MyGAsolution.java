/**
 *  package: com.softtechdesign.ga
 *  class: MyGAsolution.java
 */
package com.softtechdesign.ga;

import java.util.ArrayList;

import com.TabuSearch.MyTSsolution;
import com.mdvrp.Customer;
import com.mdvrp.Instance;
import com.mdvrp.Route;

/**
 * 
 * Wrapper class for the GA solution
 * 
 * @author Angelo Prudentino
 * @date 04/dic/2014
 *
 */
public class MyGAsolution {

	// this class should give all the entry points for a GA solution:
	// 1- the inizialization;
	// 2- methods to update the current best solution;
	// 3- getters;
	// 4- toString();
	// 5- format conversion from GAsolution to TSsolution
	//
	// if any other method could be useful, add it.

	private Instance instance;
	private ChromCustomer solution; // the actual solution is a chromosome

	/**
	 * @param instance
	 *            of the problem
	 */
	public MyGAsolution(Instance instance) {
		super();
		this.instance = instance;
		// I manage only the case with 1 depot
		int chromosomeDim = instance.getCustomersNr()
				+ instance.getVehiclesNr();
		solution = new ChromCustomer(chromosomeDim, instance);
	}

	/**
	 * @return the solution
	 */
	public ChromCustomer getSolution() {
		return solution;
	}

	/**
	 * @param solution: the solution to set
	 */
	public void UpdateSolution(ChromCustomer solution) {
		this.solution = solution;
	}

	/**
	 * @return GA solution in string format
	 */
	@Override
	public String toString() {
		return solution.getGenesAsStr();
	}

	/**
	 * @return the instance
	 */
	public Instance getInstance() {
		return instance;
	}
	
	public MyTSsolution ConvertGATS() {

		// Temporary customers array
		ArrayList<Customer> tempCust = new ArrayList<Customer>();
		// TS solution with instance informations and without the generation of
		// the routes
		MyTSsolution TSsol = new MyTSsolution(instance, false);
		// Cycle variable
		int k = 0;

		// Routes matrix for the TS
		Route[][] routes = TSsol.getRoutes();
		// Customer array from the GA (edited method)
		Customer[] GACustArray = getSolution().getGenes();

		// Write in the routes matrix
		for (int i = 1; i < GACustArray.length; i++) {
			// If the current customer is a depot,
			if (GACustArray[i].getNumber() == instance.getCustomersNr()) {
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
