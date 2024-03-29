package com.softtechdesign.ga;
import java.util.Date;

import com.mdvrp.Customer;
import com.mdvrp.Instance;
import com.mdvrp.MyLogger;

/**
 * <pre>
 * Package ga
 * --------------------------------------------------------------------------------------------
 * The GAFloat, GAString, and GASequenceList classes all extend the GA class and can be used
 * to model different populations of candidate solutions. You will generally have to extend
 * one of these classes every time you create a new GA. In the simplest cases, you will subclass
 * one of these classes and then just override and implement your own GetFitness() function. The
 * three main subclasses of GA are:
 *   GAString (chromosomes are stored as strings)
 *   GAFloat (chromosomes are stored as floating point numbers)
 *   GASequenceList (chromosomes are stored as strings, additional methods in this class handle
 *                   sorting sequences. For example, the GASalesman class extends GASequenceList)
 *
 * For example:
 *   If your chromosomes are floating point numbers, you should subclass TGAFloat and override
 *   the getFitness() function with your own.
 *
 *   If your chromosomes are strings, you should subclass TGAString and override the
 *   getFitness() function with your own.
 *
 *   If your chromosomes are characters in a sequence (or list) that needs to be rearranged, you
 *   should use TGASequenceList and override the getFitness() function with your own.
 * ---------------------------------------------------------------------------------------------
 * 
 *  This main abstract class is extended by the 3 main GA subclasses:
 *  GAString, GAFloat, GASequenceList
 *  It (obviously) contains the methods common to all GA subclasses
 * </pre>
 * @author Jeff Smith jeff@SoftTechDesign.com
 */
public abstract class GA 
{
	private static String class_name = GA.class.getName();
	private static MyLogger MyLog = new MyLogger(class_name);
	
	/** instance of the problem;
	 * used only for GARoute
	 **/
	protected Instance instance;
	protected MyGAsolution best_feasible_sol;
	
    /** probability of a mutation occuring during genetic mating. For example, 0.03 means 3% chance */
    double mutationProb; 

    /** maximum generations to evolve */
    int maxGenerations; 

    /** 1-100 (e.g. 10 = 10% chance of random selection--not based on fitness). 
     * Setting nonzero randomSelectionChance helps maintain genetic diversity during evolution
     */ 
    int randomSelectionChance; 

    /** probability that a crossover will occur during genetic mating */
    double crossoverProb; 
    
    /** dimension of chromosome (number of genes) */
    protected int chromosomeDim; 
    
    /** number of chromosomes to evolve. A larger population dim will result in a better evolution but will slow the process down */
    protected int populationDim; 
    
    /** storage for pool of chromosomes for current generation */
    Chromosome[] chromosomes; 
    
    /** storage for temporary holding pool for next generation chromosomes */
    Chromosome[] chromNextGen; 
    
    /** storage for pool of prelim generation chromosomes */
    Chromosome[] prelimChrom; 
    
    /** index of fittest chromosome in current generation */
    int bestFitnessChromIndex; 
    
    /** index of least fit chromosome in current generation */
    int worstFitnessChromIndex; 
    
    /** type of crossover to be employed during genetic mating */
    protected int crossoverType; 
    
    /** statistics--average deviation of current generation */
    double[] genAvgDeviation; 
    
    /** statistics--average fitness of current generation */
    double[] genAvgFitness; 
    
    /** compute statistics for each generation during evolution? */ 
    boolean computeStatistics; 

    /** initialize the population (chromosomes) to random values */
    abstract protected void initPopulation();
    
    /** do a random mutation on given chromosome */
    abstract protected void doRandomMutation(int iChromIndex);
    
    /** do one point crossover between the two given chromosomes */
    abstract protected void doOnePtCrossover(Chromosome Chrom1, Chromosome Chrom2);
    
    /** do two point crossover between the two given chromosomes */
    abstract protected void doTwoPtCrossover(Chromosome Chrom1, Chromosome Chrom2);
    
