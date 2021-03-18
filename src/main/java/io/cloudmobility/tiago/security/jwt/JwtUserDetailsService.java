package io.cloudmobility.tiago.security.jwt;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.cloudmobility.tiago.domain.model.Doctor;
import io.cloudmobility.tiago.domain.model.Patient;
import io.cloudmobility.tiago.security.HospitalUserPrincipal;
import io.cloudmobility.tiago.security.UserRole;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        // Stubbed data to mimic the retrieval of entities with username/password/roles typically stored database
        if (UserRole.DOCTOR.name().equalsIgnoreCase(username)) {
            final Doctor p = new Doctor();
            p.setName(username);
            p.setRoles(Collections.singletonList(UserRole.DOCTOR));
            return new HospitalUserPrincipal(p);
        }
        if (UserRole.PATIENT.name().equalsIgnoreCase(username)) {
            final Patient p = new Patient();
            p.setName(username);
            p.setRoles(Collections.singletonList(UserRole.PATIENT));
            return new HospitalUserPrincipal(p);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
