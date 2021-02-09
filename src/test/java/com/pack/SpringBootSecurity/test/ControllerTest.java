package com.pack.SpringBootSecurity.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.times;
import com.pack.SpringBootSecurity.controller.UserController;
import com.pack.SpringBootSecurity.model.Product;
import com.pack.SpringBootSecurity.service.ProductService;
import com.pack.SpringBootSecurity.service.UserService;
import com.pack.SpringBootSecurity.validator.UserValidator;

@RunWith(SpringRunner.class)
@WebMvcTest(value=UserController.class,secure=false)
//@SpringBootTest
//@AutoConfigureMockMvc
public class ControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	@MockBean
	private UserService userService;
	
	@MockBean
	private UserValidator userValidator;

	@Test
	public void testLogin() throws Exception
	{
		mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
	}
	@Test
	public void testaddProduct() throws Exception
	{
		mockMvc.perform(post("/save")
				.param("id", "1")
				.param("name","heater")
				.param("brand","mi")
				.param("madein","india")
				.param("price","20000.0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/welcome"));
		    ArgumentCaptor<Product> argCap=ArgumentCaptor.forClass(Product.class);
		    Mockito.verify(productService).saveProduct(argCap.capture());
		    assertEquals("heater",argCap.getValue().getName());
	}
	@Test
	public void testEditProduct() throws Exception
	{
		/*mockMvc.perform(post("/edit"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/welcome"));*/
		Product product=new Product(30,"AC","LG","japan",40000.0);
		Mockito.when(productService.getProductById(Mockito.anyInt())).thenReturn(product);
		ArgumentCaptor<Product> argCap=ArgumentCaptor.forClass(Product.class);
		product.setBrand("onida");
		productService.updateProduct(product);
		Mockito.verify(productService).updateProduct(argCap.capture());
	    assertEquals("onida",argCap.getValue().getBrand());
	}
	@Test
	public void testDeleteProduct() throws Exception
	{
		Integer id=1;
		mockMvc.perform(post("/deleteproduct/1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/welcome"));
		verify(productService,times(1)).deleteProduct(id);
	}
	@Test
	public void testAcessDenied() throws Exception
	{
		mockMvc.perform(get("/403"))
		.andExpect(status().isOk())
		.andExpect(view().name("403"));
	}
	@Test
	public void testCreateProduct() throws Exception
	{
		mockMvc.perform(get("/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("new_product"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("productForm"));
	}
	@Test
	public void testFetchProduct() throws Exception
	{
		Product product=new Product(32,"lappy","hcl","japan",40000.0);
		Mockito.when(productService.getProductById(Mockito.anyInt())).thenReturn(product);
		mockMvc.perform(get("/editproduct/32"))
		.andExpect(status().isOk())
		.andExpect(view().name("edit_product"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("editProduct"));
	}
	@Test
	public void testRegistration() throws Exception
	{
		mockMvc.perform(get("/registration"))
		.andExpect(status().isOk())
		.andExpect(view().name("registration"))
		.andExpect(model().attributeExists("userForm"));
	}
	@Test
	public void testWelcome() throws Exception
	{
		List<Product> plist=new ArrayList<>();
		Product product1=new Product(40,"laptop","hcl","japan",40000.0);
		Product product2=new Product(41,"laptop","hcl","japan",40000.0);
		plist.add(product1);
		plist.add(product2);
		Mockito.when(productService.fetchAllProduct()).thenReturn(plist);
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("welcome"))
		.andExpect(model().attributeExists("plist"));
	}
	@Test
	public void testUpdate() throws Exception
	{
		Product product1=new Product(33,"laptop","hcl","japan",40000.0);
		Mockito.when(productService.updateProduct(product1)).thenReturn(product1);
		Integer id=1;
		mockMvc.perform(post("/edit"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/welcome"));
	}
}
