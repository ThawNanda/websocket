package com.nexcode.websocket.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.nexcode.websocket.exception.BadRequestException;
import com.nexcode.websocket.model.entity.User;
import com.nexcode.websocket.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with -> : " + username));

		if (user == null) {
			throw new BadRequestException("User not found with this " + username);
		}

		return UserPrincipal.build(user);
	}
}