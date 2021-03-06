package com.okstatelibrary.oralhistoryworkflowcms.enums;

public enum UserRole {
	User((byte) 1),staff((byte) 2), Admin((byte) 3);

	/*
	 * Important Note: Must have semicolon at the end when there is a enum field or
	 * method
	 */
	private final byte shortCode;

	UserRole(byte code) {
		this.shortCode = code;
	}

	public byte getCode() {
		return this.shortCode;
	}
}