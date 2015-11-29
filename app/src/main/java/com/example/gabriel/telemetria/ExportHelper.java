package com.example.gabriel.telemetria;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;

public class ExportHelper {
	public static HSSFWorkbook MakeSheetFromData(sensorData data){
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] row;
		
		//Acquisition sheet
		//HSSFSheet sheet_acq = workbook.createSheet("Acquisition");
		
		//Number of items
		//row = new String[]{"Number of readings:", String.valueOf(data.size())};
		//createRow(sheet_acq, 0, row);
		
		//Period
		//row = new String[]{"Period:", String.valueOf(data.period)};
		//createRow(sheet_acq, 1, row);
		
		//Readings
		//row = new String[]{"Readings:", String.valueOf(data.nVal)};
		//createRow(sheet_acq, 2, row);
		
		//Started at
		//row = new String[]{"Started at:", String.valueOf(data.start)};
		//createRow(sheet_acq, 3, row);
		
		//Stopped at
		//row = new String[]{"Stopped at:", String.valueOf(data.stop)};
		//createRow(sheet_acq, 4, row);
		
		//Items
		//row = new String[]{"Items:"};
		//createRow(sheet_acq, 6, row);

		//Labels
		//row = new String[]{"Number", "Name", "Custom Name", "Description"};
		//createRow(sheet_acq, 7, row);
		
		//Add the items
		//for(int i=0; i<data.nItems; i++){
		//	row = new String[]{"" + i/10 + i%10, data.names[i], data.customNames[i], data.descriptions[i]};
		//	createRow(sheet_acq, 8+i, row);
		//}
		
		//Warning about the time
		//row = new String[]{"*Time is normalized by the acquisition period"};
		//createRow(sheet_acq, 9+data.nItems, row);
		//sheet_acq.addMergedRegion(new CellRangeAddress(9+data.nItems, 9+data.nItems, 0, 3));

		/////////////////////////////////////////////////////////////////
		
		//Data sheet
		HSSFSheet sheet_data = workbook.createSheet("Data");

		//Labels
		row = new String[4];
		row[0] = "Milliseconds";
        row[1] = "LatitudeGPS";
        row[2] = "LongitudeGPS";
        row[3] = "AccuracyGPS";
		createRow(sheet_data, 0, row);
		
		//Data
		double[] val = new double[4];
		for(int i=0; i<data.size(); i++){
            sensorData.Reading reading = data.get(i);
			val[0] = reading.getMs();
            val[1] = reading.getLatitudeGPS();
            val[2] = reading.getLongitudeGPS();
            val[3] = reading.getAccuracyGPS();
			createRow(sheet_data, 1+i, val);
		}
		
		return workbook;
	}
	
	//Create a row in a sheet
	private static void createRow(HSSFSheet sheet, int row_number, String[] data){
		HSSFRow row = sheet.createRow(row_number);
		for(int i=0; i<data.length; i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(data[i]);
		}
	}

	//Create a row in a sheet
	private static void createRow(HSSFSheet sheet, int row_number, double[] data){
		HSSFRow row = sheet.createRow(row_number);
		for(int i=0; i<data.length; i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(data[i]);
		}
	}
}
