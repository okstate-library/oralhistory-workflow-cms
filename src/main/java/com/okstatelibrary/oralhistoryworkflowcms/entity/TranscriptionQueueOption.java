package com.okstatelibrary.oralhistoryworkflowcms.entity;

public class TranscriptionQueueOption {
	private int Id;

	public int getId() {
		return this.Id;
	}

	public void setId(int value) {
		this.Id = value;
	}

	private String Name;

	public String getName() {
		return this.Name;
	}

	public void setName(String value) {
		this.Name = value;
	}

	private boolean IsChecked;

	public boolean getIsChecked() {
		return IsChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.IsChecked = isChecked;
	}

	public TranscriptionQueueOption(int id, String name, boolean isChecked) {
		this.Id = id;
		this.Name = name;
		this.IsChecked = isChecked;
	}

}
