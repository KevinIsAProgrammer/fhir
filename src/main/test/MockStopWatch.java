import ca.uhn.fhir.util.StopWatch;

class MockStopWatch extends StopWatch {
    long time = 0;

    MockStopWatch(long t) {
        time = t;
    }

    @Override
    public long getMillis() {
        return time;
    }
}
