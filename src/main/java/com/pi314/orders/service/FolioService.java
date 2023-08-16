package com.pi314.orders.service;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;

import java.util.*;

public interface FolioService {
    Folio findByName(String name);

    void bulkImportFolios(List<FolioDTO> folios);

    Folio findById(Long folioId);
}