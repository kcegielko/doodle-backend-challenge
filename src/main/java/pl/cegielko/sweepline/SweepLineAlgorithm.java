package pl.cegielko.sweepline;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SweepLineAlgorithm {

    public List<List<Point<?>>> getOverlapping(List<Point<?>> points, int requiredSize) {
        List<List<Point<?>>> result = new LinkedList<>();
        int currentCounter = 0;
        Point<?> startingPoint = null;
        Point<?> finishingPoint = null;
        for (Point<?> point : points) {
            int previousCounter = currentCounter;
            currentCounter += point.getIncrement();

            if (previousCounter < requiredSize && currentCounter >= requiredSize)
                startingPoint = point;
            else if (previousCounter >= requiredSize && currentCounter < requiredSize)
                finishingPoint = point;

            if (startingPoint != null && finishingPoint != null) {
                result.add(List.of(startingPoint, finishingPoint));
                startingPoint = null;
                finishingPoint = null;
            }
        }
        return result;
    }

    public static sealed abstract class Point<T> permits StartingPoint, FinishingPoint {
        private final T value;

        public Point(T value) {
            this.value = value;
        }

        public abstract int getIncrement();

        public T getValue() {
            return value;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Point<?> point)) return false;
            return Objects.equals(getValue(), point.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return "Point{" +
                    "value=" + value +
                    '}';
        }
    }

    public static final class StartingPoint<T> extends Point<T> {
        public StartingPoint(T value) {
            super(value);
        }

        @Override
        public int getIncrement() {
            return 1;
        }
    }

    public static final class FinishingPoint<T> extends Point<T> {
        public FinishingPoint(T value) {
            super(value);
        }

        @Override
        public int getIncrement() {
            return -1;
        }
    }
}
