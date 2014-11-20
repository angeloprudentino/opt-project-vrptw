/**
 *  package: com.softtechdesign.ga
 *  class: ChromRoute.java
 */
package com.softtechdesign.ga;

import com.mdvrp.Customer;
import com.mdvrp.Route;

/**
 * 
 * Chromosome class where genes are stored as an array of Route
 * @author Angelo Prudentino
 *
 */
public class ChromRoute extends Chromosome {

    /** array of genes which comprise this Chromosome */
    private Customer[] genes;

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
            sb.append(genes[i]);
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


}
