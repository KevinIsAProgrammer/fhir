class AverageCalculator {
    private long total = 0;
    private long n = 0;

    public double getAverage() {
        if (n == 0) {
            return -1;
        }
        return (double) total / n;
    }

    public void reset() {
        total = 0;
        n = 0;
    }

    public void addData(long amount) {
        total += amount;
        n += 1;
    }

    public long getTotal() {
        return total;
    }

    public long getNumberOfItems() {
        return n;
    }
}
