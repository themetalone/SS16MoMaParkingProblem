package com.github.themetalone.parking.hutchinson.distribution;

import com.github.themetalone.parking.core.street.Street;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by steff on 14.07.2016.
 */
public class ThreePhaseIntegerDistribution implements IntegerDistribution, Observer {

    private long noon;
    private long afternoon;
    private IntegerDistribution morningDistribution;
    private IntegerDistribution noonDistribution;
    private IntegerDistribution afternoonDistribution;


    private DayTime dayTime = DayTime.Morning;

    public ThreePhaseIntegerDistribution(long noon, long afternoon, int morningUpperBound, int noonUpperBound, int afternoonUpperBound) {
        this.noon = noon;
        this.afternoon = afternoon;
        this.morningDistribution = new UniformIntegerDistribution(0,morningUpperBound);
        this.noonDistribution = new UniformIntegerDistribution(0,noonUpperBound);
        this.afternoonDistribution = new UniformIntegerDistribution(0,afternoonUpperBound);
    }

    @Override
    public double probability(int i) {
        return getDistribution().probability(i);
    }

    @Override
    public double cumulativeProbability(int i) {
        return getDistribution().cumulativeProbability(i);
    }

    @Override
    public double cumulativeProbability(int i, int i1) throws NumberIsTooLargeException {
        return getDistribution().cumulativeProbability(i,i1);
    }

    @Override
    public int inverseCumulativeProbability(double v) throws OutOfRangeException {
        return getDistribution().inverseCumulativeProbability(v);
    }

    @Override
    public double getNumericalMean() {
        return getDistribution().getNumericalMean();
    }

    @Override
    public double getNumericalVariance() {
        return getDistribution().getNumericalVariance();
    }

    @Override
    public int getSupportLowerBound() {
        return getDistribution().getSupportLowerBound();
    }

    @Override
    public int getSupportUpperBound() {
        return getDistribution().getSupportUpperBound();
    }

    @Override
    public boolean isSupportConnected() {
        return this.morningDistribution.isSupportConnected() && noonDistribution.isSupportConnected() && afternoonDistribution.isSupportConnected();
    }

    @Override
    public void reseedRandomGenerator(long l) {
        this.morningDistribution.reseedRandomGenerator(l);
        this.noonDistribution.reseedRandomGenerator(l);
        this.afternoonDistribution.reseedRandomGenerator(l);
    }

    @Override
    public int sample() {
        return getDistribution().sample();
    }

    @Override
    public int[] sample(int i) {
        int[] result = new int[i];
        for (int j = 0; j < i; j++) {
            result[j] = sample();
        }
        return result;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((o instanceof Street) && (arg instanceof Long)) {
            long tick = (long) arg;
            if(tick < noon){
                dayTime = DayTime.Morning;
            }
            if(tick >= noon && tick < afternoon){
                dayTime = DayTime.Noon;
            }
            if(tick >= afternoon){
                dayTime = DayTime.Afternoon;
            }
        }
    }

    private IntegerDistribution getDistribution(){
        switch (dayTime) {
            default:
            case Morning:
                return morningDistribution;
            case Noon:
                return noonDistribution;
            case Afternoon:
                return afternoonDistribution;
        }
    }

    private enum DayTime {
        Morning, Noon, Afternoon;
    }
}
