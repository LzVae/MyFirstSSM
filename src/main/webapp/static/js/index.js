$(function(){

	to_page(1);

});

var total_Pages;

function build_emps_table(argument) {
	// body...
	$('#emps_table tbody').empty();
	var emps = argument.extend.pageinfo.list;
	total_Pages = argument.extend.pageinfo.pages;
	$.each(emps, function(index, val) {
		 /* iterate through array or object */
		 var empId_td = $('<td></td>').append(val.empId);
		 var empName_td = $('<td></td>').append(val.empName);
		 var gender = val.gender=='M'?'男':'女';
		 var gender_td = $('<td></td>').append(gender);
		 var email_td = $('<td></td>').append(val.email);
		 var deptName_td = $('<td></td>').append(val.department.deptName);

		 var editBtn = $('<button></button>').addClass('btn btn-info btn-sm edit_btn')
		 				.append($('<span></span>').addClass('glyphicon glyphicon-pencil')).append("编辑");
		// 为按钮添加一个自定义的属性，方便表示当前员工的id
		 editBtn.attr("edit-id",val.empId);
		 var deleteBtn = $('<button></button>').addClass('btn btn-danger btn-sm delete_btn')
		 				.append($('<span></span>').addClass('glyphicon glyphicon-trash')).append("删除");

		 var btn_td = $('<td></td>').append(editBtn).append(' ').append(deleteBtn);

		 $('<tr></tr>').append(empId_td).append(empName_td).append(gender_td).append(email_td).append(deptName_td)
		 				.append(btn_td)
		 				.appendTo('#emps_table tbody');

	});



}


function build_page_info(argument) {
	// body...
	$('#page_info_area').empty();
	$('#page_info_area').append('当前'+argument.extend.pageinfo.pageNum+'页，总共'+argument.extend.pageinfo.pages+'页，总共'+argument.extend.pageinfo.total+'条记录');
}






function to_page(argument) {
	// body...
	
	
	$.ajax({
		url: 'emps',
		type: 'GET',
		data: {"pn": argument},
		success:function(result){

			build_emps_table(result);

			build_page_info(result);

			build_page_nav(result);

			

		}
	});
}



// 构建分页条信息
function build_page_nav(argument) {

	$('#page_nav_area').empty();

	var page_ul = $('<ul></ul>').addClass('pagination');

	// 首页
	var firstPage_li = $('<li></li>').append($('<a></a>').append('首页').attr('href','#'));
	firstPage_li.click(function(event) {
		/* Act on the event */
		to_page(1);
	});


	// 尾页
	var lastPage_li = $('<li></li>').append($('<a></a>').append('末页').attr('href','#'));
	lastPage_li.click(function(event) {
		/* Act on the event */
		to_page(argument.extend.pageinfo.pages);
	});



	// 是否有前一页
	if (argument.extend.pageinfo.hasPreviousPage == true) {
		var prePage_li = $('<li></li>').append($('<a></a>').append('&laquo;').attr('href','#'));

		prePage_li.click(function(event) {
			/* Act on the event */
			to_page(argument.extend.pageinfo.pageNum-1);
		});

	}
	
	// 是否有后一页
	if (argument.extend.pageinfo.hasNextPage == true) {
		var nextPage_li = $('<li></li>').append($('<a></a>').append('&raquo;').attr('href','#'));

		nextPage_li.click(function(event) {
			/* Act on the event */
			to_page(argument.extend.pageinfo.pageNum+1);
		});

	}
	

	page_ul.append(firstPage_li).append(prePage_li);


	// 循环构建分页按钮信息
	$.each(argument.extend.pageinfo.navigatepageNums, function(index, val) {
		 /* iterate through array or object */
		 var num_li = $('<li></li>').append($('<a></a>').append(val).attr('href','#'));

		 if (argument.extend.pageinfo.pageNum == val) {num_li.addClass('active');}

		 num_li.click(function(event) {
		 	/* Act on the event */
		 	to_page(val);
		 });

		 page_ul.append(num_li);

	});

	page_ul.append(nextPage_li).append(lastPage_li);

	var page_nav = $('<nav></nav>').append(page_ul);

	page_nav.appendTo('#page_nav_area');

}


// ajax查询部门信息
$(function(){

	$('#emp_add_btn').click(function(event) {
	/* Act on the event */

	// 清楚表单数据
	$('#empAddModal form')[0].reset();



	$.ajax({
		url: 'depts',
		type: 'GET',
		success:function(result){

			$('#dept_select').empty();

			$.each(result.extend.depts, function(index, val) {
				 /* iterate through array or object */
				 var dept_option = $('<option></option>').append(val.deptName).attr('value',val.deptId);

				 dept_option.appendTo('#dept_select');
			});


		}
	});
	
	



		$('#empAddModal').modal({
			backdrop:'static'
		});
	});
});



