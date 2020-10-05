package pl.recordit.deteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.recordit.deteer.dto.CommentDto;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.entity.User;
import pl.recordit.deteer.facade.AuthenticationFacade;
import pl.recordit.deteer.model.InheritedPropertyMap;
import pl.recordit.deteer.rest.service.ProductRestUpdateService;
import pl.recordit.deteer.service.CommentService;
import pl.recordit.deteer.service.ProductService;
import pl.recordit.deteer.service.ProductServiceJpa;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(path = "/products")
public class ProductController {

  public static final String PRODUCTS = "products/products";

  public static final String PRODUCT = "products/product";

  public static final String NEW_PRODUCT_FORM = "products/newProductForm";

  public static final String UPDATE_PRODUCT_FORM = "products/updateProductForm";

  public static final String DOCUMENTS = "products/documents";

  public static final String ERROR = "error";

  private final ProductService productService;

  private final CommentService commentService;

  private final AuthenticationFacade authenticationFacade;

  private final Logger logger = Logger.getLogger(ProductController.class.getName());

  @Autowired
  public ProductController(ProductServiceJpa productService,
                           CommentService commentService,
                           AuthenticationFacade authenticationFacade) throws IOException {
    this.productService = productService;
    this.commentService = commentService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping(path = "/index")
  public String allProducts(HttpServletRequest request, Model model) {
    model.addAttribute("header","Lista wszystkich produktów:");
    model.addAttribute("products", authenticationFacade.isAnonymous() ? productService.findAllPublic() : productService.findAll());
    return PRODUCTS;
  }

  @GetMapping(path = "/{id}")
  public String oneProduct(Model model, @PathVariable Long id) {
    return productService
            .findBy(id)
            .filter(product -> !authenticationFacade.isAnonymous() || product.isPublic())
            .flatMap(product -> {
              model.addAttribute("header","Opis produktu");
              model.addAttribute("product", product);
              model.addAttribute("comment", CommentDto.builder().productId(product.getId()).build());
              model.addAttribute("documents",
                      productService.findAllDocumentsForProduct(id)
                              .flatMap(doc -> Stream.of(doc.generateLink("/download")))
                              .collect(Collectors.toList()));
              return Optional.of(PRODUCT);
            })
            .orElse(PRODUCTS);
  }

  @GetMapping(path = "/add")
  public String createProductForm(@ModelAttribute("product") NewProductDto product) {
    return NEW_PRODUCT_FORM;
  }

  @GetMapping(path = "/add/{parentId}")
  public String createChildProductForm(@PathVariable Long parentId, @ModelAttribute("product") NewProductDto product, Model model) {
    Optional<Product> parent = productService.findBy(parentId);
    if (parent.isPresent()) {
      model.addAttribute("parent", parent.get());
      product.setParentId(parentId);
      return NEW_PRODUCT_FORM;
    }
    return ERROR;
  }

  @PostMapping("/add")
  public String saveProduct(@ModelAttribute("product") @Valid NewProductDto product, BindingResult bindingResult, @AuthenticationPrincipal User user) {
    if (bindingResult.hasErrors()) {
      return NEW_PRODUCT_FORM;
    }
    product.setPublisher(user);
    productService.create(product);
    return "redirect:/products/index";
  }

  @GetMapping(path = "/update/{id}")
  public String updateProductForm(@PathVariable("id") long id, Model model) {
    return productService.findBy(id)
            .flatMap(product -> {
              model.addAttribute("product", product);
              return Optional.of(UPDATE_PRODUCT_FORM);
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
    return DOCUMENTS;
  }

  @GetMapping("/labels")
  public String allEnergyLabels(Model model) {
    model.addAttribute("description", "Etykiety energetyczne:");
    model.addAttribute("documents", productService.findDocuments(Product::getEnergyLabel).collect(Collectors.toList()));
    return DOCUMENTS;
  }

  @GetMapping("/sheets")
  public String allProductSheets(Model model) {
    model.addAttribute("description", "Karty produktów:");
    model.addAttribute("documents", productService.findDocuments(Product::getProductSheet).collect(Collectors.toList()));
    return DOCUMENTS;
  }

  @PostMapping("/comments/add/{productId}")
  public String addComment(@PathVariable Long productId, CommentDto commentDto, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
      User author = (User) authentication.getPrincipal();
      commentDto.setAuthorId(author.getId());
    }
    return commentService.create(commentDto)
            .flatMap(comment -> Optional.of("redirect:/products/" + productId))
            .orElse(ERROR);
  }
}
