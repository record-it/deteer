package pl.recordit.deteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.recordit.deteer.dto.CommentDto;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.entity.User;
import pl.recordit.deteer.service.CommentService;
import pl.recordit.deteer.service.ProductService;
import pl.recordit.deteer.service.ProductServiceJpa;

import javax.validation.Valid;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(path = "/products")
public class ProductController {
  private final ProductService productService;
  private final CommentService commentService;
  private final Logger logger = Logger.getLogger(ProductController.class.getName());

  @Autowired
  public ProductController(ProductServiceJpa productService, CommentService commentService) {
    this.productService = productService;
    this.commentService = commentService;
  }

  @GetMapping(path = "/index")
  public String allProducts(Model model) {
    model.addAttribute("products", productService.findAll());
    return "products/products";
  }

  @GetMapping(path = "/{id}")
  public String oneProduct(Model model, @PathVariable Long id) {
    return productService
            .findBy(id)
            .flatMap(product -> {
              model.addAttribute("product", product);
              model.addAttribute("comment", CommentDto.builder().productId(product.getId()).build());
              model.addAttribute("documents",
                      productService.findAllDocumentsForProduct(product.getId())
                              .flatMap(doc -> Stream.of(doc.generateLink("/files/download")))
                              .collect(Collectors.toList()));
              return Optional.of("products/product");
            })
            .orElse("products/products");
  }

  @GetMapping(path = "/add")
  public String createProductForm(@ModelAttribute("product") NewProductDto product) {
    return "products/newProductForm";
  }

  @PostMapping("/add")
  public String saveProduct(@ModelAttribute("product") @Valid NewProductDto product, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "products/newProductForm";
    }
    productService.create(product);
    return "redirect:/products/products";
  }

  @GetMapping(path = "/add/{parentId}")
  public String createChildProductForm(@PathVariable Long parentId, @ModelAttribute("product") NewProductDto product, Model model) {
    Optional<Product> parent = productService.findBy(parentId);
    if (parent.isPresent()) {
      model.addAttribute("parent", parent.get());
      product.setParentId(parentId);
      return "products/newProductForm";
    }
    return "error";
  }

  @GetMapping(path = "/update/{id}")
  public String updateProductForm(@PathVariable("id") long id, Model model) {
    return productService.findBy(id)
            .flatMap(product -> {
              model.addAttribute("product", product);
              return Optional.of("products/updateProductForm");
            })
            .orElse("redirect:/products/index");
  }

  @PostMapping(path = "/update/{id}")
  public String updateProduct(@PathVariable("id") long id, ProductDto dto) {
    productService.update(dto, id);
    return "redirect:/products/" + id;
  }

  @GetMapping("/manuals")
  public String allOperatingManuals(Model model) {
    model.addAttribute("description", "Instrukcje obsługi produktów:");
    model.addAttribute("documents", productService.findDocuments(Product::getOperatingManual).collect(Collectors.toList()));
    return "products/documents";
  }

  @GetMapping("/labels")
  public String allEnergyLabels(Model model) {
    model.addAttribute("description", "Etykiety energetyczne:");
    model.addAttribute("documents", productService.findDocuments(Product::getEnergyLabel).collect(Collectors.toList()));
    return "products/documents";
  }

  @GetMapping("/sheets")
  public String allProductSheets(Model model) {
    model.addAttribute("description", "Karty produktów:");
    model.addAttribute("documents", productService.findDocuments(Product::getProductSheet).collect(Collectors.toList()));
    return "products/documents";
  }

  @PostMapping("/comments/add/{productId}")
  public String addComment(@PathVariable Long productId, CommentDto commentDto, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
      User author = (User) authentication.getPrincipal();
      commentDto.setAuthorId(author.getId());
    }
    return commentService.create(commentDto)
            .flatMap(comment -> Optional.of("redirect:/products/" + productId))
            .orElse("error");
  }
}
