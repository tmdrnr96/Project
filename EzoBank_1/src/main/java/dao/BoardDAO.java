package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vo.BoardVO;

@Repository
public class BoardDAO {
	
	@Autowired
	SqlSession sqlsession;

	//전체 게시물 조회
	public List<BoardVO> select(){
		List<BoardVO> list = sqlsession.selectList("b.board_list");
		return list;
	}
	
	//페이지별 게시물 조회
	public List<BoardVO> select(Map<String, Integer> map){
		List<BoardVO> list = sqlsession.selectList("b.board_list_condition",map);
		return list;
	}
	
	//전체 게시물 수 구하기
	public int getRowTotal(){
		int cnt = sqlsession.selectOne("b.board_count");
		return cnt;
	}
	
	//새 글 추가
	public int insert(BoardVO vo) {
		int res = sqlsession.insert("b.board_insert", vo);	
		return res;
	}
	
	//상세보기를 위한 게시물 조회
	public BoardVO selectOne(int idx) {
		BoardVO vo = sqlsession.selectOne("b.board_list_one", idx);
		return vo;
	}
	
	//조회수 증가
	public int update_readhit(int idx) {
		int res = sqlsession.update("b.board_update_readhit", idx);
		return res;
	}
	
	//댓글처리를 위한 step값 증가
	public int update_step(BoardVO vo) {
		int res = sqlsession.update("b.board_update_step", vo);
		return res;
	}
	
	//댓글 달기
	public int insert_reply(BoardVO vo) {
		int res = sqlsession.update("b.board_insert_reply", vo);
		return res;
	}
	
	public int del_update(BoardVO vo) {
		int res = sqlsession.update("b.board_del_update", vo);
		return res;
	}
	
	public int update(BoardVO vo) {
		int res = sqlsession.insert("b.board_update", vo);
    	return res;
	}

}
