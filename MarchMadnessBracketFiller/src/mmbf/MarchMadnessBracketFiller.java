package mmbf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Prints a completed bracket to the console output.
 * 
 * Method {@link #getWinner(int, int) getWinner()} determines the outcome of
 * individual games.
 * 
 * @author Kole
 *
 */
public class MarchMadnessBracketFiller {

	private static final Integer[] REGION_SEEDS = { 1, 16, 8, 9, 4, 13, 5, 12, 6, 11, 3, 14, 7, 10, 2, 15 };

	private static Random random;

	private static HashMap<Integer, List<Integer>> roundToAdvancingSeedsMap = new HashMap<>();

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

		simulateRegion("Upper left quadrant");
		simulateRegion("Lower left quadrant");
		simulateRegion("Upper right quadrant");
		simulateRegion("Lower right quadrant");

		// Simulate left semi final
		System.out.print("Left half winner: ");
		if (random.nextInt(2) == 0)
			System.out.println("Upper left");
		else
			System.out.println("Lower left");

		// Simulate right semi final
		System.out.print("Right half winner: ");
		if (random.nextInt(2) == 0)
			System.out.println("Upper right");
		else
			System.out.println("Lower right");

		// Simulate final game
		System.out.print("Overall winner: ");
		if (random.nextInt(2) == 0)
			System.out.println("Left half winner");
		else
			System.out.println("Right half winner");

		System.out.println("Seed used: " + RANDOM_SEED);
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

	private static int simulateRegion(String regionName) {
		int roundCount = logBase2(REGION_SEEDS.length);
		System.out.println(regionName + ":");
		reset();
		playRound(roundCount);
		print();
		return roundToAdvancingSeedsMap.get(roundCount).get(0);
	}

	private static void playRound(int round) {
		// The first round just needs to be set up with the default starting bracket
		if (round == 0) {
			initFirstRound();
			return;
		}

		// Playing the current round depends on the completion of the previous round
		int previousRound = round - 1;
		List<Integer> previousRoundAdvancedSeeds = roundToAdvancingSeedsMap.get(previousRound);

		// Play the previous round if it hasn't been played yet
		if (previousRoundAdvancedSeeds == null) {
			playRound(previousRound);
			previousRoundAdvancedSeeds = roundToAdvancingSeedsMap.get(previousRound);
		}

		// If there is only one seed advancing from the previous round, there are no
		// more games to play
		if (previousRoundAdvancedSeeds.size() == 1)
			return;

		// Play the current round based on the previous round's advancing seeds
		roundToAdvancingSeedsMap.put(round, playGamesFromList(previousRoundAdvancedSeeds));
	}

	private static List<Integer> playGamesFromList(List<Integer> seeds) {
		List<Integer> advancingSeeds = new ArrayList<>();
		for (int i = 0; i < seeds.size(); i += 2) {
			int seed1 = seeds.get(i);
			int seed2 = seeds.get(i + 1);
			advancingSeeds.add(getWinner(seed1, seed2));
		}
		return advancingSeeds;
	}

	private static void initFirstRound() {
		roundToAdvancingSeedsMap.put(0, Arrays.asList(REGION_SEEDS));
	}

	private static void print() {
		for (int i = 0; roundToAdvancingSeedsMap.containsKey(i); i++)
			printRound(i);
	}

	private static void printRound(int round) {
		StringBuilder stringBuilder = new StringBuilder();
		char[] initialSpacing = new char[getSpacing(round)];
		char[] spaceBetweenEachSeed = new char[getSpacing(round + 1)];
		Arrays.fill(initialSpacing, ' ');
		Arrays.fill(spaceBetweenEachSeed, ' ');
		
		// Printing in reverse order so that it is easier to transcribe on an actual
		// bracket
		for (int seed : roundToAdvancingSeedsMap.get(round)) {
			stringBuilder.insert(0, spaceBetweenEachSeed);
			stringBuilder.insert(0, String.format("%2d", seed));
		}
		stringBuilder.insert(0, initialSpacing);
		stringBuilder.append('\n');

		System.out.print(stringBuilder.toString());
	}

	private static int getSpacing(int x) {
		return (int) ((Math.pow(2, x) - 1) * 2);
	}

	private static void reset() {
		roundToAdvancingSeedsMap.clear();
	}

	public static int logBase2(int x) {
		return (int) (Math.log(x) / Math.log(2));
	}
}
