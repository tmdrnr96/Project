package controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import common.MyCommon;
import common.Paging;
import dao.NoticeDAO;
import vo.NoticeVO;

@Controller
public class NoticeController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	NoticeDAO dao;
	
	@Autowired
	ServletContext app;

	//게시글 전체목록 조회
	@RequestMapping("/notice_list.do")
	public String list(Model model,Integer page) { // Integer page : 파라미터 받을 때 null인지 확인하기 위해 Integer로 파라미터를 받는다
		int nowPage = 1; //기본페이지 : 1페이지
		
		if(page!=null) {
			nowPage = page;
		}
		
		//한 페이지에 표시 될 게시물의 시작과 끝번호를 계산
		int start = (nowPage - 1) * MyCommon.Notice.BLOCKLIST + 1;
		int end = start + MyCommon.Notice.BLOCKLIST - 1;
		
		//start와 end를 Map에 저장
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("end", end);
		
		List<NoticeVO> list = dao.select(map);
		
		//전체 게시물의 갯수
		int row_total = dao.getRowTotal();
		
		//페이지 하단 메뉴 생성하기
		String pageMenu = Paging.getPaging("notice_list.do", nowPage, row_total, MyCommon.Notice.BLOCKLIST, MyCommon.Notice.BLOCKPAGE);
		
		model.addAttribute("list", list);
		model.addAttribute("pageMenu", pageMenu);
		
		//이렇게 request로도  저장 가능
		//request.setAttribute("list", list);
		//request.setAttribute("pageMenu", pageMenu);
		
		//조회수 증가를 위해 세션에 저장해뒀던 데이터를 삭제
		request.getSession().removeAttribute("show");
		
		return MyCommon.Notice.VIEW_PATH+"notice_list.jsp";
	}
	
	// 게시글 상세보기
	@RequestMapping("/notice_view.do")
	public String view(Model model,int idx,Integer page) {
		
		String content = "";
		
		//idx에 해당하는 게시글 정보 한 건 가져오기(vo로 받기)
		NoticeVO vo = dao.selectOne(idx);
		
		//세션에 저장된 값이 있는지 조회
		//글 상세보기 페이지에서 새로고침 할 때마다 조회수가 증가하는 것을 막기 위해 session을 이용
		HttpSession session = request.getSession();
		String show = (String)session.getAttribute("show");
		
		if(show == null) {
			//idx에 해당하는 게시글의 조회수를 증가
			dao.update_readhit(idx); // 글 상세보기 페이지에서 새로고침 할 때마다 조회수가 증가하는 문제점 존재
			session.setAttribute("show", "조회수 문제점 방지");
			
		}
		//하지만 다른 글의 조회수가 증가하지 않는 문제점이 발생한다 그러므로 조회수 1번 늘렸으면 session에 있는 값을 제거해야한다
		model.addAttribute("vo", vo);
		
		return MyCommon.Notice.VIEW_PATH+"notice_view.jsp";
	}
	
	//새 글 작성 폼으로 이동
	@RequestMapping("/notice_insert_form.do")
	public String insert_form() {
		return MyCommon.Notice.VIEW_PATH+"notice_write.jsp";
	}
	
	// 새 글 작성
	@RequestMapping("/notice_insert.do")
	public String insert(Model model,NoticeVO vo) {
		//업로드 될 파일의 정보를 가져오기 및 경로잡기 
		String webpath = "/resources/upload/";
		String path = app.getRealPath(webpath);
		System.out.println(path);
				
		//업로드 된 파일의 정보
		MultipartFile photo = vo.getPhoto();
		String filename="";
				
		//업로드 된 파일이 실제로 존재하는지 확인
		if(!photo.isEmpty()) {
		    //방명록에 사진을 등록할 예정
			filename = photo.getOriginalFilename();
					
			//저장할 파일의 경로잡기
			File filepath = new File(path,filename); // 이렇게 만들면 이미지가 아닌 폴더가 만들어진다
					
			if(!filepath.exists()) {
				filepath.mkdirs(); //없는 폴더를 생성
			}
			else {
				//동일 파일명 방지
				long time = System.currentTimeMillis();
				filename = String.format("%d_%s", time,filename);
				filepath = new File(path,filename);
			}
					
			try {
				//MultipartResolver가 임시저장소에 넣어둔 파일을 내가 지정한 경로로 복사
				photo.transferTo(filepath);
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
				
		String ip = request.getRemoteAddr();
		vo.setIp(ip);
		vo.setFilename(filename);
		
		
		int res = dao.insert(vo);
		
		return "redirect:notice_list.do";
	}
	
	
	//글 삭제
	@RequestMapping("/notice_del.do")
	@ResponseBody
	public String del(int idx) {
		//삭제를 위한 게시글의 정보를 얻어온다
		NoticeVO b_vo = dao.selectOne(idx);
		b_vo.setSubject("이미 삭제 된 게시글 입니다");
		
		//게시글이 삭제가 된 것처럼 업데이트
		int res = dao.del_update(b_vo);
		String result = "no";
		if(res>0) {
			result = "yes";
		}
		
		//@ResponseBody를 통해 Ajax로 전달이 된다
		return result;
	}
	
	//글 수정 폼으로 이동
	@RequestMapping("/notice_modify.do")
	public String board_update(Model model,int idx) {
		NoticeVO vo = dao.selectOne(idx);
		model.addAttribute("vo",vo);
		return MyCommon.Notice.VIEW_PATH+"notice_modify.jsp"; 
	}
	
	//글 수정
	@RequestMapping("/notice_update.do")
	public String update(NoticeVO vo, Integer page) {
		if(vo.getPhoto()!=null&&!vo.getPhoto().isEmpty()) {
			//업로드 될 파일의 정보를 가져오기 및 경로잡기 
			String webpath = "/resources/upload/";
			String path = app.getRealPath(webpath);
			System.out.println(path);
			
			//업로드 된 파일의 정보
			MultipartFile photo = vo.getPhoto();
			String filename="";
			
			//업로드 된 파일이 실제로 존재하는지 확인
			if(!photo.isEmpty()) {
				//방명록에 사진을 등록할 예정
				filename = photo.getOriginalFilename();
				
				//저장할 파일의 경로잡기
				File filepath = new File(path,filename); // 이렇게 만들면 이미지가 아닌 폴더가 만들어진다
				
				if(!filepath.exists()) {
					filepath.mkdirs(); //없는 폴더를 생성
				}
				else {
					//동일 파일명 방지
					long time = System.currentTimeMillis();
					filename = String.format("%d_%s", time,filename);
					filepath = new File(path,filename);
				}
				
				try {
					//MultipartResolver가 임시저장소에 넣어둔 파일을 내가 지정한 경로로 복사
					photo.transferTo(filepath);
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			vo.setFilename(filename);
		}
		
		String ip = request.getRemoteAddr();
		vo.setIp(ip);
		
		int res = dao.update(vo);
		
		return "notice_list.do?page="+page;
	}
	
}
