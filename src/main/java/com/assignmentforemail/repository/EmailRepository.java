package com.assignmentforemail.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.assignmentforemail.pojo.RootMailConfig;

@Repository
public interface EmailRepository extends JpaRepository<RootMailConfig, Integer>,JpaSpecificationExecutor<RootMailConfig> {
	

	@Query("SELECT u FROM RootMailConfig u WHERE cast (u.creationDate as LocalDate)=:startDate")
	List<RootMailConfig> findByEmailDate(LocalDate startDate);

	@Query("SELECT u FROM RootMailConfig u WHERE " + "lower(u.subject) LIKE lower(CONCAT('%', :bySubject, '%'))")
	ArrayList<RootMailConfig> findBySubject(@Param ("bySubject") String subject);
}
