package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Population {
    private List<Individual> individuals;

    public Population(int populationSize, Map<String, List<Teacher>> schoolTeachers) {
        individuals = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            individuals.add(new Individual(schoolTeachers));
        }
    }

    public Individual getFittest(Map<String, List<Teacher>> schoolTeachers) {
        return individuals.stream().min(Comparator.comparingInt(ind -> ind.getFitness(schoolTeachers))).orElse(null);
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }
}
