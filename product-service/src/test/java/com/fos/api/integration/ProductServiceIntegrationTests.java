package com.fos.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fos.api.model.Product;
import com.fos.api.model.request.ProductRequest;
import com.fos.api.model.request.ProductUpdateRequest;
import com.fos.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(properties = "spring.config.name=application-test")
@Testcontainers
@AutoConfigureMockMvc
@Transactional
class ProductServiceIntegrationTests{
    //MySQLContainer to run a MsSQL database during tests
    // ? is for SELF


    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.3"));

    static {
        //start the MySQL container before the tests
        mysqlContainer.start();
    }
    //to make requests to the product controller
    @Autowired
    MockMvc mockMvc;

    //for convert object to json vise-versa
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    Product createdProduct;

    @BeforeEach
    void setUp() {
        // Set up data for the tests
        //create the product for testing
        Product product = getProduct("Chicken", "Fresh Chicken", 5.66, "available");
        createdProduct = productRepository.save(product);
    }

    @AfterEach
    void cleanup(){
//       try{
//           Connection connection = DriverManager.getConnection(System.getenv("PRODUCT_DB_URL"),
//                   System.getenv("PRODUCT_USER_NAME"), System.getenv("PRODUCT_USE_PASSWORD"));
//           Statement statement = connection.createStatement();
//           String query = "ALTER TABLE Product AUTO_INCREMENT = 1;";
//           statement.executeUpdate(query);
//       } catch(Exception exception){
//
//       }

        //Clean up the database after each test
        productRepository.deleteAll();
    }
    //
    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest("Chicken meat","Fresh Chicken", 5.66 );

        String productRequestString = objectMapper.writeValueAsString(productRequest);

        //make the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString)) //content require string that's why we map the object to string in the first place
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Chicken meat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Fresh Chicken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.66));

        //assert the product is created
//        Assertions.assertEquals(1,productRepository.count());


    }

    @Test
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldGetProduct() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Chicken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Fresh Chicken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(5.66));

    }

    @Test
    void shouldUpdateProduct() throws Exception {

        //create a product request
        ProductUpdateRequest productUpdateRequest = getProductUpdateRequest("Chicken", "Fresh Chicken", 10.11,"available");

        String productRequestString = objectMapper.writeValueAsString(productUpdateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/" + createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Chicken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Fresh Chicken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("available"));
        ;

    }

    @Test
    void shouldDeleteProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("notAvailable"));
    }

    private ProductRequest getProductRequest(String productName, String description, Double price) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName(productName);
        productRequest.setDescription(description);
        productRequest.setPrice(price);

        return productRequest;

    }
    private ProductUpdateRequest getProductUpdateRequest(String productName, String description, Double price, String status) {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setProductName(productName);
        productUpdateRequest.setDescription(description);
        productUpdateRequest.setPrice(price);
        productUpdateRequest.setStatus(status);
        return productUpdateRequest;

    }


    private Product getProduct(String productName, String description, Double price, String status) {
        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setStatus(status);

        return product;

    }

}
