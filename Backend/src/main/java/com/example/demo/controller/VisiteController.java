package com.example.demo.controller;

import com.example.demo.entity.Visite;
import com.example.demo.entity.VisiteId;
import com.example.demo.service.VisiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visites")
@CrossOrigin(origins = "*")
public class VisiteController {

    @Autowired
    private VisiteService visiteService;

    @GetMapping
    public List<Visite> getAll() {
        return visiteService.getAll();
    }

    @GetMapping("/{codemed}/{codepat}/{date}")
    public Visite getById(@PathVariable String codemed,
                           @PathVariable String codepat,
                           @PathVariable String date) {
        VisiteId id = new VisiteId(codemed, codepat, LocalDate.parse(date));
        return visiteService.getById(id);
    }

    @PostMapping
    public Visite create(@RequestBody Visite visite) {
        return visiteService.save(visite);
    }

    // ✅ Version utilisant le service
    @PutMapping("/{codemed}/{codepat}/{date}")
    public Visite update(@PathVariable String codemed,
                          @PathVariable String codepat,
                          @PathVariable String date,
                          @RequestBody Visite visite) {
        
        VisiteId oldId = new VisiteId(codemed, codepat, LocalDate.parse(date));
        return visiteService.update(oldId, visite);
    }

    @DeleteMapping("/{codemed}/{codepat}/{date}")
    public void delete(@PathVariable String codemed,
                        @PathVariable String codepat,
                        @PathVariable String date) {
        VisiteId id = new VisiteId(codemed, codepat, LocalDate.parse(date));
        visiteService.delete(id);
    }
}