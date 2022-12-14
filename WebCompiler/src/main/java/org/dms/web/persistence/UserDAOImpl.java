package org.dms.web.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.dms.web.domain.CodeVO;
import org.dms.web.domain.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {
	@Autowired
	private SqlSession sqlSession;
	private static final String namespace = "org.dms.web.mapper.Mapper";

	@Override
	public void insert(UserVO user) throws Exception {
		sqlSession.insert(namespace + ".user_insert", user);
	}

	@Override
	public List<UserVO> readList() throws Exception {
		
		List<UserVO> userList=new ArrayList<UserVO>();
		userList = sqlSession.selectList(namespace + ".user_selectAll");
		for(int i = 0; i<userList.size(); i++) {
			String introduce = userList.get(i).getUser_introduce();
	
			userList.get(i).setUser_introduce(introduce);
		}
		return userList;
	}

	@Override
	public UserVO read(String id) throws Exception {
		UserVO user = sqlSession.selectOne(namespace + ".user_selectById", id);
		return user;
	}

	@Override
	public void delete(String id) throws Exception {
		sqlSession.delete(namespace + ".user_deleteById", id);
	}

	@Override
	public int update(UserVO user) throws Exception {		
		return sqlSession.update(namespace + ".user_update", user);
	}
	
	@Override
	public int saveImg(UserVO user) throws Exception {
		return sqlSession.update(namespace + ".save_img", user);
		
	}

	@Override
	public UserVO read(UserVO vo) throws Exception {
	
		UserVO user = sqlSession.selectOne(namespace + ".user_selectForLogin", vo);
		return user;	
	}
	
	@Override
	public List<CodeVO> getCodeList(String id) throws Exception {
		List<CodeVO> codeList = sqlSession.selectList(namespace + ".user_selectCodeList", id);
		return codeList;
	}

	@Override
	public boolean checkId(String value) throws Exception {
		UserVO user = sqlSession.selectOne(namespace + ".check_user_id", value);
		System.out.println("checkID User: " + user);
		
		if(user != null) {
			return false;
		}
		return true;
	}

}
