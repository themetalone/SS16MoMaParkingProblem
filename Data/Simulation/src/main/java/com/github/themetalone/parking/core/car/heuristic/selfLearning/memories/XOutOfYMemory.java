package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 19.08.2016.
 */
public class XOutOfYMemory extends AbstractMemory<List<Integer>> {

    int bestQuarterWeight = 8;
    int bestHalfWeight = 4;
    int worstHalfWeight = 2;
    int worstQuarterWeight = 1;

    public XOutOfYMemory(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public List<Integer> learnedParameter() {
        int y = super.stream().mapToInt(p->p.getParam().get(1)*weight(p.getDistance())).sum();
        int yWeight = super.stream().mapToInt(p->weight(p.getDistance())).sum();
        int learnedY = y/yWeight;
        double xByY = super.stream().mapToDouble(p->((double)p.getParam().get(0))/((double)p.getParam().get(1))*weight(p.getDistance())).sum();
        double learnedXByY = xByY/yWeight;
        int learnedX = (int)(learnedXByY*learnedY);

        LinkedList<Integer> result = new LinkedList<>();
        result.add(learnedX);
        result.add(learnedY);
        return result;
    }


    @SuppressWarnings("Duplicates")
    private int weight(int dist) {
        double distanceRatio = dist / streetLength;
        if (distanceRatio < 0.25) {
            return bestQuarterWeight;
        }
        if (distanceRatio < 0.5) {
            return bestHalfWeight;
        }
        if (distanceRatio < 0.75) {
            return worstHalfWeight;
        }
        return worstQuarterWeight;
    }

    public void setBestQuarterWeight(int bestQuarterWeight) {
        this.bestQuarterWeight = bestQuarterWeight;
    }

    public void setBestHalfWeight(int bestHalfWeight) {
        this.bestHalfWeight = bestHalfWeight;
    }

    public void setWorstHalfWeight(int worstHalfWeight) {
        this.worstHalfWeight = worstHalfWeight;
    }

    public void setWorstQuarterWeight(int worstQuarterWeight) {
        this.worstQuarterWeight = worstQuarterWeight;
    }
}
