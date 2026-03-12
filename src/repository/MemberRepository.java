package repository;

import model.Member;
import model.OrderItem;

import java.util.List;

public interface MemberRepository {
	Member login(long memberId, String password);

	List<OrderItem> getOrderHistory(long memberId);

	List<Member> getAllMembers();

	void deleteMember(long memberId);

}
