package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

public interface MemberService {
    Member selectMember(HttpServletRequest token);
}
