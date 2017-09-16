package com.ssm.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssm.crud.pojo.Department;
import com.ssm.crud.pojo.Msg;
import com.ssm.crud.service.DepartmentService;

/**
* @author zhuxindong  E-mail:501801307@qq.com
* @date ����ʱ�䣺2017��9��1�� ����4:19:11
* @version 1.0
*/

@Controller
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	
	
	/**
	 * ��ѯ���в�����Ϣ
	 * ����json����
	 * @return
	 */
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts(){
		
		List<Department> list = departmentService.getAll();
		
		return Msg.success().add("depts", list);
		
		
	}
	
	

}
