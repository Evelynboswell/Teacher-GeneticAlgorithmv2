package com.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class App {

    private static final int POPULATION_SIZE = 50;
    private static final int GENERATIONS = 100;
    private static Map<String, List<Teacher>> schoolTeachers = new HashMap<>();

    public static void main(String[] args) throws IOException {
        loadSchoolData("dataSample.csv");

        Population population = new Population(POPULATION_SIZE, schoolTeachers);
        List<Integer> bestFitnessOverTime = new ArrayList<>();

        for (int generation = 0; generation < GENERATIONS; generation++) {
            population = GeneticAlgorithm.evolvePopulation(population, schoolTeachers);
            Individual bestIndividual = population.getFittest(schoolTeachers);
            int bestFitness = bestIndividual.getFitness(schoolTeachers);
            bestFitnessOverTime.add(bestFitness);

            System.out.println("Generation " + generation + ": Best Fitness = " + bestFitness);
        }

        Individual bestSolution = population.getFittest(schoolTeachers);
        saveSolutionToCSV(bestSolution, "Solution.csv");

        FitnessPlotter.plotFitnessOverTime(bestFitnessOverTime);
    }

    private static void loadSchoolData(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String schoolName = record.get("NAMA SEKOLAH");
                String teacherName = record.get("NAMA GURU");
                int distance = Integer.parseInt(record.get("JARAK"));

                Teacher teacher = new Teacher(teacherName, distance);
                schoolTeachers.computeIfAbsent(schoolName, k -> new ArrayList<>()).add(teacher);
            }
        }
    }

    private static void saveSolutionToCSV(Individual bestSolution, String filePath) throws IOException {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            csvWriter.append("School,Teacher Name,Distance\n");

            int totalDistance = 0;
            for (Map.Entry<String, Integer> entry : bestSolution.getAssignments().entrySet()) {
                String school = entry.getKey();
                int teacherIndex = entry.getValue();
                Teacher teacher = schoolTeachers.get(school).get(teacherIndex);

                csvWriter.append(school)
                        .append(",")
                        .append(teacher.getName())
                        .append(",")
                        .append(String.valueOf(teacher.getDistance()))
                        .append("\n");

                totalDistance += teacher.getDistance();
            }

            csvWriter.append("\nTotal Distance (Fitness): ").append(String.valueOf(totalDistance)).append("\n");
        }

        System.out.println("Best solution saved to " + filePath);
    }
}
