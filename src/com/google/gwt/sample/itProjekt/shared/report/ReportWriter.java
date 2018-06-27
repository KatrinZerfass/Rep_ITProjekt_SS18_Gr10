package com.google.gwt.sample.itProjekt.shared.report;

// 
/**
 * die abstrakte Klasse ReportWriter, die ihre Methoden an den 
 * HTMLReportWriter und den TextReportWriter vererbt.
 * @author Anna-MariaGmeiner
 */
public abstract class ReportWriter {
		
		/**
		 * Process.
		 *
		 * @param r the r
		 */
		public abstract void process(AllContactsOfUserReport r);
		
		/**
		 * Process.
		 *
		 * @param r the r
		 */
		public abstract void process(AllSharedContactsOfUserReport r);
		
		/**
		 * Process.
		 *
		 * @param r the r
		 */
		public abstract void process(AllContactsWithValueReport r);
		
		public abstract void process(AllContactsWithPropertyReport r);

}
