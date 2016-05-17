package com.hc24.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * 处理PDF的工具类，基于iText
 * 
 * @author hc24
 * 
 */
public class PdfUtil {
	public static final String HTML = "F:/hb/aidale/index.html";
	public static final String DEST = "x:/i.pdf";

	
	public static void createPdf(String file) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(file));
		// step 3
		document.open();
		// step 4
		XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				new FileInputStream(HTML), Charset.forName("UTF-8"));
		// step 5
		document.close();
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) throws IOException,
			DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		PdfUtil.createPdf(DEST);
	}

}
