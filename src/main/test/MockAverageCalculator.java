class MockAverageCalculator extends AverageCalculator {
    boolean averageCalled = false;
    boolean addCalled = false;
    boolean resetCalled = false;
    long valueToAdd = 0;

    @Override
    public void reset() {
        resetCalled = true;
    }

    @Override
    public double getAverage() {
        averageCalled = true;
        return 100.0;
    }

    @Override
    public void addData(long value) {
        addCalled = true;
        valueToAdd = value;
    }
}
