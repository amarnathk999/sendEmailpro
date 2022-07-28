package com.assignmentforemail.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.assignmentforemail.pojo.RootMail;
@Service
public class MailService {
	@Value("${spring.mail.username}")
	private String fromRootData;
	@Autowired
	private JavaMailSender mailSender;
	public void sendEmail(List<String> toMail, List<String> toCcc, List<String> toBcc, String subject, String body) {
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			String[] arrTomail = (String[]) toMail.stream().toArray(String[] ::new);
			String[] arrToCcc = (String[]) toCcc.stream().toArray(String[] ::new);
			String[] arrToBcc = (String[]) toBcc.stream().toArray(String[] ::new);
			helper.setTo(arrTomail);
			if(arrToCcc.length>0) {
				helper.setTo(arrToCcc);
			}
			if(arrToBcc.length>0) {
				helper.setTo(arrToBcc);
			}

			helper.setSubject(subject);
			helper.setText(body);
			mailSender.send(message);
		}catch(Exception ec) 
		{
			ec.printStackTrace();
		}
		//return "Email send successfully!";
	}
}
