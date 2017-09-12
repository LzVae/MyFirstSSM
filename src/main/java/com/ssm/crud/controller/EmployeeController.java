package com.ssm.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssm.crud.pojo.Employee;
import com.ssm.crud.pojo.Msg;
import com.ssm.crud.service.EmployeeService;

/**
 * ����Ա����CRUD����
* @author zhuxindong  E-mail:501801307@qq.com
* @date ����ʱ�䣺2017��8��28�� ����8:45:26
* @version 1.0
*/

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	/**
	 * ��ҳ��ѯ����Ա����Ϣ�������䲿����Ϣһ���ѯ
	 * ��������json����
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1")Integer pn){
		
		/**
		* ����pagehelper��ҳ�������ҳ
		*/
		PageHelper.startPage(pn, 5);
		
		List<Employee> employees = employeeService.getAll();
		
		PageInfo page = new PageInfo(employees,5);
		
		
		return Msg.success().add("pageinfo", page);
		
		}
	
	
	
	
	
	
	

	/**
	 * ����һ����Ա��
	 * 
	 * 1.֧��JSR303У��
	 * 2.����Hibernate-validator
	 * 
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(Employee employee){
		employeeService.saveEmp(employee);
		return Msg.success();
	}
	

	
	/**
	 * ��ѯ�����Ƿ����
	 * @param empName
	 * @return
	 */
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkUser(@RequestParam(value="empName") String empName){
		
		/**
		 * ���ж��û����Ƿ�Ϸ�
		 */
		String regx = "(^[a-zA-Z0-9_-]{3,16}$)|(^[\u2E80-\u9FFF]{2,6})";
		if (!empName.matches(regx)) {
			return Msg.fail().add("msg", "�û���������2��6λ���Ļ�3��16λӢ�ĺ����ֵ����");
		}
		
		/**
		 * ���ݿ��û����ظ�У��
		 */
		boolean b = employeeService.chechUser(empName);
		
		if (b) {
			return Msg.success();
		}else {
			return Msg.fail().add("msg", "�û���������");
		}
	}
	
	
	
}
