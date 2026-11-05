package org.example.restapi.testpractice.testsection05;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 회원 CRUD 동작 코드 (이미 완성되어 있어요)
 *
 * [문제 3] 5개 메서드 위에 @Override를 붙이세요.
 * [문제 4] findMember()가 null이면 404를 응답하도록 고치세요. (3군데)
 * */
@RestController
@RequestMapping("/practice/swagger")
public class MemberSwaggerControllerImpl implements MemberSwaggerController {

    private final List<MemberDTO> members = new ArrayList<>();

    public MemberSwaggerControllerImpl() {
        members.add(new MemberDTO(1, "member01", "홍길동", "hong@test.com", LocalDate.of(2025, 3, 1)));
        members.add(new MemberDTO(2, "member02", "유관순", "yu@test.com", LocalDate.of(2025, 6, 15)));
        members.add(new MemberDTO(3, "member03", "이순신", "lee@test.com", LocalDate.of(2026, 1, 10)));
    }

    // 회원 번호로 회원 찾기 (없으면 null)
    private MemberDTO findMember(int memberNo) {
        return members.stream()
                .filter(member -> member.getNo() == memberNo)
                .findFirst()
                .orElse(null);
    }

    // 회원 전체 조회
    @GetMapping("/members")
    public ResponseEntity<MemberResponseMessage> findAllMembers() {

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("members", members);

        return ResponseEntity.ok(new MemberResponseMessage(200, "조회 성공", responseMap));
    }

    // 회원 상세 조회
    @GetMapping("/members/{memberNo}")
    public ResponseEntity<MemberResponseMessage> findMemberByNo(@PathVariable int memberNo) {

        MemberDTO foundMember = findMember(memberNo);

        // [문제 4-1] foundMember가 null이면 404

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("member", foundMember);

        // 지금은 없는 회원이어도 200 + "member": null 이 나가요. 이것도 고쳐야 할 부분이에요.
        return ResponseEntity.ok(new MemberResponseMessage(200, "조회 성공", responseMap));
    }

    // 회원 등록 → 201 Created + Location 헤더
    @PostMapping("/members")
    public ResponseEntity<Void> registMember(@RequestBody MemberDTO newMember) {

        int newMemberNo = members.isEmpty() ? 1 : members.get(members.size() - 1).getNo() + 1;

        newMember.setNo(newMemberNo);
        newMember.setJoinedAt(LocalDate.now());
        members.add(newMember);

        return ResponseEntity
                .created(URI.create("/practice/swagger/members/" + newMemberNo))
                .build();
    }

    // 회원 정보 수정 (이름, 이메일)
    @PutMapping("/members/{memberNo}")
    public ResponseEntity<Void> modifyMember(@PathVariable int memberNo, @RequestBody MemberDTO modifyInfo) {

        MemberDTO foundMember = findMember(memberNo);

        // [문제 4-2] foundMember가 null이면 404
        //           (지금은 아래 줄에서 NullPointerException → 500 에러)

        foundMember.setName(modifyInfo.getName());
        foundMember.setEmail(modifyInfo.getEmail());

        return ResponseEntity.ok().build();
    }

    // 회원 삭제 → 204 No Content
    @DeleteMapping("/members/{memberNo}")
    public ResponseEntity<Void> removeMember(@PathVariable int memberNo) {

        MemberDTO foundMember = findMember(memberNo);

        // [문제 4-3] foundMember가 null이면 404
        //           (지금은 없는 회원이어도 204가 나가요. remove(null)은 오류 없이 false만 돌려줘요.)

        members.remove(foundMember);

        return ResponseEntity.noContent().build();
    }
}
