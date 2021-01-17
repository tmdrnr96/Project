package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.MyCommon;
import common.Paging;
import dao.BoardDAO;
import vo.BoardVO;

@Controller
public class BoardController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	BoardDAO dao;

	//게시글 전체목록 조회
	@RequestMapping("/board_list.do")
	public String list(Model model,Integer page) { // Integer page : 파라미터 받을 때 null인지 확인하기 위해 Integer로 파라미터를 받는다
		int nowPage = 1; //기본페이지 : 1페이지
		
		if(page!=null) {
			nowPage = page;
		}
		
		//한 페이지에 표시 될 게시물의 시작과 끝번호를 계산
		int start = (nowPage - 1) * MyCommon.Board.BLOCKLIST + 1;
		int end = start + MyCommon.Board.BLOCKLIST - 1;
		
		//start와 end를 Map에 저장
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("end", end);
		
		List<BoardVO> list = dao.select(map);
		
		//전체 게시물의 갯수
		int row_total = dao.getRowTotal();
		
		//페이지 하단 메뉴 생성하기
		String pageMenu = Paging.getPaging("board_list.do", nowPage, row_total, MyCommon.Board.BLOCKLIST, MyCommon.Board.BLOCKPAGE);
		
		model.addAttribute("list", list);
		model.addAttribute("pageMenu", pageMenu);
		
		//이렇게 request로도  저장 가능
		//request.setAttribute("list", list);
		//request.setAttribute("pageMenu", pageMenu);
		
		//조회수 증가를 위해 세션에 저장해뒀던 데이터를 삭제
		request.getSession().removeAttribute("show");
		
		return MyCommon.Board.VIEW_PATH+"board_list.jsp";
	}
	
	// 게시글 상세보기
	@RequestMapping("/view.do")
	public String view(Model model,int idx,Integer page) {
		
		String content = "";
		
		//idx에 해당하는 게시글 정보 한 건 가져오기(vo로 받기)
		BoardVO vo = dao.selectOne(idx);
		
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
		
		return MyCommon.Board.VIEW_PATH+"board_view.jsp";
	}
	
	//새 글 작성 폼으로 이동
	@RequestMapping("/insert_form.do")
	public String insert_form() {
		return MyCommon.Board.VIEW_PATH+"board_write.jsp";
	}
	
	// 새 글 작성
	@RequestMapping("/insert1.do")
	public String insert(Model model,BoardVO vo) {
		String ip = request.getRemoteAddr();
		vo.setIp(ip);
		
		int res = dao.insert(vo);
		
		return "redirect:board_list.do";
	}
	
	//댓글창으로 이동
	@RequestMapping("/reply_form.do")
	public String reply_form(int idx,Integer page) {
		return MyCommon.Board.VIEW_PATH+"board_reply.jsp";
	}
	
	//댓글 달기
	@RequestMapping("/reply.do")
	public String reply(Model model, BoardVO vo, Integer page) {
		String ip = request.getRemoteAddr();

		//댓글을 달고 싶은 idx에 해당하는 원본 게시글의 정보를 얻어오기
		BoardVO Bvo = dao.selectOne(vo.getIdx());
		
		//기준글의 step보다 큰 값은 step+1처리
		int res = dao.update_step(vo);

		vo.setIp(ip);
		vo.setRef(Bvo.getRef());
		vo.setStep(Bvo.getStep()+1);
		vo.setDepth(Bvo.getDepth()+1);
		
		//댓글 등록
		res = dao.insert_reply(vo);
		
		return "redirect:board_list.do?page="+page; // == response.sendRedirect("list.do?page="+page)
	}
	
	//글 삭제
	@RequestMapping("/del.do")
	@ResponseBody
	public String del(int idx) {
		//삭제를 위한 게시글의 정보를 얻어온다
		BoardVO b_vo = dao.selectOne(idx);
		b_vo.setSubject("이미 삭제 된 게시글 입니다");
		b_vo.setName("unknown");
		
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
	@RequestMapping("/board_update.do")
	public String board_update(Model model,int idx) {
		BoardVO vo = dao.selectOne(idx);
		model.addAttribute("vo",vo);
		return MyCommon.Board.VIEW_PATH+"board_modify.jsp"; 
	}
	
	//글 수정
	@RequestMapping("/update.do")
	public String update(BoardVO vo, Integer page) {
		String ip = request.getRemoteAddr();
		vo.setIp(ip);
		int res = dao.update(vo);
		
		return "board_list.do?page="+page;
	}
	
}
