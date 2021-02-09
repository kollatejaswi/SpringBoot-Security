package com.pack.SpringBootSecurity.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.AssertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.pack.SpringBootSecurity.model.Product;
import com.pack.SpringBootSecurity.model.Role;
import com.pack.SpringBootSecurity.model.User;
import com.pack.SpringBootSecurity.repository.ProductRepsitory;
import com.pack.SpringBootSecurity.repository.RoleRepository;
import com.pack.SpringBootSecurity.repository.UserRepository;
import com.pack.SpringBootSecurity.service.ProductService;
import com.pack.SpringBootSecurity.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceLayerTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@MockBean
	private RoleRepository roleRepo;
	@MockBean
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@MockBean
	private ProductRepsitory productRepo;
   @Test
   public void testCreateProduct()
   {
	   Product product=new Product(12,"mobile","mi","china",20000.0);
	   Mockito.when(productRepo.save(product)).thenReturn(product);
	   assertThat(productService.saveProduct(product)).isEqualTo(product);
   }
   @Test
   public void testGetAllProducts()
   {
	   List<Product> productList=null;
	   Mockito.when(productRepo.findAll()).thenReturn(productList);
	   assertThat(productService.fetchAllProduct()).isEqualTo(productList);
   }
   @Test
   public void testDeleteProduct()
   {
	   Product product=new Product();
	   Mockito.when(productRepo.findById(11)).thenReturn(Optional.of(product));
	   productService.deleteProduct(product.getId());
	   assertFalse(productRepo.existsById(product.getId()));
   }
   @Test
   public void testUpdateProduct()
   {
	   Product product=new Product(20,"fan","orient","china",20000.0);
	   Mockito.when(productRepo.save(product)).thenReturn(product);
	   Mockito.when(productRepo.findById(20)).thenReturn(Optional.of(product));
	   product.setBrand("usha");
	   Mockito.when(productRepo.save(product)).thenReturn(product);
	   assertThat(productService.updateProduct(product)).isEqualTo(product);
   }
   @Test
   public void testUserSave()
   {
	   User user=new User();
	   Role r=new Role();
	   Mockito.when(roleRepo.findById(1l)).thenReturn(Optional.of(r));
		  user.setId(12l);
		  user.setPassword(bCryptPasswordEncoder.encode("abcd"));
		  user.setPasswordConfirm("abcd");
		  user.setUsername("ankitha");
		  user.setRole(r);
		  Mockito.when(userRepo.save(user)).thenReturn(user);
		  Mockito.when(userRepo.findById(12l)).thenReturn(Optional.of(user));
		  userService.save(user);
		  assertFalse(userRepo.existsById(user.getId()));

		  
   }
   @Test
   public void testFindName()
   {
	   User u=new User();
	   Mockito.when(userRepo.findByUsername("tejaswi")).thenReturn(u);
	   assertThat(userService.findByUsername("tejaswi")).isEqualTo(u);

	   
   }
}
