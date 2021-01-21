package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vo.NoticeVO;


@Repository
public class NoticeDAO {
	
	@Autowired
	SqlSession sqlsession;

	//전체 게시물 조회
	public List<NoticeVO> select(){
		List<NoticeVO> list = sqlsession.selectList("n.notice_list");
		return list;
	}
	
	//페이지별 게시물 조회
	public List<NoticeVO> select(Map<String, Integer> map){
		List<NoticeVO> list = sqlsession.selectList("n.notice_list_condition",map);
		return list;
	}
	
	//전체 게시물 수 구하기
	public int getRowTotal(){
		int cnt = sqlsession.selectOne("n.notice_count");
		return cnt;
	}
	
	//새 글 추가
	public int insert(NoticeVO vo) {
		int res = sqlsession.insert("n.notice_insert", vo);	
		return res;
	}
	
	//상세보기를 위한 게시물 조회
	public NoticeVO selectOne(int idx) {
		NoticeVO vo = sqlsession.selectOne("n.notice_list_one", idx);
		return vo;
	}
	
	//조회수 증가
	public int update_readhit(int idx) {
		int res = sqlsession.update("n.notice_update_readhit", idx);
		return res;
	}
	
	//삭제되는 게시글 업데이트
	public int del_update(NoticeVO vo) {
		int res = sqlsession.update("n.notice_del_update", vo);
		return res;
	}
	
	//수정
	public int update(NoticeVO vo) {
		int res = sqlsession.insert("n.notice_update", vo);
    	return res;
	}

}
