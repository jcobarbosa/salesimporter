package br.com.jcobarbosa.salesimporter.repository;

import br.com.jcobarbosa.salesimporter.model.SaleImportingFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleImportingFileRepository extends MongoRepository<SaleImportingFile, ObjectId> {

}
