package domain;

import model.TankLine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TankUtilities extends BaseFilteringUtilities {
    private List<TankLine> tankDataset;
    private final int tankIdPosition = 3;

    public TankUtilities() {
        this.tankDataset = new ArrayList<>();
    }

    public void processData(List<String> readData, int tankId) {
        List<String> tankFilteredByTankId = filterMeasurementCollectionByTankId(readData, tankIdPosition, tankId);
        List<String> tankFilteredByFullTime = filterMeasurementCollectionByFullTime(tankFilteredByTankId);
        List<TankLine> tankCollection = prepareTankDataset(tankFilteredByFullTime, tankId);
        tankCollection.forEach(tank -> this.tankDataset.add(tank));
    }

    private List<TankLine> prepareTankDataset(List<String> tankCollection, int tankId)
    {
        List<TankLine> tankDataset = new ArrayList<>();
        for (String tankLine : tankCollection)
        {
            String[] splittedLine = tankLine.split(";");
            String date = splittedLine[splittedLine.length-7];
            float height = Float.parseFloat(splittedLine[splittedLine.length-3])/1000;
            float volume = Float.parseFloat(splittedLine[splittedLine.length-2].replace(',', '.'))/1000;
/*          float wys = Float.parseFloat(wysokosc);
            float obj = Float.parseFloat(objetosc.replace(',', '.'));*/
            tankDataset.add(new TankLine(date, height, volume, tankId));
        }
        return tankDataset;
    }

    public List<Integer> getTankIdCollection(List<String> data){
        List<Integer> tankIdCollection = new ArrayList<>();
        for(String line: data) {
            String[] lineSplitted = line.split(";");
            int tankId = Integer.parseInt(lineSplitted[3]);
            tankIdCollection.add(tankId);
        }
        return tankIdCollection.stream().distinct().collect(Collectors.toList());
    }

    public List<TankLine> getTankDataset(int tankId) {
        return this.tankDataset.stream().filter(dataset -> dataset.getTankId() == tankId).collect(Collectors.toList());
    }
}

