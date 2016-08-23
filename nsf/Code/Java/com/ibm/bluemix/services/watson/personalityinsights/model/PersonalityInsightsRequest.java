package com.ibm.bluemix.services.watson.personalityinsights.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to send to a IBM Watson PersonaltyInsight request.
 * 
 * @author FDN
 * 
 */
public class PersonalityInsightsRequest {

	/*
	 * header: text/plain text/html application/json
	 */
	@JsonIgnore
	private ContentTypes contentType = ContentTypes.TEXT_PLAIN;
	@JsonIgnore
	private ContentLanguage contentLanguage = ContentLanguage.ENGLISH;

	private String source = "HR Assistant";
 
	/**
	 * Class constructor with given {@link #contentType} and
	 * {@link #contentLanguage}
	 */
	public PersonalityInsightsRequest(ContentTypes contentType,
			ContentLanguage contentLanguage) {
		this.contentType = contentType;
		this.contentLanguage = contentLanguage;
	}

	/**
	 * Class constructor.
	 */
	public PersonalityInsightsRequest() {

	}

	public enum ContentTypes {
		TEXT_PLAIN("text/plain"), TEXT_HTML("text/html"), APPLICATION_JSON(
				"application/json");

		private final String contentTypeValue;

		private ContentTypes(final String s) {
			contentTypeValue = s;
		}

		public String toString() {
			return contentTypeValue;
		}
		// further methods, attributes, etc.
	}

	public enum ContentLanguage {
		ARABIC("ar"), ENGLISH("text/html"), SPANISH("es"), JAPANESE("ja");

		private final String contentLanguageValue;

		private ContentLanguage(final String s) {
			contentLanguageValue = s;
		}

		public String toString() {
			return contentLanguageValue;
		}
		// further methods, attributes, etc.
	}

	@JsonProperty("contentItems")
	private List<ContentItem> contentItems = null;

	public List<ContentItem> getContentItems() {
		return contentItems;
	}

	public void setContentItems(List<ContentItem> contentItems) {
		this.contentItems = contentItems;
	}

	public void addContentItem(ContentItem contentItem) {
		this.contentItems.add(contentItem);
	}

	public ContentTypes getContentType() {
		return contentType;
	}

	public void setContentType(ContentTypes contentType) {
		this.contentType = contentType;
	}

	public ContentLanguage getContentLanguage() {
		return contentLanguage;
	}

	public void setContentLanguage(ContentLanguage contentLanguage) {
		this.contentLanguage = contentLanguage;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}