package com.mdvrp;

import com.TabuSearch.MovesType;
import com.softtechdesign.ga.Crossover;

public class Parameters {
	
	private static final String GATS = "gats";
	private static final String TS = "onlyts";
	
	// general parameters
	private String inputFileName;
	private String outputFileName;
	private String mode;
	private boolean help;
	
	// TS specific parameters
	private MovesType movesType;
	private double precision;
	private int iterations;
	private int startClient;
	private int randomSeed;
	private int tabuTenure;
	private boolean variableTenure;
	private String currDir;

	// GA specific parameters 
	private int chromosomeDim;
	private int populationDim;
	private double crossoverProb;
	private int randomSelectionChance;
	private int maxGenerations;
	private double mutationProb;
	private int crossoverType;
	private boolean computeStatistics;
    private double greedyRatio;    //ratio of greedy initial population (1 = all greedy, 0 = all random)
	

	
	public Parameters() {
		
		// general parameters
		mode					= GATS;
		currDir 				= System.getProperty("user.dir");
		outputFileName    		= currDir + "/output/solutions.csv";
		help 					= false;
		
		// TS specific parameters
		movesType         		= MovesType.SWAP;
		precision         		= 1E-2;
		iterations        		= 1000;
		startClient       		= -1;
		tabuTenure        		= -1;
		randomSeed		  		= -1;
		variableTenure    		= false;
		
		// GA specific parameters
		chromosomeDim 			= 0; // depends on the number of clients + number of vehicles
		populationDim 			= 50; 
		crossoverProb 			= 0.7;
		crossoverType 			= Crossover.ctTwoPoint;
		randomSelectionChance 	= 10;
		maxGenerations 			= 4000;
		mutationProb 			= 0.06;
		computeStatistics 		= true;
		greedyRatio 			= 0.2;
				
		
	}
	
	public void updateParameters(String[] args) throws Exception
	{
		if(args.length % 2 == 0){
			for(int i = 0; i < args.length; i += 2){
				switch (args[i]) {
				// general parameters
					case "-m":
						String m = args[i+1];
						if (m.compareTo(GATS)==0 || m.compareTo(TS)==0)
							setMode(m);
						else{
							String msg = "Execution mode argument must be \"gats\" or \"ts\". Set to default \"gats\"!";
							throw new Exception(msg);
						}
						break;
					case "-if":
						inputFileName = args[i+1];
						break;
					case "-of":
						outputFileName = args[i+1];
						break;
				// TS specific parameters
					case "-p":
						precision = Double.parseDouble(args[i+1]);
						break;
					case "-it":
						iterations = Integer.parseInt(args[i+1]);
						break;
					case "-sc":
						startClient = Integer.parseInt(args[i+1]);
						break;
					case "-rs":
						randomSeed = Integer.parseInt(args[i+1]);
						break;
					case "-t":
						tabuTenure = Integer.parseInt(args[i+1]);
						break;
					case "-vt":
						if(args[i+1].equalsIgnoreCase("true")){
							setVariableTenure(true);
						}else if(args[i+1].equalsIgnoreCase("false")){
							setVariableTenure(false);
						}else {
							String msg = "Variable tenure argument must be true of false. Set to default false!";
							throw new Exception(msg);
						}
						break;
				// GA specific parameters
					case "-pd":
						populationDim = Integer.parseInt(args[i+1]);
						break;
					case "-cp":
						crossoverProb = Double.parseDouble(args[i+1]);
						break;
					case "-rsc":
						randomSelectionChance = Integer.parseInt(args[i+1]);
						break;
					case "-mg":
						maxGenerations = Integer.parseInt(args[i+1]);
						break;
					case "-mp":
						mutationProb = Double.parseDouble(args[i+1]);
						break;
					case "-cs":
						if(args[i+1].equalsIgnoreCase("true")){
							setComputeStatistics(true);
						}else if(args[i+1].equalsIgnoreCase("false")){
							setComputeStatistics(false);
						}else {
							String msg = "Compute Statistics argument must be true of false. Set to default true!";
							throw new Exception(msg);
						}
						break;
					case "-gr":
						greedyRatio = Double.parseDouble(args[i+1]);
						break;
					case "-h":
						printHelp();
						help = true;
						break;
					
					default: {
						String msg = "Unknown type of argument: " + args[i];
						throw new Exception(msg);
					}
				}
			}
		}else {
			printHelp();
			
			if (args[0].compareTo("-h") == 0){
				help = true;
			}
			else
				throw new Exception();
		}
	}
	
