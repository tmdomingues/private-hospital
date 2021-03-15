CREATE TABLE IF NOT EXISTS doctors (
	id bigserial NOT NULL,
	"name" varchar(255) UNIQUE NOT NULL,
	specialization varchar(255) NOT NULL,
	CONSTRAINT doctors_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS absences (
	id bigserial NOT NULL,
	start_period timestamp NOT NULL,
	end_period timestamp NOT NULL,
    reason varchar (255) NOT NULL,
    doctor_id bigserial NOT NULL,
	CONSTRAINT absences_pk PRIMARY KEY (id),
	CONSTRAINT fk_doctor
      FOREIGN KEY(doctor_id)
	  REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS patients (
    id bigserial NOT NULL,
    "name" varchar (255) UNIQUE NOT NULL,
    CONSTRAINT patients_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS appointments (
    id bigserial NOT NULL,
    doctor_id bigserial,
    patient_id bigserial,
    from_datetime timestamp NOT NULL,
	to_datetime timestamp NOT NULL,
	description varchar (255),
    CONSTRAINT appointments_pk PRIMARY KEY (id),
    CONSTRAINT fk_doctor
      FOREIGN KEY(doctor_id)
	  REFERENCES doctors(id),
	CONSTRAINT fk_patient
      FOREIGN KEY(patient_id)
	  REFERENCES patients(id)
);


--################# Dummy Data #################################

INSERT INTO doctors (id, name, specialization)
VALUES(1, 'Dr. Skin Healer', 'Dermatology');
INSERT INTO doctors (id, name, specialization)
VALUES(2, 'Dr. Brain Picker', 'Neurology');
INSERT INTO doctors (id, name, specialization)
VALUES(3, 'Dr. Eye Candy', 'Ophthalmology');

INSERT INTO patients (id, "name")
VALUES(1, 'Estarola da Silva');
INSERT INTO patients (id, "name")
VALUES(2, 'Joseph Pipocas');
INSERT INTO patients (id, "name")
VALUES(3, 'Marcus Aurelivs');
INSERT INTO patients (id, "name")
VALUES(4, 'Professor Mambu');

