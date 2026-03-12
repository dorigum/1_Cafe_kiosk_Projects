package service;

import model.Member;
import model.OrderItem;
import repository.MemberRepository;
import java.util.List;

public class MemberService {
	private MemberRepository memberRepository = new MemberRepository();

	// 로그인 비즈니스 로직
	public Member login(String phone, String password) {
		Member member = memberRepository.login(phone, password);
		if (member != null) {
			System.out.println("로그인 성공! 환영합니다, " + member.getMemberId() + "번 회원님.");
			return member;
		} else {
			System.out.println("로그인 실패: 회원 번호 또는 비밀번호를 확인하세요.");
			return null;
		}
	}

	// 회원가입
	public boolean register(String phone, String password, int age) {
		// 유효성 검사
		if (!phone.matches("^010-\\d{4}-\\d{4}$")) {
			System.out.println("전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)");
			return false;
		}
		if (password.length() < 4) {
			System.out.println("비밀번호는 4자리 이상이어야 합니다.");
			return false;
		}
		if (memberRepository.isPhoneExists(phone)) {
			System.out.println("이미 가입된 전화번호입니다.");
			return false;
		}
		boolean result = memberRepository.register(new Member(phone, password, age));
		if (result)
			System.out.println("회원가입이 완료되었습니다!");
		return result;
	}

	// 주문 내역 출력 로직
	public void showOrderHistory(Member member) {
		if (member == null)
			return;

		List<OrderItem> history = memberRepository.getOrderHistory(member.getMemberId());
		System.out.println("\n===== " + member.getMemberId() + "번 회원님의 주문 상세 내역 =====");
		if (history.isEmpty()) {
			System.out.println("주문 내역이 없습니다.");
		} else {
			for (OrderItem item : history) {
				System.out.println(item);
			}
		}
	}
}
