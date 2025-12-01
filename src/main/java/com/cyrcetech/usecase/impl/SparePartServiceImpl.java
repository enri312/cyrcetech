package com.cyrcetech.usecase.impl;

import com.cyrcetech.entity.SparePart;
import com.cyrcetech.infrastructure.SparePartDAO;
import com.cyrcetech.usecase.SparePartService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SparePartServiceImpl implements SparePartService {
    private final SparePartDAO sparePartDAO = new SparePartDAO();

    @Override
    public SparePart createSparePart(SparePart sparePart) {
        try {
            return sparePartDAO.save(sparePart);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating spare part", e);
        }
    }

    @Override
    public void updateSparePart(SparePart sparePart) {
        try {
            sparePartDAO.update(sparePart);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating spare part", e);
        }
    }

    @Override
    public void deleteSparePart(String id) {
        try {
            sparePartDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting spare part", e);
        }
    }

    @Override
    public SparePart getSparePartById(String id) {
        try {
            return sparePartDAO.findById(id).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SparePart> getAllSpareParts() {
        try {
            return sparePartDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
