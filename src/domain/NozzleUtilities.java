package domain;

import model.NozzleLine;

import java.util.*;
import java.util.stream.Collectors;

public class NozzleUtilities extends BaseFilteringUtilities {

    private List<NozzleLine> nozzleDataset;
    private final int tankIdPosition = 3;

    NozzleUtilities() {
        nozzleDataset = new ArrayList<>();
    }

    public void processData(List<String> dataArray, int tankId) {
            List<String> nozzleFilteredByTankId = filterMeasurementCollectionByTankId(dataArray, tankIdPosition, tankId);
            List<String> nozzleFilteredByFullTime = filterMeasurementCollectionByFullTime(nozzleFilteredByTankId);
            List<NozzleLine> nozzleCollection = prepareNozzleDataset(nozzleFilteredByFullTime, tankId);
            List<NozzleLine> mergedNozzleCollection = mergeNozzleLinesBySameDate(nozzleCollection, tankId);
            mergedNozzleCollection.forEach(nozzle -> this.nozzleDataset.add(nozzle));
    }

    private List<NozzleLine> mergeNozzleLinesBySameDate(List<NozzleLine> nozzleLines, int tankId) {
        List<NozzleLine> mergedNozzleLines = new ArrayList<>();
        Map<String, List<NozzleLine>> nozzleLinesGroupedBySameDate =
                nozzleLines.stream().collect(Collectors.groupingBy(nozzle -> nozzle.getDate()));
        Set<String> dates = nozzleLinesGroupedBySameDate.keySet();
        for(String date: dates) {
            double aggregatedVolume = 0.0;
            List<NozzleLine> nozzleLinesGroup = nozzleLinesGroupedBySameDate.get(date);
            for(NozzleLine line: nozzleLinesGroup) {
                aggregatedVolume += line.getVolume();
            }
            mergedNozzleLines.add(new NozzleLine(date, aggregatedVolume, tankId));
        }
        mergedNozzleLines = mergedNozzleLines.stream().sorted(Comparator.comparing(NozzleLine::getDate)).collect(Collectors.toList());
        return mergedNozzleLines;
    }

    public List<Integer> getTankIdCollection(List<String> data){
        List<Integer> tankIdCollection = new ArrayList<>();
        for(String line: data) {
            String[] lineSplitted = line.split(";");
            int tankId = Integer.parseInt(lineSplitted[tankIdPosition]);
            tankIdCollection.add(tankId);
        }
        return tankIdCollection.stream().distinct().collect(Collectors.toList());
    }

    private List<NozzleLine> prepareNozzleDataset(List<String> nozzleArray, int tankId) {
        List<NozzleLine> nozzleDataset = new ArrayList<>();
        for (String nozzleLine : nozzleArray)
        {
            String[] splittedLine = nozzleLine.split(";");
            String date = splittedLine[splittedLine.length-7];
            float volume = Float.parseFloat(splittedLine[splittedLine.length-2].replace(',', '.'))/1000;
            //*float obj = Float.parseFloat(objetosc.replace(',', '.'));*//*
            nozzleDataset.add(new NozzleLine(date, volume, tankId));
        }
        return nozzleDataset;
    }

    public List<NozzleLine> getNozzleDataset(int tankId) {
        return this.nozzleDataset.stream().filter(dataset -> dataset.getTankId() == tankId).collect(Collectors.toList());
    }
}
