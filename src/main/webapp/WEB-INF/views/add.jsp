<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://me2.do/5BvBFJ57">
<script src="https://code.jquery.com/jquery-3.6.3.min.js"></script>
<style>
	#attachZone {
		border: 1px solid #CCC;
		width: 99%;
		height: 100px;
	}
	
	#attachZone div {
		margin: 5px 10px;
		display: flex;
		justify-content: space-between;	
	}

</style>
</head>
<body>
	
	<!-- add.jsp -->	
	<h1>게시판 <small>파일 업로드</small></h1>
	
	
	<form method="POST" action="/file/addok" enctype="multipart/form-data">
		<table class="vertical">
			<tr>
				<th>제목</th>
				<td><input type="text" name="subject" required class="full"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea name="content" required class="full"></textarea></td>
			</tr>
			<tr>
				<th>파일</th>
				<td>
					
					 <!-- <input type="file" name="attach" class="full"> -->
					
					<!-- 
					<div><input type="file" name="attach" class="full"></div>
					<div><input type="file" name="attach" class="full"></div>
					<div><input type="file" name="attach" class="full"></div>
					 -->
					

					<!-- <div><input type="file" name="attach" class="full" multiple></div> -->
					
					<div id="attachZone"></div>
					<input type="file" name="attach" id="attach" class="full" style="display: none;">
					
				</td>
			</tr>
		</table>
		<div>
			<input type="button" value="돌아가기" onclick="location.href='/file/list';">
			<button type="submit">
				<span class="material-symbols-outlined">history_edu</span>
				쓰기
			</button>
		</div>
	</form>
	
	
	<script>
		
		//첨부 파일 선택 화면 구현
		let fileList = []; //첨부파일들
		
		$('#attachZone').on('dragenter', function(e) {
										//$(this).css('background-color', 'tomato');
										
										//기본 기능을 모두 꺼야함!
										e.preventDefault();		//원래 일어나는 일을 없었던 일로 하는 것
										e.stopPropagation();		//event 버블링 event.cancel 같은 역할
								})
								 .on('dragover', function(e) {
										//$(this).css('background-color', 'gold');
										e.preventDefault();
										e.stopPropagation();	
								 })
								 .on('dragleave', function(e) {
										//$(this).css('background-color', 'cornflowerblue');
										e.preventDefault();
										e.stopPropagation();	
								 })
								 .on('drop', function(e) {
										//$(this).css('background-color', 'yellowgreen');		
										e.preventDefault();
										
										//드래그로 던져진 파일을 얻어오는 방법
										let files = e.originalEvent.dataTransfer.files;
										console.log(files);
										
										if (files != null & files != undefined) {
											
											let temp = '';
											
											for (let i=0; i<files.length; i++) {
												let f = files[i];
												
												let filename = f.name;
												let filesize = f.size / 1024/ 1024;
												filesize = filesize < 1 ? filesize.toFixed(2) : filesize.toFixed(1);
												//toFixed를 쓰면 소수 몇자리를 지정할 수 있다 + 3자리마다 ,도 추가해줌
												
												temp += `<div>
																	<span>\${filename}</span>
																	<span>\${filesize}MB</span>
																</div>`;
												
											}	//for
											
											$(this).append(temp);
											
											//<input type="file" files=""> files라는 속성이 있음 : 여기에 우리가 알아낸 첨부파일 정보 배열을 통으로 넣을 수 있다!
											$('#attach').prop('files', files);
											
										}
										
										
								 });
	
	</script>	

</body>
</html>





