	public String toString(){
		
		StringBuffer print = new StringBuffer();
		print.append("\n" + "--- General Parameters: -------------------------------------");
		print.append("\n" + "| Input File Name= " + inputFileName);
		print.append("\n" + "| Output File Name= " + outputFileName);
		print.append("\n" + "--- TS-specific Parameters: ---------------------------------");
		print.append("\n" + "| Moves Type= " + movesType);
		print.append("\n" + "| Precision: " + precision);
		print.append("\n" + "| Iterations: " + iterations);
		print.append("\n" + "| Start Client: " + startClient);
		print.append("\n" + "| Random Seed: " + randomSeed);
		print.append("\n" + "| Tabu Tenure: " + tabuTenure);
		print.append("\n" + "| Variable Tenure: " + variableTenure);
		print.append("\n" + "--- GA-specific Parameters: ---------------------------------");
		print.append("\n" + "| Chromosome Dim = " + chromosomeDim);
		print.append("\n" + "| Population Dim: " + populationDim);
		print.append("\n" + "| Crossover Probability: " + crossoverProb);
		print.append("\n" + "| Crossover Type: " + crossoverType);
		print.append("\n" + "| Random Selection Chance: " + randomSelectionChance);
		print.append("\n" + "| Max Generations: " + maxGenerations);
		print.append("\n" + "| Mutation Probability = " + mutationProb);
		print.append("\n" + "| Greedy Ratio = " + greedyRatio);
		print.append("\n" + "------------------------------------------------------");
		return print.toString();	
	}

	void printHelp(){

		StringBuffer buff = new StringBuffer("");
		
		buff.append("------------ Admitted parameters ------------------\n");
		buff.append("\n");
		buff.append("******************* Mandatory *********************\n");
		buff.append("* -if input_file                                  *\n");
		buff.append("***************************************************\n");
		buff.append("\n");
		buff.append("                    Optional\n");
		buff.append("-of output_file\n");
		buff.append("-m mode [gats | onlyts]\n");
		buff.append("-it TS_iterations\n");
		buff.append("-vt variable_tenure [true | false]\n");
		buff.append("-t tabu_tenure\n");
		buff.append("-pd population_dim\n");
		buff.append("-cp crossover_prob\n");
		buff.append("-rsc random_selection_chance\n");
		buff.append("-mg max_generations\n");
//		buff.append("-mp mutation_prob\n");
		buff.append("-gr greedy_ratio\n");
		
		System.out.println(buff.toString());
	}
	/**
	 * @return the movesType
	 */
	public MovesType getMovesType() {
		return movesType;
	}

	/**
	 * @param movesType the movesType to set
	 */
	public void setMovesType(MovesType movesType) {
		this.movesType = movesType;
	}

	/**
	 * @return the inputFileName
	 */
	public String getInputFileName() {
		return inputFileName;
	}

	/**
	 * @param inputFileName the inputFileName to set
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName() {
		return outputFileName;
	}

	/**
	 * @param outputFileName the outputFileName to set
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * @param iterations the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * @return the startClient
	 */
	public int getStartClient() {
		return startClient;
	}

	/**
	 * @return the randomSeed
	 */
	public int getRandomSeed() {
		return randomSeed;
	}

	/**
	 * @param randomSeed the randomSeed to set
	 */
	public void setRandomSeed(int randomSeed) {
		this.randomSeed = randomSeed;
	}

	/**
	 * @param startClient the startClient to set
	 */
	public void setStartClient(int startClient) {
		this.startClient = startClient;
	}

	/**
	 * @return the tabuTenure
	 */
	public int getTabuTenure() {
		return tabuTenure;
	}

	/**
	 * @param tabuTenure the tabuTenure to set
	 */
	public void setTabuTenure(int tabuTenure) {
		this.tabuTenure = tabuTenure;
	}

	/**
	 * @return the variableTenure
	 */
	public boolean isVariableTenure() {
		return variableTenure;
	}

	/**
	 * @param variableTenure the variableTenure to set
	 */
	public void setVariableTenure(boolean variableTenure) {
		this.variableTenure = variableTenure;
	}

	public double getPrecision() {
		return precision;
	}

	public String getCurrDir() {
		return currDir;
	}

	public void setCurrDir(String currDir) {
		this.currDir = currDir;
	}

	public int getChromosomeDim() {
		return chromosomeDim;
	}

	public void setChromosomeDim(int chromosomeDim) {
		this.chromosomeDim = chromosomeDim;
	}

	public int getPopulationDim() {
		return populationDim;
	}

	public void setPopulationDim(int populationDim) {
		this.populationDim = populationDim;
	}

	public double getCrossoverProb() {
		return crossoverProb;
	}

	public void setCrossoverProb(double crossoverProb) {
		this.crossoverProb = crossoverProb;
	}

	public int getRandomSelectionChance() {
		return randomSelectionChance;
	}

	public void setRandomSelectionChance(int randomSelectionChance) {
		this.randomSelectionChance = randomSelectionChance;
	}

	public int getMaxGenerations() {
		return maxGenerations;
	}

	public void setMaxGenerations(int maxGenerations) {
		this.maxGenerations = maxGenerations;
	}

	public double getMutationProb() {
		return mutationProb;
	}

	public void setMutationProb(double mutationProb) {
		this.mutationProb = mutationProb;
	}

	public int getCrossoverType() {
		return crossoverType;
	}

	public void setCrossoverType(int crossoverType) {
		this.crossoverType = crossoverType;
	}

	public boolean isComputeStatistics() {
		return computeStatistics;
	}

	public void setComputeStatistics(boolean computeStatistics) {
		this.computeStatistics = computeStatistics;
	}

	public double getGreedyRatio() {
		return greedyRatio;
	}

	public void setGreedyRatio(double greedyRatio) {
		this.greedyRatio = greedyRatio;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public boolean isGATS(){
		if (mode.compareTo(GATS)==0)
			return true;
		else 
			return false;
	}

	public boolean isHelp() {
		return help;
	}
	
}
