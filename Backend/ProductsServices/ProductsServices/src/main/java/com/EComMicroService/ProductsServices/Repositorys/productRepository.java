package com.EComMicroService.ProductsServices.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.EComMicroService.ProductsServices.Entity.products;

@Repository
public interface productRepository extends JpaRepository<products,String>{

}
