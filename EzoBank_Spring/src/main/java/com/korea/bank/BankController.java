package com.korea.bank;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import common.Common;
import dao.BankDAO;
import dao.DetailDAO;
import vo.BankVO;
import vo.DetailVO;


@Controller
public class BankController {


	BankDAO bank_dao;
	DetailDAO detail_dao;
	
	@Autowired
	ServletContext application;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest request;
	
	public void setBank_dao(BankDAO bank_dao) {
		this.bank_dao = bank_dao;
	}
	
	
	
	//
	
	/*
	 * @RequestMapping(value= {"/","ex.do"}) public String ex(Model model) {
	 * 
	 * List<BankVO> list = bank_dao.selectList(); model.addAttribute("list", list);
	 * return Common.VIEW_PATH + "ex.jsp"; }
	 */
	
	
	@RequestMapping("view.do")
	public String bank_view(String id, Model model) {

		String str = id.substring(0, id.indexOf(','));
		BankVO vo = bank_dao.selectOne(str);
		System.out.println("id:"+str);
		//왜 여기서는 id가 aa, aa로 두 개가 넘어올까요...
		
		
		if (vo != null) {
			//idx에 해당되는 객체를 정상적으로 가져온 경우
			model.addAttribute("vo",vo);
		}
	
		if (vo==null) {
			
			System.out.println("정보 없음");
		}
		
		return Common.VIEW_PATH + "bank_view.jsp";
	}
	
	
	/*
	 * @RequestMapping("det.do") public String bank_detail(Model model) {
	 * List<BankVO> list = bank_dao.selectList(); model.addAttribute("list", list);
	 * return Common.VIEW_PATH + "bank_detail.jsp"; }
	 */
	
	
	/*
	 * @RequestMapping("detail_view.do") public String detail_view(Model model, int
	 * detail) {
	 * 
	 * //사원목록 조회 List<DetailVO> list=null;
	 * 
	 * if (detail == 0) { list = detail_dao.selectView(); } else { list =
	 * detail_dao.selectView(detail); }
	 * 
	 * 
	 * model.addAttribute("list", list);
	 * 
	 * return Common.VIEW_PATH + "bank_detail.jsp"; }
	 */
	
	
	
	
	
}
