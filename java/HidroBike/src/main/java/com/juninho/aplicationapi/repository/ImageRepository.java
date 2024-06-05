package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.Image;


public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByNomeContaining(String nome);
}
