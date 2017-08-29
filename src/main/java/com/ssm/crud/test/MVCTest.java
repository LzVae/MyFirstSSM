package com.ssm.crud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;
import com.ssm.crud.pojo.Employee;

/**
* @author zhuxindong  E-mail:501801307@qq.com
* @date ����ʱ�䣺2017��8��28�� ����9:06:55
* @version 1.0
*/

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:applicationContext.xml","classpath:springmvc.xml"})
public class MVCTest {
	
	@Autowired
	WebApplicationContext context;
	
	/**
	 * �����mvc����
	 */
	MockMvc mockMvc;
	
	@Before
	public void initMockMvc(){
		
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
	}
	
	@Test
	public void testPage() throws Exception{
		
		/**
		 * ģ�������õ�����ֵ
		 */
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps.html").param("pn", "6"))
		.andReturn();
		
		MockHttpServletRequest request = result.getRequest();
		
		PageInfo pageInfo = (PageInfo) request.getAttribute("pageinfo");
		
		System.out.println("��ǰҳ�룺"+pageInfo.getPageNum());
		System.out.println("��ҳ�룺"+pageInfo.getPages());
		System.out.println("�ܼ�¼����"+pageInfo.getTotal());
		
		System.out.println("��ǰҳ����Ҫ��ʾ��ҳ�룺");
		int[] nums = pageInfo.getNavigatepageNums();
		for (int i : nums) {
			System.out.print(" "+i);
		}
		System.out.println("");
		/**
		 * ��ȡԱ������
		 */
		List<Employee> employees = pageInfo.getList();
		for (Employee employee : employees) {
			System.out.println("ID:"+employee.getEmpId()+"===>Name: "+employee.getEmpName());
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	

}
