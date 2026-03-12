package service;

import exception.BusinessRuleException;
import exception.ConflictException;
import exception.ValidationException;
import model.Member;
import model.OrderItem;
import model.Wishlist;
import repository.MemberRepository;
import repository.MemberRepositoryImpl;
import repository.WishlistRepository;
import repository.QuickOrderRepository;
import java.util.List;

public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final WishlistRepository wishlistRepository;
	private final QuickOrderRepository quickOrderRepository;

	public MemberServiceImpl() {
		this(new MemberRepositoryImpl(), new WishlistRepository(), new QuickOrderRepository());
	}

	public MemberServiceImpl(MemberRepository memberRepository, WishlistRepository wishlistRepository,
			QuickOrderRepository quickOrderRepository) {
		if (memberRepository == null) {
			throw new ValidationException("MemberRepository는 null일 수 없습니다.");
		}
		if (wishlistRepository == null) {
			throw new ValidationException("WishlistRepository는 null일 수 없습니다.");
		}
		if (quickOrderRepository == null) {
			throw new ValidationException("QuickOrderRepository는 null일 수 없습니다.");
		}
		this.memberRepository = memberRepository;
		this.wishlistRepository = wishlistRepository;
		this.quickOrderRepository = quickOrderRepository;
	}

	@Override
	public Member login(String phone, String password) {
		if (phone == null || phone.trim().isEmpty()) {
			throw new ValidationException("전화번호는 비어 있을 수 없습니다.");
		}
		if (password == null || password.trim().isEmpty()) {
			throw new ValidationException("비밀번호는 비어 있을 수 없습니다.");
		}

		// 전화번호 정규화: 하이픈 제거 후 숫자만 추출
		String digitsOnly = phone.replaceAll("[^0-9]", "");
		
		// 01012345678 형식을 010-1234-5678 형식으로 변환 (DB 저장 형식에 맞춤)
		String normalizedPhone = digitsOnly;
		if (digitsOnly.length() == 11) {
			normalizedPhone = digitsOnly.substring(0, 3) + "-" + 
			                  digitsOnly.substring(3, 7) + "-" + 
			                  digitsOnly.substring(7);
		}

		Member member = memberRepository.login(normalizedPhone, password);
		if (member == null) {
			throw new BusinessRuleException("전화번호 또는 비밀번호가 일치하지 않습니다.");
		}
		return member;
	}

	@Override
	public boolean register(String phone, String password, int age) {
		if (phone == null || !phone.matches("^010-\\d{4}-\\d{4}$")) {
			throw new ValidationException("전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)");
		}
		if (password == null || password.length() < 4) {
			throw new ValidationException("비밀번호는 4자리 이상이어야 합니다.");
		}
		if (memberRepository.isPhoneExists(phone)) {
			throw new ConflictException("이미 가입된 전화번호입니다.");
		}
		boolean result = memberRepository.register(new Member(phone, password, age));
		if (!result) {
			throw new BusinessRuleException("회원가입에 실패했습니다.");
		}
		return true;
	}

	@Override
	public List<OrderItem> getOrderHistory(Member member) {
		validateMember(member);
		return memberRepository.getOrderHistory(member.getMemberId());
	}

	@Override
	public List<Wishlist> getWishlist(Member member) {
		validateMember(member);
		return wishlistRepository.getWishlistByMember(member.getMemberId());
	}

	@Override
	public void addWishlist(Member member, long menuId) {
		validateMember(member);
		if (menuId <= 0) {
			throw new ValidationException("메뉴 ID는 1 이상이어야 합니다.");
		}
		if (wishlistRepository.isAlreadyWished(member.getMemberId(), menuId)) {
			throw new ConflictException("이미 찜한 메뉴입니다.");
		}
		boolean result = wishlistRepository.addWishlist(member.getMemberId(), menuId);
		if (!result) {
			throw new BusinessRuleException("찜 목록 추가에 실패했습니다.");
		}
	}

	@Override
	public void removeWishlist(long wishlistId) {
		if (wishlistId <= 0) {
			throw new ValidationException("찜 ID는 1 이상이어야 합니다.");
		}
		boolean result = wishlistRepository.removeWishlist(wishlistId);
		if (!result) {
			throw new BusinessRuleException("찜 삭제에 실패했습니다.");
		}
	}

	@Override
	public List<OrderItem> getQuickOrder(Member member) {
		validateMember(member);
		return quickOrderRepository.getLastOrderItems(member.getMemberId());
	}

	private void validateMember(Member member) {
		if (member == null) {
			throw new ValidationException("회원 정보가 유효하지 않습니다.");
		}
	}
}
