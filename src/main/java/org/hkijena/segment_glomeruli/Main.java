package org.hkijena.segment_glomeruli;
import com.github.dexecutor.core.*;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;
import org.apache.commons.cli.*;
import org.hkijena.segment_glomeruli.tasks.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        Option threads = new Option("t", "threads", true, "number of threads");
        threads.setRequired(false);
        options.addOption(threads);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        Path inputFilePath = Paths.get(cmd.getOptionValue("input"));
        Path outputFilePath = Paths.get(cmd.getOptionValue("output"));

        // Load data interfaces
        List<DataInterface> dataInterfaces = new ArrayList<>();

        for(Path inputImagePath : Files.walk(inputFilePath).filter(path -> path.toString().endsWith(".tif")).collect(Collectors.toList())) {
            System.out.println("Generating data interface for " + inputImagePath.toString());
            DataInterface dataInterface = new DataInterface(inputImagePath, outputFilePath.resolve(inputImagePath.getFileName()));
            dataInterfaces.add(dataInterface);
        }

        // Generate DAG
        Map<Integer, DAGTask> dagTasks = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(threads.getValue("1")));
        DexecutorConfig<Integer, Integer> dexecutorConfig = new DexecutorConfig<>(executorService, integer -> dagTasks.get(integer));
        DefaultDexecutor<Integer, Integer> dexecutor = new DefaultDexecutor<>(dexecutorConfig);

        for(DataInterface dataInterface : dataInterfaces) {
            List<Integer> lastLayer = new ArrayList<>();
            List<Integer> thisLayer = new ArrayList<>();

            // Create Tissue2D segmentations
            for(long z = 0; z < dataInterface.getInputData().getZSize(); ++z) {
                int tid = dagTasks.size();
                DAGTask task = new SegmentTissue2D(tid, dataInterface, z);
                dagTasks.put(tid, task);
                thisLayer.add(tid);
            }
            flushDependencies(dexecutor, lastLayer, thisLayer);

            // Create Tissue quantification
            {
                int tid = dagTasks.size();
                DAGTask task = new QuantifyTissue(tid, dataInterface);
                dagTasks.put(tid, task);
                thisLayer.add(tid);
            }
            flushDependencies(dexecutor, lastLayer, thisLayer);

            // Create Glomeruli 2D segmentations
            for(long z = 0; z < dataInterface.getInputData().getZSize(); ++z) {
                int tid = dagTasks.size();
                DAGTask task = new SegmentGlomeruli2D(tid, dataInterface, z);
                dagTasks.put(tid, task);
                thisLayer.add(tid);
            }
            flushDependencies(dexecutor, lastLayer, thisLayer);

            // Create Glomeruli 3D segmentation
            {
                int tid = dagTasks.size();
                DAGTask task = new SegmentGlomeruli3D(tid, dataInterface);
                dagTasks.put(tid, task);
                thisLayer.add(tid);
            }
            flushDependencies(dexecutor, lastLayer, thisLayer);

            // Create Glomeruli quantification
            {
                int tid = dagTasks.size();
                DAGTask task = new QuantifyGlomeruli(tid, dataInterface);
                dagTasks.put(tid, task);
                thisLayer.add(tid);
            }
            flushDependencies(dexecutor, lastLayer, thisLayer);

            // Create Glomeruli filtering
            {
                int tid = dagTasks.size();
                DAGTask task = new ApplyGlomeruliFiltering(tid, dataInterface);
                dagTasks.put(tid, task);
                thisLayer.add(tid);
            }
            flushDependencies(dexecutor, lastLayer, thisLayer);
        }

        dexecutor.execute(ExecutionConfig.TERMINATING);
        System.out.println("Task finished.");
        System.exit(0);
    }

    private static void flushDependencies(DefaultDexecutor<Integer, Integer> dexecutor, List<Integer> lastLayer, List<Integer> thisLayer) {
        for(Integer here : thisLayer) {
            for(Integer there : lastLayer) {
                dexecutor.addDependency(there, here);
            }
        }
        lastLayer.clear();
        lastLayer.addAll(thisLayer);
        thisLayer.clear();
    }
}
