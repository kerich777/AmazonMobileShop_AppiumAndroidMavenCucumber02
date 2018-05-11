package com.Utilities.General;




import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Wei.Lu on 5/5/2016.
 */
public class EmailHandler {
	protected static final Logger LOGGER = Logger.getLogger(EmailHandler.class.getName());

	private static final String host = System.getProperty("mail.smtp.host");
	private static final int port = Integer.parseInt(System.getProperty("mail.smtp.port"));
	private static final String senderEmailID = System.getProperty("mail.smtp.user");
	private static final String senderEmailPassword = System.getProperty("mail.smtp.password");
	private static final String[] recipients = System.getProperty("recipients").split(",");
	private static final String defaultPDFFilePath = "/target/cucumber-results-feature-overview.pdf";
	private static final String defaultHTMLFilePath = "/target/cucumber-results-test-results.html";
	private static final String emailReportName = System.getProperty("reportTitle");
	private static final String PROJECT_BASE_DIR =  System.getProperty("user.dir");
	private static final String TARGET_FOLDER = System.getProperty("user.dir") + System.getProperty("file.separator") + "target";
	private static final String RESULTS_ZIPFILE_PATH = TARGET_FOLDER + System.getProperty("file.separator") + "run_results.zip";

	/**
	 * Function to send the results of the current automation run. This function
	 * was originally written by Wei Lui. I refactored function to use apache commons email.
	 * 
	 * @throws InterruptedException
	 * 
	 * 
	 * @author  Jose Melendez Castro
	 */
	public static void emailResults() throws InterruptedException {

		if(System.getProperty("sendEmailResults").equalsIgnoreCase("false")) {
			return;
		}

		zipResults();

		LogMF.info(LOGGER,"Sending email...",null);
		String startDir = System.getProperty("user.dir");
		File pdfFile,htmlFile;
		pdfFile = new File(startDir + defaultPDFFilePath);
		htmlFile = new File(startDir + defaultHTMLFilePath);

		List<File> attachments = new ArrayList<File>();
		attachments.add(pdfFile);
		attachments.add(new File(RESULTS_ZIPFILE_PATH));


		try {
			sendEmail(emailReportName, recipients, htmlFile, attachments);
		} catch (EmailException e) {
			LogMF.error(LOGGER, "Fail to send email. Error encountered {0}.", new String[]{e.getMessage()});
			e.printStackTrace();
		}
		LogMF.info(LOGGER,"Done sending email.",null);
	}

	/**
	 * A general function to zip all the results from the current automation run.
	 *
	 * @author  Jose Melendez Castro
	 */
	public static void zipResults() {
		LogMF.info(LOGGER,"Creating zip folder containing automation results...",null);
		try {

			Project p = new Project();
			p.init();
			Zip zip = new Zip();
			zip.setProject(p);
			zip.setDestFile(new File(RESULTS_ZIPFILE_PATH));
			zip.setBasedir(new File(PROJECT_BASE_DIR));
			zip.setIncludes("**/*.html, **/*.pdf, **/*.xml, **/*.png");
			zip.perform();

		} catch(Exception e) {
			LogMF.info(LOGGER,"Fail to created zip folder containing automation results. Error: {0}", e.getMessage());
			e.printStackTrace();
		}

		LogMF.info(LOGGER,"Successfully created zip folder containing automation results",null);

	}

	/**
	 * Function to send email with the given information 
	 * to given recipients. 
	 *
	 * @author  Jose Melendez Castro
	 * @param subjectLine         The subject title of the email message. 
	 * @param recipients          A list of recepients that are going to receive the email.
	 * @param htmlFile            The html file to add as a body message in email.
	 * @param filesToAttach       A list of files to attach in email.
	 * @throws EmailException     This excepiton will be thrown if an error occurs while creating or sending email.
	 * @return  void 
	 */
	public static void sendEmail(String subjectLine, String[] recipients, File htmlFile, List<File> filesToAttach) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(host);
		
		// Align does not need authentication
		if(!System.getProperty("platform").equalsIgnoreCase("align")) {
			email.setSmtpPort(port);
			email.setAuthenticator(new DefaultAuthenticator(senderEmailID, senderEmailPassword));
			email.setSSLOnConnect(true);
		}

		for (String recipient : recipients) {
			email.addTo(recipient);
		}
		email.setFrom(senderEmailID);
		email.setSubject(subjectLine);

		email.setHtmlMsg(getHTMLText(htmlFile));

		for (File file : filesToAttach) {
			email.attach(file);
		}
		email.send();
	}

	/**
	 * Function read a file and return the file
	 * contents as a string.
	 *
	 * @author  Jose Melendez Castro
	 * @param htmlFile    The file to read contents.
	 * @return            Return the contents of the file as a String.
	 */
	private static String getHTMLText(File htmlFile) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(htmlFile));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}

}
