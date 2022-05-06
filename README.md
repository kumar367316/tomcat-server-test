# tomcat-server-test


<repositories>
        <repository>
            <id>com.e-iceblue</id>
            <name>e-iceblue</name>
            <url>http://repo.e-iceblue.com/nexus/content/groups/public/</url>
        </repository>
    </repositories>

<dependency>
            <groupId>e-iceblue</groupId>
            <artifactId>spire.doc.free</artifactId>
            <version>3.9.0</version>
</dependency>


ftp.server.name=ftp.htcindia.com
ftp.server.port=21
ftp.server.username=pnasmartcomm
ftp.server.password=#dtCt4!dt$G

package com.example.HelloWorld.sceduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//import com.aspose.pdf.facades.PdfFileEditor;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
/*import com.groupdocs.conversion.Converter;
import com.groupdocs.conversion.filetypes.FileType;
import com.groupdocs.conversion.options.convert.ConvertOptions;*/

@Service
public class HelloWorldScheduler {

	@Value("${ftp.server.name}")
	private String ftpHostName;

	@Value("${ftp.server.port}")
	private int ftpPort;

	@Value("${ftp.server.username}")
	private String ftpUserName;

	@Value("${ftp.server.password}")
	private String password;

	@Scheduled(cron = "0 * * ? * *")
	public void testScheduler() throws IOException {
		System.out.print("scheduler running");
		List<String> pclFileList = new LinkedList<String>();
		for (int i = 1; i <= 8; i++) {
			String pdfFile = "file" + i + ".pdf";
			File mergePdfFile = new File(pdfFile);
			System.out.println("Length of pdf file:" + mergePdfFile.length());
			String pclFileName = FilenameUtils.removeExtension(pdfFile.toString()) + ".pcl";
			/*
			 * Converter converter = new Converter(mergePdfFile.toString());
			 * ConvertOptions<?> convertOptions =
			 * FileType.fromExtension("pcl").getConvertOptions();
			 * converter.convert(pclFileName, convertOptions);
			 */
			/*
			 * PdfFileEditor fileEditor = new PdfFileEditor(); String[] files = new String[]
			 * { mergePdfFile.toString() }; fileEditor.concatenate(files, pclFileName);
			 */
			
			PdfDocument pdf = new PdfDocument(pdfFile);
			pdf.saveToFile("file" + i+".pcl",FileFormat.PCL);
			
			
			pclFileList.add(pclFileName);
			System.out.print("pcl file created:" + pclFileName);
		}
		String currentDate = getCurrentDate();
		this.fileTranserToFTPServer(pclFileList, currentDate);
		System.out.print("scheduler completed");
	}

	// file transfer to ftp server
	public void fileTranserToFTPServer(List<String> fileNameList, String currentDate) throws IOException {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fileInputStream = null;
		try {
			ftpClient.connect(ftpHostName, ftpPort);
			ftpClient.login(ftpUserName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.makeDirectory(currentDate);
			ftpClient.changeWorkingDirectory(currentDate);
			for (String fileName : fileNameList) {
				fileInputStream = new FileInputStream(fileName);
				ftpClient.storeFile(fileName, fileInputStream);
			}
			fileInputStream.close();
			// File archiveFile = new File(fileName);
			// archiveFile.delete();

		} catch (Exception exception) {
			System.out.print("Exception:" + exception.getMessage());
		} finally {
			ftpClient.disconnect();
		}
	}
	public String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
}


