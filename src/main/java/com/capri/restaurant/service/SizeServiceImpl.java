package com.capri.restaurant.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capri.restaurant.model.Size;
import com.capri.restaurant.repository.SizeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SizeServiceImpl implements SizeService {
	
	@Autowired
	private SizeRepository sizeRepository;

	@Override
	public List<Size> findAll() {
		return sizeRepository.findAll();
	}

	@Override
	public Size save(Size size) {
		return sizeRepository.save(size);
	}

	@Override
	public Optional<Size> getById(Long id) {
		return sizeRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		sizeRepository.deleteById(id);
	}

	@Override
	public List<Size> findAllOrderByNameAsc() {
		return sizeRepository.findByOrderByNameAsc();
	}

}
