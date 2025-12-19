package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.exceptions.NotFoundException;
import com.mx.gaby.mobiliario_web.model.entitites.Abono;
import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import com.mx.gaby.mobiliario_web.model.entitites.Renta;
import com.mx.gaby.mobiliario_web.records.*;
import com.mx.gaby.mobiliario_web.repositories.AbonoRepository;
import com.mx.gaby.mobiliario_web.repositories.DetailRentaRepository;
import com.mx.gaby.mobiliario_web.repositories.RentaRepository;
import com.mx.gaby.mobiliario_web.repositories.specification.RentaSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RentaQueryServiceImpl implements RentaQueryService{

    private final RentaRepository rentaRepository;
    private final DetailRentaRepository detailRentaRepository;
    private final AbonoRepository abonoRepository;

    public RentaQueryServiceImpl(RentaRepository rentaRepository, DetailRentaRepository detailRentaRepository, AbonoRepository abonoRepository) {
        this.rentaRepository = rentaRepository;
        this.detailRentaRepository = detailRentaRepository;
        this.abonoRepository = abonoRepository;
    }

    @Override
    public RentaResponseDTO findById(Integer id) {

        Renta renta = rentaRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Renta con ID " + id + " no encontrada."));

        List<DetailRenta> detail
                = detailRentaRepository.findByRentaId(renta.getId());

        List<Abono> abonosEntities =
                abonoRepository.findByRentaId(renta.getId());

        List<AbonoResponseDTO> abonos =
                abonosEntities.stream()
                .map(AbonoResponseDTO::fromEntity)
                .toList();


        List<DetailRentaResponseDTO> detailRentas =
                detail.stream()
                        .map(DetailRentaResponseDTO::fromEntity)
                        .toList();

        RentaTotalesDTOResponse totals
                = RentaTotalesDTOResponse.calculateTotals(renta,detail,abonos);

        return RentaResponseDTO.fromEntity(renta,detailRentas,totals);
    }

    @Override
    public List<RentaResponseDTO> getFromQuery(RentaFilterDTO rentaFilterDTO) {

        Specification<Renta> spec =
                RentaSpecification.applyFilter(rentaFilterDTO);

        List<Renta> rentas = rentaRepository.findAll(spec);

        return rentas.stream()
                // Mapea cada entidad Renta a un RentaResponseDTO
                .map(renta -> RentaResponseDTO.fromEntity(renta,null,null))
                // Colecta los resultados en una nueva lista
                .toList();

    }
}
