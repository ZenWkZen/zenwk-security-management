package com.alineumsoft.zenwk.security.person.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ResponseUser
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagePersonDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<PersonDTO> people;

	private long totalElements;

	private int totalPages;

	private int currentPage;
}
