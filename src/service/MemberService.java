package service;

import model.Member;
import model.OrderItem;

import java.util.List;

public interface MemberService {
    Member login(long memberId, String password);

    void showOrderHistory(Member member);
}
