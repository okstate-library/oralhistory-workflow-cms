package com.okstatelibrary.oralhistoryworkflowcms.enums;

public enum EquipmentType {
	AUDIO((byte) 1), VIDEO((byte) 2);

	/*
	 * Important Note: Must have semicolon at the end when there is a enum field or
	 * method
	 */
	public final byte ShortCode;

	private EquipmentType(byte code) {
		this.ShortCode = code;
	}

	public byte getCode() {
		return this.ShortCode;
	}

//private final String displayValue;
//    
//    private Color(String displayValue) {
//        this.displayValue = displayValue;
//    }
//    
//    public String getDisplayValue() {
//        return displayValue;
//    }
}
