package vb.shop.service.impl;

import org.springframework.stereotype.Service;
import vb.shop.model.Manufacturer;
import vb.shop.model.exception.ManufacturerIdException;
import vb.shop.repository.ManufacturerRepository;
import vb.shop.service.ManufacturerService;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }


    @Override
    public List<Manufacturer> findAll() {
        return this.manufacturerRepository.findAll();
    }

    @Override
    public List<Manufacturer> findAllByName(String name) {
        return this.manufacturerRepository.findAllByName(name);
    }

    @Override
    public Manufacturer findById(Long id) {
        return this.manufacturerRepository.findById(id).orElseThrow(() ->new ManufacturerIdException(id));
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return this.manufacturerRepository.save(manufacturer);
    }

    @Override
    public Manufacturer update(Long id, Manufacturer manufacturer) {
        Manufacturer manufacturer1 = this.findById(id);
        manufacturer1.setName(manufacturer.getName());
        manufacturer1.setAddress(manufacturer.getAddress());
        return this.manufacturerRepository.save(manufacturer1);
    }

    @Override
    public Manufacturer updateName(Long id, String name) {
        Manufacturer manufacturer = this.findById(id);
        manufacturer.setName(name);
        return this.manufacturerRepository.save(manufacturer);
    }

    @Override
    public void deleteById(Long id) {
        this.manufacturerRepository.deleteById(id);
    }
}
