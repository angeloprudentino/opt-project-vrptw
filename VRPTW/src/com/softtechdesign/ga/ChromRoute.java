/**
 *  package: com.softtechdesign.ga
 *  class: ChromRoute.java
 */
package com.softtechdesign.ga;

import java.util.StringTokenizer;

import com.mdvrp.Customer;
import com.mdvrp.Instance;
import com.mdvrp.Route;

/**
 * 
 * Chromosome class where genes are stored as an array of Route
 * @author Angelo Prudentino
 * @date 20/nov/2014
 *
 */
public class ChromRoute extends Chromosome {

    /** array of genes which comprise this Chromosome */
    private Customer[] genes;
    /** Route to which is associated the Chromosome */
    private Route route;
    /** Reference to the instance of the problem */
    private Instance inst;

    /**
     * Creates new string array of genes 
     * @param iGenesDim the size of the array of genes
     */
    public ChromRoute(int iGenesDim)
    {
        genes = new Customer[iGenesDim];
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
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @param inst the inst to set
     */
    public void setInst(Instance inst) {
        this.inst = inst;
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
        ChromRoute chromR = (ChromRoute)chromosome;

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
     * return the array of genes as a string
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
     * return the gene as a string
     * @return String
     */
    public String getGeneAsStr(int iGene)
    {
        String gene = "" + genes[iGene];
        return gene;
    }

    /**
     * Copy the genes from the given chromosome over the existing genes
     * @param chromosome
     */
    public void copyChromGenes(Chromosome chromosome)
    {
        int iGene;
        ChromRoute chromR = (ChromRoute)chromosome;

        for (iGene = 0; iGene < genes.length; iGene++)
            this.genes[iGene] = chromR.genes[iGene];
    }
    
    /**
     * create a new list of genes starting from a string representation
     * @param sGenes --> list of genes in string format "i - k - j"
     */
    
    public void setGenesFromStr(String sGenes){
	
	StringTokenizer tokenizer = new StringTokenizer(sGenes, " - ", false);
	int i = 0;
	while(tokenizer.hasMoreTokens()){
	    int num = Integer.parseInt(tokenizer.nextToken());
	    genes[i] = inst.getCustomerByNumID(num);
	    i++;
	}
	    
    }
    
    public int length(){
    	return genes.length;
    }

	public int getPosition(Customer gene) {
		for(int i = 0; i < genes.length; i++){
			if(cmpGeneNumber(genes[i], gene) == true) return i;
		}
		return -1; //error code because this method have to find the value into the array
	}
	
	public boolean cmpGeneNumber(Customer c1, Customer c2){
		if(c1.getNumber() == (c2.getNumber())) return true;
		return false;
	}

}
