package com.shinhan.model;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.shinhan.dto.ChallengeVO;

@Repository
public class ChallengeDAOMybatis {

	@Autowired
	SqlSession sqlSession;
	Logger logger = LoggerFactory.getLogger(ChallengeDAOMybatis.class);
	String NAMESPACE = "net.firstzone.challenge.";

	public List<ChallengeVO> selectAll() {
		List<ChallengeVO> chlist = sqlSession.selectList(NAMESPACE + "selectAll");
		logger.info("selectAll :  {}", chlist.size());
		return chlist;
	}

	public ChallengeVO selectByno(int challenge_no) {
		ChallengeVO chall = sqlSession.selectOne(NAMESPACE + "selectByno", challenge_no);
		return chall;
	}

	public int updateChall(ChallengeVO challenge) {
		int result = sqlSession.update(NAMESPACE + "updateChall", challenge);
		return result;
	}

	public int insertChal(ChallengeVO challenge) {
		int result = sqlSession.insert(NAMESPACE + "insertChal", challenge);
		return result;
	}

	public int getLike(int challenge_no) {
		int result = sqlSession.selectOne(NAMESPACE + "getLike", challenge_no);
		return result;
	}

	public int deleteChal(int challenge_no) {

		int result = sqlSession.delete(NAMESPACE + "deleteChal", challenge_no);

		return result;
	}

	public int selectBoardListCnt(ChallengeVO ChallengeVO) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(NAMESPACE + "selectBoardListCnt", ChallengeVO);
	}


	public List<Map<String, Object>> list(ChallengeVO ChallengeVO) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(NAMESPACE + "list", ChallengeVO);
	}
	

}
