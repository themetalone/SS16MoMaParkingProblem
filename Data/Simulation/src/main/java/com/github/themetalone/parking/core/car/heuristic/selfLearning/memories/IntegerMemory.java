package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.weight.WeightUtil;

/**
 * Created by steff on 19.08.2016.
 */
public class IntegerMemory extends AbstractMemory<Integer> {



    public IntegerMemory(int initialCapacity) {
        super(initialCapacity);
    }

    public Integer learnedParameter() {
        double weightedParamSum = super.stream()
                .sorted((p1, p2) -> p1.getDistance() - p2.getDistance())
                .mapToDouble((p) -> WeightUtil.getInstance().weight(p.getDistance()) * p.getParam())
                .sum();
        double totalWeight = super.stream()
                .mapToInt(Parameter::getDistance)
                .mapToDouble(p->WeightUtil.getInstance().weight(p))
                .sum();
        return (int)(weightedParamSum / totalWeight);
    }
}