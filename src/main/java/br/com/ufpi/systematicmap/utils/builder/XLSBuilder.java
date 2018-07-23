package br.com.ufpi.systematicmap.utils.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.ufpi.systematicmap.model.Article;

public class XLSBuilder {

	public static Download generateFile(List<Article> articles, String fileName) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet table = workbook.createSheet("Aba1");

		ByteArrayOutputStream file = null;

		try {
			file = new ByteArrayOutputStream();
			workbook.write(file);
			int i = 0;
			for (Article article : articles) {
				HSSFRow row = table.createRow(i);
				row.createCell(0).setCellValue(article.getId());
				row.createCell(1).setCellValue(article.getTitle());
				row.createCell(2).setCellValue(article.getAuthor());
				row.createCell(3).setCellValue(article.getYear());
				row.createCell(4).setCellValue(article.getKeywords());
				i++;
			}
			workbook.write(file);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao exportar arquivo.");
		} finally {
			try {
				file.flush();
				file.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		workbook.close();
		return new ByteArrayDownload(file.toByteArray(), "application/vnd.ms-excel", fileName);
	}
}
