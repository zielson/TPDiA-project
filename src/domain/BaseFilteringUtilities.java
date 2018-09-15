package domain;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseFilteringUtilities {
    protected List<String> filterMeasurementCollectionByTankId(List<String> collection, int index, int tankId)
    {
        return collection.stream()
                .filter(s -> {
                    String[] splittedLine = s.split(";");
                    return Integer.parseInt(splittedLine[index]) == tankId;
                }).collect(Collectors.toList());
    }

    protected List<String> filterMeasurementCollectionByFullTime(List<String> collection)
    {
        return collection.stream()
                .filter(s -> s.charAt(14) == '0' &&  s.charAt(15) == '0')
                .collect(Collectors.toList());
    }
}
