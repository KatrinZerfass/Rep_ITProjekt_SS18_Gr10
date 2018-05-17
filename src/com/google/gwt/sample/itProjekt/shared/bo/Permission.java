package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Permission, Datenstruktur für das Teilhaberschaft Business Objekt.
 * Spiegelt die Beziehung zwischen einem Nutzer und einem anderen Business Objekts wieder.
 * @author JanNoller
 * 
 */
public class Permission extends BusinessObject {

	/** Der Nutzer, welcher in einer Beziehung zum Business Object steht. */
	private User participant;
	
	/** Ein boolscher Wert, welcher signalisiert ob der Nutzer der Eigentümer eines Business Objekts ist oder nur Teilhaber. */
	private boolean isowner;
	
	/** Ein teilbares Business Objekt, das in der Beziehung zum Nutzer steht. */
	private BusinessObject shareableobject;
	
	/**
	 * Getter für den Nutzer.
	 *
	 * @return der Nutzer
	 */
	public User getParticipant() {
		return participant;
	}
	
	/**
	 * Setter für den Nutzer.
	 *
	 * @param participant der neue Nutzer
	 */
	public void setParticipant(User participant) {
		this.participant = participant;
	}

	/**
	 * Überprüft ob der Nutzer des Permission Objekts der Eigentümer ist.
	 *
	 * @return true, wenn der eingetragene Nutzer der Eigentümer ist.
	 */
	public boolean isIsowner() {
		return isowner;
	}

	/**
	 * Setter für Eigentümer flag.
	 *
	 * @param isowner der neue Status des Nutzers
	 */
	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}

	/**
	 * Getter für Business Objekt.
	 *
	 * @return das Business Objekt
	 */
	public BusinessObject getShareableobject() {
		return shareableobject;
	}

	/**
	 * Setter für Business Objekt.
	 *
	 * @param shareableobject das neue Business Objekt
	 */
	public void setShareableobject(BusinessObject shareableobject) {
		this.shareableobject = shareableobject;
	}
}
