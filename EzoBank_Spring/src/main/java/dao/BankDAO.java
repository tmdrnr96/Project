package dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vo.BankVO;

@Repository
public class BankDAO {
	
	@Autowired
	SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	//아이디 중복 체크
	public BankVO selectOne(String id){
		BankVO vo = sqlSession.selectOne("bank.bank_id_check",id);	
		return vo;
	}
	
	public BankVO ssn_selectOne(String ssn) {
		BankVO vo = sqlSession.selectOne("bank.back_ssn_check",ssn);
		return vo;
	}
		
	//회원 정보 추가
	public int insert(BankVO vo) {		
		int res = sqlSession.insert("bank.bank_account_insert",vo);		
		return res;
	}
	
	//회원 아이디 찾기
	public BankVO find_id_selectOne(BankVO vo) {		
		BankVO bankvo = sqlSession.selectOne("bank.bank_find_id",vo);
		return bankvo;
	}
	
	//회원 비밀번호 찾기
	public BankVO find_pwd_selectOne(BankVO vo){	
		BankVO list = sqlSession.selectOne("bank.bank_find_pwd", vo);
		return list;
	}
	
	//회원 비밀번호 변경
	public int password_update(BankVO vo) {	
		int res = sqlSession.update("bank.bank_change_password",vo);
		return res;
	}
	
	//로그인
	public BankVO login(BankVO vo) {
		BankVO bankvo = sqlSession.selectOne("bank.bank_login",vo);
		return bankvo;
	}

	//회원정보 수정
	public int user_modified(BankVO vo) {
		int res = sqlSession.update("bank.bank_info_update",vo);		
		return res;
	}

	
	public List<BankVO> select(){
		List<BankVO> list = sqlSession.selectList("bank.bank_list");
		return list;
	}
	
	public BankVO selectone(int idx) {
		BankVO vo = sqlSession.selectOne("bank.bank_selectone_idx", idx);
		return vo;
	}
	
	public BankVO selectone(String acc) {
		BankVO vo = sqlSession.selectOne("bank.bank_selectone_acc", acc);
		return vo;
	}
	
	//이체
	//상대방 계좌에 입금
	public int update_deposit(int idx, int amount) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("idx",idx);
		map.put("amount",amount);
		int res = sqlSession.update("bank.update_deposit", map);
		return res;
	}
	//이체
	//나의 계좌에서 출금
	public int update_withdrawal(int idx, int amount) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("idx",idx);
		map.put("amount",amount);
		int res = sqlSession.update("bank.update_withdrawal", map);
		return res;
	}
	
	//상세보기	
	//bank 안의 내용 출력
		public BankVO all_selectOne(int idx){
			BankVO vo = sqlSession.selectOne("bank.bank_all",idx);
			return vo;	
		}
	
}
