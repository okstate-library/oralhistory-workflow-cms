package com.okstatelibrary.oralhistoryworkflowcms.entity;

public enum PredefinedUserEnum {
	INTERVIEWER((short) 1);

	/*
	 * Important Note: Must have semicolon at the end when there is a enum field or
	 * method
	 */
	private final short shortCode;

	PredefinedUserEnum(short code) {
		this.shortCode = code;
	}

	public short getCode() {
		return this.shortCode;
	}
}