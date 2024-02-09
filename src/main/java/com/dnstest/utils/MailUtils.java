package com.dnstest.utils;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static jakarta.mail.Message.RecipientType.TO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MailUtils {

	private final Gmail service;

	public MailUtils() throws GeneralSecurityException, IOException {
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
		service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
				.setApplicationName("Test Mailer").build();
	}

	private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
			throws IOException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
				new InputStreamReader(MailUtils.class.getResourceAsStream("/credentials.json")));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
				clientSecrets, Set.of(GMAIL_SEND))
				.setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile())).setAccessType("offline")
				.build();

		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public void sendMail(String senderEmail, String recipientEmail) throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(senderEmail));
		email.addRecipient(TO, new InternetAddress(recipientEmail));
		email.setSubject("Eclipse Build Summary");

		Multipart emailContent = new MimeMultipart();

		MimeBodyPart textBodyPart1 = new MimeBodyPart();
		textBodyPart1.setText("""
				Hello Team,
				<br/><br/>
				<strong>Build Summary:</strong>
				""", "UTF-8", "html");

		MimeBodyPart buildSummary = new MimeBodyPart();
		StringWriter buildSummaryWriter = new StringWriter();
		IOUtils.copy(new FileInputStream(new File("test-output/emailable-report-BuildSummary.html")),
				buildSummaryWriter, StandardCharsets.UTF_8);
		buildSummary.setText(buildSummaryWriter.toString(), "UTF-8", "html");

		MimeBodyPart spacing = new MimeBodyPart();
		spacing.setText("<div style=' clear:left;' >", "UTF-8", "html");

		MimeBodyPart textBodyPart2 = new MimeBodyPart();
		textBodyPart2.setText("""
				<br/>
				<strong>Failed Test Case Analysis Report:</strong>
				""", "UTF-8", "html");

		MimeBodyPart failedbuildSummary = new MimeBodyPart();
		StringWriter failedBuildWriter = new StringWriter();
		IOUtils.copy(new FileInputStream(new File("test-output/emailable-summary-report.html")), failedBuildWriter,
				StandardCharsets.UTF_8);
		failedbuildSummary.setText(failedBuildWriter.toString(), "UTF-8", "html");

		MimeBodyPart signature = new MimeBodyPart();
		signature.setText("""
				<br/>
				<strong>Regards,
				<br/>
				KiwiQA Automation Team</strong>
				""", "UTF-8", "html");

		MimeBodyPart report = new MimeBodyPart();
		report.attachFile("extReport.html");

		emailContent.addBodyPart(textBodyPart1);
		emailContent.addBodyPart(buildSummary);
		emailContent.addBodyPart(spacing);
		emailContent.addBodyPart(textBodyPart2);
		emailContent.addBodyPart(failedbuildSummary);
		emailContent.addBodyPart(signature);
		emailContent.addBodyPart(report);

		email.setContent(emailContent);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		byte[] rawMessageBytes = buffer.toByteArray();
		String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);
		Message msg = new Message();
		msg.setRaw(encodedEmail);

		try {
			msg = service.users().messages().send("me", msg).execute();
			System.out.println("Message id: " + msg.getId());
			System.out.println(msg.toPrettyString());
		} catch (GoogleJsonResponseException e) {
			GoogleJsonError error = e.getDetails();
			if (error.getCode() == 403) {
				System.err.println("Unable to send message: " + e.getDetails());
			} else {
				throw e;
			}
		}
	}

}
