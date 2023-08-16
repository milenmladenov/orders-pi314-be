package com.pi314.orders.service;

import com.pi314.orders.model.dto.*;
import com.pi314.orders.model.entity.*;

import java.util.*;

public interface ModelService {

    Model findByName(String name);

    void bulkImportModels(List<ModelDTO> models);

    Model findById(Long modelId);
}
