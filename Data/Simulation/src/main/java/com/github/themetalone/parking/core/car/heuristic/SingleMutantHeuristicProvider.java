package com.github.themetalone.parking.core.car.heuristic;

/**
 * Created by steff on 11.07.2016.
 */
public class SingleMutantHeuristicProvider implements HeuristicProvider {

    private Heuristic population;
    private Heuristic mutant;

    private boolean mutantProvided = false;
    private int providedUntil;

    public Heuristic getPopulation() {
        return population;
    }

    public void setPopulation(Heuristic population) {
        this.population = population;
    }

    public Heuristic getMutant() {
        return mutant;
    }

    public void setMutant(Heuristic mutant) {
        this.mutant = mutant;
    }

    public int getProvidedUntil() {
        return providedUntil;
    }

    public void setProvidedUntil(int providedUntil) {
        this.providedUntil = providedUntil;
    }

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
