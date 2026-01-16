package com.EComMicroService.ProductsServices.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EComMicroService.ProductsServices.Controllers.BagController.AddToBagRequest;
import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Services.WatchListService;

@RestController
@RequestMapping("/products/watchlist")
public class WatchListController {

    private final WatchListService watchListService;

    public WatchListController(WatchListService watchListService) {
        this.watchListService = watchListService;   
    }

      @PostMapping("/add")
    public List<productDTO> addToWatchList(@RequestBody AddToBagRequest request, @RequestHeader("Authorization") String authHeader) {
        return watchListService.addItem(authHeader, request.getProductId());
    }

    @PostMapping("/remove")
    public List<productDTO> removeFromWatchList(@RequestBody AddToBagRequest request,@RequestHeader("Authorization") String authHeader) {
        return watchListService.removeItem(authHeader, request.getProductId());
    }

    @GetMapping("/WatchListItems")
    public List<productDTO> getWatchListItems(@RequestHeader("Authorization") String authHeader) {
        return watchListService.getItems(authHeader);
    }

}
