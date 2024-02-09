package com.dnstest.reports;

import java.util.List;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class CustomReporterListener implements IReporter {

	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {
	}
}