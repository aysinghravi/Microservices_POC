package com.returnservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.returnservice.model.ReturnModel;

public interface ReturnRepository extends MongoRepository<ReturnModel, String> {
}
