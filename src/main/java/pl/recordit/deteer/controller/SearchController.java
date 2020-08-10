package pl.recordit.deteer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.service.ProductService;

import java.util.List;

@Controller
@RequestMapping(path = "/search")
public class SearchController {
    private final ProductService productService;


    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String search(@RequestParam(name = "name") String name, Model model){
        List<Product> products = productService.findByName(name);
        model.addAttribute("products", products);
        model.addAttribute("header", products.size() > 0 ? "Lista znalezionych produktów:" : "Brak pasujących produktów:");
        return "products/products";
    }
}
