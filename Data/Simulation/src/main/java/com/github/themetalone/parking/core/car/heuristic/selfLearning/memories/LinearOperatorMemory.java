package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.weight.WeightUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 20.08.2016.
 */
public class LinearOperatorMemory extends AbstractMemory<List<Double>> {


    public LinearOperatorMemory(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public List<Double> learnedParameter() {
        List<Double> result = new LinkedList<>();
        double weightedVelocity = super.stream()
                .mapToDouble(p -> p.getParam().get(0) * WeightUtil.getInstance().weight(p.getDistance()))
                .sum();
        double weightedThreshold = super.stream()
                .mapToDouble(p -> p.getParam().get(1) * WeightUtil.getInstance().weight(p.getDistance()))
                .sum();
        double totalWeight = super.stream().mapToDouble(p -> WeightUtil.getInstance().weight(p.getDistance())).sum();
        result.add(weightedVelocity / totalWeight);
        result.add(weightedThreshold / totalWeight);
        return result;
    }


}
