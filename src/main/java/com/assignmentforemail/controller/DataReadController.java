package com.assignmentforemail.controller;

import java.awt.print.Pageable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignmentforemail.pojo.MailSearchData;
import com.assignmentforemail.pojo.RootMail;
import com.assignmentforemail.pojo.RootMailConfig;
import com.assignmentforemail.repository.EmailRepository;
import com.assignmentforemail.service.EmailService;
import com.assignmentforemail.service.MailService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/data")
public class DataReadController {
	ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	public static final String DATE_PATTERN="yyyy-MM-dd";
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	EmailService emailService1;
	@Autowired
	EmailService ems;
	@Autowired
	private MailService notificationService;
	@Autowired
	private EntityManager entityManager;
	@PostMapping(path="/savemail")
	public RootMail saveJson(@RequestBody RootMail rootData) {
		String msg=null;
		JsonNode root1=null;
		try {
			String strin=mapper.writeValueAsString(rootData);
			root1=mapper.readValue(strin, JsonNode.class);
			JsonNode valuesNode = mapper.readTree(strin).get("emailTo");
			List<String> emailTo = new ArrayList<>();
			for (JsonNode node : valuesNode) {
				emailTo.add(node.asText());
			}
			JsonNode valuesNode1 = mapper.readTree(strin).get("emailCC");
			List<String> emailCC = new ArrayList<>();
			for (JsonNode node : valuesNode1) {
				emailCC.add(node.asText());
			}
			JsonNode valuesNode2 = mapper.readTree(strin).get("emailBCC");
			List<String> emailBCC = new ArrayList<>();
			for (JsonNode node : valuesNode2) {
				emailBCC.add(node.asText());
			}
			String holdsub=root1.get("subject").textValue();
			String emlbody=root1.get("emailBody").textValue();
			boolean retVal=validate(emailTo,holdsub,emlbody);
			System.out.println("emailTo1 :::: "+emailTo);
			System.out.println("validate >>>>> "+retVal);
			if(retVal) {
				boolean emlVldt=validateEmailId(emailTo,emailCC,emailBCC);
				if(emlVldt){
					emailService1.saveJson(root1);//save data into Db
					notificationService.sendEmail(emailTo,emailCC,emailBCC,holdsub,emlbody);//send mail
				}

			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return rootData;
	}

	//fetch Data based on emailCreation Date
	@GetMapping(path = "findByDate/{emailDate}")
	public ResponseEntity <List<RootMailConfig>> getDataByDate(@RequestParam(value = "emailDate") String emailDate) throws ParseException{
		System.out.println("------------In Api---emailDate--------- "+emailDate);
		LocalDate emailDate1 = LocalDate.parse(emailDate);
		return new ResponseEntity<List<RootMailConfig>>((List<RootMailConfig>) emailRepository.findByEmailDate(emailDate1), HttpStatus.OK);
	}

	//fetch Data based on subject
	@GetMapping(path = "findBySubject/{bySubject}")
	public ResponseEntity <List<RootMailConfig>> getDataBySubject(@RequestParam(name = "bySubject") String bySubjectText) throws ParseException{
		System.out.println("------------In Api--bySubject---------- "+bySubjectText);
		List<RootMailConfig> data=emailRepository.findBySubject(bySubjectText);
		System.out.println("data >>>> "+data);
		return new ResponseEntity<List<RootMailConfig>>((List<RootMailConfig>) emailRepository.findBySubject(bySubjectText), HttpStatus.OK);
	}

	@GetMapping(path = "findByEmail/{byEmail}")
	public ResponseEntity <List<RootMailConfig>> getDataByEmail(@RequestParam(name = "byEmail") String byEmail){
		System.out.println("------------In Api--byEmail---------- "+byEmail);
		List<RootMailConfig> data=(List<RootMailConfig>) emailService1.findByEmailId(byEmail);
		return new ResponseEntity<List<RootMailConfig>>(data, HttpStatus.OK);
	}


	@PostMapping("/emailData/search")
	public ResponseEntity<List<RootMailConfig>> getWllknownMisSearch(@RequestBody MailSearchData emailSearchReqst) {
		boolean isFilter = false;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RootMailConfig> criteriaQuery = criteriaBuilder.createQuery(RootMailConfig.class);
		Root<RootMailConfig> itemRoot = criteriaQuery.from(RootMailConfig.class);
		List<Predicate> predicates = new ArrayList<>();
		List<RootMailConfig> vendorMisReportList = new ArrayList<>();

		if (!emailSearchReqst.getSubject().isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemRoot.get("subject")),"%"+emailSearchReqst.getSubject().toLowerCase()+"%"));
			isFilter = true;
		}

		if (emailSearchReqst.getCreateDate()!= null) {
			System.out.println("DDDDDD = "+emailSearchReqst.getCreateDate());
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date ldate=emailSearchReqst.getCreateDate();
			Date todayWithZeroTime = null;
			try {
				todayWithZeroTime = formatter.parse(formatter.format(ldate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println("Value of Date and Time" + todayWithZeroTime);
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(itemRoot.get("creationDate"), todayWithZeroTime));
			isFilter = true;
		}

		if (!emailSearchReqst.getEmailTo().isEmpty()) {
			System.out.println("emmll >>> "+emailSearchReqst.getEmailTo());
			predicates.add(criteriaBuilder.equal(itemRoot.get("toEmailID"), emailSearchReqst.getEmailTo()));
			isFilter = true;
		}

		if (isFilter) {
			criteriaQuery.where(predicates.toArray(new Predicate[0]));
			vendorMisReportList = entityManager.createQuery(criteriaQuery).getResultList();
		}
		System.out.println("Size" + vendorMisReportList.size());

		return new ResponseEntity<>(vendorMisReportList, HttpStatus.OK);
	}

	
	
//	@GetMapping("/emailData")
//		public List<RootMail> getRootData(@RequestParam(required = false) @DateTimeFormat(pattern =DATE_PATTERN) Date fromdat,
//				@DateTimeFormat(pattern =DATE_PATTERN) Date todate,@RequestParam(required = false) String subject,Pageable pgbl){
//					return ems.getRootMailData(fromdat,todate,subject,pgbl);
//	}
	
	
	
	
	
	
	
	
	public boolean validate(List<String> emailToList,String holdsub,String emlbody) {
		boolean ret=false;
		if(emailToList.size()>0 && !holdsub.isEmpty() && !emlbody.isEmpty()) {
			ret=true;
		}
		return ret;
	}

	public boolean validateEmailId(List<String> emailToList,List<String> ccEmail,List<String> bccEmail) {
		boolean retuVar=false;
		ArrayList<String> holdEmalLst=new ArrayList<String>();
		ArrayList<String> finlEmalLst=new ArrayList<String>();
		holdEmalLst.addAll(ccEmail);
		holdEmalLst.addAll(bccEmail);
		holdEmalLst.addAll(emailToList);
		System.out.println(holdEmalLst.size()+ " = holdEmalLst :: "+holdEmalLst.toString());
		String regex =  "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		for(String email : holdEmalLst){
			Matcher matcher = pattern.matcher(email);  
			System.out.println(email +" : "+ matcher.matches()+"\n");  
			boolean b=matcher.matches();
			if(b) {
				finlEmalLst.add(email);
			}
		}
		System.out.println(finlEmalLst.size()+ " = finlEmalLst :: "+finlEmalLst.toString());
		if(holdEmalLst.equals(finlEmalLst)==true) {
			retuVar=true;
		}
		return retuVar;
	}

}
