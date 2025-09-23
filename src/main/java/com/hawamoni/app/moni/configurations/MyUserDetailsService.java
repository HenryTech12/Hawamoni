package com.hawamoni.app.moni.configurations;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.mappers.UserMapper;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.info("login request email: {}",username);
      UserDTO userDTO = userRepository.findByEmail(username)
              .map(userMapper::convertToDTO)
              .orElseThrow(() -> new UsernameNotFoundException("invalid email"));
      return new UserPrincipal(userDTO);
    }
}
