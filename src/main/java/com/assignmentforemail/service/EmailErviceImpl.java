package com.assignmentforemail.service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.assignmentforemail.pojo.RootMail;
import com.assignmentforemail.pojo.RootMailConfig;
import com.assignmentforemail.repository.EmailRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import antlr.StringUtils;

@Service
public class EmailErviceImpl  implements EmailService{
	ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Autowired
	EmailRepository emailrepo;
	//	@Autowired
	//	EmailService emailService2;
	@Override
	public RootMailConfig saveJson(JsonNode jsonNode) {
		RootMailConfig inputData=new RootMailConfig();
		// TODO Auto-generated method stub
		//System.out.println("jsonNode ::: "+jsonNode);

		inputData.setEmailBCC(jsonNode.requiredAt("/emailBCC"));
		inputData.setEmailCC(jsonNode.requiredAt("/emailCC"));
		inputData.setEmailTo(jsonNode.requiredAt("/emailTo"));
		inputData.setSubject(jsonNode.get("subject").textValue());
		inputData.setEmailBody(jsonNode.get("emailBody").textValue());
		inputData.setCreationDate(new Date());
		return emailrepo.save(inputData);
	}
	@Override
	public ArrayList<RootMailConfig> findByEmailId(String byEmailId) {
		System.out.println("byEmailId ::: "+byEmailId);
		ArrayList<RootMailConfig> retLst=new ArrayList();
		List<RootMailConfig> emlList=emailrepo.findAll();
		if(!emlList.isEmpty()) {
			for (RootMailConfig rootMailConfig : emlList) {
				Iterable<JsonNode> jnod=rootMailConfig.getEmailTo();
				String dd=jnod.toString();
				if(dd.contains(byEmailId)) {
					retLst.add(rootMailConfig);
				}
			}
			System.out.println("find by email list Size ::: "+retLst.size());
		}

		return retLst;
	}
	@Override
	public ArrayList<RootMailConfig> findByMultiCriteria(String emailDate, String bySubject, String byEmailId) {
		ArrayList<RootMailConfig> holdData=null;
		if(!emailDate.isEmpty()) {
			LocalDate emailDate1 = LocalDate.parse(emailDate);
			//holdData=emailrepo.findByEmailDate(emailDate1);
		}else if(!bySubject.isEmpty()) {
			holdData=emailrepo.findBySubject(bySubject);
		}else {
			//holdData=(ArrayList<RootMailConfig>) emailService.findByEmailId(byEmailId);
			//List<RootMailConfig> emlList=emailrepo.findAll();
			ArrayList<RootMailConfig> retLst=new ArrayList();
			List<RootMailConfig> emlList1=emailrepo.findAll();
			if(!emlList1.isEmpty()) {
				for (RootMailConfig rootMailConfig : emlList1) {
					Iterable<JsonNode> jnod=rootMailConfig.getEmailTo();
					String dd=jnod.toString();
					if(dd.contains(byEmailId)) {
						retLst.add(rootMailConfig);
					}
				}
			}
		}
		return holdData;
	}
//	@Override
//	public List<RootMail> getRootMailData(Date fromdat, Date todate, String subject, Pageable pgbl) {
//		// TODO Auto-generated method stub
//		List<RootMail> retList= emailrepo.findAll(new Specification<RootMail>() {
//			
//			@Override
//			public Predicate toPredicate(Root<RootMail> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				// TODO Auto-generated method stub
//				Predicate predcate=criteriaBuilder.conjunction();
//				if(Objects.nonNull(fromdat) && Objects.nonNull(todate) && fromdat.before(todate) ) {
//					predcate=criteriaBuilder.and(predcate ,criteriaBuilder.between(root.get("creationDate"),fromdat,todate));
//				}
//				
//				if(!subject.isEmpty()) {
//					String su;
//					predcate=criteriaBuilder.and(predcate,criteriaBuilder.like(root.get("subject"), su: "%"+ subject +"%"));
//				}
//				//query.orderBy(criteriaBuilder.desc(predcate))
//			}
//		});
//		
//		return RootMailConverter.c
//	}


}
