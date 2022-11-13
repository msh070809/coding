package org.dms.web.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.dms.web.domain.ChatVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChatDAOImpl implements ChatDAO {
	
	@Autowired
	private SqlSession sqlSession;
	private static final String namespace ="org.dms.web.mapper.Mapper";
	
	@Override
	public void insert(ChatVO cvo) throws Exception {
	
		sqlSession.insert(namespace + ".chat_insert", cvo);
	}

	@Override
	public List<ChatVO> readList(int problem_id) throws Exception {
	
		List<ChatVO> chatlist = new ArrayList<ChatVO>();
		chatlist = sqlSession.selectList(namespace + ".chat_selectAll", problem_id);
		return chatlist;
	}

	@Override
	public ChatVO read(String id) throws Exception {

		return null;
	}

	@Override
	public void delete(String id) throws Exception {
	

	}

	@Override
	public void update(ChatVO student) throws Exception {
	

	}

}
