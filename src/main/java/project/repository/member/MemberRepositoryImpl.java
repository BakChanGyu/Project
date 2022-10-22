package project.repository.member;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.member.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final DataSource dataSource;

    // 회원 등록
    @Override
    public Member save(Member member) {

        String sql = "insert into member(member_id, login_id, member_name, password, email, member_type, private_key) values(?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, member.getMemberId());
            pstmt.setString(2, member.getLoginId());
            pstmt.setString(3, member.getMemberName());
            pstmt.setString(4, member.getPassword());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, member.getMemberType());
            pstmt.setString(7, member.getPrivateKey());

            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 회원 리스트 조회하기
    @Override
    public List<Member> findAll() {

        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getLong("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setMemberType(rs.getString("member_type"));
                members.add(member);
            }

            return members;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 식별번호로 조회 -> 아이디찾기등
    @Override
    public Optional<Member> findById(Long memberId) {

        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getLong("member_id"));
                member.setLoginId(rs.getString("login_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setPassword(rs.getString("password"));
                member.setEmail(rs.getString("email"));
                member.setMemberType(rs.getString("member_type"));

                return Optional.of(member);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 로그인 아이디로 조회
    @Override
    public Optional<Member> findByLoginId(String loginId) {

        String sql = "select * from member where login_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, loginId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getLong("member_id"));
                member.setLoginId(rs.getString("login_id"));
                member.setPassword(rs.getString("password"));
                member.setMemberName(rs.getString("member_name"));
                member.setEmail(rs.getString("email"));
                member.setMemberType(rs.getString("member_type"));
                member.setPrivateKey(rs.getString("private_key"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 멤버 업데이트
    @Override
    public void update(Member member) {

        String sql = "update member set login_id = ?, member_name = ?, password = ?, email = ?, member_type = ? where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getLoginId());
            pstmt.setString(2, member.getMemberName());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getMemberType());
            pstmt.setLong(6, member.getMemberId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 멤버 삭제
    @Override
    public void delete(Long memberId) {

        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, memberId);
            pstmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 이메일 인증코드발송
    @Override
    public void addPrivateKey(Long id, String code) {

        String sql = "update member set private_key = ? where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, code);
            pstmt.setLong(2, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // id를 key값으로 인증코드를 가져옴
    @Override
    public String findPrivateKeyById(Long id) {

        String sql = "select private_key from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("private_key");
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
