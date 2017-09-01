package com.ssm.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.crud.dao.DepartmentMapper;
import com.ssm.crud.pojo.Department;

/**
* @author zhuxindong  E-mail:501801307@qq.com
* @date ����ʱ�䣺2017��9��1�� ����4:21:38
* @version 1.0
*/

@Service
public class DepartmentService {

	@Autowired
	private DepartmentMapper mapper;
	
	public List<Department> getAll(){
		
		return mapper.selectByExample(null);
		
		
	}
	
	
	
}