// ajax查询员工姓名是否可用(被注册)
$(function(){

	$('#emp_name_input').change(function() {
	// body...
	var empName = this.value;
	$.ajax({
		url:'checkuser',
		data:{'empName':empName},
		type:'POST',
		success:function(result) {
			// body...
			if (result.code == 100) {

				show_validate_msg('#emp_name_input','success','用户名可用');
				$('#emp_save_btn').removeAttr('disabled');

			}else{
				show_validate_msg('#emp_name_input','error',result.extend.msg);
				
				$('#emp_save_btn').attr('disabled','disabled');
			}

		}

	});


});


})













//保存新员工
$(function(){

	$('#emp_save_btn').click(function(event) {
	/* Act on the event */

	if (!validate_add_form()) {

		return false;
	}


	$.ajax({
		url: 'emp',
		type: 'POST',
		data: $('#empAddModal form').serialize(),
		success:function(result){
			// 成功
			if (result.code == '100') { 
				$('#empAddModal').modal('hide');
				to_page(total_Pages);
			}else{  //失败时显示错误信息
				if (undefined != result.extend.errorFields.email) {			
					show_validate_msg('#emp_email_input','error',result.extend.errorFields.email);
				}
				if (undefined != result.extend.errorFields.empName) {
					show_validate_msg('#emp_name_input','error',result.extend.errorFields.empName);

				}

			}

			

		}
	});

	});

});


// 校验表单数据
function validate_add_form(argument) {
	// body...
	var emp_Name = $('#emp_name_input').val();
	var emp_email = $('#emp_email_input').val();

	var reg_name = /(^[a-zA-Z0-9_-]{3,16}$)|(^[\u2E80-\u9FFF]{2,6})/ ;
	var reg_email = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;

	if (!reg_name.test(emp_Name)) {
		// alert("用户名可以使2到6位中文或3到16位英文和数字的组合！");
		show_validate_msg('#emp_name_input','error','用户名可以使2到6位中文或3到16位英文和数字的组合！');
		
		return false;
	}else{
		
		show_validate_msg('#emp_name_input','success','');
		
	}


	if (!reg_email.test(emp_email)) {
		// alert("邮箱格式不正确！");
		show_validate_msg('#emp_email_input','error','邮箱格式不正确！');
		
		return false;
	}else{

		show_validate_msg('#emp_email_input','success','');
		
	}


	return true;
}


// 显示校验结果信息
function show_validate_msg(ele,status,msg) {
	// body...
	$(ele).parent().removeClass('has-success has-error');
	$(ele).next("span").text('');
	if ("success" == status) {

		$(ele).parent().addClass('has-success');
		$(ele).next("span").text(msg);

	}else if ("error" == status) {

		$(ele).parent().addClass('has-error');
		$(ele).next("span").text(msg);

	}

}


// 为编辑按钮绑定事件
$(document).on("click",".edit_btn",function(){
	// 查出员工信息并显示
	getEmp($(this).attr("edit-id"));

	// 查出部门信息并显示部门列表
	$('#empUpdateModal form')[0].reset();
		$.ajax({
			url: 'depts',
			type: 'GET',
			success:function(result){
	
				$('#empUpdateModal select').empty();
	
				$.each(result.extend.depts, function(index, val) {
					 /* iterate through array or object */
					 var dept_option = $('<option></option>').append(val.deptName).attr('value',val.deptId);
	
					 dept_option.appendTo('#empUpdateModal select');
				});
	
	
			}
		});

	// 把员工id传给模态框的更新按钮
		$('#emp_update_btn').attr('edit-id', $(this).attr("edit-id"));

	// 弹出模态框
	$('#empUpdateModal').modal({
		backdrop:'static'
	});
	
})



// 查询单个员工信息的方法
function getEmp(id) {
	$.ajax({
		type: "GET",
		url: "emp/"+id,
		success: function (response) {
			var empDate = response.extend.emp;

			// 清空
			$("#gender_add_update_m").removeAttr('checked');
			$("#gender_add_update_f").removeAttr('checked');
			// 构建
			$('#emp_name_update').attr('value',empDate.empName);
			$('#emp_email_update').val(empDate.email);
			if (empDate.gender=='M') {
				$("#gender_add_update_m").attr("checked","checked");
			}
			else{
				$("#gender_add_update_f").attr("checked","checked");
			}

		 	$("#dept_select_update").val([empDate.dId]);
		}
	});
}


// 点击更新，更新员工信息
$(function(){

	$('#emp_update_btn').click(function() {

		// 校验邮箱信息
		// var emp_email_up = $('#emp_email_update').val();
		// var reg_email_up = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
		// if (!reg_email_up.test(emp_email_up)) {
		// 	// alert("邮箱格式不正确！");
		// 	show_validate_msg('#emp_email_update', 'error', '邮箱格式不正确！');

		// 	return false;
		// } else {

		// 	show_validate_msg('#emp_email_update', 'success', '');

		// }


		// 发送ajax请求，更新员工信息
		$.ajax({
			type: "POST",
			url: "emp/" + $(this).attr("edit-id"),
			data: $('#empUpdateModal form').serialize()+'&_method=PUT',
			success: function (response) {
				console.log(response);
			}
		});
	})
})