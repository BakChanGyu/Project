package project.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.member.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final DataSource dataSource;

    @Override
    public Member save(Member member) {

        String sql = "insert into member(member_id, loginId, member_name, password, email, member_type) values(?, ?, ?, ?, ?, ?)";

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

            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {

        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getLong("member_id"));
                member.setLoginId(rs.getString("loginId"));
                member.setMemberName(rs.getString("member_name"));
                member.setPassword(rs.getString("password"));
                member.setEmail(rs.getString("email"));
                member.setMemberType(rs.getString("member_type"));
                member.setPrivateKey(rs.getString("private_key"));

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

    @Override
    public Optional<Member> findLoginId(String loginId) {

        String sql = "select * from member where loginId = ?";

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
                member.setLoginId(rs.getString("loginId"));
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

    // 아이디 중복체크
    @Override
    public int countId(Long id) {

        String sql = "select count(*) from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()){
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 로그인 중복체크
    @Override
    public int countLoginID(String loginId) {

        String sql = "select count(*) from member where loginId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginId);

            rs = pstmt.executeQuery();

            if (rs.next()){
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 이메일 인증코드발송
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
