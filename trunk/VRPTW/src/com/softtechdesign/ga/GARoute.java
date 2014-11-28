/**
 *  package: com.softtechdesign.ga
 *  class: GARoute.java
 */
package com.softtechdesign.ga;

/**
 * @author Angelo Prudentino
 * @date 22/nov/2014
 */
public class GARoute extends GA {

    /**
     * @param chromosomeDim
     * @param populationDim
     * @param crossoverProb
     * @param randomSelectionChance
     * @param maxGenerations
     * @param numPrelimRuns
     * @param maxPrelimGenerations
     * @param mutationProb
     * @param crossoverType
     * @param computeStatistics
     */
    public GARoute(int chromosomeDim, int populationDim, double crossoverProb, int randomSelectionChance,
	    int maxGenerations, int numPrelimRuns, int maxPrelimGenerations, double mutationProb, int crossoverType,
	    boolean computeStatistics) {
	super(chromosomeDim, populationDim, crossoverProb, randomSelectionChance, maxGenerations, numPrelimRuns,
		maxPrelimGenerations, mutationProb, crossoverType, computeStatistics);
    }

    /** 
     * @see com.softtechdesign.ga.GA#initPopulation()
     */
    @Override
    protected void initPopulation() {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doRandomMutation(int)
     */
    @Override
    protected void doRandomMutation(int iChromIndex) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doOnePtCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doOnePtCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doTwoPtCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doTwoPtCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#doUniformCrossover(com.softtechdesign.ga.Chromosome, com.softtechdesign.ga.Chromosome)
     */
    @Override
    protected void doUniformCrossover(Chromosome Chrom1, Chromosome Chrom2) {
    }

    /** 
     * @see com.softtechdesign.ga.GA#getFitness(int)
     */
    @Override
    protected double getFitness(int iChromIndex) {
	return 0;
    }

}
