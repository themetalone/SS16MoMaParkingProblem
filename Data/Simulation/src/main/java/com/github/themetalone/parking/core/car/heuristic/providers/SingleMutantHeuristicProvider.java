package com.github.themetalone.parking.core.car.heuristic.providers;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;

/**
 * Provides two heuristics, one for the population and one for a single mutant
 * Created by steff on 11.07.2016.
 */
public class SingleMutantHeuristicProvider implements HeuristicProvider {

    private Heuristic population;
    private Heuristic mutant;

    private boolean mutantProvided = false;
    private int providedUntil;

    /**
     *
     * @return the population heuristic
     */
    public Heuristic getPopulation() {
        return population;
    }

    /**
     *
     * @param population sets the population heuristic
     */
    public void setPopulation(Heuristic population) {
        this.population = population;
    }

    /**
     *
     * @return the mutant heuristic
     */
    public Heuristic getMutant() {
        return mutant;
    }

    /**
     *
     * @param mutant sets the mutant heuristic
     */
    public void setMutant(Heuristic mutant) {
        this.mutant = mutant;
    }

    /**
     *
     * @return the maximum number of population heuristics that are created before the mutant is returned
     */
    public int getProvidedUntil() {
        return providedUntil;
    }

    /**
     *
     * @param providedUntil the maximum number of population heuristics to be created before returning the mutant
     */
    public void setProvidedUntil(int providedUntil) {
        this.providedUntil = providedUntil;
    }

    /**
     * Returns randomly a population heuristic or a mutant heuristic. Only one mutant heuristic will be returned with the chance of 1%
     * @return heuristic
     */
    @Override
    public Heuristic getNewHeuristic() {
        if(mutantProvided){
            return population.copy();
        }else{
            if(Math.random() < 0.01 || providedUntil == 0){
                mutantProvided = true;
                return mutant.copy();
            }else{
                providedUntil --;
                return population.copy();
            }
        }
    }
}
