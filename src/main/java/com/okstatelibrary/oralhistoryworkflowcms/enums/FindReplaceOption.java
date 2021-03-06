package com.okstatelibrary.oralhistoryworkflowcms.enums;

public enum FindReplaceOption {
	All((byte) 1), Title((byte) 2), Description((byte) 3), Keywords((byte) 4), Subject((byte) 5);

	/*
	 * Important Note: Must have semicolon at the end when there is a enum field or
	 * method
	 */
	public final byte ShortCode;

	private FindReplaceOption(byte code) {
		this.ShortCode = code;
	}

	public byte getCode() {
		return this.ShortCode;
	}

}
