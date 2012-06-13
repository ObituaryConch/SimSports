package org.fazio.simsports.baseball.types;

import org.fazio.simsports.baseball.builders.test.RyanBraunTestPlayerBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

/**
 * @author Michael Fazio <michael.fazio@kohls.com>
 * @since 6/8/12 2:37 PM
 */
public class BaseballPlayerTest {

	private BaseballPlayer testPlayer;
	private final int testCount = 629;
	private final double testFactor = 10000;


	@Before
	public void setUp() throws Exception {
		this.testPlayer = new RyanBraunTestPlayerBuilder().build();
	}

	@Test
	public void testPlateAppearance() throws Exception {

		final Map<Results, Integer> results = new HashMap<Results, Integer>();

		final int testsToRun = (int)(this.testCount * this.testFactor);

		final int onePercent = testsToRun/100;

		for(int x=0; x<=(this.testCount * testFactor); x++) {
			if(x%onePercent == 0) System.out.println(x/onePercent + "% completed.");
			final Results result = runPlateAppearance();

			if(!results.containsKey(result)) {
				results.put(result, 0);
			}

			results.put(result, results.get(result) + 1);
		}

		this.assertTestResults(results);
		this.printTestResults(results);

	}

	private void assertTestResults(final Map<Results, Integer> results) {
		assertTrue(
			"The number of singles is incorrect.",
			this.numberIsNear(111, this.getResultAveragePerTest(results.get(Results.Single))));
		assertTrue(
			"The number of doubles is incorrect.",
			this.numberIsNear(38, this.getResultAveragePerTest(results.get(Results.Double))));
		assertTrue(
			"The number of triples is incorrect.",
			this.numberIsNear(6, this.getResultAveragePerTest(results.get(Results.Triple))));
		assertTrue(
			"The number of home runs is incorrect.",
			this.numberIsNear(33, this.getResultAveragePerTest(results.get(Results.HomeRun))));
		assertTrue(
			"The number of walks is incorrect.",
			this.numberIsNear(58, this.getResultAveragePerTest(results.get(Results.BB))));
		assertTrue(
			"The number of strikeouts is incorrect.",
			this.numberIsNear(
				93,
				this.getResultAveragePerTest(results.get(Results.StrikeoutLooking) + results.get(Results.StrikeoutSwinging))));
		assertTrue(
			"The number of hit by pitches is incorrect.",
			this.numberIsNear(5, this.getResultAveragePerTest(results.get(Results.HBP))));
		assertTrue(
			"The number of outs is incorrect.",
			this.numberIsNear(283, this.getResultAveragePerTest(results.get(Results.Out))));
	}

	private void printTestResults(final Map<Results, Integer> results) {
		for(Map.Entry<Results, Integer> entry : results.entrySet()) {
			System.out.println(((Results)entry.getKey()).getText() + " = " + this.getResultAveragePerTest(entry.getValue()));
		}

		final Results[] hitTypes = {Results.Single, Results.Double, Results.Triple, Results.HomeRun};
		final Results[] outTypes = {Results.Out, Results.StrikeoutLooking, Results.StrikeoutSwinging};

		double hits = 0;
		for(Results hitType : hitTypes) {
			hits += (double)results.get(hitType);
		}

		double outs = 0;
		for(Results outType : outTypes) {
			outs += (double)results.get(outType);
		}

		System.out.println("Hits = " + hits);
		System.out.println("Outs = " + outs);
		final double battingAverage = ((hits) / ((hits + outs)));
		System.out.println("Batting Average = ." + (int)(battingAverage * 1000));
	}

	private Results runPlateAppearance() {

		final PlateAppearanceResult playResult = (PlateAppearanceResult) this.testPlayer.getPlayResult();
		return playResult.getSingleResult();

	}

	private long getResultAveragePerTest(final Integer result) {
		return Math.round((double) result / this.testFactor);
	}

	private boolean numberIsNear(final long baseNumber, final long valueChecked) {
		final double rangeFactor = 0.98;
		final long lowRange = (long)(baseNumber * rangeFactor);
		final long highRange = (long)(baseNumber / rangeFactor);

		return valueChecked >= lowRange && valueChecked <= highRange;

	}

}