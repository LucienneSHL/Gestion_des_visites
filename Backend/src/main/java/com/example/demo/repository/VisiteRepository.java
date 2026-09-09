package com.example.demo.repository;

import com.example.demo.entity.Visite;
import com.example.demo.entity.VisiteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisiteRepository extends JpaRepository<Visite, VisiteId> {
}