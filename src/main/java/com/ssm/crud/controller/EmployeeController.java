package com.ssm.crud.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 处理员工的CRUD请求
* @author zhuxindong  E-mail:501801307@qq.com
* @date 创建时间：2017年8月28日 下午8:45:26
* @version 1.0
*/

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	/**
	 * 分页查询所有员工信息，连带其部门信息一起查询
	 * 用来返回json数据
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1")Integer pn){
		
		/**
		* 利用pagehelper分页插件来分页
		*/
		PageHelper.startPage(pn, 5);
		
		List<Employee> employees = employeeService.getAll();
		
		PageInfo page = new PageInfo(employees,5);
		
		
		return Msg.success().add("pageinfo", page);
		
		}
	
	
	
	
	
	
	

	/**
	 * 保存一个新员工
	 * 
	 * 1.支持JSR303校验
	 * 2.导入Hibernate-validator
	 * 
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result){
		/*
		 * 后台检验数据信息
		 */
		if (result.hasErrors()) {
			/*
			 * 如果校验失败，显示错误信息
			 */
			Map<String, Object> errMap = new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				/*
				 * 把错误信息封装成map对象
				 */
				errMap.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			
			/*
			 * 提交给浏览器
			 */
			return Msg.fail().add("errorFields", errMap);
			
		}else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
		
		
	}
	

	
	/**
	 * 查询姓名是否可用
	 * @param empName
	 * @return
	 */
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkUser(@RequestParam(value="empName") String empName){
		
		/**
		 * 先判断用户名是否合法
		 */
		String regx = "(^[a-zA-Z0-9_-]{3,16}$)|(^[\u2E80-\u9FFF]{2,6})";
		if (!empName.matches(regx)) {
			return Msg.fail().add("msg", "用户名必须是2到6位中文或3到16位英文和数字的组合");
		}
		
		/**
		 * 数据库用户名重复校验
		 */
		boolean b = employeeService.chechUser(empName);
		
		if (b) {
			return Msg.success();
		}else {
			return Msg.fail().add("msg", "用户名不可用");
		}
	}
	
	
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		
		Employee employee = employeeService.getEmpById(id);
		return Msg.success().add("emp", employee);
		
	}
	
	
	
	
	
	
	
	
	
}