    /** do uniform crossover between the two given chromosomes */
    abstract protected void doUniformCrossover(Chromosome Chrom1, Chromosome Chrom2);
    
    /** get the fitness value for the given chromosome */
    abstract protected double getFitness(int iChromIndex);

    /**
     * Initializes the GA using given parameters
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
    public GA(int chromosomeDim,
              int populationDim,
              double crossoverProb,
              int randomSelectionChance,
              int maxGenerations,
              double mutationProb,
              int crossoverType,
              boolean computeStatistics,
              double greedyRatio,
              Instance i)
    {
        this.randomSelectionChance = randomSelectionChance;
        this.crossoverType = crossoverType;
        this.chromosomeDim = chromosomeDim;
        this.populationDim = populationDim;
        this.computeStatistics = computeStatistics;

        this.chromosomes = new Chromosome[populationDim];
        this.chromNextGen = new Chromosome[populationDim];
        this.prelimChrom = new Chromosome[populationDim];
        this.genAvgDeviation = new double[maxGenerations];
        this.genAvgFitness = new double[maxGenerations];

        this.crossoverProb = crossoverProb;
        this.maxGenerations = maxGenerations;
        this.mutationProb = mutationProb;
    }

    /**
     * Gets the average deviation of the given generation of chromosomes
     * @param iGeneration
     * @return
     */
    public double getAvgDeviation(int iGeneration)
    {
        return (this.genAvgDeviation[iGeneration]);
    }

    /**
     * Gets the average fitness of the given generation of chromosomes
     * @param iGeneration
     * @return
     */
    public double getAvgFitness(int iGeneration)
    {
        return (this.genAvgFitness[iGeneration]);
    }

    /**
     * Returns the mutation probability
     * @return double
     */
    public double getMutationProb()
    {
        return mutationProb;
    }

    /**
     * Gets the maximum number of generations this evolution will evolve 
     * @return int
     */
    public int getMaxGenerations()
    {
        return maxGenerations;
    }

    /**
     * Gets the random selection probability
     * @return int
     */
    public int getRandomSelectionChance()
    {
        return randomSelectionChance;
    }

    /**
     * Gets the crossover probability 
     * @return double
     */
    public double getCrossoverProb()
    {
        return crossoverProb;
    }

    /**
     * Gets the dimension (size or number) of genes per chromosome
     * @return int
     */
    public int getChromosomeDim()
    {
        return chromosomeDim;
    }

    /**
     * Gets the dimension (size or number) of chromosomes in the population
     * @return int
     */
    public int getPopulationDim()
    {
        return populationDim;
    }

    /**
     * Gets the crossover type (e.g. one point, two point, uniform, roulette)
     * @return
     */
    public int getCrossoverType()
    {
        return crossoverType;
    }

    /**
     * Returns whether statistics will be computed for this evolution run
     * @return boolean
     */
    public boolean getComputeStatistics()
    {
        return computeStatistics;
    }

    /**
     * Returns the fittest chromosome in the population
     * @return Chromosome
     */
    public Chromosome getFittestChromosome()
    {
        return (this.chromosomes[bestFitnessChromIndex]);
    }

    /**
     * Gets the fitness value of the fittest chromosome in the population 
     * @return double
     */
    public double getFittestChromosomesFitness()
    {
        return (this.chromosomes[bestFitnessChromIndex].fitness);
    }

    /**
     * return a integer random number between 0 and upperBound
     * @param upperBound
     * @return int
     */
    int getRandom(int upperBound)
    {
        int iRandom = (int) (Math.random() * upperBound);
        return (iRandom);
    }

    /**
     * return a double random number between 0 and upperBound
     * @param upperBound
     * @return double
     */
    double getRandom(double upperBound)
    {
        double dRandom = (Math.random() * upperBound);
        return (dRandom);
    }

    /**
	 * @param instance: the instance of the problem
	 */
	protected void setInstance(Instance instance) {
		this.instance = instance;
	}

