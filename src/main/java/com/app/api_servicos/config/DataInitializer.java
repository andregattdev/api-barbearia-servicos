package com.app.api_servicos.config;

import com.app.api_servicos.model.Perfil;
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um admin para não duplicar toda vez que rodar
        if (usuarioRepository.findByLogin("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            // A senha será "admin123" (criptografada no banco)
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfil(Perfil.ADMIN);
            
            usuarioRepository.save(admin);
            System.out.println(">>> USUÁRIO ADMIN MESTRE CRIADO: login: admin / senha: admin123");
        }
    }
}