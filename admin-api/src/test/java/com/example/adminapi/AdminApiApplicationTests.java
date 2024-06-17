package com.example.adminapi;

import com.example.adminapi.entity.Customer;
import com.example.adminapi.mapper.CustomerMapper;
import org.apache.ibatis.type.NStringTypeHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AdminApiApplicationTests {

	@Autowired
	private CustomerMapper customerMapper;

}
