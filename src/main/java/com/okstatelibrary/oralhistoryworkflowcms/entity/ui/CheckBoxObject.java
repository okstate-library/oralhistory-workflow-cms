package com.okstatelibrary.oralhistoryworkflowcms.entity.ui;

public class CheckBoxObject {

	public CheckBoxObject() {

	}

	public CheckBoxObject(String identification, String name) {
		this.identification = identification;
		this.name = name;
		this.withCount = false;
	}

	public CheckBoxObject(String identification, String name, long count) {

		this.identification = identification;
		this.name = name;
		this.withCount = true;
		this.count = count;
	}

	public CheckBoxObject(int id, String name) {
		this.id = id;
		this.name = name;
		this.withCount = false;
	}

	public CheckBoxObject(int id, String name, long count) {
		this.id = id;
		this.name = name;
		this.withCount = true;
		this.count = count;
	}

	private String identification;

	private int id;

	private String name;

	private boolean withCount;

	private long count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		if (this.withCount) {

			return this.getSubStringName() + " (" + count + ")";
		} else {
			return this.getSubStringName();
		}
	}

	private String getSubStringName() {
		if (this.name.length() > 27) {
			return this.name.substring(0, 25) + "..";
		}

		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(long count) {
		this.count = count;
	}

	/**
	 * @return the withCount
	 */
	public boolean isWithCount() {
		return withCount;
	}

	/**
	 * @param withCount the withCount to set
	 */
	public void setWithCount(boolean withCount) {
		this.withCount = withCount;
	}

	/**
	 * @return the identification
	 */
	public String getIdentification() {
		return identification;
	}

	/**
	 * @param identification the identification to set
	 */
	public void setIdentification(String identification) {
		this.identification = identification;
	}
}
