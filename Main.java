import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Algorithms.*;


public class Main {

    private static final int NUM_TRIALS = 110;
    private static final int WARMUP_TRIALS = 10;
    private static final String CSV_FILE = "results.csv";

    /**
     * Runs the experiment for each sorting algorithm, data type, array size, and configuration.
     * Saves the results to a CSV file.
     */
    public static void runExperiment() {
        int[] sizes = {100, 1000, 10000};
        String[] structures = {"bestCase", "worstCase", "averageCase"};
        String[] dataTypes = {"int", "long", "float", "double", "char", "string"};
        Sorter[] algorithms = {
                new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>(),
                new QuickSortGPT<>(),
                new SelectionSortGPT<>(),
        };

        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"Experiment Index", "Algorithm", "Data Type", "Size", "Structure", "Trial", "Time(ms)"});  // CSV Header

        int experimentIndex = 1;
        int totalExperiments = algorithms.length * dataTypes.length * sizes.length * structures.length;  // Total configurations

        for (Sorter algorithm : algorithms) {
            for (String dataType : dataTypes) {
                for (int size : sizes) {
                    for (String structure : structures) {
                        List<Double> times = timeSortingAlgorithm(algorithm, size, structure, dataType);

                        // Only include trials after the warm-up period
                        for (int trial = WARMUP_TRIALS + 1; trial <= NUM_TRIALS; trial++) {
                            double time = times.get(trial - 1);
                            csvData.add(new String[]{
                                    String.valueOf(experimentIndex),
                                    algorithm.getClass().getSimpleName(),
                                    dataType,
                                    String.valueOf(size),
                                    structure,
                                    String.valueOf(trial - WARMUP_TRIALS),
                                    String.format("%.2f", time)
                            });
                        }

                        // Update progress bar
                        displayProgressBar(experimentIndex, totalExperiments);
                        experimentIndex++;
                    }
                }
            }
        }
        writeCsvFile(CSV_FILE, csvData);
    }

    /**
     * Measure time of execution
     * Returns a list of times for each trial in milliseconds.
     */
    private static List<Double> timeSortingAlgorithm(Sorter sorter, int size, String structure, String dataType) {
        ArrayGenerator<?> generator = new ArrayGenerator<>(size, structure, dataType);
        List<Double> times = new ArrayList<>();

        for (int i = 0; i < NUM_TRIALS; i++) {
            Comparable[] array = (Comparable[]) generator.generate();
            long startTime = System.nanoTime();
            sorter.sort(array);
            long endTime = System.nanoTime();
            times.add((endTime - startTime) / 1_000_000.0);
        }

        return times;
    }

    /**
     * Writes data to a CSV
     */
    private static void writeCsvFile(String fileName, List<String[]> data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
            System.out.println("\nResults saved!");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Displays a progress bar in the console.
     */
    private static void displayProgressBar(int current, int total) {
        int barLength = 50;
        int progress = (int) ((double) current / total * barLength);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < progress) {
                bar.append("=");
            } else {
                bar.append(".");
            }
        }
        bar.append("]");

        int percent = (int) ((double) current / total * 100);
        System.out.printf("\r%s %d%% (%d/%d)", bar, percent, current, total);

        // Remove previus progress bar
        System.out.flush();
    }

    public static void main(String[] args) {
        runExperiment();
    }
}
