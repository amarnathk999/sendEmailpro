package com.assignmentforemail.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RootMail {
	@JsonProperty("emailTo") 
	public List<String> emailTo;
	@JsonProperty("emailCC") 
	public List<String> emailCC;
	@JsonProperty("emailBCC") 
	public List<String> emailBCC;
	public String subject;
	public String emailBody;
}
