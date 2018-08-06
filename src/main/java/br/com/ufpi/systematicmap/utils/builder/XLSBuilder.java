package br.com.ufpi.systematicmap.utils.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.ufpi.systematicmap.model.Article;

public class XLSBuilder {

	public static File generateFile(List<Article> articles, String fileName) throws IOException {
		File file = new File(fileName);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet table = workbook.createSheet();

		try {
			int line = 0;
			HSSFRow rowTitle = table.createRow(line);
			generateRowTitle(rowTitle);
			line++;
			for (Article article : articles) {
				HSSFRow row = table.createRow(line);
				lineGenerator(article, row);
				line++;
			}
			workbook.write(file);
			workbook.close();
			return file;
		} catch (Exception e) {
			System.err.print("Erro ao exportar arquivo. " + e.getMessage());
		}
		return null;
	}

	private static void generateRowTitle(HSSFRow rowTitle) {
		rowTitle.createCell(0).setCellValue("Id");
		rowTitle.createCell(1).setCellValue("Title");
		rowTitle.createCell(2).setCellValue("Author");
		rowTitle.createCell(3).setCellValue("Journal");
		rowTitle.createCell(4).setCellValue("Doi");
		rowTitle.createCell(5).setCellValue("DocType");
		rowTitle.createCell(6).setCellValue("Source");
	}

	private static void lineGenerator(Article article, HSSFRow row) {
		row.createCell(0).setCellValue(article.getId());
		row.createCell(1).setCellValue(article.getTitle());
		row.createCell(2).setCellValue(article.getAuthor());
		row.createCell(3).setCellValue(article.getJournal());
		row.createCell(4).setCellValue(article.getDoi());
		row.createCell(5).setCellValue(article.getDocType());
		//TODO: Rever o enum.
		row.createCell(6).setCellValue(article.sourceView(article.getSource()));
	}
}
