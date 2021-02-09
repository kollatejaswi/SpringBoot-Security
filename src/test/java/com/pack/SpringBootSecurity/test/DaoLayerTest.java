package com.pack.SpringBootSecurity.test;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.pack.SpringBootSecurity.model.Product;
import com.pack.SpringBootSecurity.model.Role;
import com.pack.SpringBootSecurity.model.User;
import com.pack.SpringBootSecurity.repository.ProductRepsitory;
import com.pack.SpringBootSecurity.repository.RoleRepository;
import com.pack.SpringBootSecurity.repository.UserRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaoLayerTest
{
  @Autowired
  private ProductRepsitory productRepo;
  @Autowired
  private UserRepository userRepo;
  @Autowired
  private RoleRepository roleRepo;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Test
  public void testAddProduct()
  {
	  Product product=new Product(10,"tv","LG","china",20000.0);
	  Product saveInDb=productRepo.save(product);
	  Optional<Product> data=productRepo.findById(saveInDb.getId());
	  Product getFromDb=(Product)data.get();
	  assertEquals(saveInDb.getId(),getFromDb.getId());
  }
  @Test
   public void testBGetAllProducts()
   {
	  Iterable<Product> allProducts=productRepo.findAll();
	  List<Product> productList=new ArrayList<>();
	  
	  for(Product product: allProducts)
	  {
		  productList.add(product);
	  }
	  assertThat(productList.size()).isEqualTo(6);
   }
  @Test
  public void testDeleteProductById()
  {
	  productRepo.deleteById(10);
	  Iterable<Product> allProducts=productRepo.findAll();
	  List<Product> productList=new ArrayList<>();
	  
	  for(Product product: allProducts)
	  {
		  productList.add(product);
	  }
	  assertThat(productList.size()).isEqualTo(5);
  }
  @Test
  public void testUpdateProduct()
  {
	  Product product=new Product(11,"fridge","LG","china",20000.0);
	  Product saveInDb=productRepo.save(product);
	  Optional<Product> data=productRepo.findById(saveInDb.getId());
	  Product getFromDb=(Product)data.get();
	  getFromDb.setPrice(25000.0);
	  productRepo.save(getFromDb);
	  assertThat(getFromDb.getPrice()).isEqualTo(25000.0);
  }
  @Test
  @Ignore
  public void testSaveUser()
  {
	  Optional<Role> opt=roleRepo.findById(1l);
	  User user=new User();
	  user.setId(8l);
	  user.setPassword(bCryptPasswordEncoder.encode("abcd"));
	  user.setPasswordConfirm("abcd");
	  user.setUsername("anki");
	  user.setRole(opt.get());
	  User saveInDb=userRepo.save(user);
	  Optional<User> data=userRepo.findById(saveInDb.getId());
	  User getFromDb=(User)data.get();
	  assertEquals(saveInDb.getId(),getFromDb.getId());
  }
  @Test
  public void testFindUser()
  {
	  User getFromDb=userRepo.findByUsername("tejaswi");
	  assertThat(getFromDb.getUsername()).isEqualTo("tejaswi");
  }
}
