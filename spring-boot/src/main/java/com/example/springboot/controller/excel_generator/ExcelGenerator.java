package com.example.springboot.controller.excel_generator;

import com.example.springboot.model.Location;
import com.example.springboot.model.Trip;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.jdbc.Work;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    public static ByteArrayInputStream tripsToExcel(List<Trip> tripList) throws IOException {
        String[] columns = {"Id", "Name", "UserTripId", "StartDate", "FinishDate"};
        String[] columnsLocations = {"Id", "TripId", "Latitude", "Longitude"};
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ) {

            CreationHelper creationHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Trips");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for(int col=0; col<columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIndex = 1;
            for(Trip trip : tripList) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(trip.getId());
                row.createCell(1).setCellValue(trip.getName());
                //row.createCell(2).setCellValue(String.valueOf(trip.getLocations()));
                row.createCell(2).setCellValue(trip.getUserTrip().getId());
                row.createCell(3).setCellValue(trip.getStartDate());
                row.createCell(4).setCellValue(trip.getFinishDate());
            }


            int tripsAmount = tripList.size();
            Sheet sheet2 = workbook.createSheet("Locations");

            Row headerRowLocations = sheet2.createRow(0);
            for(int col=0; col<columnsLocations.length; col++) {
                Cell cell = headerRowLocations.createCell(col);
                cell.setCellValue(columnsLocations[col]);
                cell.setCellStyle(headerCellStyle);
            }
            rowIndex = 1;
            for(int i=0; i<tripsAmount; i++) {
                for(Location location : tripList.get(i).getLocations()) {
                    Row row = sheet2.createRow(rowIndex++);

                    row.createCell(0).setCellValue(location.getId());
                    row.createCell(1).setCellValue(location.getTrip().getId());
                    row.createCell(2).setCellValue(location.getLatitude());
                    row.createCell(3).setCellValue(location.getLongitude());
                }
            }


//            int tripsAmount = tripList.size();
//
//            Row headerRowLocations = sheet.createRow(tripsAmount+5);
//            for(int col=0; col<columnsLocations.length; col++) {
//                Cell cell = headerRowLocations.createCell(col);
//                cell.setCellValue(columnsLocations[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//            rowIndex = tripsAmount+5+1;
//            for(int i=0; i<tripsAmount; i++) {
//                for(Location location : tripList.get(i).getLocations()) {
//                    Row row = sheet.createRow(rowIndex++);
//
//                    row.createCell(0).setCellValue(location.getId());
//                    row.createCell(1).setCellValue(location.getTrip().getId());
//                    row.createCell(2).setCellValue(location.getLatitude());
//                    row.createCell(3).setCellValue(location.getLongitude());
//                }
//            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
