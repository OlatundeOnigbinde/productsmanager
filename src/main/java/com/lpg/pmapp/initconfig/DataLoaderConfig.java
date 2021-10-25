package com.lpg.pmapp.initconfig;

import com.lpg.pmapp.model.Category;
import com.lpg.pmapp.model.Product;
import com.lpg.pmapp.model.User;
import com.lpg.pmapp.model.repo.CategoryRepository;
import com.lpg.pmapp.model.repo.ProductRepository;
import com.lpg.pmapp.model.repo.UserRepository;
import com.lpg.pmapp.util.Util;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class DataLoaderConfig {

    private static final Logger LOGGER = Logger.getLogger(DataLoaderConfig.class.getName());

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    UserRepository userRepo;

    @Value("${file.path.categories: categories.csv}")
    private String categoryFilePath;

    @Value("${file.path.products: products.csv}")
    private String productsFilePath;

    @Bean
    public void loadCategories() throws IOException, URISyntaxException {
        LOGGER.log(Level.INFO, "loading categories  >>>>>>>>>>>>");
        try{
            LOGGER.log(Level.INFO, "path here: "+categoryFilePath);
            Files.lines(Paths.get(categoryFilePath))
                    .filter(line -> !line.contains("CATEGORY_NAME"))
                    .map(line -> {
                        Category category = new Category();
                        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                        category.setCreateDate(new Date());
                        category.setName((parts[1]));
                        return category;
                    }).forEach(categoryRepo::save);
        }catch (ConstraintViolationException | DataIntegrityViolationException e) {
            LOGGER.log(Level.SEVERE, "Error loading categories: categories already loaded previously");
        }
        LOGGER.log(Level.INFO, "completed loading categories  >>>>>>>>>>>>");
    }

    @Bean
    public void loadProducts() throws IOException, URISyntaxException {
        LOGGER.log(Level.INFO, "loading products  >>>>>>>>>>>>");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try{
            LOGGER.log(Level.INFO, "path here: "+productsFilePath);
            Files.lines(Paths.get(productsFilePath))
            .filter(line -> !line.contains("LAST_PURCHASED_DATE"))
            .map(line -> {
                Product product = new Product();
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                product.setName((parts[1]));
                product.setDescription((parts[2].replaceAll("\"", "")));
                product.setCategory(categoryRepo.findById(Long.parseLong(parts[3])).get());
                try {
                    product.setCreationDate(sdf.parse(parts[4]));
                    product.setUpdateDate(sdf.parse(parts[5]));
                    product.setLastPurchasedDate(sdf.parse(parts[6]));
                } catch (ParseException pse) {
                    LOGGER.log(Level.SEVERE, "", pse);
                }
                return product;
            }).forEach(productRepo::save);
        }catch (ConstraintViolationException | DataIntegrityViolationException e) {
            LOGGER.log(Level.SEVERE, "Error loading products: products already loaded previously");
        }
        LOGGER.log(Level.INFO, "completed loading products  >>>>>>>>>>>>");
    }

    @Bean
    public void loadUser() {
        LOGGER.log(Level.INFO, "loading default user >>>>>>>>>>>>");
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Snow");
        user.setPhoneNumber("08012345678");
        user.setEmail("johnsnow@gmail.com");
        user.setPassword(Util.hash("password123"));
        user.setCreateDate(new Date());
        user.setSessionToken("MS0xNjM1MDM0ODgwOTgxLW9sYXR1bmRlb25pZ2JpbmRlQGdtYWlsLmNvbS0wODAzMzE3MjU4OQ==");
        user.setActiveFlag(true);
        try {
            userRepo.save(user);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            LOGGER.log(Level.SEVERE, "Error saving default user: User already created previously");
        }
        LOGGER.log(Level.INFO, "completed load user >>>>>>>>>>>>");
    }
}
