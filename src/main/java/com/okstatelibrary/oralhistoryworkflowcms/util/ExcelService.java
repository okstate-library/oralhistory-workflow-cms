package com.okstatelibrary.oralhistoryworkflowcms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;

public class ExcelService {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static ByteArrayInputStream collecctionsToExcel(List<Collection> collections) {

		String[] headernames = { "Id", "Collection Name" };

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("collections");

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < headernames.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headernames[col]);
			}

			int rowIdx = 1;
			for (Collection collection : collections) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(collection.getId());
				row.createCell(1).setCellValue(collection.getCollectionName());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	public static InputStream subseriesToExcel(List<Subserie> subseries) {
		String[] headernames = { "Id", "Collection Name", "Subseries Name" };

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("collections");

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < headernames.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headernames[col]);
			}

			int rowIdx = 1;
			for (Subserie subserie : subseries) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(subserie.getId());
				row.createCell(1).setCellValue(subserie.getCollection().getCollectionName());
				row.createCell(2).setCellValue(subserie.getSubseriesName());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	public static InputStream transcriptionToExcel(List<Transcription> transcriptions) {

		String[] headernames = { "Id", "Collection Name", "Subseries Name", "Interview dates", "Interviewee",
				"Interviewer" };

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("transcriptions");

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < headernames.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headernames[col]);
			}

			int rowIdx = 1;
			for (Transcription transcription : transcriptions) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(transcription.getId());
				row.createCell(1).setCellValue(transcription.getCollection().getCollectionName());
				row.createCell(2).setCellValue(transcription.getsubseries().getSubseriesName());
				row.createCell(3).setCellValue(transcription.getInterviewDates());
				row.createCell(4).setCellValue(transcription.getInterviewee());
				row.createCell(5).setCellValue(transcription.getInterviewer());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public static List<Transcription> getTranscriptionFromExcel(String path) {

		List<Transcription> transcriptions = new ArrayList<Transcription>();

		try {
			FileInputStream file = new FileInputStream(new File(path));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			java.util.Iterator<Row> rowIterator = sheet.iterator();

			boolean isFirstRow = true;

			while (rowIterator.hasNext()) {

				Transcription transcription = new Transcription();

				Row row = rowIterator.next();

				if (!isFirstRow) {

					transcription.setTitle(row.getCell(0).getStringCellValue());
					transcription.setInterviewee(row.getCell(1).getStringCellValue());
					transcription.setInterviewer(row.getCell(2).getStringCellValue());
					transcription.setInterviewDates(row.getCell(3).getStringCellValue());
					transcription.setConvertToDigitalDate(checklCellForString(row.getCell(4)));
					transcription.setSubject(checklCellForString(row.getCell(5)));

					transcription.setKeywords(checklCellForString(row.getCell(6)));
					transcription.setDescription(checklCellForString(row.getCell(7)));
					transcription.setScopeAndContents(checklCellForString(row.getCell(8)));
					transcription.setFormat(checklCellForString(row.getCell(9)));
					transcription.setType(checklCellForString(row.getCell(10)));
					transcription.setPublisher(checklCellForString(row.getCell(11)));

					// transcription.setSu
//					transcription.setCollectionId((short) 1);
//					transcription.setSubseriesId(1);
					transcription.setCollectionName(row.getCell(12).getStringCellValue());
					transcription.setSubseriesName(checklCellForString(row.getCell(13)));

					transcription.setCoverageSpatial(checklCellForString(row.getCell(14)));
					transcription.setCoverageTemporal(checklCellForString(row.getCell(15)));
					transcription.setRights(checklCellForString(row.getCell(16)));
					transcription.setLanguage(checklCellForString(row.getCell(17)));
					transcription.setProjectCode(checklCellForString(row.getCell(18)));
					
					transcription.setTranscriberAssigned(checklCellForString(row.getCell(19)));
					transcription.setTranscriptNote(checklCellForString(row.getCell(20)));
					transcription.setEquipmentNumber(checklCellForNumeric(row.getCell(21)));
					transcription.setMetadataDraft(row.getCell(22).getStringCellValue());

					transcription.setTranscriptLocation(checklCellForString(row.getCell(23)));
					transcription.setInterviewerDescription(checklCellForString(row.getCell(24)));
					transcription.setInterviewerKeywords(checklCellForString(row.getCell(25)));
					transcription.setInterviewerSubjects(checklCellForString(row.getCell(26)));
					transcription.setReleaseForm(checklCellForBooelan(row.getCell(27)));
					transcription.setIsRestriction(checklCellForBooelan(row.getCell(28)));

					transcription.setRestrictionNote(checklCellForString(row.getCell(29)));
					transcription.setAudioEquipmentUsed(checklCellForString(row.getCell(30)));
					transcription.setVideoEquipmentUsed(checklCellForString(row.getCell(31)));
					transcription.setInterviewerNote(checklCellForString(row.getCell(32)));
					transcription.setSentOut(checklCellForBooelan(row.getCell(33)));
					transcription.setIsAccessMediaStatus(checklCellForAccessMediaStatus(row.getCell(34)));

					transcription.setIsBornDigital(checklCellForBooelan((row.getCell(35))));
					transcription.setIsOnline(checklCellForBooelan(row.getCell(36)));
					transcription.setIsConvertToDigital(checklCellForBooelan(row.getCell(37)));
					transcription.setRelationIsPartOf(checklCellForString(row.getCell(38)));

					transcriptions.add(transcription);

				}

				isFirstRow = false;
			}

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return transcriptions;
	}

	@SuppressWarnings("deprecation")
	private static String checklCellForString(Cell c) {

		if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().trim().length() > 0) {

			return c.getStringCellValue();
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	private static byte checklCellForAccessMediaStatus(Cell c) {

		if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().trim().length() > 0) {

			String cellValue = c.getStringCellValue().toLowerCase();

			if (cellValue.equals("in progress")) {
				return (byte) 1;
			}

			return (byte) 0;
		}

		return (byte) 0;
	}

	@SuppressWarnings("deprecation")
	private static byte checklCellForBooelan(Cell c) {

		if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().trim().length() > 0) {

			String cellValue = c.getStringCellValue().toLowerCase();

			if (cellValue.equals("yes") || cellValue.equals("y")) {
				return (byte) 1;
			}

			return (byte) 0;
		}

		return (byte) 0;
	}

	@SuppressWarnings("deprecation")
	private static String checklCellForNumeric(Cell c) {

		if (c != null && c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(c.getNumericCellValue());
		}

		return null;
	}

}
