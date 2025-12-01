package com.cyrcetech.usecase;

import com.cyrcetech.entity.SparePart;
import java.util.List;

public interface SparePartService {
    SparePart createSparePart(SparePart sparePart);

    void updateSparePart(SparePart sparePart);

    void deleteSparePart(String id);

    SparePart getSparePartById(String id);

    List<SparePart> getAllSpareParts();
}
