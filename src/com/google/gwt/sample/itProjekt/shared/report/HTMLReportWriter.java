package com.google.gwt.sample.itProjekt.shared.report;

public class HTMLReportWriter extends ReportWriter {

	private String reportText;
	
	@Override
	public void process(AllContactsOfUserReport r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(AllContactsReport r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(AllSharedContactsOfUserReport r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(AllContactsWithValueReport r) {
		// TODO Auto-generated method stub

	}
	
	public String getReportText() {
		return "<html><head><title>Report</title></head><body>" + this.reportText + "</body></html>";
	}
	
	public String p2HTML(CompositeParagraph p) {
		StringBuffer resultParagraph=new StringBuffer();
		
		for (int i=0; i < p.getNumParagraphs(); i++) {
			resultParagraph.append("<p>" + p.getParagraphAt(i) + "</p>");
		}
		
		return resultParagraph.toString();
	}
	
	public String p2HTML(SimpleParagraph p) {
		return "<p>" + p.toString() + "</p>";
	}

}
