package br.com.ufpi.systematicmap.utils.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;

import br.com.ufpi.systematicmap.model.Article;

public class XLSBuilder {

	private XLSBuilder() {
	}

	public static File generateFile(List<Article> articles, String fileName, Logger logger) throws IOException {
		File file = new File(fileName);
		try (HSSFWorkbook workbook = new HSSFWorkbook();) {
			HSSFSheet table = workbook.createSheet();
			return finalGenerate(table, workbook, file, articles, logger);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private static File finalGenerate(HSSFSheet table, HSSFWorkbook workbook, File file, List<Article> articles, Logger logger) {
		try {
			int line = 0;
			HSSFRow rowTitle = table.createRow(line);
			generateRowTitle(rowTitle, workbook);
			line++;
			for (Article article : articles) {
				HSSFRow row = table.createRow(line);
				lineGenerator(article, row);
				line++;
			}
			resizeColumns(table);
			workbook.write(file);
			return file;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private static void resizeColumns(HSSFSheet table) {
		for (int counter = 0; counter < 7; counter++) {
			table.autoSizeColumn(counter);
		}
	}

	private static void generateRowTitle(HSSFRow rowTitle, HSSFWorkbook workbook) {

		rowTitle.createCell(0).setCellValue("Id");
		rowTitle.createCell(1).setCellValue("Title");
		rowTitle.createCell(2).setCellValue("Author");
		rowTitle.createCell(3).setCellValue("Journal");
		rowTitle.createCell(4).setCellValue("Doi");
		rowTitle.createCell(5).setCellValue("DocType");
		rowTitle.createCell(6).setCellValue("Source");
		formatRowTittle(rowTitle, workbook);
	}

	private static void formatRowTittle(HSSFRow rowTitle, HSSFWorkbook workbook) {
		
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		
		formatFontTitle(font);
		formatStyleTitle(style,font);

		for (Cell cell : rowTitle) {
			cell.setCellStyle(style);
		}
	}

	private static void formatStyleTitle(HSSFCellStyle style, HSSFFont font) {
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);		
	}

	private static void formatFontTitle(HSSFFont font) {
		font.setBold(true);
		font.setFontName("Arial");
		font.setFontHeight(convertFontSize(12));
	}

	private static short convertFontSize(int size) {
		return ((short)(20*size));		
	}

	private static void lineGenerator(Article article, HSSFRow row) {
		row.createCell(0).setCellValue(article.getId());
		row.createCell(1).setCellValue(article.getTitle());
		row.createCell(2).setCellValue(article.getAuthor());
		row.createCell(3).setCellValue(article.getJournal());
		row.createCell(4).setCellValue(article.getDoi());
		row.createCell(5).setCellValue(article.getDocType());
		row.createCell(6).setCellValue(article.sourceView(article.getSource()));
		
	}

}
