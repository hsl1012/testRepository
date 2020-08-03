package com.fh.member.service;

import com.fh.common.ServerResponse;
import com.fh.member.model.Member;

public interface MemberService {
    ServerResponse checkMemberName(String name);

    ServerResponse checkMemberPhone(String phone);

    ServerResponse redister(Member member);

    ServerResponse register(Member member);

    ServerResponse login(Member member);

    ServerResponse queryMemberList();
}
