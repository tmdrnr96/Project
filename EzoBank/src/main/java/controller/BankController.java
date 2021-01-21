package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.SessionScope;

import common.Common;
import common.MyCommon;
import common.Paging;
import dao.BankDAO;
import dao.DetailDAO;
import vo.BankVO;
import vo.DetailVO;


@Controller
public class BankController {
	
	@Autowired
	BankDAO bdao; //bank table DAO
	
	@Autowired
	DetailDAO detaildao; //detail table DAO
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpSession session;
	
	//입금 페이지
	//로그인을 완료했으면 idx를 파라미터로 반드시 받아야 함
	//로그인을 하지 않고 클릭하면 로그인 페이지로 이동해야 함
	@RequestMapping("/deposit_page.do")
	public String deposit_page(Model model,int idx) {
		
		BankVO vo = bdao.selectone(idx);
		model.addAttribute("vo",vo);
		return MyCommon.Deposit.VIEW_PATH + "deposit_page.jsp";
	}
	
	//출금 페이지
	//로그인을 완료했으면 idx를 파라미터로 반드시 받아야 함
	//로그인을 하지 않고 클릭하면 로그인 페이지로 이동해야 함
	@RequestMapping("/withdrawal_page.do")
	public String withdrawal_page(Model model,int idx) {
		BankVO vo = bdao.selectone(idx);
		model.addAttribute("vo",vo);
		return MyCommon.Withdrawal.VIEW_PATH + "withdrawal_page.jsp";
	}
	
	//입출금 성공 Ajax
	@RequestMapping("/depositandwithdraw.do")
	@ResponseBody
	public String depositandwithdraw(int idx,int amount,int check, String account, int balance ) {

		//detailVO에 저장..
		DetailVO detailvo = new DetailVO();
		
		detailvo.setAccount(account);
		detailvo.setDet(1);
		
		int res = 0;
		String result = "";
		
		if(check==1) { // 이 부분에서 accountIfo table에 data넣기(계좌 상세보기, 의미X)
			//입금 mapper
			res = bdao.update_deposit(idx, amount);
			
			detailvo.setContent("입금");
			detailvo.setDeposit(amount);
			detailvo.setWithdraw(0);
			detailvo.setBalance(balance + amount);
		}
		else{
			//출금 mapper
			res = bdao.update_withdrawal(idx, amount);
			
			//거래내역에 쓰일 DB 추가
			detailvo.setContent("출금");
			detailvo.setDeposit(0);
			detailvo.setWithdraw(amount);	
			detailvo.setBalance(balance - amount);
		}
		
		if(res>0) {
			result = "success";

			//입출금이 성공시 추가한 detailvo를 DetailDAO로 전달	
			detaildao.insert_detail(detailvo);
			
		}
		
		return result;
	}
	
	//이체 페이지
	//로그인을 완료했으면 idx를 파라미터로 반드시 받아야 함
	//로그인을 하지 않고 클릭하면 로그인 페이지로 이동해야 함
	@RequestMapping("/transfer_page.do")
	public String transfer_page(int idx, Model model) {
		BankVO vo = bdao.selectone(idx);
		
		//전체 회원 list 가져오기
		List<BankVO> list = bdao.select();
		
		//전체 회원의 계좌를 저장하기 위한 list
		List<String> account = new ArrayList<String>();
		
		//전체 회원의 계좌 저장하기
		for(int i=0;i<list.size();i++) {
			account.add(list.get(i).getAccount());
		} 
		
		model.addAttribute("vo",vo);
		model.addAttribute("account",account);
		
		return MyCommon.Transfer.VIEW_PATH + "transfer_page.jsp";
	}
	
	//이체 페이지의 Ajax
	@RequestMapping("/transfer_account.do")
	@ResponseBody
	public String transfer_account(int idx,String acc) {
		String result = "";
			
		BankVO vo = bdao.selectone(acc); //입력받은 계좌 vo받기
		
		if(vo==null) { // 입력한 계좌가 존재하지 않다면 null을 return
			return null;
		}
		
		int idxYou = vo.getIdx();
		String name = vo.getName();
		//이체를 위한 return
		result = String.format("[{'idxMe':'%d','idxYou':'%d','name':'%s'}]", idx, idxYou,name);
		
		
		return result;
	}
	
	//이체하기를 완료하기 위한 idxMe : 내 idx , idxYou : 이체하기 위한 상대방 idx
	@RequestMapping("/transfer_money_page.do")
	public String transfer_money_page(Model model, int idxMe, int idxYou) {
		BankVO voMe = bdao.selectone(idxMe);
		model.addAttribute("voMe",voMe);
		
		//거래내역에 쓰일 DB 추가
		
		BankVO voYou = bdao.selectone(idxYou);
		model.addAttribute("voYou",voYou);
		
		return MyCommon.Transfer.VIEW_PATH + "transfer_money_page.jsp";
	}
	
	//이체 페이지의 Ajax
	@RequestMapping("/transfer_amount.do")
	@ResponseBody
	public String transfer_amount(int idxMe, int idxYou, int amount) {
		int res = 0;
		
		res = bdao.update_deposit(idxYou, amount); // 상대방 계좌 입금
		res = bdao.update_withdrawal(idxMe, amount); // 내 계좌 출금
		
		String result = "";
		
		if(res>0) {
			result = "success";
		}
		
		return result;
	}
	
