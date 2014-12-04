/**
 *  package: com.softtechdesign.ga
 *  class: MyGAsolution.java
 */
package com.softtechdesign.ga;

import com.mdvrp.Instance;

/**
 * 
 * Wrapper class for the GA solution
 * 
 * @author Angelo Prudentino
 * @date 04/dic/2014
 *
 */
public class MyGAsolution {
 
	//this class should give all the enrty points for a GA solution:
	//	   1- the inizialization;
	//	   2- methods to update the current best solution;
	//     3- getters;
	//     4- toString();
	//
	//if any other method could be usefull, add it.
	
	Instance instance;
	ChromCustomer solution;    //the actual solution is a chromosome

	/**
	 * @param instance of the problem
	 */
	public MyGAsolution(Instance instance, int chromosomeDim) {
		super();
		this.instance = instance;
		solution = new ChromCustomer(chromosomeDim);
		solution.setInstance(instance);
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
	
	
}
