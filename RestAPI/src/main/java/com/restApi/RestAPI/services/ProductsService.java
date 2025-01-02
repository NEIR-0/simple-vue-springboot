package com.restApi.RestAPI.services;

import com.restApi.RestAPI.dto.ProductsDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.product.ImageStore;
import com.restApi.RestAPI.model.product.Products;
import com.restApi.RestAPI.repository.ImageStoreRepository;
import com.restApi.RestAPI.repository.ProductsRepository;
import com.restApi.RestAPI.repository.TransactionsRepository;
import com.restApi.RestAPI.repository.UserRepository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    ImageStoreRepository imageStoreRepository;

    public ProductsDTO getAllProducts(String search, Integer duration) {
        List<Products> productList;
        List<Integer> durations = new ArrayList<>();

        // Tentukan pengurutan berdasarkan 'createdAt'
        Sort sort = Sort.by("createdAt"); // Gunakan nama kolom yang sesuai
        sort = sort.descending(); // Tentukan urutan

        if (search != null && !search.isEmpty()) {
            productList = productsRepository.findByTitleContainingIgnoreCase(search);
        } else {
            productList = productsRepository.findAll(sort);
        }

        // Filter berdasarkan duration jika ada
        if (duration != null) {
            productList = productList.stream()
                    .filter(product -> product.getDuration().equals(duration))
                    .collect(Collectors.toList());
        }

        // Mengumpulkan durasi unik
        productList.forEach(product -> {
            if (!durations.contains(product.getDuration())) {
                durations.add(product.getDuration());
            }
        });

        return new ProductsDTO(productList, durations, null);
    }

    public ProductsDTO getAllProductsAdminSite(String search, Integer duration, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Products> productPage;
        if (search != null && !search.isEmpty()) {
            productPage = productsRepository.findByTitleContainingIgnoreCaseAdminSite(search, pageRequest);
        } else {
            productPage = productsRepository.findAll(pageRequest);
        }

        List<Products> productList = productPage.getContent();

        if (duration != null) {
            productList = productList.stream()
                    .filter(product -> product.getDuration().equals(duration))
                    .collect(Collectors.toList());
        }

        List<Integer> durations = getAllProductDurations();
        ProductsDTO totalProduct = getAllProducts(search, duration);
        int totalProductCount = totalProduct.getProduct().size();

        return new ProductsDTO(productList, durations, totalProductCount);
    }

    private  List<Integer> getAllProductDurations() {
        List<Products> productList;
        List<Integer> durations = new ArrayList<>();
        productList = productsRepository.findAll();
        for (Products product : productList) {
            if (!durations.contains(product.getDuration())) {
                durations.add(product.getDuration());
            }
        }
        return durations;
    }

    public Products getProductsById(Long productId) {
        if (productId != null) {
            return productsRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("product not found"));
        }
        return null;
    }

    public ResponseDTOOutput createProduct(String title, String description, Float price, Integer duration, Integer stock, MultipartFile image, String imageName) throws IOException {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        // Validate input parameters
        if (title == null || description == null || price == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        // Create new product object
        Products newProduct = new Products();
        newProduct.setTitle(title);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setDuration(duration);
        newProduct.setStock(stock);

        // Handle image upload logic
        String uploadFolder = "uploads/"; // Set the upload folder path
        String fileName = "";

        // If imageName is provided, set it directly
        if (imageName != null && !imageName.isEmpty()) {
            newProduct.setImage("/" + uploadFolder + imageName);
        } else if (image != null && !image.isEmpty()) {
            // Otherwise, handle image file upload
            fileName = image.getOriginalFilename();
            assert fileName != null;
            String titleFormating = fileName.replaceAll("[-_]", " ")
                    .replaceAll("\\.(jpg|jpeg|png)$", "");
            ImageStore newImageStore = new ImageStore();
            newImageStore.setTitle(titleFormating);
            newImageStore.setImageName(fileName);
            imageStoreRepository.save(newImageStore);

            Path filePath = Paths.get(uploadFolder + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());
            newProduct.setImage("/" + uploadFolder + fileName);
        }

        // Save the product to the database
        productsRepository.save(newProduct);

        // Prepare success response
        responseStatus.setMsg("Product created successfully");
        responseStatus.setStatus("success");

        return responseStatus;
    }

    public ResponseDTOOutput updateProduct(String title, String description, Float price, Integer duration, Integer stock, String prevImage, MultipartFile image, String imageName, Long productId) throws IOException {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if (productId == null) {
            responseStatus.setMsg("Invalid productId");
            responseStatus.setStatus("failed");
            return responseStatus;
        }

        Products existingProduct = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));

        // Perbarui field yang tersedia dari inputProduct (dinamis)
        if (title == null || description == null || price == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        // update
        existingProduct.setTitle(title);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setDuration(duration);
        existingProduct.setStock(stock);

        // Konfigurasi upload folder
        String uploadFolder = "uploads/";
        String fileName;

        // Logika upload gambar
        if (imageName != null && !imageName.isEmpty()) {
            // Gunakan nama gambar yang sudah diberikan
            existingProduct.setImage("/" + uploadFolder + imageName);
        } else if (image != null && !image.isEmpty()) {
            // Simpan gambar baru
            fileName = image.getOriginalFilename();
            assert fileName != null;
            fileName = fileName.replaceAll("[-_]", " ")
                    .replaceAll("\\.(jpg|jpeg|png)$", "");

            // Simpan metadata gambar ke ImageStore
            ImageStore newImageStore = new ImageStore();
            newImageStore.setTitle(fileName.replace(".jpg", ""));
            newImageStore.setImageName(fileName);
            imageStoreRepository.save(newImageStore);

            // Simpan file fisik
            Path filePath = Paths.get(uploadFolder + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());

            // Update field gambar pada produk
            existingProduct.setImage("/" + uploadFolder + fileName);
        } else {
            // Gunakan gambar sebelumnya
            existingProduct.setImage(prevImage);
        }

        // Simpan perubahan ke database
        productsRepository.save(existingProduct);

        responseStatus.setMsg("products updated successfully");
        responseStatus.setStatus("success");
        return responseStatus;
    }

    public ResponseDTOOutput deleteById(Long productId){
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if (productId != null) {
            productsRepository.deleteById(productId);

            responseStatus.setMsg("products deleted successfully");
            responseStatus.setStatus("success");
            return responseStatus;
        }else{
            responseStatus.setMsg("failed due create products");
            responseStatus.setStatus("failed");
            return responseStatus;
        }
    }
}
