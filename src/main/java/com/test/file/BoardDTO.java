package com.test.file;

import java.util.List;

import lombok.Data;

@Data
public class BoardDTO {

	private String seq;
	private String subject;
	private String content;
	private String regdate;
	
	//여러개의 첨부파일을 넣을 예정
	//private String attach;
	private List<FileDTO> files;
	
}
