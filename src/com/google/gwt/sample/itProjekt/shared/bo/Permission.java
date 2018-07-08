package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Permission, Datenstruktur für das Teilhaberschaft Business Objekt.
 * Spiegelt die Beziehung zwischen einem Nutzer und einem anderen Business Objekts wieder.
 */
public class Permission extends BusinessObject {
	
	private static final long serialVersionUID = 1L;

	/** NuterID des teilenden Nutzers. */
	private int sourceUserID;
	
	/** NutzerID des teilhabenden Nutzers. */ 
	private int participantID;
	
	/** ID des zu teilenden Objekts. */
	private int shareableObjectID;
	
	/** 
	 * Auslesen der NutzerID des teilenden Nutzers. 
	 * 
	 * @return Nutzer ID des teilenden Nutzers
	 */
	
	public int getSourceUserID() {
		return sourceUserID;
	}
	
	/** 
	 * Setzen der NutzerID des teilenden Nutzers. 
	 * 
	 * @param sourceUserID die ID des teilenden Nutzers
	 */

	public void setSourceUserID(int sourceUserID) {
		this.sourceUserID = sourceUserID;
	}
	
	/** 
	 * Auslesen der NutzerID des teilhabenden Nutzers.
	 * 
	 * @return Nutzer ID des Teilhabers
	 */

	public int getParticipantID() {
		return participantID;
	}
	
	/** 
	 * Setzen der NutzerID des teilhabenden Nutzers. 
	 * 
	 * @param participantID Nutzer ID des Teilhabers
	 */

	public void setParticipantID(int participantID) {
		this.participantID = participantID;
	}
	
	/** 
	 * Auslesen des Business Objekts. 
	 * 
	 * @return ID des Business Objekts
	 */

	public int getShareableObjectID() {
		return shareableObjectID;
	}
	
	/** 
	 * Setzen des Business Objekts. 
	 * 
	 * @param shareableObjectID ID des Business Objekts
	 */

	public void setShareableObjectID(int shareableObjectID) {
		this.shareableObjectID = shareableObjectID;
	}
		
}
