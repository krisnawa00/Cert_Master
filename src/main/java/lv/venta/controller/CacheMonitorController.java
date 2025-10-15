package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CacheMonitorController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/cache-status")
    public String getCacheStatus(Model model) {
        Map<String, String> cacheInfo = new HashMap<>();
        
        for (String cacheName : cacheManager.getCacheNames()) {
            cacheInfo.put(cacheName, "Active");
        }
        
        model.addAttribute("caches", cacheInfo);
        model.addAttribute("totalCaches", cacheInfo.size());
        
        return "cache-status-page";
    }


    @PostMapping("/cache-clear")
    public String clearAllCaches(Model model) {
        try {
            for (String cacheName : cacheManager.getCacheNames()) {
                cacheManager.getCache(cacheName).clear();
            }
            model.addAttribute("message", "Visi kešatmiņas notīrīti veiksmīgi!");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Kļūda notīrot kešatmiņu: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        

        Map<String, String> cacheInfo = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            cacheInfo.put(cacheName, "✓ Active");
        }
        model.addAttribute("caches", cacheInfo);
        model.addAttribute("totalCaches", cacheInfo.size());
        
        return "cache-status-page";
    }

}