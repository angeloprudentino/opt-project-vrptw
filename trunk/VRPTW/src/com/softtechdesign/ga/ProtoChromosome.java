package com.softtechdesign.ga;


import com.mdvrp.Customer;
import com.mdvrp.Route;

public class ProtoChromosome {
	//support class in the creation of the initial population. it contains the chromosome divided in a matrix: [vehicle][customer]
	
	
	Route routes[][]; //it'a matrix for compatibility with TS format
	int NVehic;
	int NCust;
	

	public ProtoChromosome(int NVehic, int NCust) {
		
		Route routes[][] = new Route[NVehic][1];
		for(int i = 0; i<NVehic; i++){
			routes[i][0] = new Route();
		}
	}

	public void addGene(int v, Customer cust) {
		routes[v].addCustomer(cust);
	}

	
//	public Chromosome toChromosome() {
//		for(int i = 0; i<NVehic; i++){
//			
//		}
//	}


	
}