	/**
     * Main routine that runs the evolution simulation for this population of chromosomes.  
     * @return number of generations
     */
    public void evolve()
    {
        int iGen;

        MyLog.info(class_name, "evolve()", "GA start time: " + new Date().toString());
        
        //addChromosomesToLog(0, populationDim);

        iGen = 0;
        while (iGen < maxGenerations)
        {
            computeFitnessRankings();
            doGeneticMating();
            copyNextGenToThisGen();

            if (computeStatistics == true)
            {
                this.genAvgDeviation[iGen] = getAvgDeviationAmongChroms();
                this.genAvgFitness[iGen] = getAvgFitness();
            }

//            MyLog.info(class_name, "evolve()", "best chromosome at iteration " + iGen + ":");
//            MyLog.info(class_name, "evolve()", this.chromosomes[this.bestFitnessChromIndex].getGenesAsStr());
//            System.out.println("Gneration " + (iGen+1) + " completed");
            iGen++;
        }

//        MyLog.info(class_name, "evolve()", "GEN " + (iGen + 1) + " AVG FITNESS = " + this.genAvgFitness[iGen-1] + " AVG DEV = " + this.genAvgDeviation[iGen-1]);

        //addChromosomesToLog(iGen, populationDim); 

        computeFitnessRankings();
        
        ChromCustomer chr = (ChromCustomer) this.chromosomes[this.bestFitnessChromIndex];
        best_feasible_sol.UpdateSolution(chr);
        MyLog.info(class_name, "evolve()", "Best Chromosome Found: ");
        MyLog.info(class_name, "evolve()", chr.getGenesAsStr() + " Fitness= " + chr.fitness + "\n--------------------------------------------------");

        MyLog.info(class_name, "evolve()", "GA end time: " + new Date().toString());
     }

    /**
     * Go through all chromosomes and calculate the average fitness (of this generation)
     * @return double
     */
    public double getAvgFitness()
    {
        double rSumFitness = 0.0;

        for (int i = 0; i < populationDim; i++)
            rSumFitness += this.chromosomes[i].fitness;
        return (rSumFitness / populationDim);
    }

    /**
     * Select two parents from population, giving highly fit individuals a greater chance of 
     * being selected.
     * @param indexParents
     */
    public void selectTwoParents(int[] indexParents)
    {
        int indexParent1 = indexParents[0];
        int indexParent2 = indexParents[1];
        boolean bFound = false;
        int index;

        while (bFound == false)
        {
            index = getRandom(populationDim); //get random member of population

            if (randomSelectionChance > getRandom(100))
            {
                indexParent1 = index;
                bFound = true;
            }
            else
            {
                //the greater a chromosome's fitness rank, the higher prob that it will be
                //selected to reproduce
                if (this.chromosomes[index].fitnessRank + 1 > getRandom(populationDim))
                {
                    indexParent1 = index;
                    bFound = true;
                }
            }
        }

        bFound = false;
        while (bFound == false)
        {
            index = getRandom(populationDim); //get random member of population

            if (randomSelectionChance > getRandom(100))
            {
                if (index != indexParent1)
                {
                    indexParent2 = index;
                    bFound = true;
                }
            }
            else
            {
                //the greater a chromosome's fitness rank, the higher prob that it will be
                //selected to reproduce
                if ((index != indexParent1)
                    && (this.chromosomes[index].fitnessRank + 1 > getRandom(populationDim)))
                {
                    //          if (this.chromosomes[index].getNumGenesInCommon(this.chromosomes[indexParent1])+1 > getRandom(chromosomeDim))
                    //          {
                    //            indexParent2 = index;
                    //            bFound = true;
                    //          }
                    indexParent2 = index;
                    bFound = true;
                }
            }
        }

        indexParents[0] = indexParent1;
        indexParents[1] = indexParent2;
    }

