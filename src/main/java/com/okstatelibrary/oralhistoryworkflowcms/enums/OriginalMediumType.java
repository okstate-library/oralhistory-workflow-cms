package com.okstatelibrary.oralhistoryworkflowcms.enums;

public enum OriginalMediumType {

	AUDIOCASSETTE((byte) 1), MINIDV((byte) 2), OTHER((byte) 3);

	private final byte OriginalMediumTypeCode;

	OriginalMediumType(byte originalMediumTypeCode) {
		this.OriginalMediumTypeCode = originalMediumTypeCode;
	}

	public byte getOriginalMediumTypeCode() {
		return this.OriginalMediumTypeCode;
	}
}
