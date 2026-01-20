package pl.cegielko.sweepline;

import org.junit.jupiter.api.Test;
import pl.cegielko.sweepline.SweepLineAlgorithm.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SweepLineAlgorithmTest {

    /*
        TEST 1: Single interval overlap (counter >= 2)

        Intervals:
                   |A1---------A2|
        |B1----------------B2|

        Sweep Line (sorted events):
        B1 start → counter=1
        A1 start → counter=2 ← OVERLAP START
        B2 end   → counter=1 ← OVERLAP END
        A2 end   → counter=0

        Result: [A1→B2]
    */
    @Test
    public void shouldProduceOneOverlapping() {
        // Given
        SweepLineAlgorithm algorithm = new SweepLineAlgorithm();
        List<Point<?>> points = List.of(
                new StartingPoint<>("B1"),
                new StartingPoint<>("A1"),
                new FinishingPoint<>("B2"),
                new FinishingPoint<>("A2"));

        // When
        List<List<Point<?>>> result = algorithm.getOverlapping(points, 2);

        // Then
        assertEquals(List.of(List.of(new StartingPoint<>("A1"), new FinishingPoint<>("B2"))), result);
    }

    /*
        TEST 2: Two separate overlaps (counter >= 2)

        Intervals:
             |A1--A2|     |A3------------A4|
          |B1------------------------B2|

        Sweep Line:
        B1 → counter=1
        A1 → counter=2 ← OVERLAP #1 START
        A2 → counter=1 ← OVERLAP #1 END
        A3 → counter=2 ← OVERLAP #2 START
        B2 → counter=1 ← OVERLAP #2 END
        A4 → counter=0

        Result: [A1→A2], [A3→B2]
    */
    @Test
    public void shouldProduceTwoOverlapping() {
        // Given
        SweepLineAlgorithm algorithm = new SweepLineAlgorithm();
        List<Point<?>> points = List.of(
                new StartingPoint<>("B1"),
                new StartingPoint<>("A1"),
                new FinishingPoint<>("A2"),
                new StartingPoint<>("A3"),
                new FinishingPoint<>("B2"),
                new FinishingPoint<>("A4"));

        // When
        List<List<Point<?>>> result = algorithm.getOverlapping(points, 2);

        // Then
        assertEquals(List.of(
                List.of(new StartingPoint<>("A1"), new FinishingPoint<>("A2")),
                List.of(new StartingPoint<>("A3"), new FinishingPoint<>("B2"))
        ), result);
    }

    /*
        TEST 3: No overlap (counter never >= 3)

        Intervals:
          |A1-----A2|
                       |B1----------B2|
        |C1-----------------------C2|

        Sweep Line (threshold=3):
        C1 → counter=1
        A1 → counter=2
        A2 → counter=1
        B1 → counter=2
        C2 → counter=1
        B2 → counter=0

        Result: [] (max counter=2 < 3)
    */
    @Test
    public void shouldProduceNoneOverlapping() {
        // Given
        SweepLineAlgorithm algorithm = new SweepLineAlgorithm();
        List<Point<?>> points = List.of(
                new StartingPoint<>("C1"),
                new StartingPoint<>("A1"),
                new FinishingPoint<>("A2"),
                new StartingPoint<>("B1"),
                new FinishingPoint<>("C2"),
                new FinishingPoint<>("B2"));

        // When
        List<List<Point<?>>> result = algorithm.getOverlapping(points, 3);

        // Then
        assertEquals(List.of(), result);
    }
}
