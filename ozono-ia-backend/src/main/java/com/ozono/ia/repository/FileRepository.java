package com.ozono.ia.repository;

import com.ozono.ia.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    File findByFileUuid(String fileUuid);

}
