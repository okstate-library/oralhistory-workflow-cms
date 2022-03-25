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
				"Coverage spatial", "Coverage temporal", "Rights", "Language", "Identifier", "Transcript", "File name",
				"Online", "Rosetta", "Rosetta form", "Restriction", "Restriction note", "Dark archive",
				"Dark archive note", "Audio equipment used", "Video equipment used", "Equipment number",
				"Interviewer note", "General note", "Release form", "Transcriber assigned", "Audit check", "First edit",
				"Second edit", "Third edit", "Draft sent", "Edit with correction", "Final edit", "Final sent", "Status",
				"Metadata draft", "Transcript note", "Audio format", "Video format", "Born digital", "Original Medium",
				"Convert to digital", "Convert date", "Access media status", "Technical specification", "Master file",
				"Access file", "Sent out" };

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
				row.createCell(26).setCellValue(transcription.getIdentifier());
				row.createCell(27).setCellValue(transcription.getTranscript());
				row.createCell(28).setCellValue(transcription.getFileName());
				row.createCell(29).setCellValue(getBooleanValue(transcription.getIsOnline()));

				row.createCell(30).setCellValue(getBooleanValue(transcription.getIsRosetta()));
				row.createCell(31).setCellValue(getBooleanValue(transcription.getIsRestriction()));
				row.createCell(32).setCellValue(transcription.getRestrictionNote());
				row.createCell(33).setCellValue(getBooleanValue(transcription.getIsDarkArchive()));
				row.createCell(34).setCellValue(transcription.getDarkArchiveNote());
				row.createCell(35).setCellValue(transcription.getAudioEquipmentUsed());
				row.createCell(36).setCellValue(transcription.getVideoEquipmentUsed());
				row.createCell(37).setCellValue(transcription.getEquipmentNumber());
				row.createCell(38).setCellValue(transcription.getInterviewerNote());
				row.createCell(39).setCellValue(transcription.getGeneralNote());

				row.createCell(40).setCellValue(getBooleanValue(transcription.getReleaseForm()));
				row.createCell(41).setCellValue(transcription.getTranscriberAssigned());
				row.createCell(42).setCellValue(transcription.getAuditCheckCompleted());
				row.createCell(43).setCellValue(transcription.getFirstEditCompleted());
				row.createCell(44).setCellValue(transcription.getSecondEditCompleted());
				row.createCell(45).setCellValue(transcription.getThirdEditCompleted());
				row.createCell(46).setCellValue(transcription.getDraftSentDate());
				row.createCell(47).setCellValue(transcription.getEditWithCorrectionCompleted());
				row.createCell(48).setCellValue(transcription.getFinalEditCompleted());
				row.createCell(49).setCellValue(transcription.getFinalSentDate());

				row.createCell(50).setCellValue(getTranscriptStatus(transcription.getTranscriptStatus()));
				row.createCell(51).setCellValue(transcription.getMetadataDraft());
				row.createCell(52).setCellValue(transcription.getTranscriptNote());
				row.createCell(53).setCellValue(getBooleanValue(transcription.getIsAudioFormat()));
				row.createCell(54).setCellValue(getBooleanValue(transcription.getIsVideoFormat()));
				row.createCell(55).setCellValue(getBooleanValue(transcription.getIsBornDigital()));
				row.createCell(56).setCellValue(transcription.getOriginalMedium());
				row.createCell(57).setCellValue(getBooleanValue(transcription.getIsConvertToDigital()));
				row.createCell(58).setCellValue(transcription.getConvertToDigitalDate());
				row.createCell(59).setCellValue(getTranscriptStatus(transcription.getIsProductionMasterFileStatus()));

				row.createCell(60).setCellValue(transcription.getTechnicalSpecification());
				row.createCell(61).setCellValue(transcription.getMasterFileLocation());
				row.createCell(62).setCellValue(transcription.getAccessFileLocation());
				row.createCell(63).setCellValue(getBooleanValue(transcription.getSentOut()));

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

			boolean isFirstRow = true;
			int rowCount = 0;

			while (rowIterator.hasNext()) {

				Transcription transcription = new Transcription();

				Row row = rowIterator.next();

				if (!isFirstRow && row != null) {

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

					transcription.setCollectionName(row.getCell(12).getStringCellValue());
					transcription.setSeriesName(checklCellForString(row.getCell(13)));

					transcription.setCoverageSpatial(checklCellForString(row.getCell(14)));
					transcription.setCoverageTemporal(checklCellForString(row.getCell(15)));
					transcription.setRights(checklCellForString(row.getCell(16)));
					transcription.setLanguage(checklCellForString(row.getCell(17)));
					transcription.setProjectCode(checklCellForString(row.getCell(18)));

					transcription.setTranscriberAssigned(checklCellForString(row.getCell(19)));
					transcription.setTranscriptNote(checklCellForString(row.getCell(20)));
					transcription.setEquipmentNumber(checklCellForNumeric(row.getCell(21)));
					transcription.setMetadataDraft(checklCellForString(row.getCell(22)));

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
					transcription.setIsProductionMasterFileStatus(checklCellForAccessMediaStatus(row.getCell(34)));

					transcription.setIsBornDigital(checklCellForBooelan((row.getCell(35))));
					transcription.setIsOnline(checklCellForBooelan(row.getCell(36)));
					transcription.setIsConvertToDigital(checklCellForBooelan(row.getCell(37)));
					transcription.setRelationIsPartOf(checklCellForString(row.getCell(38)));
					transcription.setRelationIsPartOf(checklCellForString(row.getCell(38)));

					transcription.setGeneralNote(checklCellForString(row.getCell(39)));
					transcription.setTranscriptStatus(checklCellForAccessMediaStatus(row.getCell(40)));
					transcription.setIsAudioFormat(checklCellForBooelan(row.getCell(41)));
					transcription.setIsVideoFormat(checklCellForBooelan(row.getCell(42)));
					transcription.setOriginalMedium(checklCellForString(row.getCell(43)));

					transcription.setIsRosetta(checklCellForBooelan(row.getCell(44)));

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
