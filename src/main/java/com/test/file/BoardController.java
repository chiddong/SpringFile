package com.test.file;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class BoardController {
	
	@Autowired
	private BoardDAO dao;

	
	//목록보기 > jsp에 넘겨줄게 있으니 model 들어가야함
	@GetMapping(value="/list")
	public String list(Model model) {
		
		List<BoardDTO> list = dao.list();
		
		for (BoardDTO dto : list) {
			List<FileDTO> flist = dao.flist(dto.getSeq());
			dto.setFiles(flist);
		}
		
		model.addAttribute("list", list);
		
		return "list";
	}
	
	
	@GetMapping(value="/add")
	public String add() {
		
		return "add";
	}
	
	//한개의 파일 업로드
//	@PostMapping(value="/addok")
//	public String addok(HttpServletRequest req) {
//		
//		//우리가 쓰던 MultipartRequest와 동일
//		MultipartHttpServletRequest multi = (MultipartHttpServletRequest)req;
//		
//		String subject = multi.getParameter("subject");
//		String content = multi.getParameter("content");
//		
//		MultipartFile attach = multi.getFile("attach");
//		
//		//System.out.println(attach.getName());
//		//System.out.println(attach.getOriginalFilename());
//		//System.out.println(attach.getContentType());
//		//System.out.println(attach.getSize());
//		
//		//파일 업로드 완료 > 파일 어디 있는지? > 임시 폴더에 저장 > 우리가 원하는 폴더로 이동
//		String filename = attach.getOriginalFilename();
//		String path = req.getRealPath("resources/files");
//		
//		System.out.println(path);
//		
//		//파일명 중복 방지
//		filename = getFileName(path, filename);
//		
//		//파일 이동
//		File file2 = new File(path + "\\" + filename);
//		
//		try {
//			
//			//file.renameTo(file2)
//			attach.transferTo(file2);
//			
//		} catch (Exception e) {
//		}
//		
//		return "redirect:/list";
//	}
	
	//여러개 파일 업로드
	@PostMapping(value="/addok")
	public String addok(HttpServletRequest req) {
		
		BoardDTO dto = new BoardDTO();
		
		MultipartHttpServletRequest multi = (MultipartHttpServletRequest)req;
		
		String subject = multi.getParameter("subject");
		String content = multi.getParameter("content");
		
		dto.setSubject(subject);
		dto.setContent(content);
		
		int result = dao.add(dto);
		
		//동일한 name을 가진 애들을 getFiles로 받는다.
		List<MultipartFile> files = multi.getFiles("attach");
		String path = req.getRealPath("resources/files");
		
		System.out.println(path);
		
		for(MultipartFile file : files) {
			
			//System.out.println(file.isEmpty());
			
			if(!file.isEmpty()) {
				
				String filename = getFileName(path, file.getOriginalFilename());
				
				try {
					
					file.transferTo(new File(path + "\\" + filename));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//첨부파일 정보 insert (file이 있을때만 해야하니까!)
				FileDTO fdto = new FileDTO();
				
				fdto.setFilename(filename);	//파일명
				fdto.setFilesize(file.getSize() + "");
				fdto.setMimetype(file.getContentType());
				
				dao.addFile(fdto);
				
			}
		}
		
		
		return "redirect:/list";
		
	} //addok
	
	
	
	private String getFileName(String path, String filename) {

		//저장 폴더내의 파일명을 중복되지 않게 만들기
		//path = "resource/files"
		//filename = "test.txt"
		
		//덮어쓰기 할때 오류가 나니까 이렇게 이름이 바뀌게 해주자.
		//test.txt > test(1).txt > test(2).txt
		int n = 1; //인덱스 숫자
		int index = filename.lastIndexOf("."); //확장자 위치
		
		String tempName = filename.substring(0, index);	//"test"
		String tempExt = filename.substring(index);			//".txt"
		
		while (true) {
			
			File file = new File(path + "\\" + filename);
			
			if(file.exists()) {
				//있다. > 중복 > 파일명 변경
				
				filename = String.format("%s(%d)%s", tempName, n, tempExt);
				n++;
				
			} else {
				//없다. > 사용 가능한 파일명
				return filename;
			}
			
		}
				
	}


	@GetMapping(value="/view")
	public String view(Model model, String seq) {
		
		BoardDTO dto = dao.get(seq);
		
		dto.setFiles(dao.flist(seq));
		
		model.addAttribute("dto", dto);
		
		return "view";
	}
	
	
}




















