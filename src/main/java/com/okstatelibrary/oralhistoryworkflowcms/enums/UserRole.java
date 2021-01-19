package com.okstatelibrary.oralhistoryworkflowcms.enums;

public enum UserRole {
	USER((short) 0), ADMIN((short) 1), STAFF((short) 2);

	/*
	 * Important Note: Must have semicolon at the end when there is a enum field or
	 * method
	 */
	private final short shortCode;

	UserRole(short code) {
		this.shortCode = code;
	}

	public short getCode() {
		return this.shortCode;
	}
}