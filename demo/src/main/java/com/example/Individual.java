package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Individual {
    private Map<String, Integer> assignments;
    private int fitness = -1;

    public Individual(Map<String, List<Teacher>> schoolTeachers) {
        assignments = new HashMap<>();
        Random random = new Random();

        for (String school : schoolTeachers.keySet()) {
            int teacherIndex = random.nextInt(schoolTeachers.get(school).size());
            assignments.put(school, teacherIndex);
        }
    }

    public Individual() {
        assignments = new HashMap<>();
    }

    public Map<String, Integer> getAssignments() {
        return assignments;
    }

    public int getFitness(Map<String, List<Teacher>> schoolTeachers) {
        if (fitness == -1) {
            fitness = calculateFitness(schoolTeachers);
        }
        return fitness;
    }

    private int calculateFitness(Map<String, List<Teacher>> schoolTeachers) {
        return assignments.entrySet().stream()
                .mapToInt(e -> schoolTeachers.get(e.getKey()).get(e.getValue()).getDistance())
                .sum();
    }

    public Individual copy() {
        Individual newIndividual = new Individual();
        newIndividual.assignments = new HashMap<>(this.assignments);
        newIndividual.fitness = -1;
        return newIndividual;
    }

    public void mutate(Map<String, List<Teacher>> schoolTeachers, double mutationRate) {
        Random random = new Random();

        for (String school : assignments.keySet()) {
            if (random.nextDouble() < mutationRate) {
                int teacherIndex = random.nextInt(schoolTeachers.get(school).size());
                assignments.put(school, teacherIndex);
            }
        }
        fitness = -1;
    }
}
