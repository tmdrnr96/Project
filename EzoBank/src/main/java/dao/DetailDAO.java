package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vo.DetailVO;

@Repository
public class DetailDAO {

	@Autowired
	SqlSession sqlSession;
		
		//전체조회
	    public List<DetailVO> selectView(){
		List<DetailVO> list = sqlSession.selectList("d.detail");
		return list;
        }
	
		//오버로드
		public List<DetailVO> selectView(String account){
		List<DetailVO> list = sqlSession.selectList("d.detail_list", account);
		return list;
		}
	
	    //계좌 입금, 출금, 이체 시에 데이터 저장
		public int insert_detail(DetailVO vo) {			
		int res = sqlSession.insert("d.detail_insert",vo);					
			return res;
		}
		
		public List<DetailVO> selectList( Map<String, Integer> map ){
			 
			 List<DetailVO> list = null; 
			 list = sqlSession.selectList("d.detail_list_condition", map);
			 return list; 
			}
		
		//전체 게시물 수 구하기
		public int getRowTotal(String account) {
			
			int count = 0;
			
			List<DetailVO> list = sqlSession.selectList("d.detail_list_count",account);
			
			if(list != null) {
				count = list.size();			
			}
			 return count;
		}
		
		
		// 기간 조회 
		public List<DetailVO> selectTerm(String re_date ){
			 
			 List<DetailVO> list = null; 
			 list = sqlSession.selectList("d.detail_term", re_date);
			 return list; 
			}

	
		
}
