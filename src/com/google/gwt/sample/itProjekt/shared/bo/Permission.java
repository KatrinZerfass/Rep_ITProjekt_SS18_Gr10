package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Permission, Datenstruktur f�r das Teilhaberschaft Business Objekt.
 * Spiegelt die Beziehung zwischen einem Nutzer und einem anderen Business Objekts wieder.
 * @author JanNoller
 * 
 */
public class Permission extends BusinessObject {
	
	private int sourceUserID;
	
	private int participantID;
	
	private int shareableObjectID;
	
	/** Ein boolscher Wert, welcher signalisiert ob der Nutzer der Eigent�mer eines Business Objekts ist oder nur Teilhaber. */
	private boolean isowner;
	

	public int getSourceUserID() {
		return sourceUserID;
	}

	public void setSourceUserID(int sourceUserID) {
		this.sourceUserID = sourceUserID;
	}

	public int getParticipantID() {
		return participantID;
	}

	public void setParticipantID(int participantID) {
		this.participantID = participantID;
	}

	public int getShareableObjectID() {
		return shareableObjectID;
	}

	public void setShareableObjectID(int shareableObjectID) {
		this.shareableObjectID = shareableObjectID;
	}
	
	public boolean isIsowner() {
		return isowner;
	}

	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}
}
