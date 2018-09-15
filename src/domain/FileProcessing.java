package domain;

import model.CurvePoint;
import model.ReconciliationOutputLine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileProcessing {

    public static List<String> readFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String str;

        List<String> data = new ArrayList<String>();
        while((str = bufferedReader.readLine()) != null){
            data.add(str);
        }
        bufferedReader.close();
        return data;
    }

    public static void writeReconciliationDatasetToFile(List<ReconciliationOutputLine> reconciliationOutput, File file) throws FileNotFoundException {
        //utworzenie pliku do zapisu danych w formacie .csv
        PrintWriter printWriter = new PrintWriter(file);
        //inicjalizacja kolumn pliku .csv
        printWriter.append("TimeSpanBegin"+';'+"TimeSpanEnd"+';'+"Tank"+';'+"HeightSpanBegin"+';'+"HeightSpanEnd"+';'+"Reconciliation"+'\n');
        reconciliationOutput.forEach(line -> printWriter.append(prepareReconciliationFileLine(line)));
        printWriter.close();
    }

    private static String prepareReconciliationFileLine(ReconciliationOutputLine reconciliationOutputLine) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(reconciliationOutputLine.getDateFrom());
        stringBuilder.append(';');
        stringBuilder.append(reconciliationOutputLine.getDateTo());
        stringBuilder.append(';');
        stringBuilder.append(reconciliationOutputLine.getTankNumber());
        stringBuilder.append(';');
        stringBuilder.append(reconciliationOutputLine.getHeightBeginning());
        stringBuilder.append(';');
        stringBuilder.append(reconciliationOutputLine.getHeightEnding());
        stringBuilder.append(';');
        stringBuilder.append(reconciliationOutputLine.getReconcilation());
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    public static void writeCorrectedCalibrationCurveDatasetToFile(List<CurvePoint> dataset, File file) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.append("Height"+';'+"Volume"+'\n');
        dataset.forEach(line -> printWriter.append(line.getHeight() + ";" + line.getVolume() + '\n'));
        printWriter.close();
    }
}
