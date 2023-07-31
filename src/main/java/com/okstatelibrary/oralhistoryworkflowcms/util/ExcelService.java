package com.okstatelibrary.oralhistoryworkflowcms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.okstatelibrary.oralhistoryworkflowcms.entity.*;

public class ExcelService {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	static DataFormatter formatter = new DataFormatter();

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

	public static InputStream seriesToExcel(List<Series> series) {
		String[] headernames = { "Id", "Collection Name", "Series Name" };

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("collections");

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < headernames.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headernames[col]);
			}

			int rowIdx = 1;
			for (Series aseries : series) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(aseries.getId());
				row.createCell(1).setCellValue(aseries.getCollection().getCollectionName());
				row.createCell(2).setCellValue(aseries.getSeriesName());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	public static InputStream transcriptionToExcel(List<Transcription> transcriptions) {

		String[] headernames = { "Id", "Collection name", "Series name", "Project code", "Interview dates",
				"Interviewee", "Interviewer", "Title", "Description", "Subject", "Keywords", "Priority",
				"Reason for priority", "Initial note", "Interviewer description", "Interviewer keywords",
				"Interviewer subjects", "Scope and contents", "Format", "Type", "Publisher", "Relation is part of",
				"Coverage spatial", "Coverage temporal", "Rights", "Language", "Transcript", "File name", "Online",
				"Rosetta", "Restriction", "Restriction note", "Dark archive", "Dark archive note",
				"Audio equipment used", "Video equipment used", "Equipment number", "Interviewer note", "General note",
				"Release form", "Transcriber assigned", "Audit check", "First edit", "Second edit", "Third edit",
				"Draft sent", "Edit with correction", "Final edit", "Final sent", "Metadata draft", "Transcript note",
				"Audio format", "Video format", "Born digital", "Original Medium", "Convert to digital", "Convert date",
				"Access media status", "Technical specification", "Sent out" };

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
				row.createCell(2).setCellValue(transcription.getseries().getSeriesName());
				row.createCell(3).setCellValue(transcription.getProjectCode());
				row.createCell(4).setCellValue(transcription.getInterviewDates());
				row.createCell(5).setCellValue(transcription.getInterviewee());
				row.createCell(6).setCellValue(transcription.getInterviewer());
				row.createCell(7).setCellValue(transcription.getTitle());
				row.createCell(8).setCellValue(transcription.getDescription());
				row.createCell(9).setCellValue(transcription.getSubject());

				row.createCell(10).setCellValue(transcription.getKeywords());
				row.createCell(11).setCellValue(getBooleanValue(transcription.getIsPriority()));
				row.createCell(12).setCellValue(transcription.getReasonForPriority());
				row.createCell(13).setCellValue(transcription.getInitialNote());
				row.createCell(14).setCellValue(transcription.getInterviewerDescription());
				row.createCell(15).setCellValue(transcription.getInterviewerKeywords());
				row.createCell(16).setCellValue(transcription.getInterviewerSubjects());
				row.createCell(17).setCellValue(transcription.getScopeAndContents());
				row.createCell(18).setCellValue(transcription.getFormat());
				row.createCell(19).setCellValue(transcription.getType());

				row.createCell(20).setCellValue(transcription.getPublisher());
				row.createCell(21).setCellValue(transcription.getRelationIsPartOf());
				row.createCell(22).setCellValue(transcription.getCoverageSpatial());
				row.createCell(23).setCellValue(transcription.getCoverageTemporal());
				row.createCell(24).setCellValue(transcription.getRights());
				row.createCell(25).setCellValue(transcription.getLanguage());
				row.createCell(26).setCellValue(transcription.getTranscript());
				row.createCell(27).setCellValue(transcription.getFileName());
				row.createCell(28).setCellValue(getBooleanValue(transcription.getIsOnline()));
				row.createCell(29).setCellValue(getBooleanValue(transcription.getIsRosetta()));

				row.createCell(30).setCellValue(getBooleanValue(transcription.getIsRestriction()));
				row.createCell(31).setCellValue(transcription.getRestrictionNote());
				row.createCell(32).setCellValue(getBooleanValue(transcription.getIsDarkArchive()));
				row.createCell(33).setCellValue(transcription.getDarkArchiveNote());
				row.createCell(34).setCellValue(transcription.getAudioEquipmentUsed());
				row.createCell(35).setCellValue(transcription.getVideoEquipmentUsed());
				row.createCell(36).setCellValue(transcription.getEquipmentNumber());
				row.createCell(37).setCellValue(transcription.getInterviewerNote());
				row.createCell(38).setCellValue(transcription.getGeneralNote());
				row.createCell(39).setCellValue(getBooleanValue(transcription.getReleaseForm()));

				row.createCell(40).setCellValue(transcription.getTranscriberAssigned());
				row.createCell(41).setCellValue(transcription.getAuditCheckCompleted());
				row.createCell(42).setCellValue(transcription.getFirstEditCompleted());
				row.createCell(43).setCellValue(transcription.getSecondEditCompleted());
				row.createCell(44).setCellValue(transcription.getThirdEditCompleted());
				row.createCell(45).setCellValue(transcription.getDraftSentDate());
				row.createCell(46).setCellValue(transcription.getEditWithCorrectionCompleted());
				row.createCell(47).setCellValue(transcription.getFinalEditCompleted());
				row.createCell(48).setCellValue(transcription.getFinalSentDate());
				// row.createCell(49).setCellValue(getTranscriptStatus(transcription.getTranscriptStatus()));
				row.createCell(49).setCellValue(transcription.getMetadataDraft());

				row.createCell(50).setCellValue(transcription.getTranscriptNote());
				row.createCell(51).setCellValue(getBooleanValue(transcription.getIsAudioFormat()));
				row.createCell(52).setCellValue(getBooleanValue(transcription.getIsVideoFormat()));
				row.createCell(53).setCellValue(getBooleanValue(transcription.getIsBornDigital()));
				row.createCell(54).setCellValue(transcription.getOriginalMedium());
				row.createCell(55).setCellValue(getBooleanValue(transcription.getIsConvertToDigital()));
				row.createCell(56).setCellValue(transcription.getConvertToDigitalDate());
				row.createCell(57).setCellValue(getTranscriptStatus(transcription.getIsProductionMasterFileStatus()));
				row.createCell(58).setCellValue(transcription.getTechnicalSpecification());
				row.createCell(59).setCellValue(getBooleanValue(transcription.getSentOut()));

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException | ParseException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	public static List<Transcription> getTranscriptionFromExcel(String path) {

		List<Transcription> transcriptions = new ArrayList<Transcription>();

		try {
			FileInputStream file = new FileInputStream(new File(path));

			// Create Workbook instance holding reference to .xlsx file
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			java.util.Iterator<Row> rowIterator = sheet.iterator();

			String[] headings = { "Title", "Interviewees", "Interviewers", "Dates", "Project Code", "Collection",
					"Description", "	Subjects", "Keywords", "Location of Interview", "Format", "	Type",
					"Release Form" };

			boolean isFirstRow = true;

			while (rowIterator.hasNext()) {

				Transcription transcription = new Transcription();

				Row row = rowIterator.next();

				if (!isFirstRow && row != null) {

					transcription.setTitle(getCellData(sheet, row, headings[0]));
					transcription.setInterviewee(getCellData(sheet, row, headings[1]));
					transcription.setInterviewer(getCellData(sheet, row, headings[2]));
					transcription.setInterviewDates(getCellData(sheet, row, headings[3]));
					transcription.setProjectCode(getCellData(sheet, row, headings[4]));

					transcription.setCollectionName(getCellData(sheet, row, headings[5]));
					transcription.setDescription(getCellData(sheet, row, headings[6]));
					transcription.setSubject(getCellData(sheet, row, headings[7]));
					transcription.setKeywords(getCellData(sheet, row, headings[8]));
					transcription.setTranscriptLocation(getCellData(sheet, row, headings[9]));

					transcription.setFormat(getCellData(sheet, row, headings[10]));
					transcription.setType(getCellData(sheet, row, headings[11]));

					transcription.setReleaseForm(checklCellForBooelan(getCellData(sheet, row, headings[12])));

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

	public static String getCellData(XSSFSheet sheet, Row row, String colName) {
		try {

			int col_Num = -1;

			Row headeRow = sheet.getRow(0);

			for (int i = 0; i < headeRow.getLastCellNum(); i++) {

				if (headeRow.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
					col_Num = i;
					break;
				}
			}

			return formatter.formatCellValue(row.getCell(col_Num));

		} catch (Exception e) {
			e.printStackTrace();
			return "";// "row " + rowNum + " or column " + colNum + " does not exist in Excel";
		}
	}

	private static byte checklCellForBooelan(String cellValue) {

		String cellValueLower = cellValue.toLowerCase();

		if (cellValueLower.equals("yes") || cellValueLower.equals("y") || cellValueLower.equals("true")) {
			return (byte) 1;
		} else if (cellValueLower.equals("no") || cellValueLower.equals("n") || cellValueLower.equals("false")) {

			return (byte) 0;
		}

		return (byte) -1;
	}

	private static String getBooleanValue(byte byteValue) {
		if (byteValue == 1) {
			return "yes";
		} else {
			return "no";
		}
	}

	private static String getTranscriptStatus(byte byteValue) {
		if (byteValue == 1) {
			return "Completed";
		} else {
			return "In progress";
		}
	}

}
