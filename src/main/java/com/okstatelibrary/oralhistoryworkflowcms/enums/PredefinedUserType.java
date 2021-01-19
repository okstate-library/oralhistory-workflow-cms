package com.okstatelibrary.oralhistoryworkflowcms.enums;

public enum PredefinedUserType {
	INTERVIEWER((short) 1),
	TRANSCRIBER((short) 2);
	/*
	 * Important Note: Must have semicolon at the end when there is a enum field or
	 * method
	 */
	private final short shortCode;

	PredefinedUserType(short code) {
		this.shortCode = code;
	}

	public short getCode() {
		return this.shortCode;
	}
}