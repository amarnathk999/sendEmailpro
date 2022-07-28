package com.assignmentforemail.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class MailSearchData {
	private Date createDate;
	private String subject;
//	@JsonProperty("emailTo")
//	private String emailTo;
	private JsonNode emailTo;
//	private JsonNode emailCC;
//	private JsonNode emailBCC;
}