    /**
     * Calculate the ranking of the parameter "fitness" with respect to the current generation.
     * If the fitness is high, the corresponding fitness ranking will be high, too.
     * For example, if the fitness passed in is higher than any fitness value for any chromosome in the
     * current generation, the fitnessRank will equal the populationDim. And if the fitness is lower than
     * any fitness value for any chromosome in the current generation, the fitnessRank will equal zero.
     * @param fitness
     * @return int the fitness ranking
     */
    int getFitnessRank(double fitness)
    {
        int fitnessRank = -1;
        for (int i = 0; i < populationDim; i++)
        {
            if (fitness >= this.chromosomes[i].fitness)
                fitnessRank++;
        }

        return (fitnessRank);
    }

    /**
     * Calculate rankings for all chromosomes. High ranking numbers denote very fit chromosomes.
     */
    @SuppressWarnings("unused")
	void computeFitnessRankings()
    {
        double rValue;

        // recalc the fitness of each chromosome
        for (int i = 0; i < populationDim; i++)
            this.chromosomes[i].fitness = getFitness(i);

        for (int i = 0; i < populationDim; i++)
            this.chromosomes[i].fitnessRank = getFitnessRank(this.chromosomes[i].fitness);

        double rBestFitnessVal;
        double rWorstFitnessVal;
        for (int i = 0; i < populationDim; i++)
        {
            if (this.chromosomes[i].fitnessRank == populationDim - 1)
            {
                rBestFitnessVal = this.chromosomes[i].fitness;
                this.bestFitnessChromIndex = i;
            }
            if (this.chromosomes[i].fitnessRank == 0)
            {
                rWorstFitnessVal = this.chromosomes[i].fitness;
                this.worstFitnessChromIndex = i;
            }
        }
    }

    /**
     * Create the next generation of chromosomes by genetically mating fitter individuals of the
     * current generation.
     * Also employ elitism (so the fittest 2 chromosomes always survive to the next generation). 
     * This way an extremely fit chromosome is never lost from our chromosome pool.
     */
    void doGeneticMating()
    { 
        int iCnt, iRandom;
        int indexParent1 = -1, indexParent2 = -1;
        Chromosome Chrom1, Chrom2;

        iCnt = 0;

        //Elitism--fittest chromosome automatically go on to next gen (in 2 offspring)
        this.chromNextGen[iCnt].copyChromGenes(this.chromosomes[this.bestFitnessChromIndex]);
        iCnt++;
        this.chromNextGen[iCnt].copyChromGenes(this.chromosomes[this.bestFitnessChromIndex]);
        iCnt++;
        //Angelo--> also the worst chromosome go to next generation, in order to have more variations
        this.chromNextGen[iCnt].copyChromGenes(this.chromosomes[this.worstFitnessChromIndex]);
        iCnt++;
        this.chromNextGen[iCnt].copyChromGenes(this.chromosomes[this.worstFitnessChromIndex]);
        iCnt++;

        Chrom1 = new ChromCustomer(chromosomeDim, instance);
        Chrom2 = new ChromCustomer(chromosomeDim, instance);

        do
        {
            int indexes[] = { indexParent1, indexParent2 };
            selectTwoParents(indexes);
            indexParent1 = indexes[0];
            indexParent2 = indexes[1];

            Chrom1.copyChromGenes(this.chromosomes[indexParent1]);
            Chrom2.copyChromGenes(this.chromosomes[indexParent2]);

            if (getRandom(1.0) < crossoverProb) //do crossover
            {
                if (this.crossoverType == Crossover.ctOnePoint)
                    doOnePtCrossover(Chrom1, Chrom2);
                else if (this.crossoverType == Crossover.ctTwoPoint)
                    doTwoPtCrossover(Chrom1, Chrom2);
                else if (this.crossoverType == Crossover.ctUniform)
                    doUniformCrossover(Chrom1, Chrom2);
                else if (this.crossoverType == Crossover.ctRoulette)
                {
                    iRandom = getRandom(3);
                    if (iRandom < 1)
                        doOnePtCrossover(Chrom1, Chrom2);
                    else if (iRandom < 2)
                        doTwoPtCrossover(Chrom1, Chrom2);
                    else
                        doUniformCrossover(Chrom1, Chrom2);
                }

                this.chromNextGen[iCnt].copyChromGenes(Chrom1);
               iCnt++;
                this.chromNextGen[iCnt].copyChromGenes(Chrom2);
                iCnt++;
            }
            else //if no crossover, then copy this parent chromosome "as is" into the offspring
                {
                // CREATE OFFSPRING ONE
                this.chromNextGen[iCnt].copyChromGenes(Chrom1);
                iCnt++;

                // CREATE OFFSPRING TWO
                this.chromNextGen[iCnt].copyChromGenes(Chrom2);
                iCnt++;
            }
        }
        while (iCnt < populationDim);
    }

