package com.taller.tallerbackend.config;

import com.taller.tallerbackend.persistencias.entidades.Rol;
import com.taller.tallerbackend.persistencias.entidades.Sede;
import com.taller.tallerbackend.persistencias.entidades.TipoVehiculo;
import com.taller.tallerbackend.persistencias.entidades.Usuario;
import com.taller.tallerbackend.persistencias.repositorio.SedeRepository;
import com.taller.tallerbackend.persistencias.repositorio.TipoVehiculoRepository;
import com.taller.tallerbackend.persistencias.repositorio.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SedeRepository sedeRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoVehiculoRepository tipoVehiculoRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataSeeder(SedeRepository sedeRepository, UsuarioRepository usuarioRepository,
                      TipoVehiculoRepository tipoVehiculoRepository) {
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    @Override
    public void run(String... args) {

        // RF-10: registrar las 4 sedes (concesionarios)
        if (sedeRepository.count() == 0) {
            sedeRepository.save(new Sede(null, "Sede Norte"));
            sedeRepository.save(new Sede(null, "Sede Sur"));
            sedeRepository.save(new Sede(null, "Sede Centro"));
            sedeRepository.save(new Sede(null, "Sede Occidente"));
            System.out.println("=== 4 sedes creadas ===");
        }

        if (usuarioRepository.count() == 0) {
            Sede sedePrincipal = sedeRepository.findAll().get(0);

            // RF-16: administrador jefe (A.J.)
            Usuario superadmin = new Usuario();
            superadmin.setNombre("admin");
            superadmin.setPassword(encoder.encode("admin123"));
            superadmin.setRol(Rol.SUPERADMIN);
            superadmin.setSedeId(sedePrincipal);
            usuarioRepository.save(superadmin);

            // RF-15: personal de operaciones (P.O.) de ejemplo
            Usuario po = new Usuario();
            po.setNombre("operaciones");
            po.setPassword(encoder.encode("po123"));
            po.setRol(Rol.PERSONAL_OPERACIONES);
            po.setSedeId(sedePrincipal);
            usuarioRepository.save(po);

            System.out.println("=== Superadmin creado: usuario=admin password=admin123 ===");
            System.out.println("=== P.O. creado: usuario=operaciones password=po123 ===");
        }

        if (tipoVehiculoRepository.count() == 0) {
            tipoVehiculoRepository.save(new TipoVehiculo("Suburbano", new BigDecimal("120000")));
            tipoVehiculoRepository.save(new TipoVehiculo("Carro", new BigDecimal("90000")));
            tipoVehiculoRepository.save(new TipoVehiculo("Camioneta", new BigDecimal("150000")));
            tipoVehiculoRepository.save(new TipoVehiculo("Moto", new BigDecimal("40000")));
            System.out.println("=== Tipos de vehiculo iniciales creados ===");
        }
    }
}