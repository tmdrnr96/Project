package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.BankDAO;
import vo.BankVO;

@Controller
public class Bank_User_Controller {
	
	@Autowired
	BankDAO bank_dao;

	@Autowired
	HttpSession session;
	
	public static final String WEB_PATH = "/WEB-INF/views/bank_user/";
	
	//회원 정보 수정 폼으로 이동
	@RequestMapping("/user_info.do")
	public String user_info_update(Model model) {
		
		//로그인을 통해 session에 저장된 값인 user를 불러온다. 
		//회원 정보 수정에 필요한 정보를 session에서 꺼내서 
		//회원 수정폼에 맞게 변수에 값을 저장
		BankVO vo = (BankVO) session.getAttribute("user");
		
		//주민번호
		String ssn1 = vo.getSsn().substring(0,6); 
		String ssn2 = vo.getSsn().substring(7,14); 
		
		model.addAttribute("ssn1",ssn1);
		model.addAttribute("ssn2",ssn2);
		
		//전화 번호
		String tel1 = vo.getTel().substring(0,3);
		String tel2 = vo.getTel().substring(4,8);
		String tel3 = vo.getTel().substring(9,13);
		
		model.addAttribute("tel1", tel1);
		model.addAttribute("tel2", tel2);
		model.addAttribute("tel3", tel3);
		
		//이메일
		String email1 = vo.getEmail().substring(0,vo.getEmail().indexOf('@'));
		String email2 = vo.getEmail().substring(vo.getEmail().indexOf('@')+1,vo.getEmail().length());
		
		model.addAttribute("email1",email1);
		model.addAttribute("email2",email2);
		
		//계좌번호
		String account1 = vo.getAccount().substring(0,3);
		String account2 = vo.getAccount().substring(4,6);
		String account3 = vo.getAccount().substring(7,12);
		
		model.addAttribute("account1",account1);
		model.addAttribute("account2",account2);
		model.addAttribute("account3",account3);
		
		//주소
		String addr1 = vo.getAddr().substring(0,vo.getAddr().indexOf('/'));
		String addr2 = vo.getAddr().substring(vo.getAddr().indexOf('/')+1,vo.getAddr().length()); 
		
		model.addAttribute("addr1",addr1);
		model.addAttribute("addr2",addr2);
		
		return WEB_PATH + "bank_user_update.jsp";
	}
	
	//회원 정보 수정
	@RequestMapping("/user_modified.do")
	@ResponseBody
	public String user_modified(BankVO vo, String ssn, String account, String tel, String email) {
		
		//수정 폼에 입력된 값들을 해당하는 유저 DB를 수정한다.
		vo.setSsn(ssn);
		vo.setAccount(account);
		vo.setTel(tel);
		vo.setEmail(email);

		int res = bank_dao.user_modified(vo);
		
		String result = "no";
		
		if(res > 0) {
			
			result = "yes";
		}
		
		return result;
	}
	
	//회사 소개 페이지로 이동
	@RequestMapping("/bank_introduce.do")
	public String bank_introduce() {
		
		return "/WEB-INF/views/bank_introduce/bank_introduce_form.jsp";
	}
	
}