    /**
     * Copy the chromosomes previously created and stored in the "next" generation into the main
     * chromsosome memory pool. Perform random mutations where appropriate.
     */
    void copyNextGenToThisGen()
    {
        for (int i = 0; i < populationDim; i++)
        {
            this.chromosomes[i].copyChromGenes(this.chromNextGen[i]);

            //only mutate chromosomes if it is NOT the best
            if (i != this.bestFitnessChromIndex)
            {
                //always mutate the chromosome with the lowest fitness
                if ((i == this.worstFitnessChromIndex) || (getRandom(1.0) < mutationProb))
                    doRandomMutation(i);
            }
        }
    }

    /**
     * Display chromosome information to Log
     * @param iGeneration
     * @param iNumChromosomesToDisplay
     */
    void addChromosomesToLog(int iGeneration, int iNumChromosomesToDisplay)
    {
        StringBuffer buf = new StringBuffer("");

        if (iNumChromosomesToDisplay > this.populationDim)
            iNumChromosomesToDisplay = this.chromosomeDim;

        //Display Chromosomes
        for (int i = 0; i < iNumChromosomesToDisplay; i++)
        {
            this.chromosomes[i].fitness = getFitness(i);            
            buf.append("\nGen ").append(iGeneration).append(": Chrom ").append(i).append(" = ");
            buf.append(this.chromosomes[i].getGenesAsStr()).append("fitness = ").append(this.chromosomes[i].fitness);
            buf.append("\n--------------------------------------------------\n");
        }
//        MyLog.info(class_name, "addChromosomesToLog(int iGeneration, int iNumChromosomesToDisplay)", buf.toString());
    }

    /**
     * Get the average deviation from the current population of chromosomes. The smaller this
     * deviation, the higher the convergence is to a particular (but not necessarily optimal)
     * solution. It calculates this deviation by determining how many genes in the populuation
     * are different than the bestFitGenes. The more genes which are "different", the higher
     * the deviation.
     * @return
     */
    protected double getAvgDeviationAmongChroms()
    {
        int devCnt = 0;
        for (int iGene = 0; iGene < this.chromosomeDim; iGene++)
        {
            Customer bestFitGene = ((ChromCustomer)this.chromosomes[this.bestFitnessChromIndex]).getGene(iGene);
            for (int i = 0; i < this.populationDim; i++)
            {
            	Customer thisGene = ((ChromCustomer)this.chromosomes[i]).getGene(iGene);
                if (thisGene != bestFitGene)
                    devCnt++;
            }
        }

        return ((double)devCnt);
    }

    /**
     * Take a binary string and convert it to the long integer. For example, '1101' --> 13
     * @param sBinary
     * @return long
     */
    long binaryStrToInt(String sBinary)
    {
        long digit, iResult = 0;

        int iLen = sBinary.length();
        for (int i = iLen - 1; i >= 0; i--)
        {
            if (sBinary.charAt(i) == '1')
                digit = 1;
            else
                digit = 0;
            iResult += (digit << (iLen - i - 1));
        }
        return (iResult);
    }
}