	//거래내역(상세보기)

	@Autowired
	DetailDAO detail_dao;
		
	@RequestMapping("/aac_view.do")
	public String bank_view(int idx, Model model) {

		BankVO vo = bdao.all_selectOne(idx);
		
		if (vo != null) {
			//idx에 해당되는 객체를 정상적으로 가져온 경우
			model.addAttribute("vo",vo);
		}
	
		if (vo==null) {			
			System.out.println("정보 없음");
		}
	
		return MyCommon.Detail.VIEW_PATH + "bank_view.jsp";
	}
	
	
	//거래내역 상세보기
	@RequestMapping("/det.do")
	public String bank_detail(Model model, String account, Integer page, Integer term) {
		//세션에서 값가져오기		
		BankVO bankvo = (BankVO)session.getAttribute("user");
		
		int nowPage = 1; //기본페이지는 1페이지로
		
		if (page != null) {
			nowPage = page;
		}
		
		//한 페이지에 표시될 게시물의 시작과 끝번호를 계산
		int start = (nowPage - 1)*Common.BoardView.BLOCKLIST + 1;
		int end = start + Common.BoardView.BLOCKLIST - 1;
		
		DetailVO vo = new DetailVO();		
		vo.setStart(start);
		vo.setEnd(end);
		//session에 저장되어 있는 Account값 VO에 저장!
		vo.setAccount(bankvo.getAccount());
		
		//전체 게시물의 갯수
		int row_total = detail_dao.getRowTotal(bankvo.getAccount());
		
		System.out.println(row_total);
		System.out.println(nowPage);
		
		//페이지 하단 메뉴 생성하기
		String pageMenu = Paging.getPaging(
				"det.do", nowPage, row_total, 
				Common.BoardView.BLOCKLIST, /* 5 */
				Common.BoardView.BLOCKPAGE); /* 3 */

		model.addAttribute("pageMenu", pageMenu);

		// term : 조회 기간 
		List<DetailVO> list = null;
		Date today = new Date();
		
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat mon = new SimpleDateFormat("MM");
		SimpleDateFormat yy = new SimpleDateFormat("yyyy");
		SimpleDateFormat dd = new SimpleDateFormat("dd");
		
		
		int month = Integer.parseInt(mon.format(today));
		int year = Integer.parseInt(yy.format(today));
		int day = Integer.parseInt(dd.format(today));
		
		int re_year = 0;
		int re_mon;
		
		String re_date;		
	    String re_today = date.format(today);
	   
	    if(term == null) {
	    	term = 0;
	    }
	    
		if (term == 0) {
			list = detail_dao.selectList(vo);
			
			//1개월
		} else if( term == 10){
			
			if(month == 01) { 
				re_mon = 12;
				re_year = year-1;
			
			}else {
			
				re_mon = month - 1;
				re_year = year;
			}

			re_date = re_mon +"/"+ day +"/"+ re_year;
			
			list = detail_dao.selectTerm(re_date);

			//3개월 			
		} else {
			if (month == 01) {
				re_mon = 10;
				re_year = year-1;
			}else {
				re_mon = month - 3;
			}
			
			re_date = re_mon+"/"+day+"/"+re_year;
					
			list = detail_dao.selectTerm(re_date);

		}
		
		
		model.addAttribute("list", list);
		
		return MyCommon.Detail.VIEW_PATH+ "bank_detail.jsp";
			
	}

	/*
	 * @RequestMapping("/detail_view.do") public String detail_view(Model model,
	 * String account, int term,Integer page ) {
	 * 
	 * int nowPage = 1; //기본페이지는 1페이지로
	 * 
	 * if (page != null) { nowPage = page; }
	 * 
	 * //한 페이지에 표시될 게시물의 시작과 끝번호를 계산 int start = (nowPage -
	 * 1)*Common.BoardView.BLOCKLIST +1; int end = start +
	 * Common.BoardView.BLOCKLIST -1;
	 * 
	 * DetailVO vo = new DetailVO();
	 * 
	 * vo.setStart(start); vo.setEnd(end); vo.setAccount(account);
	 * 
	 * List<DetailVO> list = detail_dao.selectList(vo);
	 * 
	 * //전체 게시물의 갯수 int row_total = detail_dao.getRowTotal(account);
	 * 
	 * String pageMenu = Paging.getPaging( "detail_view.do", nowPage, row_total,
	 * Common.BoardView.BLOCKLIST, Common.BoardView.BLOCKPAGE );
	 * 
	 * model.addAttribute("list", list); model.addAttribute("pageMenu", pageMenu);
	 * //여기서는 페이지 관련된 처리를 전혀 안하고 있어서 그렇습니다~ //여기서 페이지 메뉴 만들면 되겠네요~ 위에 det.do처럼요!! 네
	 * 알겠습니다 //넵 화이팅! //List<DetailVO>s_list=null;
	 * 
	 * return MyCommon.Detail.VIEW_PATH + "bank_detail.jsp"; }
	 */
	
	
	

}
