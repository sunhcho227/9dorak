package com.shinhan.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan.dto.MemDeliveryVO;
import com.shinhan.dto.MemVO;

@Service
public class MyPageService {

	@Autowired
	MyPageDAOMybatis dao;

	public MemVO getMember(String login_id) {
		// TODO Auto-generated method stub
		return dao.getMember(login_id);
	}

	public int updateMember(MemVO mem) {
		// TODO Auto-generated method stub
		return dao.updateMember(mem);
	}

	public int deleteMember(String mem_id) {
		// TODO Auto-generated method stub
		return dao.deleteMember(mem_id);
		
	}

	public List<MemDeliveryVO> getDelivery(String mem_id) {
		// TODO Auto-generated method stub
		return dao.getDelivery(mem_id);
	}

	public int deleteDelivery(MemDeliveryVO memdel) {
		// TODO Auto-generated method stub
	
		return dao.deleteDelivery(memdel);
	}

	public int insertDelivery(MemDeliveryVO memDel) {
		// TODO Auto-generated method stub
		return dao.insertDelivery(memDel);
	}

	public int selectDelivery(MemDeliveryVO memDel) {
		// TODO Auto-generated method stub
		return dao.selectDelivery(memDel);
	}






	



}
