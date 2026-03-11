package service;

import model.Member;
import model.OrderHistory;
import repository.MemberRepository;
import java.util.List;

public class MemberService {
    private MemberRepository memberRepository = new MemberRepository();

    // 로그인 비즈니스 로직
    public Member login(String phone, String password) {
        Member member = memberRepository.login(phone, password);
        if (member != null) {
            System.out.println("로그인 성공! 환영합니다, " + member.getPhone() + "님.");
            return member;
        } else {
            System.out.println("로그인 실패: 휴대폰 번호 또는 비밀번호를 확인하세요.");
            return null;
        }
    }

    // 주문 내역 출력 로직
    public void showOrderHistory(Member member) {
        if (member == null) return;
        
        List<OrderHistory> history = memberRepository.getOrderHistory(member.getMemberId());
        System.out.println("\n===== " + member.getPhone() + "님의 주문 내역 =====");
        if (history.isEmpty()) {
            System.out.println("주문 내역이 없습니다.");
        } else {
            for (OrderHistory h : history) {
                System.out.println(h);
            }
        }
    }
}
