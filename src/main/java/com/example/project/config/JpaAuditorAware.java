/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.example.project.utils.SecurityUtils;

import java.util.Optional;

@Component
@Slf4j
public class JpaAuditorAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
    	Integer userId = SecurityUtils.getCurrentUserId();
        if (userId != null) {
            return Optional.of(userId);
        } else {
            return Optional.of(0);
        }

    }
}
