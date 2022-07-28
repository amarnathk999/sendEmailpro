package com.assignmentforemail.service;


import com.assignmentforemail.pojo.RootMail;
import com.assignmentforemail.pojo.RootMailConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface EmailService {
	public RootMailConfig saveJson(JsonNode jsonNode);
	public ArrayList<RootMailConfig> findByEmailId(String byEmailId);
	public ArrayList<RootMailConfig> findByMultiCriteria(String emailDate,String bySubject,String byEmailId);
	//public List<RootMail> getRootMailData(Date fromdat, Date todate, String subject, Pageable pgbl);
	
}
