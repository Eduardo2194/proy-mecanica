package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.repository.bean.Valoracion;

public interface ValoracionRepository extends JpaRepository<Valoracion, Integer> {

}
