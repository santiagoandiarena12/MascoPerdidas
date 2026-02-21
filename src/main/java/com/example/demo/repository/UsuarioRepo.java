package com.example.demo.repository;


import com.example.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("UsuarioRepo")
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
