package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithm {
    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.1;
    private static final int TOURNAMENT_SIZE = 5;

    public static Population evolvePopulation(Population pop, Map<String, List<Teacher>> schoolTeachers) {
        Population newPopulation = new Population(pop.getIndividuals().size(), schoolTeachers);
        List<Individual> newIndividuals = new ArrayList<>();

        for (int i = 0; i < pop.getIndividuals().size(); i++) {
            Individual parent1 = tournamentSelection(pop, schoolTeachers);
            Individual parent2 = tournamentSelection(pop, schoolTeachers);

            Individual offspring = crossover(parent1, parent2);
            offspring.mutate(schoolTeachers, MUTATION_RATE);
            newIndividuals.add(offspring);
        }

        newPopulation.setIndividuals(newIndividuals);
        return newPopulation;
    }

    private static Individual crossover(Individual parent1, Individual parent2) {
        Individual offspring = parent1.copy();
        Random random = new Random();

        if (random.nextDouble() < CROSSOVER_RATE) {
            for (String school : offspring.getAssignments().keySet()) {
                if (random.nextBoolean()) {
                    offspring.getAssignments().put(school, parent2.getAssignments().get(school));
                }
            }
        }
        return offspring;
    }

    private static Individual tournamentSelection(Population pop, Map<String, List<Teacher>> schoolTeachers) {
        List<Individual> tournament = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            tournament.add(pop.getIndividuals().get(random.nextInt(pop.getIndividuals().size())));
        }
        return tournament.stream().min(Comparator.comparingInt(ind -> ind.getFitness(schoolTeachers))).orElse(null);
    }
}
