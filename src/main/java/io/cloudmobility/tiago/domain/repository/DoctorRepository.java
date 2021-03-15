package io.cloudmobility.tiago.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.cloudmobility.tiago.domain.model.Doctor;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {

}
