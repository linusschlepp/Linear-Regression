package app;

import com.google.common.collect.Multimap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateExcel {

    private final double[] needArray;
    private final Multimap<Integer, Double> predictionsMap;

    public CreateExcel(double[] needArray, Multimap<Integer, Double> predictionsMap) {
        this.needArray = needArray;
        this.predictionsMap = predictionsMap;
    }

    public void deploy(){

        Workbook workbook = new XSSFWorkbook();



        Sheet sheet = workbook.createSheet("LR_Data");
        List<Row>rowList = new ArrayList<>();

        Row firstRow = sheet.createRow(0);
        AtomicInteger column = new AtomicInteger();
        firstRow.createCell(column.getAndIncrement()).setCellValue("Periods");
        firstRow.createCell(column.getAndIncrement()).setCellValue("Needs");
        predictionsMap.keySet().forEach(k ->
                firstRow.createCell(column.getAndIncrement()).setCellValue("n = "+k));

        for(int i = 0; i < needArray.length; i++) {
            Row row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(i+1);
            row.createCell(1).setCellValue(needArray[i]);
            rowList.add(row);
        }


        column.set(2);

        for(Integer integer : predictionsMap.keySet()){
            int rowIndex = 0;


            for(Double d : predictionsMap.get(integer)){
                rowList.get(rowIndex++).createCell(column.get()).setCellValue(d);
            }
            
            column.getAndIncrement();
        }


        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));
            FileOutputStream out = new FileOutputStream(fileChooser.showSaveDialog(new Stage()).getPath());
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }


}