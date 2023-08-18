package com.pi314.orders.controller;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.service.*;
import com.pi314.orders.service.impl.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsController {
    private final GroupService groupService;

    @GetMapping
    public Products getAllProducts(){
        return groupService.returnAllProducts();
    }
}
