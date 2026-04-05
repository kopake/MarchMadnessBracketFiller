package mmbf;

import java.util.Arrays;
import java.util.Random;

public class MarchMadnessBracketFillerv2 {

	private static Random random;

	private static final int[] REGION_SEEDS = { 1, 16, 8, 9, 4, 13, 5, 12, 6, 11, 3, 14, 7, 10, 2, 15 };

	private static final int MEAN_FINAL_SCORE = 144;

	private static final int STANDARD_DEVIATION_FINAL_SCORE = 7;

	public static void main(String[] args) {
		long RANDOM_SEED;

		// Use argument for seed if present
		if (args.length > 0)
		{
			try {
				RANDOM_SEED = Long.parseLong(args[0]);
			} catch (NumberFormatException e) {
				System.out.printf("\"%s\" is not a valid input for the random seed.\n");
				return;
			}
		}
		else {
			RANDOM_SEED = System.currentTimeMillis();
		}

		random = new Random(RANDOM_SEED);
		
		simulateAndPrintRegion("Upper left");
		simulateAndPrintRegion("Lower left");
		simulateAndPrintRegion("Upper right");
		simulateAndPrintRegion("Lower right");

		simulateAndPrintSingleGameWinner("Left half finalist", "Upper left", "Lower left");
		simulateAndPrintSingleGameWinner("Right half finalist", "Upper right", "Lower right");

		simulateAndPrintSingleGameWinner("Championship", "Left side", "Right Side");

		simulateAndPrintFinalTotalScore(MEAN_FINAL_SCORE, STANDARD_DEVIATION_FINAL_SCORE);

		System.out.println("\nSeed used: " + RANDOM_SEED);
	}

	private static void simulateAndPrintRegion(String regionName) {
		
		int[][] rounds = new int[(int) (Math.log(REGION_SEEDS.length) / Math.log(2)) + 1][REGION_SEEDS.length];
		
		rounds[0] = Arrays.copyOf(REGION_SEEDS, REGION_SEEDS.length);

		for (int round = 1; round < rounds.length; round++) {
			int numberOfGamesThisRound = (int) Math.pow(2, rounds.length - round - 1);
			for (int i = 0; i < numberOfGamesThisRound; i++) {
				rounds[round][i] = getWinner(rounds[round - 1][i * 2], rounds[round - 1][i * 2 + 1]);
			}
		}
		// @formatter:off
		String outputString =
				       rounds[0][ 0] +
				"\n" + "        " + rounds[1][0] + 
				"\n" + rounds[0][ 1] + 
				"\n" + "        " + "        " + rounds[2][0] + 
				"\n" + rounds[0][ 2] + 
				"\n" + "        " + rounds[1][1] + 
				"\n" + rounds[0][ 3] + 
				"\n" + "        " + "        " + "        " + rounds[3][0] + 
				"\n" + rounds[0][ 4] + 
				"\n" + "        " + rounds[1][2] + 
				"\n" + rounds[0][ 5] + 
				"\n" + "        " + "        " + rounds[2][1] + 
				"\n" + rounds[0][ 6] + 
				"\n" + "        " + rounds[1][3] + 
				"\n" + rounds[0][ 7] + 
				"\n" + "        " + "        " + "        " + "        " + rounds[4][0] + 
				"\n" + rounds[0][ 8] + 
				"\n" + "        " + rounds[1][4] + 
				"\n" + rounds[0][ 9] + 
				"\n" + "        " + "        " + rounds[2][2] + 
				"\n" + rounds[0][10] + 
				"\n" + "        " + rounds[1][5] + 
				"\n" + rounds[0][11] + 
				"\n" + "        " + "        " + "        " + rounds[3][1] + 
				"\n" + rounds[0][12] + 
				"\n" + "        " + rounds[1][6] + 
				"\n" + rounds[0][13] + 
				"\n" + "        " + "        " + rounds[2][3] + 
				"\n" + rounds[0][14] + 
				"\n" + "        " + rounds[1][7] + 
				"\n" + rounds[0][15];				
		// @formatter:on

		System.out.println(regionName + ":");
		System.out.println(outputString);
		System.out.println();
	}	

	private static void simulateAndPrintSingleGameWinner(String region, String team1, String team2) {
		System.out.print(region + ": ");
		if (random.nextInt(2) == 0)
			System.out.println(team1);
		else
			System.out.println(team2);
	}

	private static void simulateAndPrintFinalTotalScore(int meanScore, int standardDeviation) {
		System.out.println("Final Score Total: " + (int) ((random.nextGaussian() * standardDeviation) + meanScore));
	}

	/**
	 * Chooses a winner from seed1 and seed2. The winning seed is returned
	 * 
	 * @param seed1 The first seed
	 * @param seed2 The second seed
	 * @return The winning seed from the matchup
	 */
	private static int getWinner(int seed1, int seed2) {
		final double EXPONENT_VALUE = 1.5;
		double seed1Probability = Math.pow(seed1, EXPONENT_VALUE);
		double seed2Probability = Math.pow(seed2, EXPONENT_VALUE);
		double total = seed1Probability + seed2Probability;

		double randomDouble = random.nextDouble() * total;

		return randomDouble >= seed1Probability ? seed1 : seed2;
	}
}
