package com.example.Ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.Ventas.model.Boleta;
import com.example.Ventas.model.DetalleBoleta;
import com.example.Ventas.model.MetodoPago;
import com.example.Ventas.model.Pago;
import com.example.Ventas.repository.BoletaRepository;
import com.example.Ventas.repository.DetalleBoletaRepository;
import com.example.Ventas.repository.MetodoPagoRepository;
import com.example.Ventas.repository.PagoRepository;

import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    // Rango de IDs de libros existentes en el microservicio Catalogo (ajusta según tus datos reales)
    private static final int LIBRO_ID_MIN = 1;
    private static final int LIBRO_ID_MAX = 40;

    // Rango de IDs de clientes simulados (no existe microservicio de clientes)
    private static final int CLIENTE_ID_MIN = 1;
    private static final int CLIENTE_ID_MAX = 30;

    @Override
    public void run(String... args) throws Exception {

        // Evita duplicar datos si ya existen
        if (boletaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        Random random = new Random();

        // Generar métodos de pago
        String[] nombresMetodo = {"Efectivo", "Tarjeta de débito", "Tarjeta de crédito", "Transferencia", "Webpay"};
        for (String nombre : nombresMetodo) {
            MetodoPago metodoPago = new MetodoPago();
            metodoPago.setNombre(nombre);
            metodoPagoRepository.save(metodoPago);
        }
        List<MetodoPago> metodosPago = metodoPagoRepository.findAll();

        // Generar boletas con sus detalles
        for (int i = 0; i < 25; i++) {
            Boleta boleta = new Boleta();
            boleta.setFecha(fechaAleatoria(random));
            boleta.setIdCliente(CLIENTE_ID_MIN + random.nextInt(CLIENTE_ID_MAX - CLIENTE_ID_MIN + 1));

            // Generar entre 1 y 4 detalles por boleta y calcular el valor total
            int cantidadDetalles = 1 + random.nextInt(4);
            List<DetalleBoleta> detalles = new ArrayList<>();
            double valorTotal = 0.0;

            for (int j = 0; j < cantidadDetalles; j++) {
                Integer cantidad = 1 + random.nextInt(5);
                Double precioUnitario = redondear(faker.number().randomDouble(2, 5000, 35000));
                valorTotal += cantidad * precioUnitario;

                DetalleBoleta detalle = new DetalleBoleta();
                detalle.setBoleta(boleta);
                detalle.setIdLibro(LIBRO_ID_MIN + random.nextInt(LIBRO_ID_MAX - LIBRO_ID_MIN + 1));
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalles.add(detalle);
            }

            boleta.setValorTotal(redondear(valorTotal));
            boleta.setDetalles(detalles);
            boletaRepository.save(boleta); // cascade ALL guarda también los detalles

            // Generar un pago asociado a la boleta
            Pago pago = new Pago();
            pago.setBoleta(boleta);
            pago.setMetodoPago(metodosPago.get(random.nextInt(metodosPago.size())));
            pago.setMonto(boleta.getValorTotal());
            pago.setFecha(boleta.getFecha());
            pagoRepository.save(pago);
        }
    }

    private LocalDate fechaAleatoria(Random random) {
        LocalDate inicio = LocalDate.now().minusMonths(6);
        long dias = random.nextInt(180);
        return inicio.plusDays(dias);
    }

    private Double redondear(Double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}