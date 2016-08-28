package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.weight.WeightUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 19.08.2016.
 */
public class XOutOfYMemory extends AbstractMemory<List<Integer>> {

    public XOutOfYMemory(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public List<Integer> learnedParameter() {
        double y = super.stream()
                .mapToDouble(p->p.getParam().get(1)*WeightUtil.getInstance().weight(p.getDistance()))
                .sum();
        double yWeight = super.stream()
                .mapToDouble(p-> WeightUtil.getInstance().weight(p.getDistance()))
                .sum();
        int learnedY = (int)(y/yWeight);
        double xByY = super.stream()
                .mapToDouble(p->((double)p.getParam().get(0))/((double)p.getParam().get(1))*WeightUtil.getInstance().weight(p.getDistance()))
                .sum();
        double learnedXByY = xByY/yWeight;
        int learnedX = (int)(learnedXByY*learnedY);

        LinkedList<Integer> result = new LinkedList<>();
        result.add(learnedX);
        result.add(learnedY);
        return result;
    }

}
