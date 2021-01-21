package controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.BackingStoreException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.BankDAO;
import vo.BankVO;

@Controller
public class Bank_login_Controller {
	
	@Autowired
	BankDAO bank_dao;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpSession session;

	
	public static final String WEB_PATH = "/WEB-INF/views/bank_login/";
	
	
	//로그인 페이지
	@RequestMapping("/login.do")
	public String login() {
		
		return WEB_PATH + "bank_login_form.jsp";
	}
	
	//회원가입 폼으로 이동
	@RequestMapping("/join_form.do")
	public String join_form(Model model){
	
	//회원 가입시 동의 사항 텍스트 파일을 가져온다.  
	//resource 폴더 안에 있는 text/agree_text.txt의 위치
	ClassPathResource resource = new ClassPathResource("text/agree_text.txt");
	
	try {
		//text/agree_text.txt의 위치를 path에 저장
		Path path = Paths.get(resource.getURI());
		//Files.readAllLines : paht에 존재하는 파일의 내용을 읽어 list로 반환
		List<String> content = Files.readAllLines(path);
		model.addAttribute("content",content);
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
		return WEB_PATH + "bank_join_form.jsp";
	}
	
	//아이디 중복 확인
	@RequestMapping("/id_check.do")
	@ResponseBody
	public String id_check(String id) {
		
		BankVO bankvo = bank_dao.selectOne(id);
		
		String res = "no";
		
		if(bankvo == null) {
			res = "yes";
		}		
		return res;	
	}
	
	//주민번호 중복 확인
	@RequestMapping("/ssn_check.do")
	@ResponseBody
	public String ssn_check(String ssn) {
		
		BankVO bankvo = bank_dao.ssn_selectOne(ssn);
		
		String res = "no";
		
		if(bankvo == null) {
			res = "yes";
		}
		return res;
		
	}
	
	//회원 가입
	@RequestMapping("/insert.do")
	@ResponseBody
	public String member_insert(BankVO vo, String ssn, String tel, String email) {
		
		vo.setSsn(ssn);
		vo.setTel(tel);
		vo.setEmail(email);
			
		//vo에 ip 등록
		String ip = request.getRemoteAddr();		
		vo.setIp(ip);
		
		//vo에 계좌 생성 및 등록		
		 String str = String.valueOf(System.currentTimeMillis());
		
		 String account1 = str.substring(0,3);
		 String account2 = str.substring(4,6);
		 String account3 = str.substring(7,12);
		 
		 String account = account1 + "-" + account2 + "-" + account3; 		
		 vo.setAccount(account);
		 
		 int res = bank_dao.insert(vo);

		 String result = "no";

		if(res != 0) {
			result = "yes";
		} 
		
		return result;		
	}
	
	//아이디 찾기 폼으로 이동
	@RequestMapping("/find_id_form.do")
	public String find_form_id() {
		
		return WEB_PATH + "bank_find_id_form.jsp";
	}
	
	//아이디 찾기
	@RequestMapping("/find_id.do")
	@ResponseBody
	public String find_id(BankVO vo, String name, String ssn) {
		
		vo.setName(name);
		vo.setSsn(ssn);
		
		BankVO bankvo = bank_dao.find_id_selectOne(vo);
		
		String result = "no";
		
		if(bankvo != null) {
			result = bankvo.getId();
		}
		
		return result;
	}
	
	//비밀번호 찾기 폼으로 이동
	@RequestMapping("/find_pwd_form.do")
	public String find_form_pwd() {
		return WEB_PATH + "bank_find_pwd_form.jsp";
	} 
	
	//비밀번호 찾기
	@RequestMapping("/find_pwd.do")
	@ResponseBody
	public String find_pwd(Model model, BankVO vo, String name, String id, String ssn) {
		
		vo.setName(name);
		vo.setId(id);
		vo.setSsn(ssn);
		
		BankVO bankvo = bank_dao.find_pwd_selectOne(vo);
		
		String result = "no";
		
		//DB검색 결과 해당 정보에 대한 조회가 있다면 
		if(bankvo != null) {
			result = "yes";			
		}					
		return result;
				
	}
	
	//새로운 비밀번호 설정 폼으로 이동
	@RequestMapping("/bank_new_pwd.do")
	public String bank_new_pwd(Model model, String ssn) {
		
		model.addAttribute("ssn",ssn);
		
		return WEB_PATH + "bank_new_pwd.jsp";
	}
	
	//새로운 비밀번호 설정
	@RequestMapping("/change_password.do")
	@ResponseBody
	public String change_password(BankVO vo, String ssn, String pwd) {
				
		vo.setSsn(ssn);
		vo.setPwd(pwd);
				
		int res = bank_dao.password_update(vo);
		
		String result = "no";
		
		if(res > 0) {
			result = "yes";
		}		
		return result;
	}
		
	//로그인 하기!
	@RequestMapping("/sign_up.do")
	@ResponseBody
	public String sign_up(Model model, BankVO vo) {
		
		String param = "";
		String res = "";

		BankVO bankvo = bank_dao.login(vo);	

		//DB검색 결과 해당 ID가 없는 경우
		if(bankvo == null){
			param = "no_id";
			res = String.format("[{'param':'%s'}]",param);
			return res;
		}
		//DB검색 결과 해당 ID는 있지만 PWD가 없는 경우
		if(!bankvo.getPwd().equals(vo.getPwd())) {
			param = "no_pwd";
			res = String.format("[{'param':'%s'}]",param);
			return res;
		}
		
		//DB검색 결과 해당 ID와 PWD가 존재하는 경우	
		//1시간 동안 세션 저장!(로그인 상태 유지..)
		session = request.getSession();
		session.setAttribute("user",bankvo);
		session.setMaxInactiveInterval(60*60);

		param = "clear";
		res = String.format("[{'param':'%s'}]",param);
		return res;
	}
	
	
	//로그아웃
	@RequestMapping("/logout.do")
	public String logout() {
		//저장된 세션 지우기
		session.removeAttribute("user");		
		return "redirect:login.do";
	}
	
	//메인 화면
	@RequestMapping(value={"/","/main.do"})
	public String main() {		
		return "/WEB-INF/views/main/main.jsp";
	}

}

