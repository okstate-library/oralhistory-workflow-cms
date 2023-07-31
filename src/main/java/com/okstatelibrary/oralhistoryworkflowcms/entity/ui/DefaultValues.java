package com.okstatelibrary.oralhistoryworkflowcms.entity.ui;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DefaultValues {

	@JsonCreator
	public DefaultValues(@JsonProperty("rights") String rights, @JsonProperty("publisher") String publisher,
			@JsonProperty("format") String format, @JsonProperty("language") String language,
			@JsonProperty("type") String type) {

		this.setRights(rights);
		this.setPublisher(publisher);
		this.setFormat(format);
		this.setLanguage(language);
		this.setType(type);
	}

	private String rights;

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String publisher;

	private String format;

	private String language;

	private String type;
}
