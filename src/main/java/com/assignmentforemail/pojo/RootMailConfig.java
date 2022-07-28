package com.assignmentforemail.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

//@ToString
@Data
@Entity
@Table(name="email_data")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class RootMailConfig {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String subject;
	private String emailBody;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private JsonNode emailTo;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private JsonNode emailCC;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private JsonNode emailBCC;
	@Temporal(TemporalType.TIMESTAMP) //@CreationTimestamp
	private Date creationDate;//= new Date();
}
