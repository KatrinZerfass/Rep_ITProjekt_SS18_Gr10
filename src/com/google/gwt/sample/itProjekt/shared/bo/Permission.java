package com.google.gwt.sample.itProjekt.shared.bo;

public class Permission extends BusinessObject {

	private User participant;
	private boolean isowner;
	private BusinessObject shareableobject;
	
	public User getParticipant() {
		return participant;
	}
	
	public void setParticipant(User participant) {
		this.participant = participant;
	}

	public boolean isIsowner() {
		return isowner;
	}

	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}

	public BusinessObject getShareableobject() {
		return shareableobject;
	}

	public void setShareableobject(BusinessObject shareableobject) {
		this.shareableobject = shareableobject;
	}
}
